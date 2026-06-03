package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ExpenseDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import io.github.jan.supabase.postgrest.from

class ExpenseRepository {

    private val supabase = SupabaseClientProvider.client
    private val tag = "ExpenseRepository"

    /**
     * Saves a new expense record into the Supabase expenses table.
     * The receipt image upload is handled separately in ReceiptStorageRepository.
     */
    suspend fun insertExpense(expense: ExpenseDto) {
        AppLogger.info(
            tag,
            "Saving expense. Category: ${expense.categoryName}, Amount: ${expense.amount}"
        )

        try {
            supabase.from("expenses").insert(expense)

            AppLogger.info(tag, "Expense saved successfully.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to save expense.", e)
            throw Exception("Failed to save expense: ${e.message}")
        }
    }

    /**
     * Loads all expenses that belong to the logged-in user.
     * Results are sorted by newest date and latest start time first.
     */
    suspend fun getExpensesForUser(userId: String): List<ExpenseDto> {
        AppLogger.info(tag, "Loading expenses for user.")

        return try {
            val expenses = supabase.from("expenses")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<ExpenseDto>()
                .sortedWith(
                    compareByDescending<ExpenseDto> { it.date }
                        .thenByDescending { it.startTime }
                )

            AppLogger.info(
                tag,
                "Expenses loaded successfully. Count: ${expenses.size}"
            )

            expenses

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to load expenses.", e)
            throw Exception("Failed to load expenses: ${e.message}")
        }
    }

    /**
     * Loads expenses for a selected date range.
     * This is used by analytics, category totals, and dashboard filtering.
     */
    suspend fun getExpensesForUserByDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ): List<ExpenseDto> {
        AppLogger.info(
            tag,
            "Loading filtered expenses from $startDate to $endDate."
        )

        return try {
            val filteredExpenses = supabase.from("expenses")
                .select {
                    filter {
                        eq("user_id", userId)
                        gte("date", startDate)
                        lte("date", endDate)
                    }
                }
                .decodeList<ExpenseDto>()
                .sortedWith(
                    compareByDescending<ExpenseDto> { it.date }
                        .thenByDescending { it.startTime }
                )

            AppLogger.info(
                tag,
                "Filtered expenses loaded successfully. Count: ${filteredExpenses.size}"
            )

            filteredExpenses

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to load filtered expenses.", e)
            throw Exception("Failed to load filtered expenses: ${e.message}")
        }
    }
}