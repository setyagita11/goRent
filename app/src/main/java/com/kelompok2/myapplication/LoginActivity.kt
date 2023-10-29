package com.kelompok2.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kelompok2.myapplication.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var find : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        find = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(find.root)

        val username  = find.inputUsername
        val password = find.inputPassword

        find.btnLogin.setOnClickListener {

            if (username.text.isNotEmpty() && password.text.isNotEmpty()) {
                if (password.text.length >= 8){
                    startActivity(
                        Intent(this, DashboardActivity::class.java)
                            .putExtra("username", username.text.toString())
                    )
                    alert("Selamat datang di Go-Rent ${username.text}")
                    finish()

                }else {
                    alert("Pasword minimal 8 huruf")
                }

            } else {
                alert("Username dan Password tidak boleh kosong!")
            }

        }

    }

    private fun alert(msg: String) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    }
}