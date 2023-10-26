package com.kelompok2.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Kendaraan
import com.kelompok2.myapplication.GoRent.Pesanan
import com.kelompok2.myapplication.adapter.AdapterPesanan
import com.kelompok2.myapplication.databinding.ActivityInputPesananBinding

class InputPesananActivity : AppCompatActivity() {

    private lateinit var adapter: AdapterPesanan
    private val database by lazy { DBgoRent.getInstance(this) }
    private lateinit var selectedItem : String
    private var jenis : String = "0"

    private lateinit var find : ActivityInputPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnHome.setOnClickListener { onBackPressed() }

        val data = arrayOf("Status", "Sewa", "Selesai")

        val spinner = find.status
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


//        database = DBgoRent.getInstance(applicationContext)
        find.btnTmbhPsnan.setOnClickListener {

            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItem !== "Status" &&
                find.inputKndraan.text.isNotEmpty()

            ) {

                database.dao().InsertPesanan(
                    Pesanan(
                        0,
                        find.inputUsername.text.toString(),
                        find.inputAlamat.text.toString(),
                        find.inputKndraan.text.toString(),
                        find.plihKndraan.scrollX,
                        selectedItem
                    )
                )
                onBackPressed()
                alert("Data berhasil ditambahkan")
            }else{
                alert("isi data terlebih dahulu")
            }

        }

        find.btnUpdatePsnan.setOnClickListener {
            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItem !== "Status" &&
                find.inputKndraan.text.isNotEmpty()
            ) {

                database.dao().UpdatePesanan(
                    Pesanan(0,
                        find.inputUsername.text.toString(),
                        find.inputAlamat.text.toString(),
                        find.inputKndraan.text.toString(),
                        find.plihKndraan.scrollX,
                        selectedItem
                    )
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
}