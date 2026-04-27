package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("""
        SELECT * FROM expenses
        WHERE userId = :userId
        ORDER BY date DESC, startTime DESC
    """)
    fun getExpensesForUser(userId: Int): Flow<List<Expense>>

    @Query("""
        SELECT * FROM expenses
        WHERE userId = :userId
        AND date BETWEEN :startDate AND :endDate
        ORDER BY date DESC, startTime DESC
    """)
    fun getExpensesForUserByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Expense>>
}