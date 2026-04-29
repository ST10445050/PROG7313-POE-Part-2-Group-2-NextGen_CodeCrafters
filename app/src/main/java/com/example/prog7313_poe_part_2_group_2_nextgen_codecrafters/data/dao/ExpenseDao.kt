package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.model.CategoryTotal

@Dao
interface ExpenseDao {

    // Insert a new expense
    @Insert
    suspend fun insertExpense(expense: Expense)

    // Get all expenses for a specific user
    @Query("SELECT * FROM expenses WHERE userId = :userId")
    suspend fun getExpensesForUser(userId: Int): List<Expense>

    // Category Totals Report
    @Query("""
        SELECT c.name as categoryName, SUM(e.amount) as totalAmount
        FROM expenses e
        INNER JOIN categories c ON e.categoryId = c.id
        WHERE e.userId = :userId
        AND e.date BETWEEN :startDate AND :endDate
        GROUP BY c.name
    """)
    suspend fun getTotalSpentByCategory(
        userId: Int,
        startDate: String,
        endDate: String
    ): List<CategoryTotal>
}