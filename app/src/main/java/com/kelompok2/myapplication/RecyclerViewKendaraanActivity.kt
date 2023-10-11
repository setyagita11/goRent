package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kelompok2.myapplication.GoRent.AdapterKendaraan
import com.kelompok2.myapplication.GoRent.DBgoRent
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

        adapter = AdapterKendaraan(ArrayList())

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

}