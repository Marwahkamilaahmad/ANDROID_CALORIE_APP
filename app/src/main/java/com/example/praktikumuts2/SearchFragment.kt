package com.example.praktikumuts2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.praktikumuts2.databinding.ActivityAdminBinding
import com.example.praktikumuts2.databinding.FragmentProfileBinding
import com.example.praktikumuts2.databinding.FragmentSearchBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    var storage = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()
    private lateinit var imagesRef : StorageReference
    private val menuCollectionRef = db.collection("menu")

    private val menuListLiveData : MutableLiveData<List<Menu>>
            by lazy {
                MutableLiveData<List<Menu>>()
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root

        with(binding){


            getAllMenu()

            buttonSearch.setOnClickListener {
                val searchText = edtSearch.text.toString()
                filterMenu(searchText)
            }

            buttonCustomMakanan.setOnClickListener {
                val intent = Intent(requireActivity(), AddMenuActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

        }
        return view
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
        menuListLiveData.observe(requireActivity()){
                menus ->
            val adapter = MenuAdapter(menus)
            binding.recyclerView1.adapter = adapter
            binding.recyclerView1.apply {
                layoutManager = LinearLayoutManager(context)
            }
        }

    }

    private fun filterMenu(searchText: String) {
        menuListLiveData.observe(requireActivity()) { menus ->
            val filteredMenus = menus.filter { menu ->
                menu.nama_makanan.contains(searchText, ignoreCase = true)
            }
            val adapter = MenuAdapter(filteredMenus)
            binding.recyclerView1.adapter = adapter
            binding.recyclerView1.apply {
                layoutManager = LinearLayoutManager(context)
            }
        }

}
}