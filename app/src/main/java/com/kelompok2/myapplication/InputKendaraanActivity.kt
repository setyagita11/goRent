package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kelompok2.myapplication.GoRent.AdapterKendaraan
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Kendaraan
import com.kelompok2.myapplication.databinding.ActivityInputKendaraanBinding

class InputKendaraanActivity : AppCompatActivity() {

    private lateinit var find : ActivityInputKendaraanBinding
    private lateinit var adapter: AdapterKendaraan
    private lateinit var database: DBgoRent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        var id = intent.getStringExtra("idKendaraan")

        if (id == null){
            modeTambah()
        }else {
            modeEdit()
        }

        find.btnHome.setOnClickListener { onBackPressed() }

        find.btnPesanan.setOnClickListener{
            startActivity(
                Intent(this, RecyclerViewPesananActivity::class.java)
            )
        }

        val data = arrayOf("Pilih Jenis", "Mobil", "Motor")

        val spinner = find.plhJns
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        val selectedItem = spinner.selectedItem.toString()

        database = DBgoRent.getInstance(applicationContext)
        find.btnTmbh.setOnClickListener {

            if (find.inputMerek.text.isNotEmpty() &&
                selectedItem !== "Pilih Jenis" &&
                find.inputHarga.text.isNotEmpty() &&
                find.inputTersedia.text.isNotEmpty()
            ){

                database.dao().InsertKendaraan(
                    Kendaraan(0,
                        find.inputMerek.text.toString(),
                        selectedItem,
                        find.inputHarga.text.toString().toInt(),
                        find.inputTersedia.text.toString().toInt()
                    )
                )
                onBackPressed()

                alert("Data berhasil ditambahkan")
            }else{
                alert("Isi data terlebih dahulu")
            }
        }
    }
    private fun alert(msg: String) {

    }

    private fun modeEdit() {
        find.btnTmbh.visibility = View.GONE
        find.headingKendaraan.text="Edit Kendaraan"
    }

    private fun modeTambah() {
        find.btnUpdate.visibility = View.GONE
        find.headingKendaraan.text="Tambah Kendaraan"
    }

}