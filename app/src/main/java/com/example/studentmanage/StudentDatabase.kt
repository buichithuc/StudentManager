package com.example.studentmanage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile
@Database(entities = [sinhvien::class], version = 1)
abstract class StudentDatabase : RoomDatabase() {
    abstract fun studentDao() : StudentDao
    companion object{
        @Volatile
        private var INSTANCE: StudentDatabase? =null
        fun getInstance(context: Context): StudentDatabase{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StudentDatabase::class.java,
                    "students_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{INSTANCE =it}
            }
        }

    }

}