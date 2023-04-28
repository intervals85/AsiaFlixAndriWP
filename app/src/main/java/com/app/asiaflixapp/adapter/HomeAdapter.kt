package com.app.asiaflixapp.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.MoreActivity
import com.app.asiaflixapp.databinding.HomeItemBinding
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.HomeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class HomeAdapter( data: MutableList<HomeModel> = arrayListOf()) :
    BaseQuickAdapter<HomeModel, BaseViewHolder>(R.layout.home_item, data) {

    override fun convert(holder: BaseViewHolder, item: HomeModel) {
        val x = HomeItemBinding.bind(holder.itemView)
        x.categoryName.text = item.categoryName
        x.btnMore.setOnClickListener {
            val intent = Intent(context, MoreActivity::class.java)
            intent.putExtra("link", item.categoryLink)
            context.startActivity(intent)
        }

        x.recyclerView.apply {
            adapter = FilmAdapter(item.filmList)
        }
    }


}
