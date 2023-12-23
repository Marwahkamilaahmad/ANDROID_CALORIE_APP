package com.example.praktikumuts2

import android.R
import android.content.Intent
import android.icu.util.Calendar
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.example.praktikumuts2.databinding.ActivityAddMenuBinding
import com.example.praktikumuts2.databinding.FragmentAddMenuBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMenuBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    var storage = FirebaseStorage.getInstance().reference
    var db = FirebaseFirestore.getInstance()
    private lateinit var imagesRef : StorageReference
    private val menuCollectionRef = db.collection("menu")

    private val menuListLiveData : MutableLiveData<List<Menu>>
            by lazy {
                MutableLiveData<List<Menu>>()
            }

    private val jenisMakan = arrayOf(
        "pilih waktu makan", "sarapan", "makan siang", "makan malam"
    )


    private var jenisMakanDipilih:String = "";
    private var selectedTime: String? = null
    private var waktuInput: String? = null
    private var selectedTimeMakan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddMenuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val dbNote = NoteRoomDatabase.getDatabase(this)
        mNotesDao = dbNote?.noteDao()!!
        executorService = Executors.newSingleThreadExecutor()

        binding.timePickerMakan.visibility = View.GONE

        val data = intent.extras

        with(binding){
            val id = data?.getString("extraid")
            kaloriMakanan.setText(data?.getString("extrajumlahkalori"))
            namaMakanan.setText(data?.getString("extranamamakanan"))

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            waktuInput = currentDate

            btnTambahWaktuMakan.setOnClickListener {
                binding.timePickerMakan.visibility = View.VISIBLE
            }

            val adapterWaktuMakan =
                ArrayAdapter(this@AddMenuActivity, R.layout.simple_spinner_item, jenisMakan)
            adapterWaktuMakan.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)
            binding.spinnerJenisMakan.adapter = adapterWaktuMakan

            binding.spinnerJenisMakan.onItemSelectedListener=
                object  : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id : Long) {
                        jenisMakanDipilih = jenisMakan[position]
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }

            timePickerMakan.setOnTimeChangedListener{view, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
            }


            btnApplyWaktuMakan.setOnClickListener {
                waktuMulaiMakan.setText(selectedTime)
            }

            submitBtn.setOnClickListener {
                val waktu = waktuMulaiMakan.text.toString()
                val nama = namaMakanan.text.toString()
                val sajian = takaranSaji.text.toString()
                val kalori = kaloriMakanan.text.toString()

                val newMenu = Note( nama_makanan = nama, tanggal = waktu, waktu_makan = jenisMakanDipilih, takaran_saji = sajian, jumlah_kalori = kalori)
                insert(newMenu)
                val intent = Intent(this@AddMenuActivity, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }




        }




    }


    private fun insert(menu: Note) {
        executorService.execute { mNotesDao.insert(menu) }
    }

    private fun update(menu : Note){
        executorService.execute{ mNotesDao.update(menu)}
    }
}