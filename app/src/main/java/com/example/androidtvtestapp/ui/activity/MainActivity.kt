package com.example.androidtvtestapp.ui.activity

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.androidtvtestapp.R
import com.example.androidtvtestapp.ui.fragment.MainFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_browse_fragment, MainFragment())
                .commitNow()
        }
    }
}