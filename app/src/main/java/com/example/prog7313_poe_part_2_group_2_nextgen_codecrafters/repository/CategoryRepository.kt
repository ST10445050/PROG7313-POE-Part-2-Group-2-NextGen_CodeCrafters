package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.repository

import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remote.SupabaseClientProvider
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels.CategoryDto
import io.github.jan.supabase.postgrest.from

class CategoryRepository {

    private val supabase = SupabaseClientProvider.client

    suspend fun getCategoriesForUser(userId: String): List<CategoryDto> {
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
                seedDefaultCategories(userId)

                supabase.from("categories")
                    .select {
                        filter {
                            eq("user_id", userId)
                        }
                    }
                    .decodeList<CategoryDto>()
                    .sortedBy { it.name.lowercase() }
            } else {
                categories
            }

        } catch (e: Exception) {
            throw Exception("Failed to load categories: ${e.message}")
        }
    }

    private suspend fun seedDefaultCategories(userId: String) {
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

        supabase.from("categories").insert(defaultCategories)
    }

    suspend fun addCategory(
        userId: String,
        name: String
    ) {
        try {
            supabase.from("categories").insert(
                CategoryDto(
                    userId = userId,
                    name = name.trim()
                )
            )
        } catch (e: Exception) {
            throw Exception("Failed to save category: ${e.message}")
        }
    }

    suspend fun deleteCategory(categoryId: String) {
        try {
            supabase.from("categories")
                .delete {
                    filter {
                        eq("category_id", categoryId)
                    }
                }
        } catch (e: Exception) {
            throw Exception("Failed to delete category: ${e.message}")
        }
    }
}