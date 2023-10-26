package com.kelompok2.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok2.myapplication.DetailKendaraanActivity
import com.kelompok2.myapplication.GoRent.Kendaraan
import com.kelompok2.myapplication.GoRent.Pesanan
import com.kelompok2.myapplication.R
import java.util.ArrayList

class AdapterPesanan (val list: ArrayList<Pesanan>) :
    RecyclerView.Adapter<AdapterPesanan.ViewHolder>() {
    class ViewHolder (view: View):RecyclerView.ViewHolder(view) {

        val namaPemesan = itemView.findViewById<TextView>(R.id.txtNama)
        val pesanKndraan = itemView.findViewById<TextView>(R.id.txtKendaraan)
        val alamatPemesan = itemView.findViewById<TextView>(R.id.txtAlamat)
        val durasiWaktu = itemView.findViewById<TextView>(R.id.txtDurasi)

        val detail = itemView.findViewById<CardView>(R.id.btnDtailPsnan)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_pesanan, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaPemesan.text = list[position].nama_pemesan
        holder.alamatPemesan.text = list[position].alamat
        holder.pesanKndraan.text = list[position].kendaraan
        holder.durasiWaktu.text = list[position].waktu_sewa.toString()

        holder.detail.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailKendaraanActivity::class.java).putExtra("idPesanan", list[position].id.toString())
            context.startActivity(intent)

        }
    }

    fun setDataP(newList: List<Pesanan>){
        list.clear()
        list.addAll(newList)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}