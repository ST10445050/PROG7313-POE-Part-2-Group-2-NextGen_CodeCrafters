package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.remoteModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,

    val name: String,
    val surname: String,
    val email: String,
    val gender: String,
    val phone: String,
    val username: String,

    @SerialName("full_name")
    val fullName: String
)