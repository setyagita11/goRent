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

        val inusername  = find.inputUsername
        val inpassword = find.inputPassword
        val pasword= "12345678"
        val user = listOf<String>("guru" ,"murid")
        find.btnLogin.setOnClickListener {

            if (inusername.text.isNotEmpty() && inpassword.text.isNotEmpty()) {
                if (inpassword.text.length >= 8){


                    if (inusername.text.toString() in user && inpassword.text.toString()== pasword) {
                        startActivity(
                            Intent(this, DashboardActivity::class.java)
                                .putExtra("username", inusername.text.toString())
                        )
                        alert("Selamat datang di Go-Rent ${inusername.text}")
                        finish()
                    } else {
                        alert("pasword atau username salah")
                    }
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