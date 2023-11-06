package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.kelompok2.myapplication.GoRent.DBgoRent
import com.kelompok2.myapplication.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var find: ActivityDashboardBinding
    private val db by lazy { DBgoRent.getInstance(this) }
    private var backButtonPressedTime = 0L
    private val backButtonThreshold = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(find.root)

        val imageList = ArrayList<SlideModel>() // Create image list

        imageList.add(SlideModel(R.drawable.dashboard_motor, ))
        imageList.add(SlideModel(R.drawable.dashboard_mobil, ))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

//        menghitung jumlah kendaraan
        val jumlahKendaraan = db.dao().getJumlahKendaraan()
        jumlahKendaraan.observe(this, Observer { count ->
            var totalKendaraan = 0
            count.forEach { item ->
                totalKendaraan += item
            }
            find.tvJumlahKendaraan.setText(totalKendaraan.toString())
        })

//        menghitung jumlah pesanan dengan status sewa
        val jumlahPesanan = db.dao().getJumlahPesananSewa()
        jumlahPesanan.observe(this, Observer { totalPsn ->
            find.tvJumlahPesanan.setText(totalPsn.toString())
        })

//        mengambil data username
        val username = intent.getStringExtra("username").toString()
        find.tvWellcome.text = "Halo, $username"

//        navbar
        find.btnKendaraan.setOnClickListener {
            startActivity(
                Intent(this, RecyclerViewKendaraanActivity::class.java)
            )
        }
        find.btnPesanan.setOnClickListener {
            startActivity(
                Intent(this, RecyclerViewPesananActivity::class.java)
            )
        }

//        btn logout
        find.logout.setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Logout")
            builder.setMessage("Apakah anda yakin ingin Logout?")

            builder.setPositiveButton("Ya") { dialog, which ->

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }

//    konfirmasi keluar apk
    override fun onBackPressed() {
        if (backButtonPressedTime + backButtonThreshold > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        backButtonPressedTime = System.currentTimeMillis()
    }
}