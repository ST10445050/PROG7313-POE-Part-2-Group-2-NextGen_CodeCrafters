package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget_goals")
data class BudgetGoal(

    @PrimaryKey(autoGenerate = true)
    val budgetGoalId: Int = 0,

    val userId: Int,
    val month: Int,
    val year: Int,
    val minimumGoal: Double,
    val maximumGoal: Double
)