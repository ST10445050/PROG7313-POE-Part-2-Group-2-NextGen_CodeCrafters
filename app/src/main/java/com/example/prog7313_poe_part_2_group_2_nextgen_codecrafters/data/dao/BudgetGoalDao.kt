package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.BudgetGoal

/**
 * Data Access Object (DAO) for handling all database operations
 * related to the BudgetGoal entity.
 *
 * This interface provides methods to insert and retrieve
 * budget goal records from the Room database.
 */
@Dao
interface BudgetGoalDao {

    /**
     * Inserts a new budget goal into the database.
     *
     * This method is marked as suspend because it must be executed
     * on a background thread (Room requirement).
     *
     * @param budgetGoal The BudgetGoal object to be saved.
     */
    @Insert
    suspend fun insertBudgetGoal(
        budgetGoal: BudgetGoal
    )

    /**
     * Retrieves a budget goal for a specific user, month, and year.
     *
     * This ensures that each user has only one budget goal
     * per month and year combination.
     *
     * @param userId The ID of the user.
     * @param month The month of the budget goal (1–12).
     * @param year The year of the budget goal.
     *
     * @return The BudgetGoal if found, otherwise null.
     */
    @Query("""
        SELECT * FROM budget_goals
        WHERE userId = :userId
        AND month = :month
        AND year = :year
        LIMIT 1
    """)
    suspend fun getBudgetGoal(
        userId: Int,
        month: Int,
        year: Int
    ): BudgetGoal?
}