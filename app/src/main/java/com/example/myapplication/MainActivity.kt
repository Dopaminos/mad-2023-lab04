package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openListActivity(view: View) {
        val intent = Intent(this, CardList::class.java)
        startActivity(intent)
    }

    fun openCreateActivity(view: View) {
        val intent = Intent(this, CreateActivity::class.java)
        startActivity(intent)
    }

    fun openViewActivity(view: View) {
        //val intent = Intent(this, ViewActivity::class.java)
        //startActivity(intent)
    }
}