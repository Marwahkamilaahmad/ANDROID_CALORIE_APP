package com.example.praktikumuts2

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.praktikumuts2.databinding.ItemMenuBinding
import com.example.praktikumuts2.databinding.ItemSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class MenuAdapter(private val listItem : List<Menu>) : RecyclerView.Adapter<MenuAdapter.ItemSearchUserViewHolder>() {
    private val firestore = FirebaseFirestore.getInstance()
    var storage = FirebaseStorage.getInstance().reference
    private val menuCollectionRef = firestore.collection("menu")



    inner class ItemSearchUserViewHolder(private val binding : ItemSearchBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Menu){
            with(binding){
                txtNamaMakanan.text = data.nama_makanan
                txtKalori.text = data.jumlah_kalori


                val namaMenu = data.nama_makanan

                val imagesRef = storage.child("images/${namaMenu}.jpg")
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .into(nameImage)
                }


                }

            itemView.setOnClickListener{
                val intent = Intent(binding.root.context, AddMenuActivity::class.java)
                    .apply {
                        putExtra("extraid", data.id)
                        putExtra("extratanggal", data.tanggal)
                        putExtra("extrajumlahkalori", data.jumlah_kalori)
                        putExtra("extranamamakanan", data.nama_makanan)
                    }

                binding.root.context.startActivity(intent)


            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuAdapter.ItemSearchUserViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSearchUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.ItemSearchUserViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

}