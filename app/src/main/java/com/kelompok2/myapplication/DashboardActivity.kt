package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kelompok2.myapplication.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var find: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(find.root)

        val username = intent.getStringExtra("username").toString()//untuk meletakkan data(username)

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
}