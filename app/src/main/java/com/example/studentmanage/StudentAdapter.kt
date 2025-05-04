package com.example.studentmanage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class StudentAdapter(val students : List<Student>) : BaseAdapter(){
    override fun getCount() = students.size

    override fun getItem(position: Int) = students[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       val itemView : View
       val viewHolder : ViewHolder
       if(convertView == null){
           itemView = LayoutInflater.from(parent?.context).inflate(R.layout.student_infor,parent, false)
           viewHolder = ViewHolder()
           viewHolder.hoten = itemView.findViewById<TextView>(R.id.hoten)
           viewHolder.mssv = itemView.findViewById<TextView>(R.id.mssv)
           itemView.tag = viewHolder
       }else{
           itemView = convertView
           viewHolder = convertView.tag as ViewHolder
       }
        val student = students[position]
        viewHolder.hoten.text = student.hoten
        viewHolder.mssv.text = student.mssv
        return itemView
    }
    class ViewHolder{
        lateinit var hoten : TextView
        lateinit var mssv : TextView
    }

}