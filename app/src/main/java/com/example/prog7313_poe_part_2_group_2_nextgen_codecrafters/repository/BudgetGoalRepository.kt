package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.BudgetGoalDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import io.github.jan.supabase.postgrest.from

class BudgetGoalRepository {

    private val supabase = SupabaseClientProvider.client
    private val tag = "BudgetGoalRepository"

    /**
     * Loads the saved budget goal for a specific user, month, and year.
     * This is used by the Budget Goals screen and the Analytics progress tracker.
     */
    suspend fun getBudgetGoal(
        userId: String,
        month: Int,
        year: Int
    ): BudgetGoalDto? {
        AppLogger.info(tag, "Loading budget goal for month: $month, year: $year.")

        return try {
            val goal = supabase.from("budget_goals")
                .select {
                    filter {
                        eq("user_id", userId)
                        eq("month", month)
                        eq("year", year)
                    }
                }
                .decodeList<BudgetGoalDto>()
                .firstOrNull()

            if (goal != null) {
                AppLogger.info(tag, "Budget goal loaded successfully.")
            } else {
                AppLogger.warning(tag, "No budget goal found for selected month and year.")
            }

            goal

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to load budget goal.", e)
            throw Exception("Failed to load budget goal: ${e.message}")
        }
    }

    /**
     * Saves a budget goal for the selected month and year.
     * If a goal already exists, it updates it instead of creating a duplicate row.
     */
    suspend fun saveBudgetGoal(goal: BudgetGoalDto) {
        AppLogger.info(
            tag,
            "Saving budget goal for month: ${goal.month}, year: ${goal.year}."
        )

        try {
            // Check if the user already has a goal for this month and year.
            val existingGoal = getBudgetGoal(
                userId = goal.userId,
                month = goal.month,
                year = goal.year
            )

            if (existingGoal?.budgetGoalId != null) {
                AppLogger.info(tag, "Existing budget goal found. Updating budget goal.")

                // Update the existing row so one user only has one goal per month/year.
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

                AppLogger.info(tag, "Budget goal updated successfully.")

            } else {
                AppLogger.info(tag, "No existing budget goal found. Inserting new budget goal.")


                supabase.from("budget_goals").insert(goal)

                AppLogger.info(tag, "Budget goal inserted successfully.")
            }

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to save budget goal.", e)
            throw Exception("Failed to save budget goal: ${e.message}")
        }
    }
}