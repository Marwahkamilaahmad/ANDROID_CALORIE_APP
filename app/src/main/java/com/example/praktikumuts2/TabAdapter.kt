package com.example.praktikumuts2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter (fm: FragmentManager, Lifecycle : Lifecycle) : FragmentStateAdapter(fm, Lifecycle){
    val page = arrayOf<Fragment>( RegisterFragment(), LoginFragment())

    override fun getItemCount(): Int {

        return page.size
    }

    override fun createFragment(position: Int): Fragment {

        return page[position]
    }


}