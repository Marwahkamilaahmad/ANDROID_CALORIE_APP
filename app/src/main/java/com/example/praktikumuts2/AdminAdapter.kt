package com.example.praktikumuts2

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.praktikumuts2.databinding.ItemMenuBinding
import com.example.praktikumuts2.databinding.ItemSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

typealias onClickDelete = (Menu) -> Unit
class AdminAdapter(private val listItem : List<Menu>, private val onClickDelete: onClickDelete) : RecyclerView.Adapter<AdminAdapter.ItemMenuViewHolder>() {
    var storage = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()
    private val menuCollectionRef = db.collection("menu")


    inner class ItemMenuViewHolder(private val binding : ItemMenuBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(data: Menu){
            with(binding){
                txtKalori.text = "${data.jumlah_kalori} kal"
                txtMakanan.text = data.nama_makanan
                txtTanggal.text = "post at ${data.tanggal}"

                val namaMenu = data.nama_makanan

                val imagesRef = storage.child("images/${namaMenu}.jpg")
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()

                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .into(nameImage)
                }

                buttonUpdate.setOnClickListener{
                    val intent = Intent(binding.root.context, AdminEditActivity::class.java)
                        .apply {
                            putExtra("extraid", data.id)
                            putExtra("extratanggal", data.date)
                            putExtra("extradeskripsi", data.description)
                            putExtra("extranominal", data.nominal)
                            putExtra("extranama", data.nama)
                            putExtra("extraalamat", data.alamat)
                        }
                    binding.root.context.startActivity(intent)
                }

                buttonDelete.setOnClickListener {
                    val posisi = listItem[position]
                    deleteBudget(posisi)
                }
                itemView.

            }
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminAdapter.ItemMenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminAdapter.ItemMenuViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }


}