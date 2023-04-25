package com.app.asiaflixapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ListAdapter
import com.app.asiaflixapp.databinding.FragmentLibraryBinding
import com.app.asiaflixapp.model.FilmModel

class LibraryFragment : Fragment(), View.OnClickListener {

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
        for (i in 0..15) {
            films.add(
                FilmModel("this is titl $i","https://imagecdn.me/cover/love-star-2023-1680537786.png",
                    "https://www1.watchasian.id/love-star-2023-episode-22.html $i", "Ep 12" ,"2023")
            )
        }
        binding.recyclerView.adapter = ListAdapter(films)
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

    private fun getFav() {
        films.clear()
    }
}