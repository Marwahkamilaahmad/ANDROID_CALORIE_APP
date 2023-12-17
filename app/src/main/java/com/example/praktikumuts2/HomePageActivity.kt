package com.example.praktikumuts2

import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.praktikumuts2.GetstartedActivity.Companion.EXTRA_BERAT_TARGET
import com.example.praktikumuts2.GetstartedActivity.Companion.EXTRA_MAKSIMUMKAL
import com.example.praktikumuts2.GetstartedActivity.Companion.EXTRA_TANGGAL_TARGET
import com.example.praktikumuts2.InputdataActivity.Companion.EXTRA_JENIS_MAKAN
import com.example.praktikumuts2.InputdataActivity.Companion.EXTRA_KALORIMAKAN
import com.example.praktikumuts2.InputdataActivity.Companion.EXTRA_KALORIWORKOUT
import com.example.praktikumuts2.InputdataActivity.Companion.EXTRA_LATESTUPDATE
import com.example.praktikumuts2.databinding.ActivityHomePageBinding
import com.example.praktikumuts2.databinding.ActivityInputdataBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private var waktuHariIni: String? = null
    private var jenisMakan: String? = null
    private var kaloriMakan: String? = null
    private var kaloriDataWorkout: String? = null
    private var latestUpdate: String? = null
    private var maksimum_kalori: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        maksimum_kalori = intent.getStringExtra(EXTRA_MAKSIMUMKAL)
        val berat_target = intent.getStringExtra(EXTRA_BERAT_TARGET)
        val tanggal_target = intent.getStringExtra(EXTRA_TANGGAL_TARGET)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        waktuHariIni = currentDate

        val tanggal_target_diet = dateFormat.parse(tanggal_target)
        val current_tanggal_diet = dateFormat.parse(currentDate)

        val differentDate = tanggal_target_diet.time - current_tanggal_diet.time
        val differentDays = (differentDate / (24 * 60 * 60 * 1000)).toInt()

        with(binding){

            textTanggalHariIni.text = waktuHariIni
            targetBerat.text = berat_target
            textTanggalTarget.text = tanggal_target
            textSisaKalori.text = maksimum_kalori

            sisaHari.text = differentDays.toString()

            btnInputData.setOnClickListener{
                val intentToDataActivity = Intent(this@HomePageActivity, InputdataActivity::class.java)
                launcher.launch(intentToDataActivity)
            }
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                val data = result.data
                jenisMakan = data?.getStringExtra(EXTRA_JENIS_MAKAN)
                kaloriMakan = data?.getStringExtra(EXTRA_KALORIMAKAN)
                kaloriDataWorkout = data?.getStringExtra(EXTRA_KALORIWORKOUT)
                latestUpdate = data?.getStringExtra(EXTRA_LATESTUPDATE)

                with(binding){
                    latestUpdateMakan.text = latestUpdate
                    latestUpdateWorkout.text = latestUpdate
                    kaloriTerbuang.text = kaloriDataWorkout
                    kaloriWorkout.text = kaloriDataWorkout
                    if(jenisMakan.equals("sarapan")){
                        kaloriSarapan.text = kaloriMakan
                    }
                    if(jenisMakan.equals("makan siang")){
                        kaloriMakanSiang.text = kaloriMakan
                    }
                    if(jenisMakan.equals("makan malam")){
                        kaloriMakanMalam.text = kaloriMakan
                    }

                    val totalKalori = kaloriSarapan.text.toString().toInt() + kaloriMakanMalam.text.toString().toInt() + kaloriMakanSiang.text.toString().toInt()
                    konsumsiKalori.text = totalKalori.toString()
                    val sisaKalori = maksimum_kalori.toString().toInt() - totalKalori + kaloriTerbuang.text.toString().toInt()
                    textSisaKalori.text = sisaKalori.toString()

                }
            }

        }


}

