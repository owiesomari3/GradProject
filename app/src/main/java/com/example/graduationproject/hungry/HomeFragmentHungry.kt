package com.example.graduationproject.hungry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityAfterLoginHungryBinding
import com.example.graduationproject.databinding.FragmentHomeHungryBinding
import com.example.graduationproject.databinding.RecyclerHomeBinding


class HomeFragmentHungry : Fragment() {

    lateinit var binding:FragmentHomeHungryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_hungry, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val foods=ArrayList<DataFood>()
        binding= FragmentHomeHungryBinding.inflate(layoutInflater)
        binding.recyclerHome.layoutManager=LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
        val adapter=CustomAdapterFood(foods)
        binding.recyclerHome.adapter=adapter
    }
}