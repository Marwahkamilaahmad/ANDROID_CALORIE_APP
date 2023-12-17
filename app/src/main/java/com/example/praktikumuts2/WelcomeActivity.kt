package com.example.praktikumuts2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.praktikumuts2.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

            getStarted.setOnClickListener{
                val intentToGetStarted = Intent(this@WelcomeActivity, GetstartedActivity::class.java)
                startActivity(intentToGetStarted)
            }
        }




    }
}