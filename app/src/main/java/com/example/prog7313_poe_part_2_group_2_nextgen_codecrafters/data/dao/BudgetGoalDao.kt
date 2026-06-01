package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.BudgetGoal
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgetGoal(budgetGoal: BudgetGoal)

    @Query("""
        SELECT * 
        FROM budget_goals 
        WHERE userId = :userId 
        AND month = :month 
        AND year = :year 
        LIMIT 1
    """)
    fun getBudgetGoalForMonth(
        userId: Int,
        month: Int,
        year: Int
    ): Flow<BudgetGoal?>
}