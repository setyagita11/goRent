package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kelompok2.myapplication.databinding.ActivityRecyclerViewKendaraanBinding

class RecyclerViewKendaraanActivity : AppCompatActivity() {

    private lateinit var find : ActivityRecyclerViewKendaraanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find=ActivityRecyclerViewKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnPesanan.setOnClickListener {
            onBackPressed()
            startActivity(
                Intent(this, RecyclerViewPesananActivity::class.java)
            )
        }

        find.btnHome.setOnClickListener { onBackPressed() }

    }
}