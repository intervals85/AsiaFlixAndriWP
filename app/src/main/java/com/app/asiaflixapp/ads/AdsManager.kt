package com.app.asiaflixapp.ads

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import com.app.asiaflixapp.helper.PreferenceHelper
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkSettings
import java.util.concurrent.TimeUnit

class AdsManager(var context: Context, adView: FrameLayout ) {
    val TAG = "AdsManagerTAG"
    private lateinit var onListener: OnListener
    private lateinit var interstitialAd: MaxInterstitialAd
    var retryAttempt = 0

    fun setOnListener(onListener: OnListener) {
        this.onListener = onListener
    }

    var count = 0

    init {
        val userSetting = AppLovinSdkSettings(context)

        AppLovinSdk.getInstance(PreferenceHelper.getSDKKEY(context), userSetting, context)
        AppLovinSdk.getInstance(context).settings.testDeviceAdvertisingIds =
            arrayListOf("0d6ad676-56f2-4805-9d86-10ca1ad1abe1")
        //AppLovinSdk.getInstance(this).showMediationDebugger();
        AppLovinSdk.getInstance(context).mediationProvider = "max"

        AppLovinSdk.getInstance(context).initializeSdk {
            adView.addView(getBannerAdsAppLvin(context))
        }

        initInterAds()
    }

    private fun initInterAds() {
        interstitialAd = MaxInterstitialAd(PreferenceHelper.getINTERID(context), context as Activity?)
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

    fun ShowMaxInterstitialAd(className: String) {
        val hasVisit = PreferenceHelper.getHasVisitPage(context, className)
        val maxVisit = PreferenceHelper.getINTERINTERVAL(context)
        if (hasVisit==maxVisit) {
            if (interstitialAd!!.isReady) {
                interstitialAd!!.showAd()
                PreferenceHelper.setHasVisitPage(context, className, 0)
            }
        }

    }

    fun updateLayoutHasVisit(className: String) {
        val hasVisit = PreferenceHelper.getHasVisitPage(context, className)
        val value = hasVisit?.plus(1)
        Log.d(TAG, "updateLayoutHasVisit: $value")
        if (value != null) {
            PreferenceHelper.setHasVisitPage(context, className, value)
        }
    }


    fun getBannerAdsAppLvin(context: Context?): MaxAdView {
        val adView = MaxAdView(PreferenceHelper.getBANNERID(context!!), context)
        adView.setListener(object :MaxAdListener, MaxAdViewAdListener {
            override fun onAdLoaded(ad: MaxAd?) {
                Log.d(TAG, "onAdLoaded: ")

            }

            override fun onAdDisplayed(ad: MaxAd?) {
                Log.d(TAG, "onAdDisplayed: ")

            }

            override fun onAdHidden(ad: MaxAd?) {
                Log.d(TAG, "onAdHidden: ")

            }

            override fun onAdClicked(ad: MaxAd?) {
                Log.d(TAG, "onAdClicked: ")

            }

            override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
                Log.d(TAG, "onAdLoadFailed: ${error?.message}")

            }

            override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
                Log.d(TAG, "onAdDisplayFailed: ${error?.message}")

            }

            override fun onAdExpanded(ad: MaxAd?) {
                Log.d(TAG, "onAdExpanded: ")

            }

            override fun onAdCollapsed(ad: MaxAd?) {
                Log.d(TAG, "onAdCollapsed: ")

            }
        })

        // Stretch to the width of the screen for banners to be fully functional
        val width = ViewGroup.LayoutParams.MATCH_PARENT

        // Banner height on phones and tablets is 50 and 90, respectively
        val heightPx = ViewGroup.LayoutParams.MATCH_PARENT

        adView?.layoutParams = FrameLayout.LayoutParams(width, heightPx)

        adView?.loadAd()
        return adView
    }


    interface OnListener {
        fun onCloseAds()
        fun onFailedLoadAds()
        fun onTicker(count: Int)
        fun onFinish(ready: Boolean)
    }

}