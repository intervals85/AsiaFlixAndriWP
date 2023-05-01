package com.app.asiaflixapp.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.app.asiaflixapp.EncDec.DencGetResponse
import im.delight.android.webview.AdvancedWebView
/*import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdk.SdkInitializationListener
import com.applovin.sdk.AppLovinSdkConfiguration
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError*/
import org.jsoup.Jsoup
import com.app.asiaflixapp.exo.PlayerActivity
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.EpisodeAdapter
import com.app.asiaflixapp.helper.FetchPage
import com.app.asiaflixapp.helper.Utils
import com.app.asiaflixapp.model.EpisodeModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class EpisodeActivity : PlayerActivity(), AdvancedWebView.Listener {
    var TAG = "EpisodeActivityTAG"
    private var episodes: ArrayList<EpisodeModel>? = null
    private var episodeAdapter: EpisodeAdapter? = null

    //private var interstitialAd: MaxInterstitialAd? = null
    private var retryAttempt = 0
    private var TEST_PAGE_URL: String? = null
    var linkStream = arrayListOf<String>()
    var fetchPage: FetchPage? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        film_image = intent.getStringExtra("film_image")!!
        film_title = intent.getStringExtra("film_title")!!
        episodes = ArrayList()
        episodeAdapter = EpisodeAdapter(episodes!!)
        binding.recyclerView.setAdapter(episodeAdapter)
        binding.recyclerView.setLayoutManager(GridLayoutManager(this, 3))
        binding.linearLayout.visibility=View.GONE
        binding.swipeRefreshLayout.setRefreshing(true)
        episodeAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val item = episodes!![position]
                val intent = Intent(this@EpisodeActivity, EpisodeActivity::class.java)
                intent.putExtra("film_title", film_title)
                intent.putExtra("film_image", film_image)
                intent.putExtra("link", item.episodeLink)
                intent.putExtra("title", item.episode)
                startActivity(intent)
            }
        })
        binding.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            getData()
        })
        getData()

        //TODO IKLAN
        /*    AppLovinSdk.getInstance(this).mediationProvider = "max"
            AppLovinSdk.initializeSdk(this) {
                ShowMaxBannerAd()
                LoadMaxInterstitialAd()
            }*/
    }

    private fun getData() {
        if (!Utils.isNetworkAvailable(this@EpisodeActivity)) {
            Toast.makeText(
                this@EpisodeActivity,
                "No Internet Connection or Maintenance",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // ParsePageTask().execute(intent.getStringExtra("link"))
            fetchPage = FetchPage(jsonListener)
            link = if (intent.getStringExtra("link")!!.startsWith("http"))
                intent.getStringExtra("link")!!
            else Utils.base_url + intent.getStringExtra("link")!!


            fetchPage!!.execute(link)
            gotoWebView(link)
        }
    }

    fun server1(view: View?) {
        binding.webview!!.stopLoading()
        binding.webview!!.settings.javaScriptEnabled = false
        binding.webview!!.visibility = View.GONE
        binding.exoPlayerView.visibility = View.VISIBLE
        if (linkStream.size != 0) {
            if (playerView!!.player!!.isPlaying) {
                playerView!!.player!!.playWhenReady = false
                simpleExoPlayer!!.stop()
            }
            playerView!!.player!!.playWhenReady = true
            playVideo(linkStream.first())
        }
    }

    fun server2(view: View?) {
        binding.webview!!.stopLoading()
        binding.webview!!.settings.javaScriptEnabled = false
        binding.webview!!.visibility = View.GONE
        binding.exoPlayerView.visibility = View.VISIBLE

        if (linkStream.size == 2) {
            if (playerView!!.player!!.isPlaying) {
                playerView!!.player!!.playWhenReady = false
                simpleExoPlayer!!.stop()
            }
            playerView!!.player!!.playWhenReady = true
            playVideo(linkStream[1])
        }
    }

    fun server3(view: View?) {
        if (playerView!!.player!!.isPlaying) playerView!!.player!!.stop()
        binding.webview!!.settings.javaScriptEnabled = true
        binding.exoPlayerView.visibility = View.GONE
        binding.webview!!.visibility = View.VISIBLE

        Toast.makeText(
            this@EpisodeActivity,
            "Double Click Video For Fullscreen :)",
            Toast.LENGTH_SHORT
        ).show()

    }
    //TODO IKLAN

    /*fun ShowMaxBannerAd() {
         val maxBannerAdView: MaxAdView = findViewById(R.id.MaxAdView)
        maxBannerAdView.loadAd();
    }

    fun LoadMaxInterstitialAd() {
        interstitialAd = MaxInterstitialAd("cb347433e2c6f285", this)
        val maxAdListener: MaxAdListener = object : MaxAdListener {
            override fun onAdLoaded(maxAd: MaxAd) {
                retryAttempt = 0
            }

            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
                retryAttempt++
                val delayMillis = TimeUnit.SECONDS.toMillis(
                    Math.pow(2.0, Math.min(6, retryAttempt).toDouble()).toLong()
                )
                Handler().postDelayed({ interstitialAd!!.loadAd() }, delayMillis)
            }

            override fun onAdDisplayFailed(maxAd: MaxAd, error: MaxError) {
                interstitialAd!!.loadAd()
            }

            override fun onAdDisplayed(maxAd: MaxAd) {}
            override fun onAdClicked(maxAd: MaxAd) {}
            override fun onAdHidden(maxAd: MaxAd) {
                interstitialAd!!.loadAd()
            }
        }
        interstitialAd!!.setListener(maxAdListener)
        interstitialAd!!.loadAd()
    }

    fun ShowMaxInterstitialAd() {
        if (interstitialAd!!.isReady) {
            interstitialAd!!.showAd()
        }
    }*/

    override fun onPageStarted(url: String, favicon: Bitmap) {
        Log.d(TAG, "onPageStarted: ")
    }

    override fun onPageFinished(url: String) {

    }

    override fun onPageError(errorCode: Int, description: String, failingUrl: String) {
        Log.d(TAG, "onPageError: ")
    }

    override fun onDownloadRequested(
        url: String,
        suggestedFilename: String,
        mimeType: String,
        contentLength: Long,
        contentDisposition: String,
        userAgent: String
    ) {
        Log.d(TAG, "onDownloadRequested: ")
    }

    override fun onExternalPageRequest(url: String) {
        Log.d(TAG, "onExternalPageRequest: ")
    }

    private fun gotoWebView(src: String?) {
        binding.swipeRefreshLayout!!.isEnabled = false
        TEST_PAGE_URL = "https:$src"
        Log.i("gotoWebView: ", src!!)
        //   mWebView.setListener(this, this);
        binding.webview!!.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        /*     mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(false);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);*/
        binding.webview!!.visibility = View.INVISIBLE
        binding.webview!!.settings.javaScriptEnabled = true
        binding.webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains("download")) {
                    return true
                }
                Log.i("webView", url)
                //   url.replace("http://", "https://");
                if (url.startsWith("https://asianload.io/")) {
                    view.loadUrl(url)
                    return false
                } else if (url.startsWith("https://vidcloud9.com/")) {
                    view.loadUrl(url)
                    return false
                }
                if (url.contains("watchasian")) {
                    view.loadUrl(url)
                    return false
                }
                return true
            }

            override fun onLoadResource(view: WebView, url: String) {
                if (url.contains("encrypt")) {
                    Log.d(TAG, "onLoadResource: $url")
                    getLinkStream(url)
                }
                super.onLoadResource(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView, url: String) {}
        }
        binding.webview!!.webChromeClient = WebChromeClientCustom()
        //        mWebView.setWebChromeClient(new WebChromeClient() {
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                Toast.makeText(TestActivity.this, title, Toast.LENGTH_SHORT).show();
//            }
//
//        });
//

        // mWebView.addHttpHeader("X-Requested-With", "");
        binding.webview!!.loadUrl(src)
    }

    private fun getLinkStream(src: String) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, src, null,
            Response.Listener { response ->
                try {
                    findViewById<View>(R.id.linear_list).visibility = View.VISIBLE
                    val value = JSONObject(DencGetResponse.decrypt(response.getString("data")))
                    val jsonArray = value.getJSONArray("source_bk")
                    val link = jsonArray.getJSONObject(0)
                    val jsonArray1 = value.getJSONArray("source")
                    val linkDownload = jsonArray1.getJSONObject(0)
                    val link1 = link.getString("file")
                    linkStream.add(link1)

                    val link2 = linkDownload.getString("file")
                    linkStream.add(link2)
                    Log.d(TAG, "onResponse 1: $link1")
                    Log.d(TAG, "onResponse 2: $link2")

                    if (linkStream.size != 0) playVideo(linkStream.first())

                } catch (e: JSONException) {
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
        val requestQueue = Volley.newRequestQueue(this@EpisodeActivity)
        requestQueue.add(jsonObjectRequest)
    }

    //TODO IKLAN
/*    private fun checkingInterAds() {
        if (Constant.getPageView(this@EpisodeActivity, javaClass.simpleName)
            == Constant.getInterPerClick(this@EpisodeActivity)
        ) {
            Constant.setPageView(this@EpisodeActivity, javaClass.simpleName, 1)
        } else {
            val pageView = Constant.getPageView(this@EpisodeActivity, javaClass.simpleName) + 1
            Constant.setPageView(this@EpisodeActivity, javaClass.simpleName, pageView)
        }
    }*/

    var isReady = false

    private inner class WebChromeClientCustom : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun onHideCustomView() {
            (window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            requestedOrientation = mOriginalOrientation
            mCustomViewCallback!!.onCustomViewHidden()
            mCustomViewCallback = null
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (!isReady && newProgress > 80) {
                isReady = true
                binding.frame!!.visibility = View.GONE
                binding.webview!!.visibility = View.VISIBLE
                binding.webview!!.loadUrl(
                    "javascript:(function() { " +
                            "var iframe = document.getElementsByClassName('watch_video watch-iframe')[0]; document.body.innerHTML = ''; document.body.appendChild(iframe); " +
                            "})()"
                )
                Log.d(TAG, "onPageFinished: ")
            }
            binding.webview!!.loadUrl(
                "javascript:(function() { " +
                        "document.getElementsByClassName('jw-icon')[14].style.display='none'; })()"
            )
        }

        override fun onShowCustomView(
            paramView: View,
            paramCustomViewCallback: CustomViewCallback
        ) {
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = paramView
            mOriginalSystemUiVisibility = window.decorView.systemUiVisibility
            mOriginalOrientation = requestedOrientation
            mCustomViewCallback = paramCustomViewCallback
            (window
                .decorView as FrameLayout)
                .addView(
                    mCustomView,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            window.decorView.systemUiVisibility = FULL_SCREEN_SETTING
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            mCustomView!!.setOnSystemUiVisibilityChangeListener { visibility: Int -> updateControls() }
        }

        override fun getDefaultVideoPoster(): Bitmap? {
            return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        }

        fun updateControls() {
            val params = mCustomView!!.layoutParams as FrameLayout.LayoutParams
            params.bottomMargin = 0
            params.topMargin = 0
            params.leftMargin = 0
            params.rightMargin = 0
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            mCustomView!!.layoutParams = params
            window.decorView.systemUiVisibility = FULL_SCREEN_SETTING
            //            orientationEventListener.enable();
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onBackPressed() {
        //TODO IKLAN
        //ShowMaxInterstitialAd();
        saveToHistoryDB()
        finish()
        super.onBackPressed()
    }


    companion object {
        private const val FULL_SCREEN_SETTING = View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    private val jsonListener = object : FetchPage.Listener {
        override fun onSuccess(result: String) {
            binding.errorLayout.root.visibility = View.GONE
            binding.linearLayout.visibility = View.VISIBLE
            binding.swipeRefreshLayout.isRefreshing = false
            fetchPage = null

            //TODO IKLAN
            // checkingInterAds()
            episodes!!.clear()
            findViewById<View>(R.id.linearLayout).visibility = View.VISIBLE
            val document = Jsoup.parse(result)
            binding.textViewTitle!!.text = intent.getStringExtra("title")
            findViewById<View>(R.id.textViewMoreDrama).setOnClickListener {
                val intent = Intent(this@EpisodeActivity, DetailActivity::class.java)
                intent.putExtra(
                    "link", "https://watchasian.la" + document.getElementsByClass("watch-drama")
                        .select("div.category").select("a").attr("href")
                )
                startActivity(intent)
            }

            val elements = document.getElementsByClass("watch-drama")
                .select("div.block-tab").select("div.tab-content").select("iframe")
            Log.i("getResult Epi", elements.attr("src"))
            // loadVideo(elements.attr("src"));
            // gotoWebView(elements.attr("src"));
            getLinkStream(elements.attr("src"))
            //get Elemen episode
            val elements1 = document.getElementsByClass("block-tab").select("div.tab-content")
                .select("ul.all-episode").select("li")
            for (i in elements1.indices) {
                Log.i("getList", elements1[i].select("a").text())
                val title = elements1[i].select("a").select("h3").text().replace(
                    document.getElementsByClass("watch-drama")
                        .select("div.category").select("a").text(), ""
                )
                val link = Utils.base_url + elements1[i].select("a").attr("href")
                episodes!!.add(EpisodeModel(title, link))
            }
            episodeAdapter!!.notifyDataSetChanged()
        }

        override fun onFailed(error: String) {
            binding.errorLayout.root.visibility = View.VISIBLE
            binding.linearLayout.visibility = View.GONE
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onDestroy() {
        saveToHistoryDB()
        super.onDestroy()
    }
}