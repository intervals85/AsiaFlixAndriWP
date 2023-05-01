package com.app.asiaflixapp.exo


import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.app.MediaRouteButton
import com.app.asiaflixapp.databinding.ActivityEpisodeBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.gms.cast.MediaMetadata.*
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import com.app.asiaflixapp.R
import com.app.asiaflixapp.db.History
import com.app.asiaflixapp.db.LocalDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

open class PlayerActivity : AppCompatActivity() {
    private var isShowingTrackSelectionDialog = false
    private var trackSelector: DefaultTrackSelector? = null
    var speed = arrayOf("0.25x", "0.5x", "Normal", "1.5x", "2x")

    //demo url
     var url1 = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8";
  //  var url1 = "https://scontent.xx.fbcdn.net/v/t39.25447-2/323642663_584469806846408_5570185725061974310_n.mp4?_nc_cat=100&vs=9ccc2a51871b28b4&_nc_vs=HBksFQAYJEdDZGxTaFBJRVRoN2toTUNBQ1pKUmFOblJrMU5ibWRqQUFBRhUAAsgBABUAGCRHTHk5TkJOekhCZzY1Sk1BQUFENVpDaHNmbXR2YnJGcUFBQUYVAgLIAQBLB4gScHJvZ3Jlc3NpdmVfcmVjaXBlATENc3Vic2FtcGxlX2ZwcwAQdm1hZl9lbmFibGVfbnN1YgAgbWVhc3VyZV9vcmlnaW5hbF9yZXNvbHV0aW9uX3NzaW0AKGNvbXB1dGVfc3NpbV9vbmx5X2F0X29yaWdpbmFsX3Jlc29sdXRpb24AHXVzZV9sYW5jem9zX2Zvcl92cW1fdXBzY2FsaW5nABFkaXNhYmxlX3Bvc3RfcHZxcwAVACUAHAAAJprFzdzs0rACFZBOKAJDMxgLdnRzX3ByZXZpZXccF0By6AAAAAAAGClkYXNoX2k0bGl0ZWJhc2ljXzVzZWNnb3BfaHExX2ZyYWdfMl92aWRlbxIAGBh2aWRlb3MudnRzLmNhbGxiYWNrLnByb2Q4ElZJREVPX1ZJRVdfUkVRVUVTVBsOiBVvZW1fdGFyZ2V0X2VuY29kZV90YWcGb2VwX2hkE29lbV9yZXF1ZXN0X3RpbWVfbXMBMAxvZW1fY2ZnX3J1bGUHdW5tdXRlZBNvZW1fcm9pX3JlYWNoX2NvdW50BTI4NDM4EW9lbV9pc19leHBlcmltZW50ABFvZW1fcm9pX3VzZXJfdGllcgN1Z2Meb2VtX3JvaV9wcmVkaWN0ZWRfd2F0Y2hfdGltZV9zATAWb2VtX3JvaV9yZWNpcGVfYmVuZWZpdAUwLjAwMCVvZW1fcm9pX3N0YXRpY19iZW5lZml0X2Nvc3RfZXZhbHVhdG9yC3Byb2dyZXNzaXZlDG9lbV92aWRlb19pZA84ODk0MjI4ODU4MTYzMTUSb2VtX3ZpZGVvX2Fzc2V0X2lkEDM0ODg5MzYxMzgwMDkyODMVb2VtX3ZpZGVvX3Jlc291cmNlX2lkDzY2OTkyNjQxMTU3OTcyNRxvZW1fc291cmNlX3ZpZGVvX2VuY29kaW5nX2lkDzU2NDAzODMwODU1MjM0NQ52dHNfcmVxdWVzdF9pZA82YzJmYjQ4ZTRjMDE0MjElAhwcHBXw5hcbAVUAAhsBVQACHBUCAAAAFoC6twMAJcQBGweIAXMENzk2MwJjZAoyMDIzLTAxLTAyA3JjYgUyODQwMANhcHAFVmlkZW8CY3QZQ09OVEFJTkVEX1BPU1RfQVRUQUNITUVOVBNvcmlnaW5hbF9kdXJhdGlvbl9zBTMwMi41AnRzFXByb2dyZXNzaXZlX2VuY29kaW5ncwA%3D&ccb=1-7&_nc_sid=41a7d5&_nc_ohc=fxguD11Hc-IAX_RRtPc&_nc_ht=video.fcgk6-2.fna&oh=00_AfC3vctndkj1GBj_kRN3ZlpjpSiT19H9b6ksF1UEcfMLHg&oe=63C5601F&_nc_rid=276832375647167"

