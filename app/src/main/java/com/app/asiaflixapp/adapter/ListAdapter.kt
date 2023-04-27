package com.app.asiaflixapp.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.DetailActivity
import com.app.asiaflixapp.databinding.FilmItemBinding
import com.app.asiaflixapp.databinding.HomeItemBinding
import com.app.asiaflixapp.databinding.ListItemBinding
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.HomeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ListAdapter(data: MutableList<FilmModel> = arrayListOf()) :
    BaseQuickAdapter<FilmModel, BaseViewHolder>(R.layout.list_item, data) {

    override fun convert(holder: BaseViewHolder, item: FilmModel) {
        val x = ListItemBinding.bind(holder.itemView)
        if (item.episode==null) x.cardEpisode.visibility=View.GONE
        x.textEpisode.text = item.episode
         Utils.loadImage(context, item.imageUrl, x.image)
        x.textTitle.text = item.title
        x.textYear.text = item.released
        x.root.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }


}
