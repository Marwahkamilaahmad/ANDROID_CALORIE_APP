package com.example.praktikumuts2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.praktikumuts2.databinding.ActivityAdminBinding
import com.example.praktikumuts2.databinding.ActivityAdminEditBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AdminEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEditBinding
    private var selectedTime: String? = null
    private var updateId = ""
    var db = FirebaseFirestore.getInstance()
    private val menuCollectionRef = db.collection("menu")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminEditBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val data = intent.extras
        with(binding) {
            addNama.setText(data?.getString("extranamamakanan"))
            addKalori.setText(data?.getString("extrajumlahkalori"))
            addTanggalTambah.setText(data?.getString("extratanggal"))

            binding.addTanggal.setOnDateChangeListener { view, year, month, dayOfMonth ->
                selectedTime = String.format("%02d-%02d-%d", dayOfMonth, month + 1, year)
            }

            binding.btnApplyWaktuMakan.setOnClickListener {
                binding.addTanggalTambah.text = selectedTime
            }

            btnSave.setOnClickListener {
                val namaMenu = addNama.getText().toString()
                val kalori = addKalori.getText().toString()
                val waktu = addTanggalTambah.getText().toString()
                updateId = data?.getString("extraid").toString()

                val updateNew = Menu( nama_makanan = namaMenu, jumlah_kalori = kalori, waktu_makan = waktu, id = updateId)
                updateMenu(updateNew)

                val intentToHomeA = Intent(this@AdminEditActivity, AdminActivity::class.java)
                startActivity(intentToHomeA)
                finish()

            }

        }
    }

    private fun updateMenu(menu : Menu){
        menuCollectionRef.document(updateId).set(menu)
            .addOnFailureListener{
                Log.d("MainActivity", "Error updating budget : ",
                    it)
            }
    }
}