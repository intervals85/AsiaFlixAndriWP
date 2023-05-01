package com.app.asiaflixapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//data class Favorite(val title:String, val image:String, val link:String)

@Entity
data class Favorite (
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "createAt") val createAt: Long
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}