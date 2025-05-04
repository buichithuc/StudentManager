package com.example.studentmanage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FormStudent : AppCompatActivity() {
    lateinit var ten: EditText
    lateinit var mssv: EditText
    lateinit var email: EditText
    lateinit var sdt: EditText
    lateinit var add: Button

    var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_student)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ten = findViewById(R.id.edittext_name)
        mssv = findViewById(R.id.edittext_mssv)
        email = findViewById(R.id.edittext_email)
        sdt = findViewById(R.id.edittext_sdt)
        add = findViewById(R.id.button)

        intent.getStringExtra("name")?.let { ten.setText(it) }
        intent.getStringExtra("studentId")?.let { mssv.setText(it) }
        intent.getStringExtra("email")?.let { email.setText(it) }
        intent.getStringExtra("sdt")?.let { sdt.setText(it) }
        position = intent.getIntExtra("position", -1)


        add.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("name", ten.text.toString())
                putExtra("studentId", mssv.text.toString())
                putExtra("email", email.text.toString())
                putExtra("sdt", sdt.text.toString())
                putExtra("position", position)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
