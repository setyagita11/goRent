package com.kelompok2.myapplication

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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


        val pieChart: PieChart = findViewById(R.id.pieChart)

        // Data untuk Pie Chart
        val entries = listOf(
            PieEntry(30f, "Data 1"),
            PieEntry(50f, "Data 2"),
            PieEntry(20f, "Data 3")
        )

        // Konfigurasi warna dan label
        val colors = listOf(Color.BLUE, Color.GREEN, Color.RED)
        val dataSet = PieDataSet(entries, "Pie Chart")
        dataSet.colors = colors

        // Konfigurasi Pie Data
        val pieData = PieData(dataSet)
        pieChart.data = pieData

        // Konfigurasi tampilan
        pieChart.description.isEnabled = false
        pieChart.isRotationEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.animateY(1000)

        // Refresh chart
        pieChart.invalidate()




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