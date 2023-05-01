package com.app.asiaflixapp.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.DetailActivity
import com.app.asiaflixapp.databinding.FilmItemBinding
import com.app.asiaflixapp.databinding.HomeItemBinding
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.HomeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class FilmAdapter(data: MutableList<FilmModel> = arrayListOf()) :
    BaseQuickAdapter<FilmModel, BaseViewHolder>(R.layout.film_item, data) {
    val TAG = "FilmAdapterTAG"
    override fun convert(holder: BaseViewHolder, item: FilmModel) {
        val x = FilmItemBinding.bind(holder.itemView)
        x.textEpisode.text = item.episode
        Utils.loadImage(context, item.imageUrl, x.image)
        x.root.setOnClickListener {
            Log.d(TAG, "convert: ${item.filmLink}")
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("link", item.filmLink)
            intent.putExtra("linkNotActual", true )
            holder.itemView.context.startActivity(intent)
        }
    }

    fun fixURL(url: String): String {
        val data = url.split("/")
        data.forEach {
            Log.d(TAG, "fixURL: $it")
        }
        return data[3]
    }


}
