package com.example.praktikumuts2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.praktikumuts2.databinding.ActivityAdminAddBinding
import com.example.praktikumuts2.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    var storage = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()
    private lateinit var imagesRef : StorageReference
    private val menuCollectionRef = db.collection("menu")

    private val menuListLiveData : MutableLiveData<List<Menu>>
            by lazy {
                MutableLiveData<List<Menu>>()
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAdminBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            buttonAdd.setOnClickListener{
                startActivity(Intent(this@AdminActivity, AdminAddActivity::class.java))
            }





            getAllMenu()
        }
    }
    private fun getAllMenu(){
        menuCollectionRef.addSnapshotListener{ snapshots, error ->
            if(error != null){
                Log.d("MainActivity", "error", error)
                return@addSnapshotListener
            }
            val menus = arrayListOf<Menu>()
            snapshots?.forEach{
                    documentReference ->
                menus.add(
                    Menu(id = documentReference.id,
                        nama_makanan = documentReference.get("nama_makanan").toString(),
                        jumlah_kalori = documentReference.get("jumlah_kalori").toString(),
                        tanggal = documentReference.get("tanggal").toString(),
                    )
                )

            }
            if (menus != null){
                menuListLiveData.postValue(menus)
            }
        }
        observeMenu()
    }
    private fun observeMenu() {
        menuListLiveData.observe(this){
                menus ->
            val adapter = AdminAdapter(menus){
//                    menus->
//                deleteMenu(menus)
            }
            binding.rvRoom.adapter = adapter
            binding.rvRoom.apply {
                layoutManager = LinearLayoutManager(context)
            }
        }

    }
}