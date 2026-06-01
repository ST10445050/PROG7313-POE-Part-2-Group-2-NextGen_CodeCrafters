package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import kotlinx.coroutines.flow.Flow

data class CategorySpending(
    val categoryId: Int,
    val totalAmount: Double
)

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE userId = :userId ORDER BY date DESC, startTime DESC")
    fun getExpensesForUser(userId: Int): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE userId = :userId AND date BETWEEN :startDate AND :endDate ORDER BY date DESC, startTime DESC")
    fun getExpensesForUserByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Expense>>

    @Query("SELECT categoryId, SUM(amount) as totalAmount FROM expenses WHERE userId = :userId GROUP BY categoryId")
    fun getSpendingByCategory(userId: Int): Flow<List<CategorySpending>>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId")
    fun getTotalSpending(userId: Int): Flow<Double?>

    // New query for your visual budget progress tracking feature.
    // This calculates total spending over a specific period, such as the past month.
    @Query("""
        SELECT SUM(amount) 
        FROM expenses 
        WHERE userId = :userId 
        AND date BETWEEN :startDate AND :endDate
    """)
    fun getTotalSpendingForPeriod(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<Double?>
}