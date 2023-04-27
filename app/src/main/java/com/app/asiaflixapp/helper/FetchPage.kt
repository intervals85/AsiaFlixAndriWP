package com.app.asiaflixapp.helper

import android.os.AsyncTask
import android.util.Log
import org.jsoup.Jsoup


class FetchPage(var listener: Listener) : AsyncTask<String?, Void?, String?>() {

    override fun doInBackground(vararg urls: String?): String? {
        return try {
            val document = Jsoup.connect(urls[0]!!).get()
            val element = document.getElementsByTag("html")
            element.toString()
        } catch (ignored: Exception) {
            ignored.message?.let { Log.i("doInBackground: ", it) }
            null
        }
    }

    override fun onPostExecute(result: String?) {
        if (result != null) {
            listener.onSuccess(result)
        }else  listener.onFailed("something went wrong.. ")
    }

    interface Listener{
        fun onSuccess(result: String)
        fun onFailed(error: String)
    }

}