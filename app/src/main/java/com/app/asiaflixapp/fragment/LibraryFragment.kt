package com.app.asiaflixapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.FilmAdapter
import com.app.asiaflixapp.adapter.HistoryAdapter
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.FragmentLibraryBinding
import com.app.asiaflixapp.db.LocalDatabase
import com.app.asiaflixapp.model.FilmModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryFragment : Fragment(), View.OnClickListener {
    val TAG ="LibraryFragmentTAG"
    val database: LocalDatabase by lazy { LocalDatabase.getDatabase(requireContext()) }
    val films :ArrayList<FilmModel> = arrayListOf()
    lateinit var binding:FragmentLibraryBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(layoutInflater)
        getHistory()
        binding.textFav.setOnClickListener(this)
        binding.textHistory.setOnClickListener(this)
        return binding.root
    }
    private fun getHistory() {
        val adapter = HistoryAdapter()
        binding.recyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.recyclerView.adapter=adapter
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                database.historyDao().getAllHistory().forEach {
                    adapter.addData(it)
                    Log.d(TAG, "getHistory: ${it.title} ${it.episode}")
                }
            }
        }

    }

    private fun getFav() {
        binding.recyclerView.layoutManager =GridLayoutManager(requireContext(), 3)
        val adapter = ListAdapter()
        binding.recyclerView.adapter=adapter
        GlobalScope.launch {
            withContext(Dispatchers.Main){
                database.favoriteDao().getAllFav().forEach {
                    val film =FilmModel(it.title, it.image, it.link)
                    adapter.addData(film)
                    Log.d(TAG, "getFav: ${it.title} ${it.image} ${it.link}")
                }
            }
        }
    }


    override fun onClick(p0: View) {
        when(p0.id) {
            R.id.text_history ->{
                binding.textFav.background=null
                binding.textHistory.setBackgroundResource(R.drawable.background_button_library)
                getHistory()
            }
            R.id.text_fav->{
                binding.textHistory.background=null
                binding.textFav.setBackgroundResource(R.drawable.background_button_library)
                getFav()
            }
        }
    }

}