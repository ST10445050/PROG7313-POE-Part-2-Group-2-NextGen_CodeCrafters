package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.ui.categories

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database.AppDatabase
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Category
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    // Loads all categories from RoomDB.
    // This includes both the default categories and user-created categories.
    val categories = categoryDao.getAllCategories()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    var showAddCategoryBox by mutableStateOf(true)
        private set

    var categoryName by mutableStateOf("")
        private set

    init {
        // Inserts the default FinTrack categories when the ViewModel starts.
        // These categories will be available for every user when using the app.
        seedDefaultCategories()
    }

    private fun seedDefaultCategories() {
        viewModelScope.launch {
            val existingCategories = categories.value.map { it.name.lowercase().trim() }

            val defaultCategories = listOf(
                "Food",
                "Transport",
                "Groceries"
            )

            defaultCategories.forEach { defaultName ->
                if (!existingCategories.contains(defaultName.lowercase())) {
                    categoryDao.insertCategory(
                        Category(name = defaultName)
                    )
                }
            }
        }
    }

    fun onAddCategoryClick() {
        showAddCategoryBox = true
    }

    fun onCategoryNameChange(newName: String) {
        categoryName = newName
    }

    fun cancelAddCategory() {
        categoryName = ""
        showAddCategoryBox = true
    }

    fun saveCategory() {
        val cleanName = categoryName.trim()

        if (cleanName.isNotEmpty()) {
            viewModelScope.launch {
                categoryDao.insertCategory(
                    Category(name = cleanName)
                )

                categoryName = ""
                showAddCategoryBox = true
            }
        }
    }
}