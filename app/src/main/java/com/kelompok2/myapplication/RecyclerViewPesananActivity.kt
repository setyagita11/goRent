package com.kelompok2.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok2.myapplication.GoRent.DBgoRent
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

        find.btnKendaraan.setOnClickListener {
            onBackPressed()
            startActivity(
                Intent(this, RecyclerViewKendaraanActivity::class.java)
            )
        }

        find.btnHome.setOnClickListener { onBackPressed() }

        adapterP = AdapterPesanan(arrayListOf())

        find.listPesanan.adapter = adapterP
        find.listPesanan.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        find.listPesanan.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))

        find.btnTambahPesanan.setOnClickListener {
            startActivity(
                Intent(this, InputPesananActivity::class.java ))
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

}