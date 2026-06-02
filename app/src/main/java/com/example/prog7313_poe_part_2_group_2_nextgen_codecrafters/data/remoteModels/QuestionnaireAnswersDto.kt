package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionnaireAnswersDto(
    @SerialName("user_id")
    val userId: String,

    @SerialName("employment_status")
    val employmentStatus: String,

    @SerialName("monthly_income")
    val monthlyIncome: Double,

    @SerialName("selected_categories")
    val selectedCategories: String,

    @SerialName("financial_goal")
    val financialGoal: String,

    @SerialName("monthly_savings_goal")
    val monthlySavingsGoal: Double,

    @SerialName("dashboard_type")
    val dashboardType: String = "personalized"
)