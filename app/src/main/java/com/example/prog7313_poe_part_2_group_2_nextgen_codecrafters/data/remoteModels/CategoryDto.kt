package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("category_id")
    val categoryId: String? = null,

    @SerialName("user_id")
    val userId: String,

    val name: String
)