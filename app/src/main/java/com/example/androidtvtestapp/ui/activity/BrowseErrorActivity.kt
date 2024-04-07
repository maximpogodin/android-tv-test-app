package com.example.androidtvtestapp.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.androidtvtestapp.R
import com.example.androidtvtestapp.ui.fragment.ErrorFragment
import com.example.androidtvtestapp.ui.fragment.MainFragment

class BrowseErrorActivity : FragmentActivity() {

    private lateinit var mErrorFragment: ErrorFragment
    private lateinit var mSpinnerFragment: SpinnerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }

        testError(intent.getStringExtra("errorMessage"))
    }

    private fun testError(text: String?) {
        mErrorFragment = ErrorFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_browse_fragment, mErrorFragment)
            .commit()

        mSpinnerFragment = SpinnerFragment()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_browse_fragment, mSpinnerFragment)
            .commit()

        val timerDelay = resources.getInteger(R.integer.timer_delay)
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            supportFragmentManager
                .beginTransaction()
                .remove(mSpinnerFragment)
                .commit()
            mErrorFragment.setErrorContent(text)
        }, timerDelay.toLong())
    }

    class SpinnerFragment : Fragment() {
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            val progressBar = ProgressBar(container?.context)
            val spinnerSize = Size(
                resources.getInteger(R.integer.spinner_width),
                resources.getInteger(R.integer.spinner_height)
            )

            if (container is FrameLayout) {
                val layoutParams =
                    FrameLayout.LayoutParams(spinnerSize.width, spinnerSize.height, Gravity.CENTER)
                progressBar.layoutParams = layoutParams
            }
            return progressBar
        }
    }
}