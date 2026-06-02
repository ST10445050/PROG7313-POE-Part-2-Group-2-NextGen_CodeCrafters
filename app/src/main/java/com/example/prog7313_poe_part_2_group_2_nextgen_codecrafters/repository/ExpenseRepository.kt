package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ExpenseDto
import io.github.jan.supabase.postgrest.from

class ExpenseRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun insertExpense(expense: ExpenseDto) {
        try {
            supabase.from("expenses").insert(expense)
        } catch (e: Exception) {
            throw Exception("Failed to save expense: ${e.message}")
        }
    }

    suspend fun getExpensesForUser(userId: String): List<ExpenseDto> {
        return try {
            supabase.from("expenses")
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
        } catch (e: Exception) {
            throw Exception("Failed to load expenses: ${e.message}")
        }
    }

    suspend fun getExpensesForUserByDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ): List<ExpenseDto> {
        return try {
            supabase.from("expenses")
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
        } catch (e: Exception) {
            throw Exception("Failed to load filtered expenses: ${e.message}")
        }
    }
}