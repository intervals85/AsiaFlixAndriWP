package com.app.asiaflixapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.ActivitySearchBinding
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.FilmModel
import org.jsoup.Jsoup

class SearchActivity : AppCompatActivity() {
    val TAG ="SearchActivityTAG"
    lateinit var binding:ActivitySearchBinding
    var   fetchPage : FetchPage?=null
    var page = 1
    lateinit var adapter: ListAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems:Int = 0
    private var scrollOutItems:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }
        getData()
        initRV()
    }

    private fun getData() {
        fetchPage =   FetchPage( listenerResponse)
        fetchPage!!.execute(Utils.base_url+intent.getStringExtra("link")+"&page="+page)
    }


    private fun initRV() {
        adapter = ListAdapter()
        gridLayoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter=adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = gridLayoutManager.childCount
                totalItems = gridLayoutManager.itemCount
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition()
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
                    page++
                    fetchPage =  FetchPage( listenerResponse)
                    fetchPage!!.execute(Utils.base_url+intent.getStringExtra("link")+"&page="+ page )
                    binding.loadingProgress.visibility= View.VISIBLE
                }
            }
        })

    }

    private  val listenerResponse = object :FetchPage.Listener {
        override fun onSuccess(result: String) {
            binding.swipeRefreshLayout.isEnabled= true
            binding.linearMain.visibility=View.VISIBLE
            binding.errorLayout.root.visibility=View.GONE
            binding.loadingProgress.visibility= View.GONE
            fetchPage =null
            val doc = Jsoup.parse(result)
            val contentLeft = doc.getElementsByClass("content-left")
                .select("div.tab-container")

            val filmsElements =  contentLeft.select("div.left-tab-1").select("ul.list-episode-item")
                .select("li")
            if (filmsElements.isEmpty()) Utils.toast(this@SearchActivity, "Sorry, can not find what you looking for")
            Log.d(TAG, "onSuccess: ${ doc.getElementsByClass("content-left")}")
            filmsElements.forEach {
                val title = it.select("a").select("h3").text()
                val image = it.select("a").select("img").attr("data-original")
                val link = it.select("a").attr("href")
                val film = FilmModel(title,image,  link)
                adapter.addData(film)
            }
        }

        override fun onFailed(error: String) {
            binding.swipeRefreshLayout.isEnabled= false
            if (page==1) {
                binding.swipeRefreshLayout.isEnabled= true
                binding.linearMain.visibility=View.GONE
                binding.errorLayout.root.visibility=View.VISIBLE
            }
            Log.d(TAG, "onFailed: ")
            binding.loadingProgress.visibility= View.GONE

        }

    }
}