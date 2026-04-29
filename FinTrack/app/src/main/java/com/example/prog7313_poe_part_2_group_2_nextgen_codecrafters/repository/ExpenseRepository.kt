package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao

class ExpenseRepository(private val dao: ExpenseDao) {

    suspend fun getExpensesForUser(userId: Int): List<Expense> {
        return dao.getExpensesForUser(userId)
    }

    suspend fun insert(expense: Expense) {
        dao.insertExpense(expense)
    }
}