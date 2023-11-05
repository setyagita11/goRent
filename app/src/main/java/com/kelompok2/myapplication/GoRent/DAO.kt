package com.kelompok2.myapplication.GoRent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
   //kendaraan
    @Query("SELECT*FROM kendaraan ")
    fun getAllKendaraan() : List<Kendaraan>
    @Insert
    fun InsertKendaraan(kendaraan: Kendaraan)
    @Update
    fun UpdateKendaraan(kendaraan: Kendaraan)
    @Delete
    fun DeleteKendaraan(kendaraan: Kendaraan)
    @Query("SELECT*FROM kendaraan WHERE id=:id")
    fun getIDkendaraan (id:Int) : List<Kendaraan>
    @Query("SELECT merk FROM kendaraan")
    fun getAllMerk() : Array<String>
    @Query("SELECT * FROM kendaraan WHERE merk=:merk")
    fun getAllKendaraanByMerk(merk : String) : List<Kendaraan>

      //pesanan
    @Insert
    fun InsertPesanan(pesanan: Pesanan)
    @Update
    fun UpdatePesanan(pesanan: Pesanan)
    @Delete
    fun DeletePesanan(pesanan: Pesanan)
    @Query("SELECT*FROM pesanan ")
    fun getAllPesanan():List<Pesanan>
    @Query("SELECT*FROM pesanan WHERE id=:id")
    fun getIDPesanan (id:Int) : List<Pesanan>
    @Query("SELECT * FROM pesanan WHERE kendaraan = :kendaraan")
    fun cekKendaraanYgDigunakan(kendaraan: String) : Boolean
}