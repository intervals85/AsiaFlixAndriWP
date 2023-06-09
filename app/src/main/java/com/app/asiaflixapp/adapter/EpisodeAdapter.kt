package com.app.asiaflixapp.adapter

import android.content.Intent
import android.view.View
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.DetailActivity
import com.app.asiaflixapp.activity.EpisodeActivity
import com.app.asiaflixapp.databinding.EpisodeItemBinding
import com.app.asiaflixapp.databinding.FilmItemBinding
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.EpisodeModel
import com.app.asiaflixapp.model.FilmModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class EpisodeAdapter(data: ArrayList<EpisodeModel> = arrayListOf()) :
    BaseQuickAdapter<EpisodeModel, BaseViewHolder>(R.layout.episode_item, data) {

    override fun convert(holder: BaseViewHolder, item: EpisodeModel) {
        val x = EpisodeItemBinding.bind(holder.itemView)
        x.textEpisode.text = item.episode
        //x.root.setOnClickListener {
/*
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtra("link", item.episodeLink)
            intent.putExtra("title", item.episode)
            context.startActivity(intent)*/
       // }

    }


}