    //208A8A8A
    var simpleExoPlayer: SimpleExoPlayer? = null
    var playerView: PlayerView? = null
    lateinit var    dialog :BottomSheetDialog
    var episodeId: Int = 0
    lateinit var farwordBtn :ImageView
    lateinit var rewBtn :ImageView
    lateinit var setting :ImageView
    lateinit var speedBtn :ImageView
    lateinit var speedTxt :TextView
    lateinit var exoCast : MediaRouteButton
    lateinit var castContext: CastContext
    lateinit var binding : ActivityEpisodeBinding
    var film_image =""
    var film_title =""
    var link =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpisodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (CookieHandler.getDefault() !== DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER)
        }
        Log.d(TAG, "onCreate: $url1")
        trackSelector = DefaultTrackSelector(this)
        // simpleExoPlayer = new ExoPlayer.Builder(this).build();
        initPlayer()
    }




    private fun initPlayer() {
        simpleExoPlayer = SimpleExoPlayer.Builder(this).setTrackSelector(trackSelector!!).build()
        playerView =binding.exoPlayerView
        playerView!!.player = simpleExoPlayer

        playerView!!.setControllerVisibilityListener { visibility ->
            Log.d(  TAG,  "onVisibilityChange: $visibility" )
        }
        castContext= CastContext.getSharedInstance(this)

        exoCast =  playerView!!.findViewById (R.id.exo_cast)
        CastButtonFactory.setUpMediaRouteButton(applicationContext, exoCast)
        initCast()


        farwordBtn = playerView!!.findViewById(R.id.fwd)
        rewBtn  = playerView!!.findViewById<ImageView>(R.id.rew)
        setting  = playerView!!.findViewById<ImageView>(R.id.exo_track_selection_view)
        speedBtn  = playerView!!.findViewById<ImageView>(R.id.exo_playback_speed)
        speedTxt  = playerView!!.findViewById<TextView>(R.id.speed)
        speedBtn.setOnClickListener {
            val builder = AlertDialog.Builder(this@PlayerActivity)
            builder.setTitle("Set Speed")
            builder.setItems(speed) { dialog, which ->
                // the user clicked on colors[which]
                if (which == 0) {
                    //  speedTxt.setVisibility(View.VISIBLE);
                    //   speedTxt.setText("0.25X");
                    val param = PlaybackParameters(0.5f)
                    simpleExoPlayer!!.playbackParameters = param
                }
                if (which == 1) {
                    // speedTxt.setVisibility(View.VISIBLE);
                    // speedTxt.setText("0.5X");
                    val param = PlaybackParameters(0.5f)
                    simpleExoPlayer!!.playbackParameters = param
                }
                if (which == 2) {
                    // speedTxt.setVisibility(View.GONE);
                    val param = PlaybackParameters(1f)
                    simpleExoPlayer!!.playbackParameters = param
                }
                if (which == 3) {
                    //  speedTxt.setVisibility(View.VISIBLE);
                    //  speedTxt.setText("1.5X");
                    val param = PlaybackParameters(1.5f)
                    simpleExoPlayer!!.playbackParameters = param
                }
                if (which == 4) {
                    // speedTxt.setVisibility(View.VISIBLE);
                    //  speedTxt.setText("2X");
                    val param = PlaybackParameters(2f)
                    simpleExoPlayer!!.playbackParameters = param
                }
            }
            builder.show()
        }
        farwordBtn.setOnClickListener { simpleExoPlayer!!.seekTo(simpleExoPlayer!!.currentPosition + 10000) }
        rewBtn.setOnClickListener {
            val num = simpleExoPlayer!!.currentPosition - 10000
            if (num < 0) {
                simpleExoPlayer!!.seekTo(0)
            } else {
                simpleExoPlayer!!.seekTo(simpleExoPlayer!!.currentPosition - 10000)
            }
        }
        val fullscreenButton = playerView!!.findViewById<ImageView>(R.id.fullscreen)
        fullscreenButton.setOnClickListener {
            val orientation = this@PlayerActivity.resources.configuration.orientation
            requestedOrientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                // code for portrait mode

                binding.swipeRefreshLayout.isEnabled=false
                val params = binding.relativeVideo.getLayoutParams()
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height =ViewGroup.LayoutParams.MATCH_PARENT
                binding.relativeVideo.setLayoutParams(params)
                binding.linearList.visibility=View.GONE
                binding.nestedScrollView.visibility=View.GONE
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                // code for landscape mode
                binding.swipeRefreshLayout.isEnabled=true
                val params = binding.relativeVideo.getLayoutParams()
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = getResources().getDimension(R.dimen.video_height).toInt()
                binding.relativeVideo.setLayoutParams(params)

                binding.linearList.visibility=View.VISIBLE
                binding.nestedScrollView.visibility=View.VISIBLE
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }


        playerView!!.findViewById<View>(R.id.exo_play).setOnClickListener { simpleExoPlayer!!.play() }
        playerView!!. findViewById<View>(R.id.exo_pause).setOnClickListener { simpleExoPlayer!!.pause() }
        simpleExoPlayer!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == ExoPlayer.STATE_READY) {
                    Log.d(TAG, "onPlaybackStateChanged: ")

                }
            }
        })
        setting.setOnClickListener {
            Toast.makeText(this, "vlivk", Toast.LENGTH_SHORT).show()
            if (!isShowingTrackSelectionDialog
                && TrackSelectionDialog.willHaveContent(trackSelector)
            ) {
                isShowingTrackSelectionDialog = true
                val trackSelectionDialog = TrackSelectionDialog.createForTrackSelector(
                    trackSelector
                )  /* onDismissListener= */
                { dismissedDialog: DialogInterface? -> isShowingTrackSelectionDialog = false }
                trackSelectionDialog.show(supportFragmentManager,  /* tag= */null)
            }
        }
    }


    private fun initCast() {
        if (castContext.castState != CastState.NO_DEVICES_AVAILABLE)
            exoCast.visibility = View.VISIBLE

        castContext.addCastStateListener { state ->
            if (state == CastState.NO_DEVICES_AVAILABLE) exoCast.visibility = View.GONE else {
                if (exoCast.visibility === View.GONE) exoCast.visibility = View.VISIBLE
            }
        }
    }

    fun playVideo(link: String?) {
        val mediaItem = link?.let { MediaItem.fromUri(it) }
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, "MY_USER_AGENT")
        // val videoSource  =   HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        if (mediaItem != null) {
            simpleExoPlayer!!.setMediaItem(mediaItem)
        }
        simpleExoPlayer!!.prepare()
        simpleExoPlayer!!.play()

        val castPlayer = CastPlayer(castContext)
        castPlayer.setSessionAvailabilityListener(object :SessionAvailabilityListener{
            override fun onCastSessionAvailable() {
                if (mediaItem != null) {
                    castPlayer.setMediaItem(mediaItem)
                }
            }

            override fun onCastSessionUnavailable() {

            }
        })
    }

