package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ProfileDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class AuthRepository {

    private val supabase = SupabaseClientProvider.client
    private val tag = "AuthRepository"

    suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        gender: String,
        phone: String,
        username: String,
        password: String
    ): String {
        AppLogger.info(tag, "Registration started.")

        try {
            supabase.auth.signUpWith(Email) {
                this.email = email.trim()
                this.password = password
            }

            AppLogger.info(tag, "Supabase auth signup completed.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Registration failed during auth signup.", e)
            throw Exception("Auth signup failed: ${e.message}")
        }

        val userId = supabase.auth.currentUserOrNull()?.id
            ?: run {
                AppLogger.error(tag, "Registration failed because user ID was not found.")
                throw Exception("Registration failed. User ID not found. Make sure Confirm Email is OFF.")
            }

        try {
            AppLogger.info(tag, "Saving new user profile.")

            supabase.from("profiles").insert(
                ProfileDto(
                    id = userId,
                    name = name,
                    surname = surname,
                    email = email.trim(),
                    gender = gender,
                    phone = phone,
                    username = username.trim(),
                    fullName = "$name $surname"
                )
            )

            AppLogger.info(tag, "Registration successful and profile saved.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Registration failed during profile insert.", e)
            throw Exception("Profile insert failed: ${e.message}")
        }

        return userId
    }

    suspend fun loginUserWithUsername(
        username: String,
        password: String
    ): String {
        AppLogger.info(tag, "Login started.")

        val profile = try {
            AppLogger.info(tag, "Searching profile by username.")

            supabase.from("profiles")
                .select {
                    filter {
                        eq("username", username.trim())
                    }
                }
                .decodeSingle<ProfileDto>()

        } catch (e: Exception) {
            AppLogger.error(tag, "Login failed because username was not found.", e)
            throw Exception("Username not found")
        }

        try {
            AppLogger.info(tag, "Signing in user with linked email.")

            supabase.auth.signInWith(Email) {
                email = profile.email.trim()
                this.password = password
            }

            AppLogger.info(tag, "Login successful.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Login failed during Supabase authentication.", e)
            throw Exception("Invalid username or password")
        }

        return supabase.auth.currentUserOrNull()?.id
            ?: run {
                AppLogger.error(tag, "Login failed because user ID was not found after sign in.")
                throw Exception("Login failed. User ID not found.")
            }
    }

    suspend fun checkEmailExists(email: String): Boolean {
        AppLogger.info(tag, "Checking whether email exists for password reset.")

        return try {
            val profiles = supabase.from("profiles")
                .select {
                    filter {
                        eq("email", email.trim())
                    }
                }
                .decodeList<ProfileDto>()

            val exists = profiles.isNotEmpty()

            if (exists) {
                AppLogger.info(tag, "Email check completed. Matching profile found.")
            } else {
                AppLogger.warning(tag, "Email check completed. No matching profile found.")
            }

            exists

        } catch (e: Exception) {
            AppLogger.error(tag, "Email check failed.", e)
            throw Exception("Email check failed: ${e.message}")
        }
    }

    suspend fun sendPasswordResetEmail(email: String) {
        AppLogger.info(tag, "Password reset email requested.")

        try {
            supabase.auth.resetPasswordForEmail(
                email = email.trim(),
                redirectUrl = "https://glistening-flan-bbdc92.netlify.app/"
            )

            AppLogger.info(tag, "Password reset email sent successfully.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Password reset email failed.", e)
            throw Exception("Password reset email failed: ${e.message}")
        }
    }

    suspend fun logoutUser() {
        AppLogger.info(tag, "Logout started.")

        try {
            supabase.auth.signOut()
            AppLogger.info(tag, "Logout successful.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Logout failed.", e)
            throw e
        }
    }

    fun getCurrentUserId(): String? {
        AppLogger.debug(tag, "Getting current user ID.")
        return supabase.auth.currentUserOrNull()?.id
    }
}