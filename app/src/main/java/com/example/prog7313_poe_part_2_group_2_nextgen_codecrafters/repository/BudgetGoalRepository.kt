package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.BudgetGoalDto
import io.github.jan.supabase.postgrest.from

class BudgetGoalRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getBudgetGoal(
        userId: String,
        month: Int,
        year: Int
    ): BudgetGoalDto? {
        return try {
            supabase.from("budget_goals")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("month", month)
                        eq("year", year)
                    }
                }
                .decodeList<BudgetGoalDto>()
                .firstOrNull()
        } catch (e: Exception) {
            throw Exception("Failed to load budget goal: ${e.message}")
        }
    }

    suspend fun saveBudgetGoal(goal: BudgetGoalDto) {
        try {
            val existingGoal = getBudgetGoal(
                userId = goal.userId,
                month = goal.month,
                year = goal.year
            )

            if (existingGoal?.budgetGoalId != null) {
                supabase.from("budget_goals")
                    .update(
                        mapOf(
                            "minimum_goal" to goal.minimumGoal,
                            "maximum_goal" to goal.maximumGoal
                        )
                    ) {
                        filter {
                            eq("budget_goal_id", existingGoal.budgetGoalId)
                        }
                    }
            } else {
                supabase.from("budget_goals").insert(goal)
            }

        } catch (e: Exception) {
            throw Exception("Failed to save budget goal: ${e.message}")
        }
    }
}