package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.model.CategoryTotal

@Dao
interface ExpenseDao {


    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    //  added feature: Category Totals Report
    @Query("""
        SELECT categoryId, SUM(amount) as totalAmount
        FROM expenses
        WHERE userId = :userId
        AND date BETWEEN :startDate AND :endDate
        GROUP BY categoryId
    """)
    suspend fun getTotalSpentByCategory(
        userId: Int,
        startDate: String,
        endDate: String
    ): List<CategoryTotal>
}