package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseDto(
    @SerialName("expense_id")
    val expenseId: String? = null,

    @SerialName("user_id")
    val userId: String,

    @SerialName("category_id")
    val categoryId: String? = null,

    @SerialName("category_name")
    val categoryName: String,

    val date: String,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("end_time")
    val endTime: String,

    val description: String,

    val amount: Double,

    @SerialName("photo_path")
    val photoPath: String? = null
)