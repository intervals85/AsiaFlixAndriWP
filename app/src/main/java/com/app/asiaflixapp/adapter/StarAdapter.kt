package com.app.asiaflixapp.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.DetailActivity
import com.app.asiaflixapp.activity.StarActivity
import com.app.asiaflixapp.databinding.FilmItemBinding
import com.app.asiaflixapp.databinding.HomeItemBinding
import com.app.asiaflixapp.databinding.StarItemBinding
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.FilmModel
import com.app.asiaflixapp.model.HomeModel
import com.app.asiaflixapp.model.StarModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.Gson

class StarAdapter(data: MutableList<StarModel> = arrayListOf()) :
    BaseQuickAdapter<StarModel, BaseViewHolder>(R.layout.star_item, data) {

    override fun convert(holder: BaseViewHolder, item: StarModel) {
        val x = StarItemBinding.bind(holder.itemView)
        item.imageUrl?.apply {
            Utils.loadImage(context, this, x.image)
        }

        x.textViewName.text = item.name
        x.textViewBirth.text = item.birth
        x.textViewCity.text = item.city
        x.root.setOnClickListener {
            val json = Gson().toJson(item)
            val intent = Intent(holder.itemView.context, StarActivity::class.java)
            intent.putExtra("json", json)
            holder.itemView.context.startActivity(intent)
        }
    }


}
