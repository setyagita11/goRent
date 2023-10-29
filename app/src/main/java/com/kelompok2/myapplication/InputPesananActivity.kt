package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    private var opsiStatus : String = "0"
    private lateinit var find : ActivityInputPesananBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

        var id = intent.getStringExtra("idPesanan")

        if (id==null){
            modeTambah()
        }else{
            modeEdit(id.toString().toInt())
        }

        find.btnKembali.setOnClickListener {onBackPressed()}

        val dataStatus = arrayOf("Status", "Sewa", "Selesai")

        val spinnerStatus = find.status
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataStatus)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = spinnerAdapter

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        spinnerStatus.setSelection(opsiStatus.toInt())


//        database = DBgoRent.getInstance(applicationContext)
        find.btnTmbhPsnan.setOnClickListener {

            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItem !== "Status" &&
                find.inputWaktuSewa.text.isNotEmpty()

            ) {

                database.dao().InsertPesanan(
                    Pesanan(
                        0,
                        find.inputUsername.text.toString(),
                        find.inputAlamat.text.toString(),
                        find.plihKndraan.scrollX.toString(),
                        find.inputWaktuSewa.text.toString().toInt(),
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
                find.inputWaktuSewa.text.isNotEmpty()
            ) {

                database.dao().UpdatePesanan(Pesanan(
                    id.toString().toInt(),
                        find.inputUsername.text.toString(),
                        find.inputAlamat.text.toString(),
                        find.plihKndraan.scrollX.toString(),
                        find.inputWaktuSewa.text.toString().toInt(),
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

    private fun modeEdit(id:Int) {
        find.btnTmbhPsnan.visibility = View.GONE
        find.headingPesanan.text="Edit Pesanan"

        val data = database.dao().getIDPesanan(id)[0]
        find.inputUsername.setText(data.nama_pemesan)
        find.inputAlamat.setText(data.alamat)
        find.inputWaktuSewa.setText(data.waktu_sewa.toString())
        opsiStatus = if (data.status=="Sewa") "1" else "2"
    }
    private fun modeTambah() {
        find.btnUpdatePsnan.visibility = View.GONE
        find.headingPesanan.text="Tambah Pesanan"
    }
}