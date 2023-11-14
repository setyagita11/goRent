package com.kelompok2.myapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
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

        find.tambahPesanan.setOnClickListener{
            startActivity(
                Intent(this, InputPesananActivity::class.java)
            )
        }

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

        find.listPesanan.adapter = adapterP
        find.listPesanan.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        find.listPesanan.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))

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
            }
        }
        find.listPesanan.adapter = adapterP
    }
    private fun alert (msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()

    }

}