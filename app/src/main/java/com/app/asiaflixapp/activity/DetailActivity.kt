package com.app.asiaflixapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.CastAdapter
import com.app.asiaflixapp.adapter.EpisodeAdapter
import com.app.asiaflixapp.databinding.ActivityDetailBinding
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.CastModel
import com.app.asiaflixapp.model.EpisodeModel
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class DetailActivity : AppCompatActivity(), FetchPage.Listener {
    val TAG ="DetailActivityTAG"
    lateinit var binding:ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOnClick()
        FetchPage( this)
            .execute(Utils.base_url + intent.getStringExtra("link"))
    }
    override fun onSuccess(result: String) {
        val document  = Jsoup.parse(result)
        val elements = document.getElementsByClass("block").select("div.details")
        val image = elements.select("div.img").select("img").attr("src")
        Utils.loadImage(applicationContext, image, binding.image)
        val title = elements.select("div.info").select("h1").text()
        binding.textTitle.text= title
        binding.textReleased.text =""
        val p = elements.select("div.info").select("p")
        p.forEachIndexed { index, it ->
            if (index==2) {
                binding.textDescription.text = it.text()
            }
            if (index in 5..7) {
                it.select("span").remove()
                val text = it.text()
                binding.textReleased.append("$text | ")
            }

            if (index == p.size-1) {
                binding.linearTag.removeAllViews()
                val a = it.select("a")
                a.forEach { genre->
                    Log.d(TAG, "onSuccess: ${genre.text()}")
                   binding.linearTag.addView( Utils.tagView( genre.text(), layoutInflater))
                }
            }
        }

        val otherName =  elements.select("div.info").select("p.other_name")
        binding.textTitle2.text = otherName.text().replace("Other name: ","")
        val castElement = document.getElementsByClass("block").select("div.slider-star")
            .select("div.item")
        setUpForCast(castElement)
        val episodeElements = document.getElementsByClass("block-tab")
            .select("div.tab-content")
            .select("ul.all-episode").select("li")

        getEpisode(episodeElements)
    }

    private fun setUpForCast(castElement: Elements) {
        val list :ArrayList<CastModel> = arrayListOf()
        castElement.forEach {
            val name = it.select("a").select("h3").text()
            val image = it.select("a").select("img").attr("src")
            val link =  it.select("a").attr("href")
            list.add(CastModel(name, image, link))
        }
        binding.recyclerViewCast.adapter= CastAdapter(list)
    }

    private fun getEpisode(episodeElements: Elements) {
        val episodes :ArrayList<EpisodeModel> = arrayListOf()
        episodeElements.forEach {
            val title = it.select("a").select("h3").text()
                .replace(binding.textTitle.text.toString(), "")
            val link =it.select("a").attr("href")
            Log.d(TAG, "getEpisode: $title")
            episodes.add(EpisodeModel(title, link))
        }
        binding.recyclerView.adapter = EpisodeAdapter(episodes)
        binding.btnSort.setOnClickListener {
            episodes.reverse()
            binding.recyclerView.adapter = EpisodeAdapter(episodes)
        }
    }

    override fun onFailed(error: String) {
        Log.d(TAG, "onFailed: $error")
    }


    private fun initOnClick() {
        binding.btnShare.setOnClickListener {
            val text = "I just watch ${binding.textTitle.text.toString()} with ${resources.getString(
                R.string.app_name)} , you can download here  https://play.google.com/store/apps/developer?id=$packageName "
            val b = Intent(
                Intent.ACTION_SEND)
            b.type = "text/plain"
            b.putExtra(
                Intent.EXTRA_TEXT,  text)
            startActivity(Intent.createChooser(
                b,
                "Share Via"))
        }
        binding.btnBack.setOnClickListener { finish() }
    }

}