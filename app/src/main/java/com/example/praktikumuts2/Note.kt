package com.example.praktikumuts2

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "data_menu")
data class Note (
    @PrimaryKey(autoGenerate = true)

    @NonNull
    val id : Int = 0,
    @ColumnInfo(name = "jumlah_kalori")
    val jumlah_kalori : String = "",
    @ColumnInfo(name = "nama_makanan")
    val nama_makanan : String = "",
    @ColumnInfo(name = "tanggal")
    val tanggal : String = "",
    @ColumnInfo(name = "takaran_saji")
    val takaran_saji: String = "",
    @ColumnInfo(name = "waktu_makan")
    val waktu_makan: String = "",

    )