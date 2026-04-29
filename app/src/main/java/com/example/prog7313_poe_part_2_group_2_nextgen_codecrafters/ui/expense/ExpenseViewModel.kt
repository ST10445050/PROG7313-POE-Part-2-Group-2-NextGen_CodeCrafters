package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.ExpenseDao
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpenseViewModel(
    application: Application,
    private val expenseDao: ExpenseDao
) : AndroidViewModel(application) {

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            expenseDao.insertExpense(expense)
        }
    }

    fun getExpensesForUser(userId: Int): Flow<List<Expense>> {
        return expenseDao.getExpensesForUser(userId)
    }

    fun getExpensesForUserByDateRange(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<List<Expense>> {
        return expenseDao.getExpensesForUserByDateRange(
            userId = userId,
            startDate = startDate,
            endDate = endDate
        )
    }
}