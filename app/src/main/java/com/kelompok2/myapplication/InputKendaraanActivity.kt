package com.kelompok2.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.kelompok2.myapplication.GoRent.AdapterKendaraan
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Kendaraan
import com.kelompok2.myapplication.databinding.ActivityInputKendaraanBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputKendaraanActivity : AppCompatActivity() {

    private lateinit var find : ActivityInputKendaraanBinding
    private val database by lazy { DBgoRent.getInstance(this) }
    private lateinit var selectedItem : String
    private var jenis : String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bg_elemen));
        }

//          navbar kembali
        find.btnKembaliKendaraan.setOnClickListener{onBackPressed()}

//          meletakkan data(idKendaraan(id))
        var id = intent.getStringExtra("idKendaraan")

//        identifikasi mode
        if (id == null){
            modeTambah()
        }else {
            modeEdit(id.toString().toInt())
        }

//        set spinner
        val data = arrayOf("Pilih Jenis", "Mobil", "Motor")
        val spinner = find.plhJns
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, data)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        val textView = spinner.getChildAt(0) as? TextView
        textView?.setTextColor(Color.RED)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
        spinner.setSelection(jenis.toInt())

//        ketika btn tambah di klik
        find.btnTmbh.setOnClickListener {

//            validasi jika kosong
            if (find.inputMerek.text.isNotEmpty() &&
                selectedItem !== "Pilih Jenis" &&
                find.inputHarga.text.isNotEmpty() &&
                find.inputTersedia.text.isNotEmpty()
            ){

//                mencegah eror ketika merk sama
                try {
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

                } catch (e : Exception){
                    alert("Merk tersebut sudah tersedia")
                }

            }else{
                alert("Isi data terlebih dahulu")
            }
        }

//        ketika btn update di klik
        find.btnUpdate.setOnClickListener {
            if (find.inputMerek.text.isNotEmpty()&&
                selectedItem !== "Pilih Jenis" &&
                find.inputHarga.text.isNotEmpty()&&
                find.inputTersedia.text.isNotEmpty()
                ){

//                mencegah eror ketika merk sama
                try {
                    database.dao().UpdateKendaraan(Kendaraan(
                        id.toString().toInt(),
                        find.inputMerek.text.toString(),
                        selectedItem,
                        find.inputHarga.text.toString().toInt(),
                        find.inputTersedia.text.toString().toInt())
                    )
                    onBackPressed()
                    alert("Data berhasil diubah")

                } catch (e : Exception) {
                    alert("Merk tersebut sudah tersedia")
                }

            }else{
                alert("Ubah data terlebih dahulu")
            }

        }

    }

//    fungtion pemberitahuan
    private fun alert(pesan: String) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show()
    }

//    mode edit
    private fun modeEdit(id:Int) {
        find.btnTmbh.visibility = View.GONE
        find.headingKendaraan.text="Edit Kendaraan"

        val dataKendaraan = database.dao().getKendaraanByID(id)[0]
        find.inputMerek.setText(dataKendaraan.merk)
        find.inputHarga.setText(dataKendaraan.harga_sewa.toString())
        find.inputTersedia.setText(dataKendaraan.persediaan.toString())
        jenis = if (dataKendaraan.jenis == "Mobil") "1" else "2"

    }

//    mode tambah
    private fun modeTambah() {
        find.btnUpdate.visibility = View.GONE
        find.headingKendaraan.text="Tambah Kendaraan"
    }

}