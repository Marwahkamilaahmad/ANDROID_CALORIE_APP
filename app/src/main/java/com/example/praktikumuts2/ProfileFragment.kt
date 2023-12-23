package com.example.praktikumuts2

import PrefManager
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.praktikumuts2.databinding.FragmentProfileBinding
import com.example.praktikumuts2.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        // Ambil informasi role dari Firestore
        db.collection("users").document(userId!!)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val username = documentSnapshot.getString("username")
                val kalori = documentSnapshot.getString("target_kalori")
                val beratbadan = documentSnapshot.getString("berat_badan")
                val tinggibadan = documentSnapshot.getString("tinggi_badan")
                binding.textNama.text = username
                binding.textEmail.text =  currentUser.email
                binding.textKalori.text = kalori
                binding.textBeratBadan.text = beratbadan
                binding.textTinggiBadan.text = tinggibadan
            }

        with(binding){
            buttonLogout.setOnClickListener {
                val message : String = "are you sure want to logout ? ";
                showCustomDialogBox(message)
                PrefManager.getInstance(this@ProfileFragment.requireActivity()).clear()
            }
        }
        return view
    }

    private fun showCustomDialogBox(message: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_logout_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Setup dialog components
        dialog.findViewById<TextView>(R.id.textViewMessage)?.text = message

        // Set button actions
        val buttonYes = dialog.findViewById<Button>(R.id.button_yes)
        val buttonNo = dialog.findViewById<Button>(R.id.button_no)

        buttonYes.setOnClickListener {
            dialog.dismiss()
            auth.signOut()
            val intent = Intent(requireContext(), MainActivity2::class.java)
            startActivity(intent)
            activity?.finish()
        }

        buttonNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



}