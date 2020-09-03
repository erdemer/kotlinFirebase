package com.erdemer.kotlininstagramfirebase

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_upload.*
import java.lang.Exception
import java.util.*

class UploadActivity : AppCompatActivity() {
    private lateinit var selectedImgUri : Uri
    private lateinit var selectedBitmap: Bitmap
    private lateinit var storage : FirebaseStorage
    private lateinit var database : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()
    }
    fun uploadImg(view: View){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            startActivityForResult(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),2)
        }

    }
    fun upload(view: View){
        //UUID -> image name
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference.child("uploadImages").child(imageName)

        if (selectedImgUri != null) {
            reference.putFile(selectedImgUri).addOnSuccessListener { taskSnapShot->
                val uploadedPictureRef = FirebaseStorage.getInstance().reference.child("uploadImages").child(imageName)
                uploadedPictureRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    println(downloadUrl)
                    var sp = this.getSharedPreferences("com.erdemer.kotlininstagramfirebase",
                        MODE_PRIVATE)
                    var userName =sp.getString("userName","No Name")
                    var profilePhoto = sp.getString("profilePhotos"," ")

                    val postMap = hashMapOf<String,Any>()
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("userEmail",auth.currentUser!!.email.toString())
                    postMap.put("comment",editTextComment.text.toString())
                    postMap.put("date",Timestamp.now())
                    postMap.put("userName",userName!!)
                    postMap.put("profilePhoto",profilePhoto!!)

                   database.collection("Posts").add(postMap).addOnCompleteListener { task ->
                       if (task.isComplete && task.isSuccessful){
                           finish()
                       }
                   }.addOnFailureListener { e->
                       Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_LONG).show()
                   }
                }

            }
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivityForResult(Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI),2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK && data != null){
                selectedImgUri = data.data!!

                try {
                    if (selectedImgUri != null){
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P){
                            val source = ImageDecoder.createSource(this.contentResolver,selectedImgUri)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,selectedImgUri)
                        }
                    } else {
                        selectedBitmap = BitmapFactory.decodeResource(this.resources,R.drawable.blank_profile)
                    }
                    uploadImg.setImageBitmap(selectedBitmap)
                } catch (e : Exception){
                    e.printStackTrace()
                }


            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }

}