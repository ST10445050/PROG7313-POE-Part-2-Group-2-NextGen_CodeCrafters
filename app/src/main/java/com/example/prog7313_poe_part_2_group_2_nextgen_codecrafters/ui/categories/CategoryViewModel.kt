package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.CategoryDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.CategoryRepository
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val categoryRepository = CategoryRepository()

    var categories by mutableStateOf<List<CategoryDto>>(emptyList())
        private set

    var categoryName by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadCategories(userId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                categories = categoryRepository.getCategoriesForUser(userId)
            } catch (e: Exception) {
                errorMessage = e.message ?: "Could not load categories"
            } finally {
                isLoading = false
            }
        }
    }

    fun onCategoryNameChange(newName: String) {
        categoryName = newName
    }

    fun saveCategory(userId: String) {
        val cleanName = categoryName.trim()

        if (cleanName.isEmpty()) {
            errorMessage = "Please enter a category name"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                categoryRepository.addCategory(
                    userId = userId,
                    name = cleanName
                )

                categoryName = ""
                categories = categoryRepository.getCategoriesForUser(userId)

            } catch (e: Exception) {
                errorMessage = e.message ?: "Could not save category"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteCategory(userId: String, categoryId: String) {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                categoryRepository.deleteCategory(categoryId)
                categories = categoryRepository.getCategoriesForUser(userId)

            } catch (e: Exception) {
                errorMessage = e.message ?: "Could not delete category"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }
}