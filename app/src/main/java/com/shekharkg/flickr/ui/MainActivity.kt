package com.shekharkg.flickr.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.shekharkg.flickr.R
import com.shekharkg.flickr.utils.InjectorUtils

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, InjectorUtils.provideMainViewModelFactory(application))
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
