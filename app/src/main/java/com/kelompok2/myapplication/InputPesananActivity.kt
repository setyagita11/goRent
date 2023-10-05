package com.kelompok2.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kelompok2.myapplication.databinding.ActivityInputPesananBinding

class InputPesananActivity : AppCompatActivity() {

    private lateinit var find : ActivityInputPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnHome.setOnClickListener { onBackPressed() }

    }
}