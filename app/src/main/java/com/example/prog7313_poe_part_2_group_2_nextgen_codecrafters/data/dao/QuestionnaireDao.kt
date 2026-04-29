package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers

@Dao
interface QuestionnaireDao {

    @Insert
    suspend fun insertAnswers(answers: QuestionnaireAnswers)

    @Query("SELECT * FROM questionnaire_answers WHERE userId = :userId LIMIT 1")
    suspend fun getAnswersByUserId(userId: Int): QuestionnaireAnswers?

    @Query("DELETE FROM questionnaire_answers WHERE userId = :userId")
    suspend fun deleteAnswersByUserId(userId: Int)
}