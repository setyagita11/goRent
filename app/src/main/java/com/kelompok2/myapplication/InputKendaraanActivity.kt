package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kelompok2.myapplication.databinding.ActivityInputKendaraanBinding
import com.kelompok2.myapplication.databinding.ActivityInputPesananBinding

class InputKendaraanActivity : AppCompatActivity() {

    private lateinit var find : ActivityInputKendaraanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnHome.setOnClickListener { onBackPressed() }

        find.btnPesanan.setOnClickListener{
            startActivity(
                Intent(this, RecyclerViewPesananActivity::class.java)
            )
        }

    }
}