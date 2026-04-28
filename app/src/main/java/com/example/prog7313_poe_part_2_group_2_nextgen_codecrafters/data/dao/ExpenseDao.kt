package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.*
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    fun getExpensesForUser(userId: Int): Flow<List<Expense>>
}