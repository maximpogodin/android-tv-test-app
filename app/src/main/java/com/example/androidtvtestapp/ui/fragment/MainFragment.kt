package com.example.androidtvtestapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import com.example.androidtvtestapp.R
import com.example.androidtvtestapp.model.Video
import com.example.androidtvtestapp.repository.VideoRepository
import com.example.androidtvtestapp.ui.activity.BrowseErrorActivity
import com.example.androidtvtestapp.ui.activity.PlaybackActivity
import com.example.androidtvtestapp.ui.presenter.CardPresenter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title = getString(R.string.app_name)
        onItemViewClickedListener = ItemViewClickedListener()

        CoroutineScope(Dispatchers.IO).launch {
            val repository = VideoRepository()
            val videos = repository.getVideos(10)

            Handler(Looper.getMainLooper()).post {
                buildUI(videos)
            }
        }
    }

    private fun buildUI(videos : List<Video>) {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter(requireContext())

        val listRowAdapter = ArrayObjectAdapter(cardPresenter)
        val header = HeaderItem("New")

        for (video in videos) {
            listRowAdapter.add(video)
        }

        rowsAdapter.add(ListRow(header, listRowAdapter))
        adapter = rowsAdapter
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            val gson = Gson()

            if (item is Video) {
                val intent = Intent(activity!!, PlaybackActivity::class.java)
                val videoJson = gson.toJson(item)
                intent.putExtra("videoJson", videoJson)

                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    PlaybackActivity.SHARED_ELEMENT_NAME
                )
                    .toBundle()
                startActivity(intent, bundle)
            } else if (item is String) {
                if (item.contains(getString(R.string.error_fragment))) {
                    val intent = Intent(activity!!, BrowseErrorActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(activity!!, item, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}