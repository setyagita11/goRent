package com.kelompok2.myapplication.GoRent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok2.myapplication.R
import java.util.*

class AdapterKendaraan (val list: ArrayList<Kendaraan> val listener: kendaraan) : RecyclerView.Adapter<AdapterKendaraan.ViewHolder>() {
    class ViewHolder (view: View):RecyclerView.ViewHolder(view) {

        val merek = itemView.findViewById<TextView>(R.id.txtMerek)
        val harga = itemView.findViewById<TextView>(R.id.txtHarga)
        val tersedia = itemView.findViewById<TextView>(R.id.txtTersedia)
        var listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).
            inflate(R.layout.adapter_kendaraan,parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.merek.text = list[position].merk
        holder.harga.text = list[position].harga_sewa.toString()
        holder.tersedia.text = list[position].persediaan.toString()

    }

    fun setData(newList: List<Kendaraan>){
        list.clear()
        list.addAll(newList)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}