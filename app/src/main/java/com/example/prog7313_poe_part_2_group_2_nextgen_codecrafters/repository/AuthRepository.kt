package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ProfileDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from

class AuthRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        gender: String,
        phone: String,
        username: String,
        password: String
    ): String {
        try {
            supabase.auth.signUpWith(Email) {
                this.email = email.trim()
                this.password = password
            }
        } catch (e: Exception) {
            throw Exception("Auth signup failed: ${e.message}")
        }

        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("Registration failed. User ID not found. Make sure Confirm Email is OFF.")

        try {
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
        } catch (e: Exception) {
            throw Exception("Profile insert failed: ${e.message}")
        }

        return userId
    }

    suspend fun loginUserWithUsername(
        username: String,
        password: String
    ): String {
        val profile = try {
            supabase.from("profiles")
                .select {
                    filter {
                        eq("username", username.trim())
                    }
                }
                .decodeSingle<ProfileDto>()
        } catch (e: Exception) {
            throw Exception("Username not found")
        }

        try {
            supabase.auth.signInWith(Email) {
                email = profile.email.trim()
                this.password = password
            }
        } catch (e: Exception) {
            throw Exception("Invalid username or password")
        }

        return supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("Login failed. User ID not found.")
    }

    suspend fun checkEmailExists(email: String): Boolean {
        return try {
            val profiles = supabase.from("profiles")
                .select {
                    filter {
                        eq("email", email.trim())
                    }
                }
                .decodeList<ProfileDto>()

            profiles.isNotEmpty()
        } catch (e: Exception) {
            throw Exception("Email check failed: ${e.message}")
        }
    }

    suspend fun sendPasswordResetEmail(email: String) {
        try {
            supabase.auth.resetPasswordForEmail(
                email = email.trim(),
                redirectUrl = "https://glistening-flan-bbdc92.netlify.app/"
            )
        } catch (e: Exception) {
            throw Exception("Password reset email failed: ${e.message}")
        }
    }

    suspend fun logoutUser() {
        supabase.auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}