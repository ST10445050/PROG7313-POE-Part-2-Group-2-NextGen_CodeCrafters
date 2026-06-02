package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.expense

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.ExpenseDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ExpenseRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.ReceiptStorageRepository
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {

    private val expenseRepository = ExpenseRepository()
    private val receiptStorageRepository = ReceiptStorageRepository()

    var expenses by mutableStateOf<List<ExpenseDto>>(emptyList())
        private set

    var filteredExpenses by mutableStateOf<List<ExpenseDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadExpenses(userId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                expenses = expenseRepository.getExpensesForUser(userId)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Could not load expenses"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadExpensesByDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                filteredExpenses = expenseRepository.getExpensesForUserByDateRange(
                    userId = userId,
                    startDate = startDate,
                    endDate = endDate
                )
            } catch (e: Exception) {
                errorMessage = e.message ?: "Could not load filtered expenses"
            } finally {
                isLoading = false
            }
        }
    }

    fun addExpense(
        context: Context,
        userId: String,
        categoryId: String?,
        categoryName: String,
        date: String,
        startTime: String,
        endTime: String,
        description: String,
        amount: Double,
        imageUri: Uri?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val uploadedPhotoUrl = if (imageUri != null) {
                    receiptStorageRepository.uploadReceipt(
                        context = context,
                        userId = userId,
                        imageUri = imageUri
                    )
                } else {
                    null
                }

                expenseRepository.insertExpense(
                    ExpenseDto(
                        userId = userId,
                        categoryId = categoryId,
                        categoryName = categoryName,
                        date = date,
                        startTime = startTime,
                        endTime = endTime,
                        description = description,
                        amount = amount,
                        photoPath = uploadedPhotoUrl
                    )
                )

                expenses = expenseRepository.getExpensesForUser(userId)

                onSuccess()

            } catch (e: Exception) {
                errorMessage = e.message ?: "Could not save expense"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}