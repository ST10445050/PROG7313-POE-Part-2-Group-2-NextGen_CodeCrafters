package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// Team: Uncomment these once you have created your DAO and Entity files
// import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.QuestionnaireDao
// import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.dao.UserDao
// import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.QuestionnaireAnswers
// import com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.data.entities.User

@Database(
    entities = [], // Add your entity classes here (e.g., User::class)
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Team: Uncomment these functions once the corresponding DAO interfaces are created
    // abstract fun userDao(): UserDao
    // abstract fun questionnaireDao(): QuestionnaireDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fintrack_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
