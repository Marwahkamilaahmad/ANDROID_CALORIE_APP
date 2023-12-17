package com.example.praktikumuts2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.praktikumuts2.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val view = binding.root

        val sharedPreferences = SharedPreference(requireActivity())

        with(binding){

            btnLogin.setOnClickListener {
                val password = textPassLogin.text.toString()
                val email = textEmail.text.toString()

                if (email.isEmpty()) {
                    textEmail.setError("username ini harus diisi!")
                } else if (password.isEmpty()){
                    textPassLogin.setError("password ini harus diisi!")// Menghapus pesan kesalahan jika sudah diisi
                } else {
                    LoginFirebase(email,password, sharedPreferences)

//                    val intent = Intent(this@LoginFragment.requireActivity(), HomePageActivity::class.java)
//                    startActivity(intent)
                }
            }
        }
        return view
    }

//    private fun LoginFirebase(email: String, password: String, sharedPreferences: SharedPreference) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener(requireActivity()) { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(requireContext(), "Selamat datang $email", Toast.LENGTH_SHORT).show()
//                    sharedPreferences.setStatusLogin(true) // Mengatur status login menjadi true setelah login berhasil
//                    val intent = Intent(requireContext(), ProfileActivity::class.java)
//                    startActivity(intent)
//                } else {
//                    Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }

    private fun LoginFirebase(email: String, password: String, sharedPreferences: SharedPreference) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val userId = currentUser?.uid

                    // Ambil informasi role dari Firestore
                    db.collection("users").document(userId!!)
                        .get()
                        .addOnSuccessListener { documentSnapshot ->
                            val role = documentSnapshot.getString("role")

                            if (role == "admin") {
                                // Navigasi ke halaman admin
                                val intent = Intent(requireContext(), AdminActivity::class.java)
                                startActivity(intent)
                            } else {
                                // Navigasi ke halaman user biasa
                                val intent = Intent(requireContext(), ProfileActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Failed to fetch user role: $e",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(requireContext(), "${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



}