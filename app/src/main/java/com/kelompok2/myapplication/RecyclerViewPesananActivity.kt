package com.kelompok2.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kelompok2.myapplication.databinding.ActivityRecyclerViewKendaraanBinding
import com.kelompok2.myapplication.databinding.ActivityRecyclerviewPesananBinding

class RecyclerViewPesananActivity : AppCompatActivity() {

    private lateinit var find : ActivityRecyclerviewPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityRecyclerviewPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnKendaraan.setOnClickListener {
            onBackPressed()
            startActivity(
                Intent(this, RecyclerViewKendaraanActivity::class.java)
            )
        }

        find.btnHome.setOnClickListener { onBackPressed() }
    }
}