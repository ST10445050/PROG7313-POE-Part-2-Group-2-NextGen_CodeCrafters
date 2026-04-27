package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questionnaire_answers")
data class QuestionnaireAnswers(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val employmentStatus: String,
    val monthlyIncome: Double,
    val spendingCategories: String,
    val financialGoal: String,
    val monthlySavingsGoal: Double,
    val dashboardType: String
)