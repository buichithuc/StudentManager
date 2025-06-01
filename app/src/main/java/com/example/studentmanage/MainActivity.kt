package com.example.studentmanage

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnCreateContextMenuListener
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var listStudent : ListView
    private val students = mutableListOf<Student>()
    val adapter = StudentAdapter(students)
    lateinit var db: SQLiteDatabase

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val data = it.data!!
            val name = data.getStringExtra("name") ?: ""
            val studentId = data.getStringExtra("studentId") ?: ""
            val email = data.getStringExtra("email") ?: ""
            val sdt = data.getStringExtra("sdt") ?: ""

            students.add(Student(name, studentId, email, sdt))
            listStudent.adapter = adapter
            db.execSQL("insert into tblStudent(id, name, phone, email) values (?, ?, ?, ?)", arrayOf(studentId, name, sdt, email))
        }
    }
    private val updateLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val data = result.data!!
            val name = data.getStringExtra("name") ?: ""
            val studentId = data.getStringExtra("studentId") ?: ""
            val email = data.getStringExtra("email") ?: ""
            val sdt = data.getStringExtra("sdt") ?: ""

            val position = data.getIntExtra("position", -1)
            if (position != -1) {
                students[position] = Student(name, studentId, email, sdt)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Cập nhật sinh viên thành công!", Toast.LENGTH_SHORT).show()
            }
            db.execSQL("update tblStudent set name = ?, phone = ?, email = ? where id = ?", arrayOf(name, sdt, email))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listStudent = findViewById(R.id.listview)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        registerForContextMenu(listStudent)

        db = SQLiteDatabase.openDatabase(filesDir.path + "/mydb", null, SQLiteDatabase.CREATE_IF_NECESSARY)
        createTable()

    }

    fun createTable(){
        try{
            db.execSQL("create table tblStudent (" +
            "id integer primary key autoincrement," +
            "name text," + "phone text," +"email text)")
        }catch(ex: Exception){
            ex.printStackTrace()
        }
    }


    override fun onCreateContextMenu(menu : ContextMenu, v : View, menuInfo : ContextMenu.ContextMenuInfo){
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val infor = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val selectedStudent = students[infor.position]
        return when(item.itemId){
            R.id.action_update -> {
                val intent = Intent(this, FormStudent::class.java).apply {
                    putExtra("name", selectedStudent.hoten)
                    putExtra("studentId", selectedStudent.mssv)
                    putExtra("email", selectedStudent.email)
                    putExtra("sdt", selectedStudent.sdt)
                    putExtra("position", infor.position)
                }
                updateLauncher.launch(intent)
                true
            }
            R.id.action_delete -> {
                val deletedStudent = students[infor.position]
                students.removeAt(infor.position)
                db.execSQL("delete from tblStudent where id = ?", arrayOf(deletedStudent.mssv))
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Đã xóa: ${deletedStudent.hoten}", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_email -> {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(selectedStudent.email))
                    putExtra(Intent.EXTRA_SUBJECT, "Thông báo từ ứng dụng")
                    putExtra(Intent.EXTRA_TEXT, "Xin chào ${selectedStudent.hoten},...")
                }
                startActivity(Intent.createChooser(emailIntent, "Chọn ứng dụng gửi Email"))
                true
            }
            R.id.action_call -> {
                val callIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = android.net.Uri.parse("tel:${selectedStudent.sdt}")
                }
                startActivity(callIntent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                val intent = Intent(this, FormStudent::class.java)
                launcher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
