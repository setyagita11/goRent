package com.kelompok2.myapplication

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Pesanan
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bg_elemen));
        }

//        btn kembali
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
            override fun onNothingSelected(p0: AdapterView<*>?) { }
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
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
        val indexSpnKendaraan = if (opsiKendaraan == "null") 0 else newData.indexOf(opsiKendaraan)
        spnMerk.setSelection(indexSpnKendaraan)

//        ketika btn tambah di klik
        find.btnTmbhPsnan.setOnClickListener {

//            validasi jika kosong
            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItemKendaraan !== "Pilih Kendaraan" &&
                selectedItemStatus !== "Status" &&
                find.inputWaktuSewa.text.isNotEmpty()

            ) {

                val idKendaraan = database.dao().getID(selectedItemKendaraan)
                val dataKdrn = database.dao().getKendaraanByID(idKendaraan)[0]

//                validasi ketika persediaan kosong
                if (dataKdrn.persediaan != 0) {
                    insertKdrn(idKendaraan, dataKdrn.persediaan)
                } else {
                    alert("Kendaraan yang anda pilih tidak tersedia")
                }

            }else{
                alert("isi data terlebih dahulu")
            }
        }

//        ketika btn update di klik
        find.btnUpdatePsnan.setOnClickListener {

//            validasi jika kosong
            if (
                find.inputUsername.text.isNotEmpty() &&
                find.inputAlamat.text.isNotEmpty() &&
                selectedItemKendaraan !== "Pilih Kendaraan" &&
                selectedItemStatus !== "Status" &&
                find.inputWaktuSewa.text.isNotEmpty()
            ) {
//                mengambil data lama
                val dataPesanan = database.dao().getIDPesanan(id.toString().toInt())[0]
                val oldDataKdrn = database.dao().getKendaraanByID(dataPesanan.id_kendaraan)[0]
//                mengambil data baru
                val idKendaraan = database.dao().getID(selectedItemKendaraan)
                val newDataKdrn = database.dao().getKendaraanByID(idKendaraan)[0]
//                initial persediaan
                var newPersediaan: Int
                var oldPersediaan: Int
//                ketika kendaraan tidak diubah
                if (oldDataKdrn.id == newDataKdrn.id) {
                    when {
//                        ketika status tidak diubah
                        dataPesanan.status == selectedItemStatus -> {
                            update(id.toString().toInt(), oldDataKdrn.id)
                        }
//                        ketika status diubah ke sewa
                        dataPesanan.status == "Selesai" && selectedItemStatus == "Sewa" -> {
                            newPersediaan = oldDataKdrn.persediaan - 1
//                            validasi ketika persediaan menjadi minus
                            if (newPersediaan >= 0){
//                              menggubah persediaan dan update
                                updatePersediaan(newPersediaan, oldDataKdrn.id)
                                update(id.toString().toInt(), oldDataKdrn.id)
                            } else {
                                alert("Tidak dapat diubah karena persediaan kosong")
                            }
                        }
//                        ketika status diubah ke selesai
                        dataPesanan.status == "Sewa" && selectedItemStatus == "Selesai" -> {
                            newPersediaan = oldDataKdrn.persediaan + 1
//                            validasi ketika persediaan menjadi minus
                            if (newPersediaan >= 0){
//                              menggubah persediaan dan update
                                updatePersediaan(newPersediaan, oldDataKdrn.id)
                                update(id.toString().toInt(), oldDataKdrn.id)
                            } else {
                                alert("Tidak dapat diubah karena persediaan kosong")
                            }
                        }
                    }
//                    ketika kendaraan diubah
                } else {
                    when {
//                        ketika status masih tetap selesai
                        dataPesanan.status == "Selesai" && selectedItemStatus == "Selesai" -> {
                            update(id.toString().toInt(), newDataKdrn.id)
                        }
//                        ketika status masih tetap sewa
                        dataPesanan.status == "Sewa" && selectedItemStatus == "Sewa" -> {
                            newPersediaan = newDataKdrn.persediaan - 1
                            oldPersediaan = oldDataKdrn.persediaan + 1
//                            validasi ketika persediaan menjadi minus
                            if (newPersediaan >= 0) {
//                              menggubah persediaan dan update
                                updatePersediaan(newPersediaan, newDataKdrn.id)
                                updatePersediaan(oldPersediaan, oldDataKdrn.id)
                                update(id.toString().toInt(), newDataKdrn.id)
                            } else {
                                alert("Tidak dapat diubah karena persediaan kosong")
                            }
                        }
//                        ketika status diubah menjadi sewa
                        dataPesanan.status == "Selesai" && selectedItemStatus == "Sewa" -> {
                            newPersediaan = newDataKdrn.persediaan - 1
//                            validasi ketika persediaan menjadi minus
                            if (newPersediaan >= 0) {
//                              menggubah persediaan dan update
                                updatePersediaan(newPersediaan, newDataKdrn.id)
                                update(id.toString().toInt(), newDataKdrn.id)
                            } else {
                                alert("Tidak dapat diubah karena persediaan kosong")
                            }
                        }
//                        ketika status diubah menjadi selesai
                        dataPesanan.status == "Sewa" && selectedItemStatus == "Selesai" -> {
//                            menggubah persediaan dan update
                            oldPersediaan = oldDataKdrn.persediaan + 1
                            updatePersediaan(oldPersediaan, oldDataKdrn.id)
                            update(id.toString().toInt(), newDataKdrn.id)
                        }
                    }
                }
            }else{
                alert("Data tidak boleh kosong")
            }

        }

    }

    private fun insertKdrn(idKendaraan: Int, persediaan: Int) {
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
//        mengubah persediaan
        if (selectedItemStatus == "Sewa"){
            val newPersediaan = persediaan - 1
            database.dao().updatePersediaan(newPersediaan, idKendaraan)
        }

        onBackPressed()
        alert("Data berhasil ditambahkan")
    }

    private fun update(idPsn: Int, idKdrnBaru: Int){

            database.dao().UpdatePesanan(Pesanan(
                idPsn,
                find.inputUsername.text.toString(),
                find.inputAlamat.text.toString(),
                idKdrnBaru,
                find.inputWaktuSewa.text.toString().toInt(),
                selectedItemStatus
            ))
            onBackPressed()
            alert("Data berhasil diubah")

    }
    private fun updatePersediaan(persediaanBaru: Int, idKdrn: Int){
        database.dao().updatePersediaan(persediaanBaru, idKdrn)
    }

    private fun alert(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun modeEdit(id:Int) {
        find.btnTmbhPsnan.visibility = View.GONE
        find.headingPesanan.text="Edit Pesanan"

        val dataPesanan = database.dao().getIDPesanan(id)[0]
        val dataKdrn = database.dao().getKendaraanByID(dataPesanan.id_kendaraan)[0]

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