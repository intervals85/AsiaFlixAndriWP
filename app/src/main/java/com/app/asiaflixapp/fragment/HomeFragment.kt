package com.app.asiaflixapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.HomeAdapter
import com.app.asiaflixapp.adapter.SliderAdapter
import com.app.asiaflixapp.databinding.FragmentHomeBinding
import com.app.asiaflixapp.model.BannerModel
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.HomeModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

class HomeFragment : Fragment() {

    lateinit var binding :FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        getData()
        initSlider()
        return binding.root
    }

    private fun initSlider() {
        val sliderAdapter = SliderAdapter(requireContext())
        binding.imageSlider?.setSliderAdapter(sliderAdapter!!)
        binding.imageSlider?.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider?.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider?.startAutoCycle()
        for (i in 0..5) {
            val item =BannerModel("this title $i", "https://imagecdn.me/cover/love-star-2023-1680537786.png", "2023","https://www1.watchasian.id/love-star-2023-episode-22.html $i", )
            sliderAdapter.addItem(item)
        }
        sliderAdapter.notifyDataSetChanged()
    }

    private fun getData() {
        val list :ArrayList<HomeModel> = arrayListOf()
        val films :ArrayList<FilmModel> = arrayListOf()
        for (i in 0..5) {
            films.add(FilmModel("this is titl $i","https://imagecdn.me/cover/love-star-2023-1680537786.png",
                "https://www1.watchasian.id/love-star-2023-episode-22.html $i", "Ep 12" ))
        }
        for (i in 0..3) {
            list.add(HomeModel("Korea $i", "link $i", films))

        }
        binding.recyclerView.adapter =HomeAdapter(list)
    }
}