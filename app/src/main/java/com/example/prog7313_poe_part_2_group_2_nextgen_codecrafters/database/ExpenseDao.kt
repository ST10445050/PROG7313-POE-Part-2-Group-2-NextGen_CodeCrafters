package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): LiveData<List<Expense>>

    @Delete
    suspend fun delete(expense: Expense)
}