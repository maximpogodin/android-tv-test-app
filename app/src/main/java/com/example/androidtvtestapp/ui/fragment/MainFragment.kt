package com.example.androidtvtestapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.example.androidtvtestapp.model.VideoList
import com.example.androidtvtestapp.network.Api
import com.example.androidtvtestapp.network.ExternalApiOptions
import com.example.androidtvtestapp.network.RetrofitClient
import com.example.androidtvtestapp.ui.activity.BrowseErrorActivity
import com.example.androidtvtestapp.ui.activity.PlaybackActivity
import com.example.androidtvtestapp.ui.presenter.CardPresenter
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        title = getString(R.string.app_name)
        onItemViewClickedListener = ItemViewClickedListener()

        loadVideos()
    }

    private fun loadVideos() {
        val api = RetrofitClient.instance.create(Api::class.java)

        api.getVideos(3, ExternalApiOptions.API_KEY, 10, 9).enqueue(object :
            Callback<VideoList> {
            override fun onFailure(call: Call<VideoList>, t: Throwable) {

            }

            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call<VideoList>, response: Response<VideoList>) {
                if (response.isSuccessful) {
                    val responseResult = response.body()
                    val videos = responseResult?.videos ?: emptyList()

                    val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
                    val cardPresenter = CardPresenter()

                    val listRowAdapter = ArrayObjectAdapter(cardPresenter)
                    val header = HeaderItem("New")

                    for (video in videos) {
                        listRowAdapter.add(video)
                    }

                    rowsAdapter.add(ListRow(header, listRowAdapter))
                    adapter = rowsAdapter
                }
            }
        })
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