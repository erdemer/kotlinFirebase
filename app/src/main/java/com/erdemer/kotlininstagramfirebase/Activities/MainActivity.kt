package com.erdemer.kotlininstagramfirebase.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.erdemer.kotlininstagramfirebase.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textViewMainRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(-1)
    }

    fun goToLogin(view: View){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}