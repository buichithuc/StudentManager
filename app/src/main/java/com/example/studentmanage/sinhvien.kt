package com.example.studentmanage

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "Student")
data class sinhvien(
    @PrimaryKey
    val mssv: Int,
    val hoten: String,
    val email: String,
    val sdt: String
)
