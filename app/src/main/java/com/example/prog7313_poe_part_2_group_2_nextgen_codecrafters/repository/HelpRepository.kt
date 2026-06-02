package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.HelpFaqDto
import io.github.jan.supabase.postgrest.from

class HelpRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getHelpFaqs(): List<HelpFaqDto> {
        return try {
            supabase.from("help_faqs")
                .select()
                .decodeList<HelpFaqDto>()
                .sortedBy { it.orderIndex }
        } catch (e: Exception) {
            throw Exception("Failed to load help information: ${e.message}")
        }
    }
}