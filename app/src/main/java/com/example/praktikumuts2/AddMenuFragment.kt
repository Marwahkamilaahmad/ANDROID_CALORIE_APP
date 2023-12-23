package com.example.praktikumuts2

import android.R
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.example.praktikumuts2.databinding.FragmentAddMenuBinding
import com.example.praktikumuts2.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Locale


class AddMenuFragment : Fragment() {

    private lateinit var binding: FragmentAddMenuBinding
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
        super.onCreate(savedInstanceState)
        arguments?.let {


            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            waktuInput = currentDate


            val adapterWaktuMakan =
                ArrayAdapter(requireContext(), R.layout.simple_spinner_item, jenisMakan)
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


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMenuBinding.inflate(inflater, container, false)
        val view = binding.root




        return view
    }



}