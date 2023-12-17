package com.example.praktikumuts2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praktikumuts2.databinding.ItemMenuBinding
import com.google.firebase.firestore.FirebaseFirestore


class MenuAdapter(private val listItem : List<Menu>) : RecyclerView.Adapter<MenuAdapter.ItemMenuUserViewHolder>() {
    private val firestore = FirebaseFirestore.getInstance()
    private val budgetCollectionRef = firestore.collection("menu")


    inner class ItemMenuUserViewHolder(private val binding : ItemMenuBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(data: Menu){

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuAdapter.ItemMenuUserViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemMenuUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuAdapter.ItemMenuUserViewHolder, position: Int) {
        holder.bind(listItem[position])
    }

    override fun getItemCount(): Int {
        return listItem.size
    }

}