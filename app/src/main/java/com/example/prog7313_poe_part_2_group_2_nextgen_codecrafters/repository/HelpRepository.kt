package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.HelpFaqDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import io.github.jan.supabase.postgrest.from

class HelpRepository {

    private val supabase = SupabaseClientProvider.client
    private val tag = "HelpRepository"

    /**
     * Loads the Help & Support FAQ content from Supabase.
     * This data is not user-specific, so every user sees the same help information.
     */
    suspend fun getHelpFaqs(): List<HelpFaqDto> {
        AppLogger.info(tag, "Loading help FAQs.")

        return try {
            val faqs = supabase.from("help_faqs")
                .select()
                .decodeList<HelpFaqDto>()
                .sortedBy { it.orderIndex }

            AppLogger.info(
                tag,
                "Help FAQs loaded successfully. Count: ${faqs.size}"
            )

            faqs

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to load help FAQs.", e)
            throw Exception("Failed to load help information: ${e.message}")
        }
    }
}