package com.example.praktikumuts2

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_konsumsi")
data class Menu (

    @PrimaryKey(autoGenerate = true)
    @NonNull val id : String = "",
    @ColumnInfo(name = "waktu_makan")
    val waktu_makan: String = "",
//    @ColumnInfo(name = "imageUrl")
//    val imageUrl: String = "",
    @ColumnInfo(name = "nama_makanan")
    val nama_makanan : String = "",
    @ColumnInfo(name = "tanggal")
    val tanggal : String = "",
    @ColumnInfo(name = "takaran_saji")
    val takaran_saji : String = "",
    @ColumnInfo(name = "jumlah_kalori")
    val jumlah_kalori: String = "",
)