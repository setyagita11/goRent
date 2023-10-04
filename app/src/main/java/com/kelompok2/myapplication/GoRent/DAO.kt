package com.kelompok2.myapplication.GoRent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DAO {
   //
    @Query("SELECT*FROM kendaraan ")
    fun getAllKendaraan():List<Kendaraan>
    @Insert
    fun Insert(kendaraan: Kendaraan)
    @Update
    fun Update(kendaraan: Kendaraan)
    @Delete
    fun Delete(kendaraan: Kendaraan)


      //pesanan
    @Insert
    fun Insert(pesanan: Pesanan)
    @Update
    fun Update(pesanan: Pesanan)
    @Delete
    fun Delete(pesanan: Pesanan)
    @Query("SELECT*FROM pesanan ")
    fun getAllPesanan():List<Pesanan>
}