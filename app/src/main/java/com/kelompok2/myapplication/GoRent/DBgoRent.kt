package com.kelompok2.myapplication.GoRent

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pesanan::class, Kendaraan::class], version = 3)
abstract class DBgoRent : RoomDatabase(){

    abstract fun dao() : DAO

    companion object{
        @Volatile
        private var instance : DBgoRent?=null
        @Synchronized
        fun getInstance(context: Context) : DBgoRent{

            if (instance==null){
                instance= Room.databaseBuilder(context, DBgoRent::class.java, "Database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}