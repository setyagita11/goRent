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

    private lateinit var find : ActivityInputPesananBinding
    private lateinit var selectedItemStatus : String
    private lateinit var selectedItemKendaraan : String
    private val database by lazy { DBgoRent.getInstance(this) }
    private var opsiKendaraan : String = "null"
    private var opsiStatus : String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityInputPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

        find.btnKembali.setOnClickListener {onBackPressed()}

//        mengambil id pesanan
        val id = intent.getStringExtra("idPesanan")

//        indentifikasi mode
        if (id==null){
            modeTambah()
        }else{
            modeEdit(id.toString().toInt())
        }

//        set spiner status
        val dataStatus = arrayOf("Status", "Sewa", "Selesai")
        val spnStatus = find.status
        val spnStatusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dataStatus)
        spnStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnStatus.adapter = spnStatusAdapter
        spnStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemStatus = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        spnStatus.setSelection(opsiStatus.toInt())

//        set spinner kendaraan
        val dataMerk = database.dao().getAllMerk()
        val newData = arrayOf("Pilih Kendaraan") + dataMerk
        val spnMerk = find.plihKndraan
        val spnMerkAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, newData)
        spnMerkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnMerk.adapter = spnMerkAdapter
        spnMerk.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItemKendaraan = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        val indexSpnKendaraan = if (opsiKendaraan == "null") 0 else newData.indexOf(opsiKendaraan)
        spnMerk.setSelection(indexSpnKendaraan)

//        ketika btn tambah di klik
        find.btnTmbhPsnan.setOnClickListener {

            val idKendaraan = database.dao().getID(selectedItemKendaraan)
            val dataKdrn = database.dao().getKendaraanByID(idKendaraan)[0]

//            validasi jika kosong
            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItemKendaraan !== "Pilih Kendaraan" &&
                selectedItemStatus !== "Status" &&
                find.inputWaktuSewa.text.isNotEmpty()

            ) {

//                validasi ketika persediaan kosong
                if (dataKdrn.persediaan != 0) {

                    database.dao().InsertPesanan(
                        Pesanan(
                            0,
                            find.inputUsername.text.toString(),
                            find.inputAlamat.text.toString(),
                            idKendaraan,
                            find.inputWaktuSewa.text.toString().toInt(),
                            selectedItemStatus
                        )
                    )

//                    mengubah persediaan
                    if (selectedItemStatus == "Sewa"){
                        val newPersediaan = dataKdrn.persediaan - 1
                        database.dao().updatePersediaan(newPersediaan, dataKdrn.merk)
                    }

                    onBackPressed()
                    alert("Data berhasil ditambahkan")
                } else {
                    alert("Kendaraan yang anda pilih tidak tersedia")
                }

            }else{
                alert("isi data terlebih dahulu")
            }
        }

//        ketika btn update di klik
        find.btnUpdatePsnan.setOnClickListener {

            val idKendaraan = database.dao().getID(selectedItemKendaraan)
            val dataKdrn = database.dao().getKendaraanByID(idKendaraan)[0]
            var newPersediaan : Int

//            validasi jika kosong
            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItemKendaraan !== "Pilih Kendaraan" &&
                selectedItemStatus !== "Status" &&
                find.inputWaktuSewa.text.isNotEmpty()
            ) {

//                mengubah persediaan
                if (selectedItemStatus == "Selesai"){
                    newPersediaan = dataKdrn.persediaan + 1
                } else {
                    newPersediaan = dataKdrn.persediaan - 1
                }

//                validasi ketika persediaan menjadi minus
                if (newPersediaan >= 0){
                    database.dao().UpdatePesanan(Pesanan(
                        id.toString().toInt(),
                        find.inputUsername.text.toString(),
                        find.inputAlamat.text.toString(),
                        idKendaraan,
                        find.inputWaktuSewa.text.toString().toInt(),
                        selectedItemStatus
                    )
                    )
                    database.dao().updatePersediaan(newPersediaan, dataKdrn.merk)
                    onBackPressed()
                    alert("Data berhasil diubah")
                } else {
                    alert("Tidak dapat diubah karena persediaan kosong")
                }
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

        val dataPesanan = database.dao().getIDPesanan(id)[0]
        val dataKdrn = database.dao().getKendaraanByID(dataPesanan.id)[0]

        find.inputUsername.setText(dataPesanan.nama_pemesan)
        find.inputAlamat.setText(dataPesanan.alamat)
        find.inputWaktuSewa.setText(dataPesanan.waktu_sewa.toString())
        opsiStatus = if (dataPesanan.status=="Sewa") "1" else "2"
        opsiKendaraan = dataKdrn.merk
    }
    private fun modeTambah() {
        find.btnUpdatePsnan.visibility = View.GONE
        find.headingPesanan.text="Tambah Pesanan"

    }

}