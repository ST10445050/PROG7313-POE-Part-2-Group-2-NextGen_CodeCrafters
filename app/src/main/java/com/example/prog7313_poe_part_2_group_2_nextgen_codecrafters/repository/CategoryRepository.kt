package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.CategoryDto
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils.AppLogger
import io.github.jan.supabase.postgrest.from

class CategoryRepository {

    private val supabase = SupabaseClientProvider.client
    private val tag = "CategoryRepository"

    suspend fun getCategoriesForUser(userId: String): List<CategoryDto> {
        AppLogger.info(tag, "Loading categories.")

        return try {
            val categories = supabase.from("categories")
                .select {
                    filter {
                        eq("user_id", userId)
                    }
                }
                .decodeList<CategoryDto>()
                .sortedBy { it.name.lowercase() }

            if (categories.isEmpty()) {
                AppLogger.info(tag, "No categories found. Seeding default categories.")

                seedDefaultCategories(userId)

                val seededCategories = supabase.from("categories")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                    }
                    .decodeList<CategoryDto>()
                    .sortedBy { it.name.lowercase() }

                AppLogger.info(
                    tag,
                    "Default categories seeded and loaded. Count: ${seededCategories.size}"
                )

                seededCategories
            } else {
                AppLogger.info(
                    tag,
                    "Categories loaded successfully. Count: ${categories.size}"
                )

                categories
            }

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to load categories.", e)
            throw Exception("Failed to load categories: ${e.message}")
        }
    }

    private suspend fun seedDefaultCategories(userId: String) {
        AppLogger.info(tag, "Default category seed started.")

        val defaultCategories = listOf(
            CategoryDto(
                userId = userId,
                name = "Food"
            ),
            CategoryDto(
                userId = userId,
                name = "Transport"
            ),
            CategoryDto(
                userId = userId,
                name = "Groceries"
            )
        )

        try {
            supabase.from("categories").insert(defaultCategories)
            AppLogger.info(tag, "Default categories saved successfully.")
        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to seed default categories.", e)
            throw Exception("Failed to seed default categories: ${e.message}")
        }
    }

    suspend fun addCategory(
        userId: String,
        name: String
    ) {
        AppLogger.info(tag, "Saving category.")

        try {
            supabase.from("categories").insert(
                CategoryDto(
                    userId = userId,
                    name = name.trim()
                )
            )

            AppLogger.info(tag, "Category saved successfully.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to save category.", e)
            throw Exception("Failed to save category: ${e.message}")
        }
    }

    suspend fun deleteCategory(categoryId: String) {
        AppLogger.info(tag, "Deleting category.")

        try {
            supabase.from("categories")
                .delete {
                    filter {
                        eq("category_id", categoryId)
                    }
                }

            AppLogger.info(tag, "Category deleted successfully.")

        } catch (e: Exception) {
            AppLogger.error(tag, "Failed to delete category.", e)
            throw Exception("Failed to delete category: ${e.message}")
        }
    }
}