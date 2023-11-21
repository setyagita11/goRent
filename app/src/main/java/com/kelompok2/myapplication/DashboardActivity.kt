package com.kelompok2.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.bg_elemen));
        }

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
            logout()
        }

//        mengambil data username
        val username = intent.getStringExtra("username").toString()
        find.tvWellcome.text = "Halo, $username"

//        image slider
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.dashboard_motor, ))
        imageList.add(SlideModel(R.drawable.dashboard_mobil, ))
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

//        mengambil data secara live
        val jumlahPesananSelesai = db.dao().getJumlahPesananSelesai()
        val jumlahPesananSewa = db.dao().getJumlahPesananSewa()
        jumlahPesananSewa.observe(this, Observer { psnSewa ->
            jumlahPesananSelesai.observe(this, Observer { psnSelesai ->

                val totalPsn = psnSewa.toFloat() + psnSelesai.toFloat()
                val valuePsnSewa = (psnSewa / totalPsn) * 100
                val valuePsnSelesai = (psnSelesai / totalPsn) * 100
                val entries = listOf(
                    PieEntry(valuePsnSewa, "Sewa"),
                    PieEntry(valuePsnSelesai, "Selesai")
                )
//                menjalankan pieChart
                setPieChart(entries)
//                mengisi data card total pesanan
                find.tvJumlahPesanan.text = totalPsn.toInt().toString()
            })
        })

//        mengambil data secara live
        val kendaraanTersedia = db.dao().getJumlahKendaraan()
        kendaraanTersedia.observe(this, Observer { arrayTesedia ->
            var totalKdrnTersedia = 0
            arrayTesedia.forEach {
                totalKdrnTersedia += it
            }
//                mengisi data card total kendaraan
            find.tvJumlahKendaraan.setText(totalKdrnTersedia.toString())
        })

    }

    private fun logout() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Logout")
        builder.setMessage("Apakah anda yakin ingin Logout?")

        builder.setPositiveButton("Ya") { dialog, which ->

//            menghapus data jika mengaktifkan ingat saya
            val sharedPreferences = getSharedPreferences("DataUser", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

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

    private fun setPieChart(entries : List<PieEntry>){

        val pieChart = find.pieChart
        // Konfigurasi warna dan label
        val colors = listOf(Color.parseColor("#dc244b"), Color.parseColor("#50b99b"))
        val dataSet = PieDataSet(entries, "")
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