package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.model

data class ExpenseWithCategory(
    val expenseId: Int,
    val userId: Int,
    val categoryId: Int,
    val categoryName: String,
    val date: String,
    val amount: Double
)