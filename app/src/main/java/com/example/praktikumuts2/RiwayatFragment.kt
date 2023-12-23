package com.example.praktikumuts2

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.praktikumuts2.databinding.FragmentRiwayatBinding
import com.example.praktikumuts2.databinding.FragmentSearchBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class RiwayatFragment : Fragment() {

    private lateinit var binding: FragmentRiwayatBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {



        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRiwayatBinding.inflate(inflater, container, false)
        executorService = Executors.newSingleThreadExecutor()
        val dbNote = NoteRoomDatabase.getDatabase(requireContext())
        mNotesDao = dbNote?.noteDao()!!
        val view = binding.root

        getAllNotes()

        with(binding){

        }
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        with(binding){
            buttonAdd.setOnClickListener{
            findNavController().navigate(R.id.action_riwayatFragment_to_searchFragment2)
            }
        }


    }

    private fun getAllNotes() {
        mNotesDao.allMenus.observe(requireActivity()) { menu ->
            val noteAdapter = NoteAdapter(ArrayList<Note>()) {
                    menu -> deleteMenu(menu)
            }
            binding.rvRiwayat.apply {
                adapter = noteAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            noteAdapter.setData(menu)
        }


    }

    private fun deleteMenu(menu: Note){
        executorService.execute{ mNotesDao.delete(menu)}
    }


}