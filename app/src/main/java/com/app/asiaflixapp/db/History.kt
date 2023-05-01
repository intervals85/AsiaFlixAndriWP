package com.app.asiaflixapp.db

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity
data class History (
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "episode") val episode: String,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "runtime") val runtime: String,
    @ColumnInfo(name = "duration") val duration: String,
    @ColumnInfo(name = "createAt") val createAt: Long
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
