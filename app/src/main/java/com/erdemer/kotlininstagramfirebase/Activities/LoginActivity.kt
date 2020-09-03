package com.erdemer.kotlininstagramfirebase.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.erdemer.kotlininstagramfirebase.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            startActivity(Intent(this, FeedActivity::class.java))
            finish()
        }

        textViewLoginRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    fun backIntent(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun login(view: View){
        auth.signInWithEmailAndPassword(editTextEmail.text.toString(),editTextPassword.text.toString()).addOnCompleteListener {task ->
            if (task.isSuccessful){
               startActivity(Intent(applicationContext, FeedActivity::class.java))
                finish()
                Toast.makeText(applicationContext,"Welcome: ${auth.currentUser?.email.toString()}",Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {e ->
            if (e!= null){
                Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
    }

}