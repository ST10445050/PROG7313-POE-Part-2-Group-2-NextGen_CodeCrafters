package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val surname: String,
    val email: String,
    val username: String,
    val password: String
)