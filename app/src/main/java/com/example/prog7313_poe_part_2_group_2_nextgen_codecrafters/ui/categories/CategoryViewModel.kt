package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.CategoryDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository.CategoryRepository
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    private val categoryRepository = CategoryRepository()
    private val tag = "CategoryViewModel"

    var categories by mutableStateOf<List<CategoryDto>>(emptyList())
        private set

    var categoryName by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Loads all categories for the logged-in user.
     * Default categories are handled inside CategoryRepository if the user has none.
     */
    fun loadCategories(userId: String) {
        AppLogger.debug(tag, "User requested category load.")

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                categories = categoryRepository.getCategoriesForUser(userId)

                AppLogger.info(
                    tag,
                    "Categories loaded successfully. Count: ${categories.size}"
                )

            } catch (e: Exception) {
                AppLogger.error(tag, "Category load failed.", e)
                errorMessage = e.message ?: "Could not load categories"

            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Updates the local category input state as the user types.
     * This is not logged to avoid spamming Logcat on every key press.
     */
    fun onCategoryNameChange(newName: String) {
        categoryName = newName
    }

    /**
     * Saves a new category after validating that the field is not empty.
     * Validation failures are logged once when the user attempts to save.
     */
    fun saveCategory(userId: String) {
        AppLogger.info(tag, "User started saving category.")

        val cleanName = categoryName.trim()

        if (cleanName.isEmpty()) {
            AppLogger.warning(tag, "Category save validation failed: category name is empty.")
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

                // Clear the input after saving so the user can add another category easily.
                categoryName = ""

                // Refresh the list so the UI immediately shows the newly saved category.
                categories = categoryRepository.getCategoriesForUser(userId)

                AppLogger.info(
                    tag,
                    "Category saved successfully and category list refreshed. Count: ${categories.size}"
                )

            } catch (e: Exception) {
                AppLogger.error(tag, "Category save failed.", e)
                errorMessage = e.message ?: "Could not save category"

            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Deletes a category and reloads the list afterwards.
     * This keeps the UI state aligned with the Supabase database.
     */
    fun deleteCategory(userId: String, categoryId: String) {
        AppLogger.info(tag, "User started deleting category.")

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                categoryRepository.deleteCategory(categoryId)

                categories = categoryRepository.getCategoriesForUser(userId)

                AppLogger.info(
                    tag,
                    "Category deleted successfully and category list refreshed. Count: ${categories.size}"
                )

            } catch (e: Exception) {
                AppLogger.error(tag, "Category delete failed.", e)
                errorMessage = e.message ?: "Could not delete category"

            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Clears the current UI error message after it has been displayed.
     */
    fun clearError() {
        AppLogger.debug(tag, "Category error message cleared.")
        errorMessage = null
    }
}