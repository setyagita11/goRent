package com.kelompok2.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kelompok2.myapplication.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var find : ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(find.root)

        val username = intent.getStringExtra("username").toString()

        find.tvWellcome.text = "Hello, $username"

    }
}