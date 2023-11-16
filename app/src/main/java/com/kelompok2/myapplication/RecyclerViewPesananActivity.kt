package com.kelompok2.myapplication

import android.R
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Pesanan
import com.kelompok2.myapplication.adapter.AdapterPesanan
import com.kelompok2.myapplication.databinding.ActivityRecyclerviewPesananBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewPesananActivity : AppCompatActivity() {

    private lateinit var find : ActivityRecyclerviewPesananBinding
    private val db by lazy { DBgoRent.getInstance(this) }
    private lateinit var adapterP : AdapterPesanan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find= ActivityRecyclerviewPesananBinding.inflate(layoutInflater)
        setContentView(find.root)

//        ketika btn tambah ditekan
        find.tambahPesanan.setOnClickListener{
            startActivity(
                Intent(this, InputPesananActivity::class.java)
            )
            find.etSearchPsn.text.clear()
        }

//        navigasi
        find.btnKendaraan.setOnClickListener {
            onBackPressed()
            startActivity(
                Intent(this, RecyclerViewKendaraanActivity::class.java)
            )
        }
        find.btnHome.setOnClickListener { onBackPressed() }

        adapterP = AdapterPesanan(arrayListOf(),
            object:AdapterPesanan.pesanan{
                override fun hapus(pesanan: Pesanan) {
                    hapusData(pesanan)
                }
            })

//        recyclerView
        find.listPesanan.adapter = adapterP
        find.listPesanan.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

//        cari nama & alamat pesanan
        find.etSearchPsn.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
            override fun onTextChanged(key: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (key.isNullOrEmpty()){
                    tampilDataPesanan()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = db.dao().cariPesanan("%$key%")
                        adapterP.setDataP(data)
                        withContext(Dispatchers.Main) {
                            adapterP.notifyDataSetChanged()
                            if (data.isEmpty()){
                                find.tvNotifSearch2.visibility = View.VISIBLE
                            } else {
                                find.tvNotifSearch2.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            override fun afterTextChanged(p0: Editable?) { }
        })

//        filter status
        val data = arrayOf("Filter Status", "Sewa", "Selesai")
        val spinner = find.spnFilter
        val spinnerAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, data)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        val textView = spinner.getChildAt(0) as? TextView
        textView?.setTextColor(Color.RED)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                if (selectedItem == "Filter Status"){
                    tampilDataPesanan()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = db.dao().getByStatus(selectedItem)
                        adapterP.setDataP(data)
                        withContext(Dispatchers.Main) {
                            adapterP.notifyDataSetChanged()
                            if (data.isEmpty()){
                                find.tvNotifSearch2.visibility = View.VISIBLE
                            } else {
                                find.tvNotifSearch2.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
        spinner.setSelection(0)

    }
    private fun hapusData (pesanan: Pesanan){
        val dialog = AlertDialog.Builder(this)
        dialog.apply {
            setTitle("konfirmasi hapus data")
            setMessage("Apakah anda yakin akan menghapus data ${pesanan.nama_pemesan}?")
            setNegativeButton("Batal"){
                dialogInterface: DialogInterface,i:Int->
                dialogInterface.dismiss()
            }
            setPositiveButton("hapus"){
                dialogInterface:DialogInterface,i:Int->
                dialogInterface.dismiss()
//                mengecek apakah status sudah selesai
                if (pesanan.status == "Selesai") {
                    CoroutineScope(Dispatchers.IO).launch {
                        db.dao().DeletePesanan(pesanan)
                    }
                    recreate()
                    alert("Pesanan ${pesanan.nama_pemesan} berhasil dihapus")
                } else {
                    alert("Pesanan ${pesanan.nama_pemesan} belum selesai")
                }
            }
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        tampilDataPesanan()
    }

    fun tampilDataPesanan() {
        find.listPesanan.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val data = db.dao().getAllPesanan()
            adapterP.setDataP(data)
            withContext(Dispatchers.Main) {
                adapterP.notifyDataSetChanged()
                find.tvNotifSearch2.visibility = View.GONE
            }
        }
        find.listPesanan.adapter = adapterP
    }

    private fun alert (msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

}