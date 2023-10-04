package com.kelompok2.myapplication.GoRent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "kendaraan")
data class kendaraan(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id : Int,
    @ColumnInfo (name = "merk") val merk : String,
    @ColumnInfo(name = "harga") val harga : Int,
    @ColumnInfo(name = "jenis") val jenis : String,
    @ColumnInfo(name = "harga sewa") val hargasewa : Int,
    @ColumnInfo(name = "persediaan") val persediaan : Int,
)
