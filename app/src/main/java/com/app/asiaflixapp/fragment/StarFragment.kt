package com.app.asiaflixapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.FragmentStarBinding
import com.app.asiaflixapp.model.FilmModel


class StarFragment : Fragment() {

    lateinit var binding:FragmentStarBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStarBinding.inflate(layoutInflater)
        return binding.root
    }


}