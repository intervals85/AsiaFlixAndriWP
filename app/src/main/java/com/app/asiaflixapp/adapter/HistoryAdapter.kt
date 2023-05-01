package com.app.asiaflixapp.adapter

import android.content.Intent
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.EpisodeActivity
import com.app.asiaflixapp.databinding.HistoryItemBinding
import com.app.asiaflixapp.db.History
import com.app.asiaflixapp.helper.Utils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import java.util.*


class HistoryAdapter(data: MutableList<History> = arrayListOf()) :
    BaseQuickAdapter<History, BaseViewHolder>(R.layout.history_item, data) {

    override fun convert(holder: BaseViewHolder, item: History) {
        val x = HistoryItemBinding.bind(holder.itemView)
        x.textViewEpisode.text = item.episode
        Utils.loadImage(context, item.image, x.image)
        x.textViewTitle.text = item.title
        val LocaleBylanguageTag  = Locale.forLanguageTag("en")
        val messages  = TimeAgoMessages.Builder().withLocale(LocaleBylanguageTag).build()
        x.textViewDate.text = TimeAgo.using(item.createAt.toLong(),messages)
        x.root.setOnClickListener {
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtra("film_title",item.title)
            intent.putExtra("film_image",  item.image)
            intent.putExtra("link", item.link)
            intent.putExtra("title", item.episode)
            context.startActivity(intent)
        }
    }


}
