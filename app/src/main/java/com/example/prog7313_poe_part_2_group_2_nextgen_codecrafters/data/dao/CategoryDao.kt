package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    // Inserts a new category into RoomDB.
    @Insert
    suspend fun insertCategory(category: Category)

    // Loads all categories from RoomDB in alphabetical order.
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>

    // Deletes a category using its ID.
    @Query("DELETE FROM categories WHERE categoryId = :categoryId")
    suspend fun deleteCategory(categoryId: Int)

    // Checks whether a category already exists before inserting it.
    // COLLATE NOCASE means Food, food, and FOOD are treated as the same category.
    @Query("""
        SELECT * FROM categories
        WHERE name = :name COLLATE NOCASE
        LIMIT 1
    """)
    suspend fun getCategoryByName(name: String): Category?
}