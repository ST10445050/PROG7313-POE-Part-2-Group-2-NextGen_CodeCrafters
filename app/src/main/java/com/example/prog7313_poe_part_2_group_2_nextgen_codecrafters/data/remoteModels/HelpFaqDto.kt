package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HelpFaqDto(
    @SerialName("faq_id")
    val faqId: String? = null,

    val question: String,

    val answer: String,

    @SerialName("order_index")
    val orderIndex: Int
)