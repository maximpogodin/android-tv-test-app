package com.example.androidtvtestapp.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import com.example.androidtvtestapp.model.File
import com.example.androidtvtestapp.model.Video
import com.example.androidtvtestapp.repository.VideoRepository
import com.example.androidtvtestapp.ui.activity.BrowseErrorActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val firstTrailer = getFirstTrailer(video)

        if (firstTrailer != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val trailerUri = getTrailerUri(video.id, firstTrailer.id)

                playVideo(trailerUri)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mTransportControlGlue.pause()
    }

    override fun onStop() {
        super.onStop()
        mTransportControlGlue.pause()
    }

    private fun getFirstTrailer(video : Video): File? {
        return video.files.find { it.name.equals("Trailer", ignoreCase = true) }
    }

    private fun getTrailerUri(videoId : Int, fileId : Int): String? {
        val repository = VideoRepository()

        return repository.getVideoUrl(videoId, fileId)?.uri
    }

    private fun playVideo(uri : String?) {
        if (uri != null) {
            playerAdapter.setDataSource(Uri.parse(uri))
        } else {
            showErrorActivity()
        }
    }

    private fun showErrorActivity() {
        val intent = Intent(activity!!, BrowseErrorActivity::class.java)
        intent.putExtra("errorMessage", "No trailer available")
        startActivity(intent)
    }
}