package com.app.asiaflixapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.asiaflixapp.adapter.EpisodeAdapter
import com.app.asiaflixapp.databinding.ActivityDetailBinding
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.EpisodeModel

class DetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utils.loadImage(this, "https://imagecdn.me/cover/love-star-2023-1680537786.png", binding.image)
        getEpisode()
        initOnClick()
    }

    private fun initOnClick() {
        binding.btnBack.setOnClickListener { finish() }
    }

    private fun getEpisode() {
        val episodes :ArrayList<EpisodeModel> = arrayListOf();
        for (i in 1 ..30){
            episodes.add(EpisodeModel("Episode $i", "this link $i"))
        }
        binding.recyclerView.adapter = EpisodeAdapter(episodes)
        binding.nested.fullScroll(View.FOCUS_UP);
        binding.nested.fullScroll(View.FOCUS_UP);
    }
}