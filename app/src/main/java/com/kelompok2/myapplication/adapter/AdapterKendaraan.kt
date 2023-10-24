package com.kelompok2.myapplication.GoRent

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kelompok2.myapplication.DetailKendaraanActivity
import com.kelompok2.myapplication.InputKendaraanActivity
import com.kelompok2.myapplication.R
import java.util.*

class AdapterKendaraan (val list: ArrayList<Kendaraan> ,val listener: kendaraanv1)
    : RecyclerView.Adapter<AdapterKendaraan.ViewHolder>() {
    class ViewHolder (view: View):RecyclerView.ViewHolder(view) {

        val merek = itemView.findViewById<TextView>(R.id.txtMerek)
        val harga = itemView.findViewById<TextView>(R.id.txtHarga)
        val tersedia = itemView.findViewById<TextView>(R.id.txtTersedia)
        val hapus= itemView.findViewById<ImageView>(R.id.imgDelete)
        val edit= itemView.findViewById<ImageView>(R.id.imgEdit)
        val detail= itemView.findViewById<CardView>(R.id.btnDetailKendaraan)

    }
    interface  kendaraanv1{
        fun delete(kendaraan: Kendaraan)
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
        holder.hapus.setOnClickListener{
            listener.delete(list[position])
        }
        holder.edit.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, InputKendaraanActivity::class.java).putExtra("idKendaraan", list[position].id.toString())
            context.startActivity(intent)
        }
        holder.detail.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailKendaraanActivity::class.java).putExtra("idKendaraan", list[position].id.toString())
            context.startActivity(intent)

        }

    }

    fun setData(newList: List<Kendaraan>){
        list.clear()
        list.addAll(newList)
    }

    override fun getItemCount(): Int {
        return list.size
    }


}