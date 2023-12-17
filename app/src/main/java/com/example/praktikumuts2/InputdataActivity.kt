package com.example.praktikumuts2

import android.R
import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isNotEmpty
import com.example.praktikumuts2.databinding.ActivityInputdataBinding
import java.text.SimpleDateFormat
import java.util.Locale

class InputdataActivity : AppCompatActivity() {

    private lateinit var binding:ActivityInputdataBinding
    private val jenisMakan = arrayOf(
        "pilih waktu makan", "sarapan", "makan siang", "makan malam"
    )

    private var jenisMakanDipilih:String = "";
    private var selectedTime: String? = null
    private var waktuInput: String? = null
    private var selectedTimeMakan: String? = null


    companion object{
        const val EXTRA_JENIS_MAKAN = "extra1"
        const val EXTRA_KALORIMAKAN= "extra2"
        const val EXTRA_KALORIWORKOUT = "extra3"
        const val EXTRA_LATESTUPDATE = "extra4"
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInputdataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)
        waktuInput = currentDate

        with(binding) {
            timePicker.visibility = View.GONE
            btnApplyWaktu.visibility = View.GONE

            timePickerMakan.visibility = View.GONE
            btnApplyWaktuMakan.visibility = View.GONE

            val adapterWaktuMakan =
                ArrayAdapter(this@InputdataActivity, R.layout.simple_spinner_item, jenisMakan)
            adapterWaktuMakan.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            spinnerJenisMakan.adapter = adapterWaktuMakan

            btnTambahWaktu.setOnClickListener {
                timePicker.visibility = View.VISIBLE
                btnApplyWaktu.visibility = View.VISIBLE  }


            btnTambahWaktuMakan.setOnClickListener {
                timePickerMakan.visibility = View.VISIBLE
                btnApplyWaktuMakan.visibility = View.VISIBLE  }

            timePicker.setOnTimeChangedListener{view, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)

            }
            timePickerMakan.setOnTimeChangedListener{view, hourOfDay, minute ->
                selectedTimeMakan = String.format("%02d:%02d", hourOfDay, minute)

            }
            btnApplyWaktuMakan.setOnClickListener(){
                waktuMulaiMakan.text = selectedTimeMakan
            }

            btnApplyWaktu.setOnClickListener(){
                textWaktuWorkout.text = selectedTime
            }

            spinnerJenisMakan.onItemSelectedListener=
                object  : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id : Long) {
                        jenisMakanDipilih = jenisMakan[position]

                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

                submitBtn.setOnClickListener{
                    val kaloriWorkoutText = kaloriWorkout.text.toString()
                    val kaloriMakananText = kaloriMakanan.text.toString()

                    val kaloriMakananInt = if (kaloriMakananText.isNotEmpty()) {
                        kaloriMakananText.toInt()
                    } else {
                        0 // Nilai default jika kosong
                    }

                    val kaloriWorkoutInt = if (kaloriWorkoutText.isNotEmpty()) {
                        kaloriWorkoutText.toInt()
                    } else {
                        0 // Nilai default jika kosong
                    }
                    val intentToHomeFromInput = Intent(this@InputdataActivity, HomePageActivity::class.java)
                                            .apply {
                        putExtra(EXTRA_JENIS_MAKAN, jenisMakanDipilih)
                            putExtra(EXTRA_KALORIMAKAN, kaloriMakananInt.toString())
                            putExtra(EXTRA_KALORIWORKOUT,kaloriWorkoutInt.toString())
                            putExtra(EXTRA_LATESTUPDATE,waktuInput)
                    }
                    setResult(Activity.RESULT_OK, intentToHomeFromInput)
                    finish()

                    }

                }

        }

    }


