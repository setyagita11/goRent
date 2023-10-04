package com.kelompok2.myapplication.GoRent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "pesanan")
data class Pesanan(

    @PrimaryKey( autoGenerate = true)
    @ColumnInfo(name = "id")            val id          : Int,
    @ColumnInfo(name = "nama pesanan")  val namapesanan : String,
    @ColumnInfo(name = "alamat")        val alamat      : String,
    @ColumnInfo(name = "kendaraan")     val kendaraan   : String,
    @ColumnInfo(name = "sewa waktu")    val sewawaktu   : Int,
    @ColumnInfo(name = "status")        val status      : String,
)
