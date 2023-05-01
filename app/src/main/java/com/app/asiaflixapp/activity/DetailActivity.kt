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
import com.app.asiaflixapp.db.Favorite
import com.app.asiaflixapp.db.LocalDatabase
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.CastModel
import com.app.asiaflixapp.model.EpisodeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class DetailActivity : AppCompatActivity(), FetchPage.Listener {
    val database: LocalDatabase by lazy { LocalDatabase.getDatabase(this) }
    val TAG = "DetailActivityTAG"
    lateinit var binding: ActivityDetailBinding
    var image = ""
    var actualLink =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOnClick()
        binding.relativeMain.visibility=View.GONE
        binding.swipeRefreshLayout.isRefreshing=true
        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }
        getData()

        initButtonFav()
    }

    private fun getData() {
        actualLink = getActualLink(intent.getStringExtra("link"))
        if (intent.hasExtra("linkNotActual")) {
            FetchPage(object : FetchPage.Listener {
                override fun onSuccess(result: String) {
                    val document = Jsoup.parse(result)
                    val link = document.getElementsByClass("watch-drama")
                        .select("div.category").select("a").attr("href")
                    Log.d(TAG, "onSuccess: $link")
                    actualLink =getActualLink(link)
                    FetchPage(this@DetailActivity)
                        .execute(actualLink)
                }

                override fun onFailed(error: String) {
                    binding.relativeMain.visibility=View.GONE
                    binding.errorLayout.root.visibility=View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing=false
                }
            }).execute(actualLink)
        } else {
            FetchPage(this)
                .execute(actualLink)
        }
    }

    private fun initButtonFav() {
        val size = database.favoriteDao().checkFav(actualLink).size
        if (size>0 ) binding.fab.setImageDrawable(resources.getDrawable(R.drawable.ic_heart))
        else  binding.fab.setImageDrawable(resources.getDrawable(R.drawable.ic_love))
        binding.fab.setOnClickListener {
            if (size>0) database.favoriteDao().deleteFavoriteByLink(actualLink)
            else {
                val fav = Favorite(
                    binding.textTitle.text.toString(),
                    image,   actualLink, System.currentTimeMillis()
                )
                database.favoriteDao().insertFavorite(fav)
            }
            initButtonFav()
        }

    }


    private fun getActualLink(stringExtra: String?): String {
        return if (stringExtra!!.startsWith("http"))
            stringExtra
        else Utils.base_url + stringExtra!!
    }

    override fun onSuccess(result: String) {
        binding.relativeMain.visibility=View.VISIBLE
        binding.errorLayout.root.visibility=View.GONE
        binding.swipeRefreshLayout.isRefreshing=false
        val document = Jsoup.parse(result)
        val elements = document.getElementsByClass("block").select("div.details")
          image = elements.select("div.img").select("img").attr("src")
        Utils.loadImage(applicationContext, image, binding.image)
        val title = elements.select("div.info").select("h1").text()
        binding.textTitle.text = title
        binding.textReleased.text = ""
        val p = elements.select("div.info").select("p")
        p.forEachIndexed { index, it ->
            if (index == 2) {
                binding.textDescription.text = it.text()
            }
            if (index in 5..7) {
                it.select("span").remove()
                val text = it.text()
                binding.textReleased.append("$text | ")
            }

            if (index == p.size - 1) {
                binding.linearTag.removeAllViews()
                val a = it.select("a")
                a.forEach { genre ->
                    Log.d(TAG, "onSuccess: ${genre.text()}")
                    binding.linearTag.addView(Utils.tagView(genre.text(), layoutInflater))
                }
            }
        }

        val otherName = elements.select("div.info").select("p.other_name")
        binding.textTitle2.text = otherName.text().replace("Other name: ", "")
        val castElement = document.getElementsByClass("block").select("div.slider-star")
            .select("div.item")
        setUpForCast(castElement)
        val episodeElements = document.getElementsByClass("block-tab")
            .select("div.tab-content")
            .select("ul.all-episode").select("li")

        getEpisode(episodeElements)
    }

    private fun setUpForCast(castElement: Elements) {
        val list: ArrayList<CastModel> = arrayListOf()
        castElement.forEach {
            val name = it.select("a").select("h3").text()
            val image = it.select("a").select("img").attr("src")
            val link = it.select("a").attr("href")
            list.add(CastModel(name, image, link))
        }
        binding.recyclerViewCast.adapter = CastAdapter(list)
        if (list.isEmpty()) binding.linearCast.visibility = View.GONE
    }

    private fun getEpisode(episodeElements: Elements) {
        val episodes: ArrayList<EpisodeModel> = arrayListOf()
        episodeElements.forEach {
            val title = it.select("a").select("h3").text()
                .replace(binding.textTitle.text.toString(), "")
            val link = it.select("a").attr("href")
            Log.d(TAG, "getEpisode: $title")
            episodes.add(EpisodeModel(title, link))
        }
        val adapter = EpisodeAdapter(episodes)
        binding.recyclerView.adapter = adapter
        binding.btnSort.setOnClickListener {
            episodes.reverse()
            adapter.data = episodes
            binding.recyclerView.adapter = adapter
        }
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = episodes[position]
                val intent = Intent(this@DetailActivity, EpisodeActivity::class.java)
                intent.putExtra("film_title", binding.textTitle.text.toString())
                intent.putExtra("film_image",  image)
                intent.putExtra("link", item.episodeLink)
                intent.putExtra("title", item.episode)
                startActivity(intent)
            }
        })
    }

    override fun onFailed(error: String) {
        binding.relativeMain.visibility=View.GONE
        binding.errorLayout.root.visibility=View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing=false
        Log.d(TAG, "onFailed: $error")
    }


    private fun initOnClick() {
        binding.btnShare.setOnClickListener {
            val text = "I just watch ${binding.textTitle.text.toString()} with ${
                resources.getString(
                    R.string.app_name
                )
            } , you can download here  https://play.google.com/store/apps/developer?id=$packageName "
            val b = Intent(
                Intent.ACTION_SEND
            )
            b.type = "text/plain"
            b.putExtra(
                Intent.EXTRA_TEXT, text
            )
            startActivity(
                Intent.createChooser(
                    b,
                    "Share Via"
                )
            )
        }
        binding.btnBack.setOnClickListener { finish() }
    }

}