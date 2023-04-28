package com.app.asiaflixapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.app.asiaflixapp.R
import com.app.asiaflixapp.activity.DetailActivity
import com.app.asiaflixapp.model.BannerModel
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(context: Context) : SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private val context: Context
    private var mSliderItems: MutableList<BannerModel> = ArrayList()

    init {
        this.context = context
    }

    fun renewItems(sliderItems: MutableList<BannerModel>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(sliderItem: BannerModel) {
        mSliderItems.add(sliderItem)
        notifyDataSetChanged()
    }

    fun getItem(): MutableList<BannerModel> {
        return  mSliderItems
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View = LayoutInflater.from(parent.context).inflate(R.layout.slider_item, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val sliderItem: BannerModel = mSliderItems[position]
        viewHolder.textTitle.text = sliderItem.title
        viewHolder.textYear.text = sliderItem.released
       Glide.with(viewHolder.itemView)
            .load(sliderItem.imageUrl)
            .into(viewHolder.imageViewBackground)
        viewHolder.itemView.setOnClickListener {
            val intent = Intent(viewHolder.itemView.context, DetailActivity::class.java)
            intent.putExtra("link", sliderItem.filmUrl)
            viewHolder.itemView.context.startActivity(intent)
        }
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(view: View) : ViewHolder(view) {
        var view: View
        var imageViewBackground: ImageView
        var textYear: TextView
        var textTitle: TextView

        init {
            imageViewBackground = view.findViewById(R.id.image)
            textYear = view.findViewById(R.id.text_year)
            textTitle = view.findViewById(R.id.text_title)
            this.view = view
        }
    }

}