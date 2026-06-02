package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetGoalDto(
    @SerialName("budget_goal_id")
    val budgetGoalId: String? = null,

    @SerialName("user_id")
    val userId: String,

    val month: Int,

    val year: Int,

    @SerialName("minimum_goal")
    val minimumGoal: Double,

    @SerialName("maximum_goal")
    val maximumGoal: Double
)