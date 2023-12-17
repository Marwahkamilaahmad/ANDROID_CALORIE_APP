package com.example.praktikumuts2

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import com.example.praktikumuts2.databinding.ActivityGetstartedBinding

class GetstartedActivity : AppCompatActivity() {

    private var jenisTujuanDiet:String = "";

    private lateinit var binding: ActivityGetstartedBinding
    private val berat = arrayOf(
        "kg","lbs", "pounds"
    )
    private val tujuanDiet = arrayOf(
        "pilih tujuan diet", "cutting", "bulking", "maintaning"
    )
    private var selectedDate: String? = null
    companion object{
        const val EXTRA_BERAT_TARGET = "extra1"
        const val EXTRA_MAKSIMUMKAL= "extra2"
        const val EXTRA_TANGGAL_TARGET = "extra3"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityGetstartedBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            datePicker.visibility = View.GONE
            btnApplyTanggal.visibility = View.GONE

            datePicker.init(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            ){_, year, monthOfYear, dayOfMonth ->
                selectedDate = "$dayOfMonth/${monthOfYear+1}/$year"
            }

            btnApplyTanggal.setOnClickListener(){
                textTanggalGetStarted.setText(selectedDate)
            }

            val adapterKeberatan =
                ArrayAdapter(this@GetstartedActivity, android.R.layout.simple_spinner_item, berat)
            adapterKeberatan.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            spinnerBerat.adapter = adapterKeberatan
            spinnerBeratTujuan.adapter = adapterKeberatan

            val adapterTujuanDiet =
                ArrayAdapter(this@GetstartedActivity, android.R.layout.simple_spinner_item, tujuanDiet)
            adapterTujuanDiet.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            spinnerTujuanDiet.adapter = adapterTujuanDiet

            spinnerTujuanDiet.onItemSelectedListener=
                object  : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id : Long) {
                        jenisTujuanDiet = tujuanDiet[position]
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

            btnTambahtgl.setOnClickListener{
                datePicker.visibility = View.VISIBLE
                btnApplyTanggal.visibility = View.VISIBLE
            }

//            btnHitung.setOnClickListener{
//
//                val maksimumKaloriIn = if( maksimumKalori.text.isNotEmpty()){
//                    maksimumKalori.text.toString()
//                    }else{
//                    Toast.makeText(this@GetstartedActivity, "maksimum kal wajib diisi", Toast.LENGTH_SHORT).show()
//                }
//                val beratTujuanIn = if( textBeratTujuan.text.isNotEmpty()){
//                    textBeratTujuan.text.toString()
//                }else{
//                    Toast.makeText(this@GetstartedActivity, "target berat wajib diisi", Toast.LENGTH_SHORT).show()
//                }
//
//                val tanggalTargetIn = if( textTanggalGetStarted.text.isNotEmpty()){
//                    textTanggalGetStarted.text.toString()
//                }else{
//                    Toast.makeText(this@GetstartedActivity, "target tanggal wajib diisi", Toast.LENGTH_SHORT).show()
//                }
//
//
//                val intentToHomePage = Intent(this@GetstartedActivity, HomePageActivity::class.java)
//                    .apply {
//                        putExtra(EXTRA_MAKSIMUMKAL, maksimumKaloriIn.toString())
//                        putExtra(EXTRA_BERAT_TARGET, beratTujuanIn.toString())
//                        putExtra(EXTRA_TANGGAL_TARGET, tanggalTargetIn.toString())
//                    }
//                startActivity(intentToHomePage)
//
//
//            }

            btnHitung.setOnClickListener {
                val maksimumKaloriIn = maksimumKalori.text.toString()
                val beratTujuanIn = textBeratTujuan.text.toString()
                val tanggalTargetIn = textTanggalGetStarted.text.toString()

                if (maksimumKaloriIn.isNotEmpty() && beratTujuanIn.isNotEmpty() && tanggalTargetIn.isNotEmpty()) {
                    val intentToHomePage = Intent(this@GetstartedActivity, HomePageActivity::class.java)
                        .apply {
                            putExtra(EXTRA_MAKSIMUMKAL, maksimumKaloriIn)
                            putExtra(EXTRA_BERAT_TARGET, beratTujuanIn)
                            putExtra(EXTRA_TANGGAL_TARGET, tanggalTargetIn)
                        }
                    startActivity(intentToHomePage)
                } else {
                    // Menampilkan pesan Toast jika ada yang tidak diisi
                    if (maksimumKaloriIn.isEmpty()) {
                        Toast.makeText(this@GetstartedActivity, "Maksimum kalori wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                    if (beratTujuanIn.isEmpty()) {
                        Toast.makeText(this@GetstartedActivity, "Target berat wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                    if (tanggalTargetIn.isEmpty()) {
                        Toast.makeText(this@GetstartedActivity, "Target tanggal wajib diisi", Toast.LENGTH_SHORT).show()
                    }
                }
            }






        }
    }



}