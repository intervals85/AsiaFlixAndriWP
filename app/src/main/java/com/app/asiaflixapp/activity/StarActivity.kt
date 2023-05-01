package com.app.asiaflixapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.ActivityStarBinding
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.StarModel
import com.google.gson.Gson
import org.jsoup.Jsoup

class StarActivity : AppCompatActivity(), FetchPage.Listener, View.OnClickListener {
    val TAG ="StarActivityTAG"
    lateinit var binding: ActivityStarBinding
    lateinit var starModel: StarModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipeRefreshLayout.isRefreshing=true
        binding.relativeMain.visibility=View.GONE
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }
        getData()
        initData()
        binding.textProfile.setOnClickListener(this)
        binding.textFilms.setOnClickListener(this)
    }

    private fun getData() {
        starModel = Gson().fromJson(intent.getStringExtra("json"), StarModel::class.java)
        FetchPage(this).execute(Utils.base_url+starModel.link)
    }

    private fun initData() {
        Utils.loadImage(this, starModel.imageUrl!!, binding.image)
        Utils.loadImage(this, starModel.imageUrl!!, binding.imageAvatar)
        binding.textViewName.text = starModel.name
    }

    override fun onSuccess(result: String) {
        binding.relativeMain.visibility=View.VISIBLE
        binding.errorLayout.root.visibility=View.GONE
        binding.swipeRefreshLayout.isRefreshing=false
        val doc = Jsoup.parse(result)
        val contentleft = doc.getElementsByClass("content-left")
            .select("div.info")
        contentleft.select("h1").remove()

        Log.d(TAG, "onSuccess: ${contentleft.select("p").first()?.text()}")

        val otherName =  contentleft.select("p").first()?.text()
        binding.textMore.text=  Html.fromHtml("<p> $otherName </p>")
        contentleft.select("p").first()?.remove()
        binding.textMore.append( Html.fromHtml(contentleft.toString()))

        val filmsElements =  doc.getElementsByClass("content-left").select("div.block-tab")
            .select("div.left-tab-1").select("ul.list-episode-item")
            .select("li")
        val adapter= ListAdapter()
        filmsElements.forEach {
            val title = it.select("a").select("h3").text()
            val image = it.select("a").select("img").attr("data-original")
            val link = it.select("a").attr("href")
            val film = FilmModel(title,image,
                link,  )
            adapter.addData(film)
        }
        binding.textFilm.text ="Play in Film ${adapter.data.size}"
        binding.recyclerView.adapter= adapter

    }

    override fun onFailed(error: String) {
        binding.relativeMain.visibility=View.GONE
        binding.errorLayout.root.visibility=View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing=false

    }

    override fun onClick(p0: View ) {
        when(p0.id) {
            R.id.text_profile ->{
                binding.textFilms.background=null
                binding.textProfile.setBackgroundResource(R.drawable.background_button_library)
                binding.textMore.visibility=View.VISIBLE
                binding.recyclerView.visibility=View.GONE
            }
            R.id.text_films->{
                binding.textProfile.background=null
                binding.textFilms.setBackgroundResource(R.drawable.background_button_library)
                binding.textMore.visibility=View.GONE
                binding.recyclerView.visibility=View.VISIBLE
            }
        }
    }
}