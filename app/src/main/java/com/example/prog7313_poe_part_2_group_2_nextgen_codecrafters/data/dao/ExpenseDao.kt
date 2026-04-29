package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.model.CategoryTotal
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.model.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // Insert a new expense
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("""
    SELECT 
        e.expenseId,
        e.userId,
        e.categoryId,
        c.name AS categoryName,
        e.date,
        e.amount
    FROM expenses e
    INNER JOIN categories c ON e.categoryId = c.categoryId
    WHERE e.userId = :userId
""")
    fun getExpensesWithCategoryForUser(userId: Int): Flow<List<ExpenseWithCategory>>

    // Get all expenses for a specific user
    @Query("SELECT * FROM expenses WHERE userId = :userId")
    fun getExpensesForUser(userId: Int): Flow<List<Expense>>

    // Category Totals Report
    @Query("""
        SELECT c.name as categoryName, SUM(e.amount) as totalAmount
        FROM expenses e
        INNER JOIN categories c ON e.categoryId = c.categoryId
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