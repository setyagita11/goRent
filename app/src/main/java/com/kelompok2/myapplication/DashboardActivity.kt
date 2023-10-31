package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.kelompok2.myapplication.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var find: ActivityDashboardBinding
    private var backButtonPressedTime = 0L
    private val backButtonThreshold = 3000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(find.root)

        val username = intent.getStringExtra("username").toString()
        val imageList = ArrayList<SlideModel>() // Create image list

        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(SlideModel(R.drawable.motor, ))
        imageList.add(SlideModel(R.drawable.mobil, ))

        //val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        //imageSlider.setImageList(imageList)


        find.tvWellcome.text = "Hello, $username"

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

        find.logout.setOnClickListener() {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Konfirmasi Logout")
            builder.setMessage("Apakah Anda yakin ingin logout?")

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

    override fun onBackPressed() {
        if (backButtonPressedTime + backButtonThreshold > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        backButtonPressedTime = System.currentTimeMillis()
    }
}