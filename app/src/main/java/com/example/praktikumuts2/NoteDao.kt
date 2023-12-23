package com.example.praktikumuts2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(menu : Note)

    @Update
    fun update(menu : Note)

    @Delete()
    fun delete(menu:Note)

    @get:Query("SELECT * FROM data_menu ORDER BY id ASC")
    val allMenus : LiveData<List<Note>>

    @Query("SELECT SUM(jumlah_kalori) FROM data_menu")
    fun getTotalCalories(): LiveData<Int>

}