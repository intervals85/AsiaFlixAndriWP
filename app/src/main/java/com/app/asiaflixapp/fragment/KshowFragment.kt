package com.app.asiaflixapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.HomeAdapter
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.FragmentKshowBinding
import com.app.asiaflixapp.model.FilmModel


class KshowFragment : Fragment() {

    lateinit var binding:FragmentKshowBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKshowBinding.inflate(layoutInflater)
        getData()
        return binding.root
    }

    private fun getData() {
        val films :ArrayList<FilmModel> = arrayListOf()
        for (i in 0..15) {
            films.add(
                FilmModel("this is titl $i","https://imagecdn.me/cover/love-star-2023-1680537786.png",
                "https://www1.watchasian.id/love-star-2023-episode-22.html $i", "Ep 12" ,"2023")
            )
        }
        binding.recyclerView.adapter = ListAdapter(films)
    }
}