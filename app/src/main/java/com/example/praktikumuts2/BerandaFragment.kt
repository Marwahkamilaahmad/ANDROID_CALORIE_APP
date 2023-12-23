package com.example.praktikumuts2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import com.example.praktikumuts2.databinding.FragmentBerandaBinding
import com.example.praktikumuts2.databinding.FragmentRiwayatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.ExecutorService

class BerandaFragment : Fragment() {
    private lateinit var binding: FragmentBerandaBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inisialisasi NoteDao
        val dbNote = NoteRoomDatabase.getDatabase(requireActivity())
        mNotesDao = dbNote?.noteDao()!!

        // Inisialisasi FirebaseFirestore
        db = FirebaseFirestore.getInstance()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val kalori = documentSnapshot.getString("target_kalori")
                    kalori?.let {
                        getAllCal(it.toInt())
                    }
                }
        }

        return view
    }

    private fun getAllCal(kalori: Int) {
        mNotesDao.getTotalCalories().observe(viewLifecycleOwner) { totalCalories ->
            totalCalories?.let {
                val remainingCalories = kalori - it
                binding.txtSisaKalori.text = remainingCalories.toString()

                if (remainingCalories <= 500) {
                    // Jika sisa kalori kurang dari 500, tampilkan notifikasi
                    showNotification()
                }
            }
        }
    }

    private fun showNotification() {
        val notificationIntent = Intent(requireContext(), MyBroadcastReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            requireContext(),
//            0,
//            notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "MyChannelId"
        val channelName = "MyChannelName"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Low Calories")
            .setContentText("Remaining calories are less than 500.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(200, builder.build())
    }
}
