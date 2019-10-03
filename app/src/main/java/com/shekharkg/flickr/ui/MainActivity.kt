package com.shekharkg.flickr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.shekharkg.flickr.R
import com.shekharkg.flickr.adapter.PhotosAdapter
import com.shekharkg.flickr.utils.InjectorUtils
import com.shekharkg.flickr.utils.NetworkState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, InjectorUtils.provideMainViewModelFactory(application))
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        initObservers()

        initListeners()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PhotosAdapter()
    }

    private fun initObservers() {
        viewModel.photos?.observe(this, Observer {

        })

        viewModel.networkState.observe(this, Observer {
            it?.let { state ->
                when {
                    state.status == NetworkState.LOADING.status -> {
                    }
                    state.status == NetworkState.SUCCESS.status -> {
                    }
                    state.status == NetworkState.FAILED.status -> {
                    }
                }

            }
        })
    }

    private fun initListeners() {
        swipeToRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        actionRetry.setOnClickListener {
            viewModel.refreshData()
        }
    }
}
