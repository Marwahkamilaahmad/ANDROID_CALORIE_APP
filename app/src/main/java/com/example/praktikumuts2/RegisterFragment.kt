
package com.example.praktikumuts2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.praktikumuts2.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    var db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        val view = binding.root

        with(binding){
            settingCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
                btnRegister.isEnabled = isChecked
            }

            btnRegister.setOnClickListener() {

                val username = textUname.text.toString()
                val password = textPass.text.toString()
                val email = textEmail.text.toString()
                val targetKal = textKaloritarget.text.toString()
                val beratB = textBeratbadan.text.toString()
                val tinggiB = textTinggibadan.text.toString()


                if (username.isEmpty()) {
                    textUname.setError("username ini harus diisi!")
                } else if (password.isEmpty()){
                    textPass.setError("password ini harus diisi!")// Menghapus pesan kesalahan jika sudah diisi
                } else if(email.isEmpty()) {
                    textEmail.setError("email ini harus diisi!")
                } else {
                    // Menggantikan fragment saat ini dengan LoginFragment

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()){ task ->
                            if (task.isSuccessful){
                                val prefManager = PrefManager.getInstance(requireContext())
                                prefManager.saveEmail(email)
                                prefManager.savePassword(password)
                                prefManager.setLoggedIn(true)

                                val user = auth.currentUser
                                val userId = user?.uid

                                val newUsers = Users(
                                    id= userId.toString(),
                                    username = username, tinggi_badan = tinggiB,
                                    berat_badan = beratB, target_kalori = targetKal
                                )
                                addUser(newUsers)

                                Toast.makeText(
                                    requireContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }else{
                                Toast.makeText(
                                    requireContext(),
                                    "Authentication failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show();

                            }

                        }
                    val activity = requireActivity() as MainActivity2
                    activity.switchToLoginTab()

                    val fragmentManager = requireActivity().supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.addToBackStack(null)

                    transaction.commit()

                }

            }
        }


        return view
    }

    private fun addUser(user : Users){
        db.collection("users")
            .document(user.id) // Menggunakan document dengan ID yang sama dengan user.id
            .set(user)
            .addOnFailureListener { e ->
                Log.d("MainActivity", "Error adding user : ", e)
                Toast.makeText(
                    requireContext(),
                    "insert Failed",
                    Toast.LENGTH_SHORT
                ).show();
            }
    }

}