package com.kelompok2.myapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private val db by lazy { DBgoRent.getInstance(this) }
    private lateinit var adapter : AdapterKendaraan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find=ActivityRecyclerViewKendaraanBinding.inflate(layoutInflater)
        setContentView(find.root)

        adapter = AdapterKendaraan(arrayListOf(),
            object : AdapterKendaraan.kendaraanv1{
                override fun delete (kendaraan: Kendaraan) {
                    deletedata(kendaraan) //deletedata:function
                }
            })

        find.btnPesanan.setOnClickListener {
            onBackPressed()
            startActivity(
                Intent(this, RecyclerViewPesananActivity::class.java)
            )
        }

        find.btnHome.setOnClickListener { onBackPressed() }

        find.listKndraan.adapter = adapter
        find.listKndraan.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        find.listKndraan.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))

        find.btnTambahKendaraan.setOnClickListener {
        startActivity(
            Intent(this, InputKendaraanActivity::class.java ))
        }


        find.logout1.setOnClickListener() {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Logout")
            builder.setMessage("Apakah Anda yakin ingin logout?")

            builder.setPositiveButton("Hapus") { dialog, which ->


                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("Tidak") { dialog, which ->
                // Batal logout, tutup dialog
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()


        }
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
                if (db.dao().cekKendaraanYgDigunakan(kendaraan.merk)) {
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
            }
        }
        find.listKndraan.adapter = adapter
    }

    private fun alert(msg: String) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()


    }

}