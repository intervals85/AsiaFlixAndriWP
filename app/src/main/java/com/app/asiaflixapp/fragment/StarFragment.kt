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
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.adapter.StarAdapter
import com.app.asiaflixapp.databinding.FragmentStarBinding
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.StarModel
import org.jsoup.Jsoup


class StarFragment : Fragment() {
    val TAG ="StarFragmentTAG"
    lateinit var binding:FragmentStarBinding
    var   fetchPage : FetchPage?=null
    var page = 1
    lateinit var adapter: StarAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    private var isScrolling = false
    private var currentItems = 0
    private var totalItems:Int = 0
    private var scrollOutItems:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStarBinding.inflate(layoutInflater)
        fetchPage =   FetchPage( listenerResponse)
        fetchPage!!.execute(Utils.base_url+"list-star.html?page="+page)
        initRV()
        return binding.root
    }

    private fun initRV() {
        adapter = StarAdapter()
        gridLayoutManager = GridLayoutManager(requireContext(), 2)
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
                    fetchPage!!.execute(Utils.base_url+"list-star.html?page="+ page )
                    binding.loadingProgress.visibility= View.VISIBLE
                }
            }
        })

    }

    private val listenerResponse = object :FetchPage.Listener {
        override fun onSuccess(result: String) {
            binding.loadingProgress.visibility= View.GONE
            fetchPage =null
            val doc = Jsoup.parse(result)
            val contentLeft = doc.getElementsByClass("content-left")

            val filmsElements =  contentLeft.select("ul.list-star")
                .select("li")
            filmsElements.forEachIndexed { index, it ->
                if (it.select("a").isNotEmpty()) {
                    val starModel =StarModel( )
                    val title = it .select("h3").text()
                    val image = it.select("a").select("img").attr("data-original")
                    val link = it.select("a").attr("href")
                    starModel.imageUrl = image
                    starModel.name = title
                    starModel.link = link
                    starModel.city =   it.select("ul").select("li")[1]?.text()
                    starModel.birth = it.select("ul").select("li")[2]?.text()
                    adapter.addData(starModel)
                }
            }




        }

        override fun onFailed(error: String) {
            Log.d(TAG, "onFailed: $error")
            binding.loadingProgress.visibility= View.GONE

        }

    }
}