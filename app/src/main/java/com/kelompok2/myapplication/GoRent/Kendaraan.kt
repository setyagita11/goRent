package com.kelompok2.myapplication.GoRent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "kendaraan", indices = [Index(value = ["merk"], unique = true)])
data class Kendaraan(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")         val id         : Int,
    @ColumnInfo(name = "merk")       val merk       : String,
    @ColumnInfo(name = "jenis")      val jenis      : String,
    @ColumnInfo(name = "harga_sewa") val harga_sewa : Int,
    @ColumnInfo(name = "persediaan") val persediaan : Int
)
