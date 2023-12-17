package com.example.praktikumuts2

import com.google.firebase.firestore.Exclude

data class Users(
    @set:Exclude @get:Exclude
    @Exclude
    var id : String,
    var username : String = "",
    var target_kalori : String = "",
    var tinggi_badan: String = "",
    var berat_badan: String = "",
    var role : String = "user"
)