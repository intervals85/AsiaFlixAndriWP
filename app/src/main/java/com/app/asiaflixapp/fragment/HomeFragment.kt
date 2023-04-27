package com.app.asiaflixapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.HomeAdapter
import com.app.asiaflixapp.adapter.SliderAdapter
import com.app.asiaflixapp.databinding.FragmentHomeBinding
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.BannerModel
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.HomeModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import org.jsoup.Jsoup

class HomeFragment : Fragment(), FetchPage.Listener {
    val TAG ="HomeFragmentTAG"
    lateinit var binding :FragmentHomeBinding
    val list :ArrayList<HomeModel> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

      //  getData()
      //  initSlider()
        FetchPage(this).execute(Utils.base_url)
        getPopular()
        return binding.root
    }

    private fun getPopular() {
        val sliderAdapter = SliderAdapter(requireContext())
        binding.imageSlider?.setSliderAdapter(sliderAdapter!!)
        binding.imageSlider?.setIndicatorAnimation(IndicatorAnimationType.WORM)
        binding.imageSlider?.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.imageSlider?.startAutoCycle()

        FetchPage(object :FetchPage.Listener{
            override fun onSuccess(result: String) {
                val doc = Jsoup.parse(result)
                val views = doc.select("ul").select("li")
                views.forEach {
                    val title = it.select("a").select("p.title").text()
                    val released =  it.select("a").select("p.reaslead").text()
                    val link =  it.select("a").attr("href")
                    val image =  it.select("a").select("div.cover").attr("style")

                    val split = image.split("'")
                    val item =BannerModel(title, split[1],
                        released,link, )
                    sliderAdapter.addItem(item)

                    Log.d(TAG, "onSuccess: $title $released $link $image ")
                }
                sliderAdapter.notifyDataSetChanged()
            }

            override fun onFailed(error: String) {

            }

        }).execute(Utils.base_url+"anclytic.html?id=1")
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
   /*     for (i in 0..5) {
            films.add(FilmModel("this is titl $i","https://imagecdn.me/cover/love-star-2023-1680537786.png",
                "https://www1.watchasian.id/love-star-2023-episode-22.html $i", "Ep 12" ))
        }
        for (i in 0..3) {
            list.add(HomeModel("Korea $i", "link $i", films))

        }
        binding.recyclerView.adapter =HomeAdapter(list)*/
    }

    override fun onSuccess(result: String) {
        val doc = Jsoup.parse(result)
        val contentLeft = doc.getElementsByClass("content-left")
            .select("div.tab-container")

        val recentDrama =  contentLeft.select("div.left-tab-1").select("ul")
            .select("li")

        val recentFilms :ArrayList<FilmModel> = arrayListOf()
        recentDrama.forEach {
            val title = it.select("a").select("h3").text()
            val episode = it.select("a").select("span.ep").text()
            val image = it.select("a").select("img").attr("data-original")
            val link = it.select("a").attr("href")
            recentFilms.add(FilmModel(title,image,
                link, episode ))
        }
        list.add(HomeModel("Recently Drama", "recently-added", recentFilms))

        val recentMovies :ArrayList<FilmModel> = arrayListOf()

        val recentMovie =  contentLeft.select("div.left-tab-2").select("ul")
            .select("li")
        recentMovie.forEach {
            val title = it.select("a").select("h3").text()
            val episode = it.select("a").select("span.ep").text()
            val image = it.select("a").select("img").attr("data-original")
            val link = it.select("a").attr("href")

            recentMovies.add(FilmModel(title,image,
                link, episode ))
        }
        list.add(HomeModel("Recently Movie", "recently-added-movie", recentMovies))

        val recentKhows :ArrayList<FilmModel> = arrayListOf()
        val recentKhow =  contentLeft.select("div.left-tab-3").select("ul")
            .select("li")
        recentKhow.forEach {
            val title = it.select("a").select("h3").text()
            val episode = it.select("a").select("span.ep").text()
            val image = it.select("a").select("img").attr("data-original")
            val link = it.select("a").attr("href")

            recentKhows.add(FilmModel(title,image,
                link, episode ))
        }
        list.add(HomeModel("Recently KShow", "recently-added-kshow", recentKhows))

        binding.recyclerView.adapter =HomeAdapter(list)


    }

    override fun onFailed(error: String) {

    }
}