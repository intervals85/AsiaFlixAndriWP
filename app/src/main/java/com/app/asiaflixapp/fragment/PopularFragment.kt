package com.app.asiaflixapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.FragmentPopularBinding
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.BannerModel
import com.app.asiaflixapp.model.FilmModel
import org.jsoup.Jsoup

class PopularFragment : Fragment()  {

    lateinit var binding :FragmentPopularBinding
      var   fetchPage :FetchPage?=null
    var page = 1
    lateinit var adapter: ListAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems:Int = 0
    private var scrollOutItems:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularBinding.inflate(layoutInflater)
        fetchPage =   FetchPage( listenerResponse)
        fetchPage!!.execute(Utils.base_url+"most-popular-drama?page="+page)
        initRV()
        return binding.root
    }

    private fun initRV() {
        adapter = ListAdapter()
        gridLayoutManager = GridLayoutManager(requireContext(), 3)
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
                    fetchPage!!.execute(Utils.base_url+"most-popular-drama?page="+ page )
                    binding.loadingProgress.visibility= View.VISIBLE
                }
            }
        })

    }

    val listenerResponse = object :FetchPage.Listener {
        override fun onSuccess(result: String) {
            binding.loadingProgress.visibility= View.GONE
            fetchPage =null
            val doc = Jsoup.parse(result)
            val contentLeft = doc.getElementsByClass("content-left")
                .select("div.tab-container")

            val filmsElements =  contentLeft.select("div.left-tab-1").select("ul.list-episode-item")
                .select("li")
            filmsElements.forEach {
                val title = it.select("a").select("h3").text()
                val image = it.select("a").select("img").attr("data-original")
                val link = it.select("a").attr("href")
                val film = FilmModel(title,image,
                    link,  )
                adapter.addData(film)
            }
        }

        override fun onFailed(error: String) {
            binding.loadingProgress.visibility= View.GONE

        }

    }


}