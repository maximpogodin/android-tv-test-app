package com.example.androidtvtestapp.ui.activity

import com.example.androidtvtestapp.ui.fragment.PlaybackVideoFragment
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.androidtvtestapp.model.Video
import com.google.gson.Gson

class PlaybackActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            val extras = intent.extras
            val data = intent.data

            var video : Video? = null
            val gson = Gson()

            if (data != null) {
                video = gson.fromJson(data.getQueryParameter("videoJson"), Video::class.java)
            }
            else if (extras != null) {
                video = gson.fromJson(intent.getStringExtra("videoJson"), Video::class.java)
            }

            if (video != null) {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, PlaybackVideoFragment(video))
                    .commit()
            }
            else {
                val intent = Intent(this, BrowseErrorActivity::class.java)
                intent.putExtra("errorMessage", "Error receiving video")
                startActivity(intent)
            }
        }
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "Playback"
    }
}