package com.kelompok2.myapplication

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Kendaraan
import com.kelompok2.myapplication.adapter.AdapterPesanan
import com.kelompok2.myapplication.databinding.ActivityInputPesananBinding

class InputPesananActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterPesanan
    private lateinit var database: DBgoRent

    private lateinit var find : ActivityInputPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnHome.setOnClickListener { onBackPressed() }
        database = DBgoRent.getInstance(applicationContext)
        find.btnTmbhPsnan.setOnClickListener {

            if (find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                find.inputKndraan.text.isNotEmpty()

            ) {

                database.dao().InsertKendaraan(
                    Kendaraan(
                        0,
                        find.inputUsername.text.toString(),
                        find.inputAlamat.text.toString(),
                        find.inputKndraan.text.toString().toInt(),
                        find.plihKndraan.scrollX
                    )
                )
            } else {
                startActivity(
                    Intent(this, RecyclerViewPesananActivity::class.java)
                )
            }
        }
    }
}