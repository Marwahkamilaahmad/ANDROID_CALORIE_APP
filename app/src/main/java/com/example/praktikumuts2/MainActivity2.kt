package com.example.praktikumuts2

import PrefManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.praktikumuts2.databinding.ActivityMain2Binding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var mediator: TabLayoutMediator
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = PrefManager.getInstance(this@MainActivity2)
        title = "Dashboard"
        binding = ActivityMain2Binding.inflate(layoutInflater)
        val firebaseFirestore = FirebaseFirestore.getInstance()
        val firebaseAuth = FirebaseAuth.getInstance()

        setContentView(binding.root)
        if(pref.isLoggedIn()){
            val email = pref.getEmail()
            val password = pref.getPassword()
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val dataUser = firebaseFirestore.collection("users").document(firebaseAuth.currentUser?.uid.toString())
                Toast.makeText(this@MainActivity2, "Akun sudah ada yang login", Toast.LENGTH_SHORT).show()
                dataUser.get().addOnSuccessListener {
                    if (it.getString("role") == "admin"){
                        val intent = Intent(this@MainActivity2, AdminActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@MainActivity2, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }


        with(binding){
            viewPager2 = viewPager
            viewPager.adapter= TabAdapter(supportFragmentManager, this@MainActivity2.lifecycle)
            mediator = TabLayoutMediator(tabLayout, viewPager){
                    tab, position ->
                when(position){
                    0 -> tab.text = "Register"
                    1 -> tab.text = "Login"
                }
            }
            mediator.attach()

        }



    }
    fun switchToLoginTab() {
        viewPager2.setCurrentItem(1, true) // Mengganti ke tab "Login" (indeks 1)
    }
}