//    private fun buildMediaQueueItem(video: String): MediaItem {
//        val metadata  =  com.google.android.gms.cast.MediaMetadata(MEDIA_TYPE_MOVIE)
//        metadata.putString(KEY_TITLE, "Title")
//        metadata.putString(KEY_SUBTITLE, "Subtitle")
//        metadata.addImage(WebImage(Uri.parse("any-image-url")))
//
//        val mediaInfo = MediaInfo.Builder(video)
//            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//            .setContentType(MimeTypes.VIDEO_MP4)
//            .setMetadata(metadata)
//            .build()
//        val mediaItem = MediaQueueItem.Builder(mediaInfo).build()
//    }

    fun releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer!!.release()

            simpleExoPlayer = null
            trackSelector = null
        }
    }

    public override fun onStop() {
        super.onStop()
        //  releasePlayer()
    }

    companion object {
        private const val TAG = "PlayerActivityTAG"
        private var DEFAULT_COOKIE_MANAGER: CookieManager? = null

        init {
            DEFAULT_COOKIE_MANAGER = CookieManager()
            DEFAULT_COOKIE_MANAGER!!.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        }
    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()


    fun saveToHistoryDB(){
        var duration :Long = simpleExoPlayer!!.duration
        var runtime :Long = simpleExoPlayer!!.contentPosition
        if (binding.webview.visibility==View.VISIBLE){
            duration = 0
            runtime=0
        }
        val history =History(film_title, film_image, binding.textViewTitle.text.toString(),
            link,runtime.toString(),duration.toString(), System.currentTimeMillis())
        val database: LocalDatabase by lazy { LocalDatabase.getDatabase(this) }
        GlobalScope.launch {
            if (database.historyDao().getLastHistory().isNotEmpty()) {
               database.historyDao().deleteHistory(history)
            }
            database.historyDao().insertHistory(history)
        }
        if (simpleExoPlayer?.isPlaying == true) simpleExoPlayer!!.stop()
    }
}