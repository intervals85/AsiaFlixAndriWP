package com.app.asiaflixapp.db


import android.content.Context
import android.os.AsyncTask
import android.util.Log
import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app.asiaflixapp.helper.Utils


@Database(entities = [History::class,Favorite::class] , version = 3)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        fun getDatabase(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    Utils.DATABSENAME
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(@NonNull db: SupportSQLiteDatabase) {
                            Log.i( "onCreate: ","LocalDatabase")
                            super.onCreate(db) 
                            INSTANCE?.let { PopulateDbAsyncTask(it).execute() }
                        }
                        
                    }).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


       class PopulateDbAsyncTask internal constructor(instance: LocalDatabase) :
        AsyncTask<Void?, Void?, Void?>() {

         override fun doInBackground(vararg p0: Void?): Void? {
             return null;
         }
     }
}
