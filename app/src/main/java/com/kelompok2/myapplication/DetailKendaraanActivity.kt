package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.databinding.ActivityDetailKendaraanBinding
import com.kelompok2.myapplication.databinding.ActivityInputKendaraanBinding

class DetailKendaraanActivity : AppCompatActivity() {

    private val db by lazy { DBgoRent.getInstance(this) }
    private lateinit var find : ActivityDetailKendaraanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        find= ActivityDetailKendaraanBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(find.root)

        find.btnBack.setOnClickListener {onBackPressed()}

//        mengambil data dari database
        val id = intent.getStringExtra("idKendaraan").toString().toInt()
        val data = db.dao().getKendaraanByID(id)[0]

//        setdata ke layout
        find.dataKmerek.setText(data.merk)
        find.dataKharga.setText(data.harga_sewa.toString())
        find.dataKtersedia.setText(data.persediaan.toString())
        find.dataKjenis.setText(data.jenis)
        if (data.jenis == "Motor") find.imgKendaraan.setImageResource(R.drawable.motor)




    }
}