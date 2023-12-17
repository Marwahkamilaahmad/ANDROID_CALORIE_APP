package com.example.praktikumuts2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.praktikumuts2.databinding.ActivityMain2Binding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private lateinit var mediator: TabLayoutMediator
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Dashboard"
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)


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