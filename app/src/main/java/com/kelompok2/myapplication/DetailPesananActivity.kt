package com.kelompok2.myapplication

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.databinding.ActivityDetailPesananBinding

class DetailPesananActivity : AppCompatActivity() {

    private lateinit var find : ActivityDetailPesananBinding
    private val db by lazy { DBgoRent.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        find = ActivityDetailPesananBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(find.root)

        find.btnBackPsnan.setOnClickListener {onBackPressed()}

        var id = intent.getStringExtra("idPesanan").toString().toInt()

        val data = db.dao().getIDPesanan(id)[0]
        val dataKendaraan = db.dao().getAllKendaraanByMerk(data.kendaraan)[0]

        find.dataPnama.setText(data.nama_pemesan)
        find.dataPmerek.setText(data.kendaraan)
        find.dataPalamat.setText(data.alamat)
        find.dataPdurasi.setText(data.waktu_sewa.toString())
        find.dataPstatus.setText(data.status)
        if (dataKendaraan.jenis == "Motor") find.imgKendaraan.setImageResource(R.drawable.motor)




    }
}