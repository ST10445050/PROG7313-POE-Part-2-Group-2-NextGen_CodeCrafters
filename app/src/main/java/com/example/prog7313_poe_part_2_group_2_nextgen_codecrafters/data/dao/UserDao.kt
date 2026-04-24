package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User

@Dao
interface UserDao {

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun loginUser(username: String, password: String): User?

    @Query("UPDATE users SET password = :newPassword WHERE email = :email")
    suspend fun updatePassword(email: String, newPassword: String)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>
}