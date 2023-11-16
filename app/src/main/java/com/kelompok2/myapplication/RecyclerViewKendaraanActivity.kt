package com.kelompok2.myapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok2.myapplication.GoRent.AdapterKendaraan
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.GoRent.Kendaraan
import com.kelompok2.myapplication.databinding.ActivityRecyclerViewKendaraanBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class RecyclerViewKendaraanActivity : AppCompatActivity() {

    private lateinit var find : ActivityRecyclerViewKendaraanBinding
    private lateinit var adapter : AdapterKendaraan
    private val db by lazy { DBgoRent.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityRecyclerViewKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        adapter = AdapterKendaraan(arrayListOf(),
            object : AdapterKendaraan.kendaraanv1{
                override fun delete (kendaraan: Kendaraan) {
                    deletedata(kendaraan) //deletedata:function
                }
            })

//        ketika btn tambah ditekan
        find.tambahKendaraan.setOnClickListener {
            startActivity(
                Intent(this, InputKendaraanActivity::class.java)
            )
            find.etSearchKdrn.text.clear()
            find.etSearchKdrn.clearFocus()
        }

//        navigasi
        find.btnPesanan.setOnClickListener {
            onBackPressed()
            startActivity(
                Intent(this, RecyclerViewPesananActivity::class.java)
            )
        }
        find.btnHome.setOnClickListener { onBackPressed() }

//        recyclerView
        find.listKndraan.adapter = adapter
        find.listKndraan.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

//        cari nama kendaraan
        find.etSearchKdrn.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(key: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (key.isNullOrEmpty()){
                    tampilDataKendaraan()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data = db.dao().cariMerk("%$key%")
                        adapter.setData(data)
                        withContext(Dispatchers.Main) {
                            adapter.notifyDataSetChanged()
                            if (data.isEmpty()){
                                find.tvNotifSearch.visibility = View.VISIBLE
                            } else {
                                find.tvNotifSearch.visibility = View.GONE
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) { }

        })
    }

    private fun deletedata(kendaraan: Kendaraan) {

       val dialog= AlertDialog.Builder(this)

        dialog.apply {

            setTitle("Konfirmasi hapus data")
            setMessage("Apakah anda yakin menghapus data ${kendaraan.merk}?")

            setNegativeButton("Batal"){
                    dialogInterface:DialogInterface, i:Int -> dialogInterface.dismiss()
            }
            setPositiveButton("Hapus") {
                    dialogInterface:DialogInterface,i :Int-> dialogInterface.dismiss()

//              cek apakah kendaraan digunakan dalam pesanan
                if (db.dao().cekKendaraanYgDigunakan(kendaraan.id)) {
                    alert("Kendaraan sedang digunakan di Pesanan")
                } else {
                    CoroutineScope(Dispatchers.IO).launch{
                        db.dao().DeleteKendaraan(kendaraan)
                    }
                    recreate()
                    alert("data ${kendaraan.merk} berhasil dihapus")
                }
            }
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        tampilDataKendaraan()
    }

    fun tampilDataKendaraan() {
        find.listKndraan.layoutManager = LinearLayoutManager(this)
        CoroutineScope(Dispatchers.IO).launch {
            val data = db.dao().getAllKendaraan()
            adapter.setData(data)
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
                find.tvNotifSearch.visibility = View.GONE
            }
        }
        find.listKndraan.adapter = adapter
    }

    private fun alert(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}