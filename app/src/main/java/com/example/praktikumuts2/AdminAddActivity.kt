package com.example.praktikumuts2

import android.R
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.praktikumuts2.databinding.ActivityAdminAddBinding
import com.example.praktikumuts2.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.io.File

class AdminAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddBinding
    private var selectedTime: String? = null
    private lateinit var auth: FirebaseAuth
    var db = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance().reference
    private var waktuInput: String? = null
    private var updateId = ""
    private var selectedImageUri: Uri? = null
    private val menuCollectionRef = db.collection("menu")

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        binding.addTanggal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            selectedTime = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
        }

        binding.btnApplyWaktuMakan.setOnClickListener {
            binding.addTanggalTambah.text = selectedTime
        }

        binding.btnSave.setOnClickListener {
            val namaMenu = binding.addNama.text.toString()
            val tanggal = selectedTime.toString()
            val kalori = binding.addKalori.text.toString()
//            val imagesRefs = storage.child("images/${namaMenu}.jpg").toString()
            val newMenu = Menu(
                tanggal = tanggal, jumlah_kalori = kalori, nama_makanan = namaMenu
            )
            addMenu(newMenu)
            startActivity(Intent(this@AdminAddActivity, AdminActivity::class.java))
            finish()
        }
        binding.addGambar.setOnClickListener{
            selectImage()
        }
    }

    private fun addMenu(menu: Menu) {
        menuCollectionRef.add(menu).addOnFailureListener {
            Log.d(
                "MainActivity", "Error adding budget : ",
                it
            )
        }
    }

    private fun selectImage() {
        val items = arrayOf<CharSequence>("take photos", "choose from library", "cancel")
        val builder = AlertDialog.Builder(this@AdminAddActivity)
        builder.setTitle("app name")
        builder.setItems(items) { dialog, item ->
            when (items[item]) {
                "take photos" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 10)

                }
                "choose from library" -> {
                    val intent = Intent(Intent.ACTION_PICK)
                    intent.type = "image/*"
                    startActivityForResult(intent, 20)
                }
                "cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun upload(imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
        val namaMenu = binding.addNama.text.toString()
        val imagesRef =
            storageRef.child("images/${namaMenu}.jpg") // Ganti nama file sesuai kebutuhan
        val uploadTask = imagesRef.putFile(imageUri)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            Toast.makeText(
                this@AdminAddActivity,
                "Gambar berhasil diunggah ke Firebase",
                Toast.LENGTH_SHORT
            ).show()
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    Log.d("AdminAddActivity", "URL Gambar: $imageUrl")


                    Glide.with(this@AdminAddActivity)
                        .load(imageUrl)
                        .into(binding.addGambar)
                }.addOnFailureListener { exception ->

                    Log.e("AdminAddActivity", "Gagal mendapatkan URL gambar: $exception")
                }
            }.addOnFailureListener { exception ->
                Log.e("AdminAddActivity", "Gagal mengunggah gambar: $exception")
            }

//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                10 -> {

                }
                20 -> {

                    selectedImageUri = data?.data
                    selectedImageUri?.let { uri ->
                        upload(uri)
                    }
                }
            }
        }
    }



}