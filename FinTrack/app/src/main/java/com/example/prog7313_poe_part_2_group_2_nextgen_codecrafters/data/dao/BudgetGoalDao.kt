package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.BudgetGoal

@Dao
interface BudgetGoalDao {

    @Insert
    suspend fun insertBudgetGoal(
        budgetGoal: BudgetGoal
    )

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