package com.app.asiaflixapp.db
import androidx.room.*

import com.app.asiaflixapp.db.History

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history   ORDER BY id DESC ")
    fun getAllHistory():List<History>



    @Query("SELECT * FROM history ORDER BY id DESC LIMIT 1")
    fun getLastHistory():List<History>


    @Query("SELECT * FROM history WHERE link = :link ORDER BY id DESC LIMIT 1")
    fun getLastWatchByFilm(link: String):List<History>

    @Query("SELECT * FROM history WHERE  createAt >= :date   ORDER BY id DESC ")
    fun getHistoryToday(date :Long):List<History>


    @Query("SELECT * FROM history WHERE createAt BETWEEN :starDate AND :endDate  ORDER BY id DESC  ")
    fun getHistoryBetweenDate(starDate :Long,endDate :Long):List<History>

    @Insert
    fun insertHistory(vararg history: History)

    @Query("DELETE  FROM history ")
    fun deleteAllHistory()


   @Query("DELETE  FROM history WHERE link = :link ")
    fun deleteHistoryByEpisodeLink(link: String)

    @Query("SELECT  * FROM history WHERE link = :link ")
    fun getHistoryByEpisodeLink(link: String):List<History>

    @Delete
    fun deleteHistory(history: History)
}
