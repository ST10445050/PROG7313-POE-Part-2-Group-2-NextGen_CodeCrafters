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
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import kotlinx.coroutines.launch

class ExpenseViewModel : ViewModel() {

    private val expenseRepository = ExpenseRepository()
    private val receiptStorageRepository = ReceiptStorageRepository()
    private val tag = "ExpenseViewModel"

    var expenses by mutableStateOf<List<ExpenseDto>>(emptyList())
        private set

    var filteredExpenses by mutableStateOf<List<ExpenseDto>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads all expenses for the current user.
     * This is used by the dashboard and expense list to keep spending data updated.
     */
    fun loadExpenses(userId: String) {
        AppLogger.debug(tag, "User requested expense list load.")

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                expenses = expenseRepository.getExpensesForUser(userId)

                AppLogger.info(
                    tag,
                    "Expense list loaded successfully. Count: ${expenses.size}"
                )

            } catch (e: Exception) {
                AppLogger.error(tag, "Expense list load failed.", e)
                errorMessage = e.message ?: "Could not load expenses"

            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Loads expenses within a selected date range.
     * This supports analytics, reports, and date-based filtering.
     */
    fun loadExpensesByDateRange(
        userId: String,
        startDate: String,
        endDate: String
    ) {
        AppLogger.debug(
            tag,
            "User requested filtered expense load from $startDate to $endDate."
        )

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                filteredExpenses = expenseRepository.getExpensesForUserByDateRange(
                    userId = userId,
                    startDate = startDate,
                    endDate = endDate
                )

                AppLogger.info(
                    tag,
                    "Filtered expenses loaded successfully. Count: ${filteredExpenses.size}"
                )

            } catch (e: Exception) {
                AppLogger.error(tag, "Filtered expense load failed.", e)
                errorMessage = e.message ?: "Could not load filtered expenses"

            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Adds a new expense.
     * If a receipt image is selected, it is uploaded first and the Supabase Storage URL is saved with the expense.
     */
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
        AppLogger.info(
            tag,
            "User started adding expense. Category: $categoryName, Amount: $amount"
        )

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                // Receipt upload is optional, so the app only uploads when the user selected an image.
                val uploadedPhotoUrl = if (imageUri != null) {
                    AppLogger.info(tag, "Receipt image selected. Upload started.")

                    val photoUrl = receiptStorageRepository.uploadReceipt(
                        context = context,
                        userId = userId,
                        imageUri = imageUri
                    )

                    AppLogger.info(tag, "Receipt upload completed successfully.")
                    photoUrl

                } else {
                    AppLogger.info(tag, "No receipt image selected. Continuing without receipt upload.")
                    null
                }

                // Save the final expense record after the optional receipt upload is complete.
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

                // Refresh the local state so the dashboard and expense list can show the latest saved record.
                expenses = expenseRepository.getExpensesForUser(userId)

                AppLogger.info(
                    tag,
                    "Expense added successfully and expense list refreshed. Count: ${expenses.size}"
                )

                onSuccess()

            } catch (e: Exception) {
                AppLogger.error(tag, "Expense add failed.", e)
                errorMessage = e.message ?: "Could not save expense"

            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Clears the current UI error message after it has been shown to the user.
     */
    fun clearError() {
        AppLogger.debug(tag, "Expense error message cleared.")
        errorMessage = null
    }
}