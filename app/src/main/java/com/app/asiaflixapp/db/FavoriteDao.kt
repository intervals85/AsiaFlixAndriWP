package com.app.asiaflixapp.db
import androidx.room.*


@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite   ORDER BY id DESC ")
    fun getAllFav():List<Favorite>



    @Query("SELECT * FROM favorite ORDER BY id DESC LIMIT 1")
    fun getLastHistory():List<Favorite>

    @Query("SELECT  * FROM favorite WHERE link = :link ")
    fun checkFav(link :String):List<Favorite>


    @Query("SELECT * FROM favorite WHERE createAt BETWEEN :starDate AND :endDate  ORDER BY id DESC  ")
    fun getFavoriteBetweenDate(starDate :Long,endDate :Long):List<Favorite>

    @Insert
    fun insertFavorite(vararg favorite: Favorite)

    @Query("DELETE  FROM favorite ")
    fun deleteAllFavorite()

    @Query("DELETE  FROM favorite  WHERE link = :link ")
    fun deleteFavoriteByLink(link :String)

    @Query("SELECT  * FROM favorite WHERE id = :id ")
    fun getFavoriteById(id: Int):List<Favorite>

    @Delete
    fun deleteFavorite(favorite: Favorite )


}
