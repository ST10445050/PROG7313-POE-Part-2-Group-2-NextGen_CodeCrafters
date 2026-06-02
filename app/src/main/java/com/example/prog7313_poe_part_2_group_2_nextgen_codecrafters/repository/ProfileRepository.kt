package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ProfileDto
import io.github.jan.supabase.postgrest.from

class ProfileRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getProfile(userId: String): ProfileDto? {
        return try {
            supabase.from("profiles")
                .select {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<ProfileDto>()
        } catch (e: Exception) {
            null
        }
    }
}