package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.QuestionnaireAnswersDto
import io.github.jan.supabase.postgrest.from

class QuestionnaireRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun saveQuestionnaireAnswers(
        userId: String,
        employmentStatus: String,
        monthlyIncome: Double,
        selectedCategories: String,
        financialGoal: String,
        monthlySavingsGoal: Double
    ) {
        supabase.from("questionnaire_answers").insert(
            QuestionnaireAnswersDto(
                userId = userId,
                employmentStatus = employmentStatus,
                monthlyIncome = monthlyIncome,
                selectedCategories = selectedCategories,
                financialGoal = financialGoal,
                monthlySavingsGoal = monthlySavingsGoal,
                dashboardType = "personalized"
            )
        )
    }

    suspend fun getQuestionnaireAnswers(userId: String): QuestionnaireAnswersDto? {
        return try {
            supabase.from("questionnaire_answers")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeSingle<QuestionnaireAnswersDto>()
        } catch (e: Exception) {
            null
        }
    }
}