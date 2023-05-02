package  com.app.asiaflixapp.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

object PreferenceHelper {

    private var mySharedPreferences: SharedPreferences? = null
    private const val PREF = "pref"
    val BANNERID = "BANNERID"
    val INTERID = "INTERID"
    val INTERINTERVAL = "INTERINTERVAL"
    val SDKKEY = "SDKKEY"

    fun setSDKKEY(context: Context, id: String?) {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val myEditor = mySharedPreferences!!.edit()
        if (id != null) {
            myEditor.putString(SDKKEY, id)
        }
        myEditor.commit()
        myEditor.apply()
    }

    fun getSDKKEY(context: Context): String? {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return mySharedPreferences!!.getString(SDKKEY,null)
    }

    fun setINTERINTERVAL(context: Context, value: Int?) {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val myEditor = mySharedPreferences!!.edit()
        if (value != null) {
            myEditor.putInt(INTERINTERVAL, value)
        }
        myEditor.commit()
        myEditor.apply()
    }

    fun getINTERINTERVAL(context: Context): Int? {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return mySharedPreferences!!.getInt(INTERINTERVAL,0)
    }

    fun setHasVisitPage(context: Context, className:String, value :Int){
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val myEditor = mySharedPreferences!!.edit()
        if (value != null) {
            myEditor.putInt(className, value)
        }
        myEditor.commit()
        myEditor.apply()
    }

    fun getHasVisitPage(context: Context, className:String): Int? {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return mySharedPreferences!!.getInt(className,0)
    }

    fun setINTERID(context: Context, id: String?) {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val myEditor = mySharedPreferences!!.edit()
        if (id != null) {
            myEditor.putString(INTERID, id)
        }
        myEditor.commit()
        myEditor.apply()
    }

    fun getINTERID(context: Context): String? {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return mySharedPreferences!!.getString(INTERID,null)
    }


    fun setBANNERID(context: Context, id: String?) {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val myEditor = mySharedPreferences!!.edit()
        if (id != null) {
            myEditor.putString(BANNERID, id)
        }
        myEditor.commit()
        myEditor.apply()
    }

    fun getBANNERID(context: Context): String? {
        mySharedPreferences = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return mySharedPreferences!!.getString(BANNERID,null)
    }




}
