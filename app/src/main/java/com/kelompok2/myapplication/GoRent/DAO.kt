package com.kelompok2.myapplication.GoRent

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.concurrent.Flow

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
    fun getKendaraanByID (id:Int) : List<Kendaraan>
    @Query("SELECT merk FROM kendaraan")
    fun getAllMerk() : Array<String>
    @Query("SELECT persediaan FROM kendaraan")
    fun getJumlahKendaraan() : LiveData<Array<Int>>
    @Query("UPDATE kendaraan SET persediaan = :newPersediaan WHERE id = :id")
    fun updatePersediaan(newPersediaan : Int, id: Int)
    @Query("SELECT id FROM kendaraan WHERE merk = :merk")
    fun getID(merk: String) : Int

      //pesanan
    @Insert
    fun InsertPesanan(pesanan: Pesanan)
    @Update
    fun UpdatePesanan(pesanan: Pesanan)
    @Delete
    fun DeletePesanan(pesanan: Pesanan)
    @Query("SELECT*FROM pesanan ORDER BY id DESC")
    fun getAllPesanan():List<Pesanan>
    @Query("SELECT*FROM pesanan WHERE id=:id")
    fun getIDPesanan (id:Int) : List<Pesanan>
    @Query("SELECT * FROM pesanan WHERE id_kendaraan = :id_kendaraan")
    fun cekKendaraanYgDigunakan(id_kendaraan: Int) : Boolean
    @Query("SELECT COUNT(*) FROM pesanan WHERE status = 'Sewa'")
    fun getJumlahPesananSewa() : LiveData<Int>
    @Query("SELECT COUNT(*) FROM pesanan WHERE status = 'Selesai'")
    fun getJumlahPesananSelesai() : LiveData<Int>

}