package com.example.androidtvtestapp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.leanback.app.ErrorSupportFragment
import com.example.androidtvtestapp.R

class ErrorFragment : ErrorSupportFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = resources.getString(R.string.app_name)
    }

    internal fun setErrorContent(text: String?) {
        message = if (text.isNullOrEmpty()) {
            resources.getString(R.string.error_fragment_message)
        } else {
            text
        }

        imageDrawable =
            ContextCompat.getDrawable(activity!!, androidx.leanback.R.drawable.lb_ic_sad_cloud)
        setDefaultBackground(TRANSLUCENT)
        buttonText = resources.getString(R.string.dismiss_error)
        buttonClickListener = View.OnClickListener {
            fragmentManager!!.beginTransaction().remove(this@ErrorFragment).commit()
        }
    }

    companion object {
        private const val TRANSLUCENT = true
    }
}