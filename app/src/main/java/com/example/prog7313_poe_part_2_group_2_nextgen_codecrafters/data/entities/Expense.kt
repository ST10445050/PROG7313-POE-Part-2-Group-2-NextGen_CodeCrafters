package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val expenseId: Int = 0,
    val userId: Int,
    val categoryId: Int,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val amount: Double,
    val photoPath: String?
)
