package com.kelompok2.myapplication

import android.content.Context
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

        val sharedPreferences = getSharedPreferences("DataUser", Context.MODE_PRIVATE)

        val inusername  = find.inputUsername
        val inpassword = find.inputPassword

        val keyUser = sharedPreferences.getString("username", "")

        if (!keyUser.isNullOrEmpty()) {
            login(keyUser)
        }

        find.btnLogin.setOnClickListener {

            if (inusername.text.isNotEmpty() && inpassword.text.isNotEmpty()) {

                if (inpassword.text.length >= 8){

                    login(inusername.text.toString())

                }else {
                    alert("Pasword minimal 8 huruf")
                }

            }else {
                alert("Username dan Password tidak boleh kosong!")
            }

        }

    }

    private fun login(username: String) {

        if (find.rememberMe.isChecked){
            val sharedPreferences = getSharedPreferences("DataUser", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("username", username)
            editor.apply()
        }
        startActivity(
            Intent(this, DashboardActivity::class.java)
                .putExtra("username", username)
        )
        alert("Selamat datang di Go-Rent $username")
        finish()

    }

    private fun alert(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}