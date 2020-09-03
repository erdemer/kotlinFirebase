package com.erdemer.kotlininstagramfirebase.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.erdemer.kotlininstagramfirebase.Model.Post
import com.erdemer.kotlininstagramfirebase.R
import com.erdemer.kotlininstagramfirebase.Adapter.RVAdapter
import com.erdemer.kotlininstagramfirebase.UploadActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    var userNameFromFB: ArrayList<String> = ArrayList()
    var commentFromFB: ArrayList<String> = ArrayList()
    var profilePhotoFromFB: ArrayList<String> = ArrayList()
    var uploadPhotoFromFB: ArrayList<String> = ArrayList()

    var adapter: RVAdapter? = null
    val posts = arrayListOf<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        toolbar.setTitle("")
        setSupportActionBar(toolbar)



        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()


        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

      //  val postTest = Post("Test123","Hi Guys!","https://firebasestorage.googleapis.com/v0/b/kotlinfirebaseinsta-179b9.appspot.com/o/profilePhotos%2F2aee2d70-a73d-473d-acc7-763b0bd93dc3.jpg?alt=media&token=20fc60db-ce22-4936-b66b-05967ccc35dd","https://firebasestorage.googleapis.com/v0/b/kotlinfirebaseinsta-179b9.appspot.com/o/uploadImages%2F44dd3a8d-84ee-4277-bb54-415ddacd1f61.jpg?alt=media&token=a698a45c-340d-4bfd-99bd-75d3aadb2758")

      //  posts.add(postTest)


        adapter = RVAdapter(posts)
        recyclerView.adapter = adapter

        getDataFromFirestore()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater.inflate(R.menu.options_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_post) {
            //Upload activity
            startActivity(Intent(applicationContext, UploadActivity::class.java))

        } else if (item.itemId == R.id.logout) {
            var sp = this.getSharedPreferences("com.erdemer.kotlininstagramfirebase",
                MODE_PRIVATE)
            sp.edit().remove("userName")
            sp.edit().remove("profilePhotos")
            //Logout
            auth.signOut()
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()

        }

        return super.onOptionsItemSelected(item)
    }

    fun getDataFromFirestore() {


        database.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(applicationContext, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) {
                        posts.clear()
                        val documents = value.documents
                        for (document in documents) {
                            val comment = document.get("comment") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val profilePhoto = document.get("profilePhoto") as String
                            val userName = document.get("userName") as String
                            val timestamp = document.get("date") as Timestamp
                            val date = timestamp.toDate()

                            val tempPost = Post(userName,comment,profilePhoto,downloadUrl)
                            posts.add(tempPost)
                            adapter!!.notifyDataSetChanged()

                        }
                    }
                }
            }
        }
    }
}