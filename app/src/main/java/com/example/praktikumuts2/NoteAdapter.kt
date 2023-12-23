package com.example.praktikumuts2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.praktikumuts2.databinding.ItemSearchBinding
import com.example.praktikumuts2.databinding.ItemUserBinding

typealias onItemDelete = (Note) -> Unit
class NoteAdapter(
        private val menus: ArrayList<Note>,
        private val onItemDelete: onItemDelete
    ) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

        inner class NoteViewHolder(private val binding: ItemUserBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(data: Note) {

                with(binding) {
                    txtKalori.text = data.jumlah_kalori
                    txtNamaMakanan.text = data.nama_makanan
                    txtJamMakanan.text = data.tanggal
                    txtTakaranSaji.text = data.takaran_saji
                    txtWaktuMakanan.text = data.waktu_makan

                    buttonDelete.setOnClickListener {
                        onItemDelete(data)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NoteViewHolder(binding)
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            val menu = menus[position]
            holder.bind(menu)
        }

        override fun getItemCount() = menus.size

        fun setData(newList: List<Note>) {
            menus.clear()
            menus.addAll(newList)
            notifyDataSetChanged()
        }



    }
