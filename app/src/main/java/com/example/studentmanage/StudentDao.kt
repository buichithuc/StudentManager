package com.example.studentmanage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
@Dao
interface StudentDao {
    @Insert
    suspend fun addStudent(student: sinhvien) :Long
    @Query("UPDATE Student SET hoten = :name, email = :email, sdt = :phone where mssv = :studentId" )
    suspend fun updateStudent(studentId: Int, name: String, email: String, phone: String) : Int
    @Delete
     suspend fun deleteStudent(student: sinhvien): Int
    @Query("delete from Student where mssv=:keyword")
    suspend fun deleteByMssv(keyword: Int):Int

}