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
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }

        val userId = supabase.auth.currentUserOrNull()?.id
            ?: throw Exception("Registration failed. User ID not found.")

        supabase.from("profiles").insert(
            ProfileDto(
                id = userId,
                name = name,
                surname = surname,
                email = email,
                gender = gender,
                phone = phone,
                username = username,
                fullName = "$name $surname"
            )
        )

        return userId
    }

    suspend fun loginUser(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun logoutUser() {
        supabase.auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return supabase.auth.currentUserOrNull()?.id
    }
}