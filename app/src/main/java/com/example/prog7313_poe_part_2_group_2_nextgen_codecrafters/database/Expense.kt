package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val category: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val description: String,
    val imageUri: String? = null
)