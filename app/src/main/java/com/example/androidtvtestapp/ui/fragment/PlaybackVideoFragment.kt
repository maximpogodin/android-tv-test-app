package com.example.androidtvtestapp.ui.fragment

import com.example.androidtvtestapp.network.RetrofitClient
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import com.example.androidtvtestapp.model.Video
import com.example.androidtvtestapp.model.VideoUrl
import com.example.androidtvtestapp.network.Api
import com.example.androidtvtestapp.network.ExternalApiOptions
import com.example.androidtvtestapp.ui.activity.BrowseErrorActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaybackVideoFragment (private val video: Video) : VideoSupportFragment() {

    private lateinit var mTransportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>
    private lateinit var playerAdapter: MediaPlayerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        playerAdapter = MediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        mTransportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
        mTransportControlGlue.host = glueHost
        mTransportControlGlue.title = video.name
        mTransportControlGlue.subtitle = video.description
        mTransportControlGlue.playWhenPrepared()

        playVideo()
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }

    override fun onStop() {
        super.onStop()
        mTransportControlGlue.pause()
    }

    private fun playVideo() {
        val trailerFile = video.files.find { it.name.equals("Trailer", ignoreCase = true) }

        if (trailerFile != null) {
            val api = RetrofitClient.instance.create(Api::class.java)

            api.getVideoUrl(video.id, trailerFile.id, 0, 3, ExternalApiOptions.API_KEY).enqueue(object :
                Callback<VideoUrl> {
                override fun onResponse(call: Call<VideoUrl>, response: Response<VideoUrl>) {
                    if (response.isSuccessful) {
                        val videoUrl = response.body()

                        if (videoUrl != null) {
                            playerAdapter.setDataSource(Uri.parse(videoUrl.uri))
                        }
                    }
                }

                override fun onFailure(call: Call<VideoUrl>, t: Throwable) {}
            })
        }
        else {
            val intent = Intent(activity!!, BrowseErrorActivity::class.java)
            intent.putExtra("errorMessage", "No trailer available")
            startActivity(intent)
        }
    }
}