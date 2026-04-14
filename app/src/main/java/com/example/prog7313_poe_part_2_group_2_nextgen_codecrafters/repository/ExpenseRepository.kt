package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import androidx.lifecycle.LiveData
// IMPORTANT: These imports link your packages
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.database.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.database.ExpenseDao

class ExpenseRepository(private val dao: ExpenseDao) {

    // Explicitly define the type to fix the "platform call/nullability" warning
    val allExpenses: LiveData<List<Expense>> = dao.getAllExpenses()

    suspend fun insert(expense: Expense) {
        dao.insert(expense)
    }

    suspend fun delete(expense: Expense) {
        dao.delete(expense)
    }
}