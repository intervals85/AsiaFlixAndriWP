package com.app.asiaflixapp.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.asiaflixapp.BuildConfig
import com.app.asiaflixapp.EncDec.DencGetResponse
import com.app.asiaflixapp.R
import com.app.asiaflixapp.databinding.ActivitySplashBinding
import com.app.asiaflixapp.helper.PreferenceHelper
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    val TAG ="SplashActivityTAG"
    lateinit var binding :ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
    }

    private fun getData() {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,  resources.getString(R.string.link_update), null,
            Response.Listener { response ->
                try {
                    val verison =  response.getString("verison")
                    if (verison.toString().equals(BuildConfig.VERSION_NAME)) {
                        val link = response.getString("link_update").toString()
                        showDialogUpdate(link)
                    }else initPrefAds(response)
                /*    val idApp =  response.getString("id_app")
                    val verison =  response.getString("verison")
                    val idApp =  response.getString("verison")*/

                } catch (e: JSONException) {
                    Log.i("getRespon", e.toString())
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error ->
                Log.i("getRespon", error.toString())
                error.stackTrace
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers["x-requested-with"] = "XMLHttpRequest"
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                return headers
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }

    private fun initPrefAds(response: JSONObject) {
        val sdk_key =  response.getString("sdk_key")
        PreferenceHelper.setSDKKEY(this, sdk_key)
        val banner =  response.getString("banner")
        PreferenceHelper.setBANNERID(this, banner)
        val interstitial =  response.getString("interstitial")
        PreferenceHelper.setINTERID(this,interstitial)
        val interstitialInterval =  response.getInt("interstitial_interval")
        PreferenceHelper.setINTERINTERVAL(this,  interstitialInterval)
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

    private fun showDialogUpdate(link: String) {

        val alertDialog: AlertDialog = AlertDialog.Builder(
            this@SplashActivity,
            R.style.AlertDialogCustom
        )
            .setTitle(getString(R.string.app_name))
            .setMessage(getString(R.string.app_name).toString() + " New version Available")
            .setPositiveButton(
                "Update",
                DialogInterface.OnClickListener { dialogInterface: DialogInterface?, i: Int ->
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse (link)
                    )
                    startActivity(intent)
                    finish()
                    System.exit(0)
                }).create()
        alertDialog.show()
        alertDialog.setIcon(R.mipmap.ic_logo)
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
    }


    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tap Once Again To Close..", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}