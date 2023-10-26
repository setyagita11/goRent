package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    private lateinit var adapter: AdapterKendaraan
    private val database by lazy { DBgoRent.getInstance(this) }
    private lateinit var selectedItem : String
    private var jenis : String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        var id = intent.getStringExtra("idKendaraan")

        if (id == null){
            modeTambah()
        }else {
            modeEdit(id.toString().toInt())
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

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinner.setSelection(jenis.toInt())

//        val selectedItem = spinner.selectedItem.toString()

//        database = DBgoRent.getInstance(applicationContext)
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

        find.btnUpdate.setOnClickListener {
            if (find.inputMerek.text.isNotEmpty()&&
                selectedItem !== "Pilih Jenis" &&
                find.inputHarga.text.isNotEmpty()&&
                find.inputTersedia.text.isNotEmpty()){

                database.dao().UpdateKendaraan(Kendaraan(
                    id.toString().toInt(),
                    find.inputMerek.text.toString(),
                    selectedItem,
                    find.inputHarga.text.toString().toInt(),
                    find.inputTersedia.text.toString().toInt())
                )
                onBackPressed()
                alert("Data berhasil diubah")
            }else{
                alert("Ubah data terlebih dahulu")
            }

        }

    }
    private fun alert(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun modeEdit(id:Int) {
        find.btnTmbh.visibility = View.GONE
        find.headingKendaraan.text="Edit Kendaraan"

        val data = database.dao().getIDkendaraan(id)[0]
        find.inputMerek.setText(data.merk)
        find.inputHarga.setText(data.harga_sewa.toString())
        find.inputTersedia.setText(data.persediaan.toString())
        jenis = if (data.jenis == "Mobil") "1" else "2"

    }

    private fun modeTambah() {
        find.btnUpdate.visibility = View.GONE
        find.headingKendaraan.text="Tambah Kendaraan"

    }

}