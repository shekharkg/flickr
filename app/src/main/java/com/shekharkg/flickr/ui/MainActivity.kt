package com.shekharkg.flickr.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    private lateinit var adapter: PhotosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        initObservers()

        initListeners()
    }

    private fun initView() {
        adapter = PhotosAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewModel.photos?.observe(this, Observer {
            it?.let { photos ->
                adapter.photos = photos
                adapter.notifyDataSetChanged()

                hideViews()
            }
        })

        viewModel.networkState.observe(this, Observer {
            it?.let { state ->
                when {
                    state.status == NetworkState.LOADING.status -> {
                        if (adapter.photos.isEmpty() && progressbar.visibility == View.GONE)
                            progressbar.visibility = View.VISIBLE
                    }
                    state.status == NetworkState.SUCCESS.status -> {
                        hideViews()
                    }
                    state.status == NetworkState.FAILED.status -> {
                        Toast.makeText(this@MainActivity, state.msg, Toast.LENGTH_LONG).show()
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
            emptyView.visibility = View.GONE
            viewModel.refreshData()
        }
    }

    private fun hideViews() {
        if (swipeToRefreshLayout.isRefreshing) swipeToRefreshLayout.isRefreshing = false

        if (progressbar.visibility == View.VISIBLE) progressbar.visibility = View.GONE

        if (emptyView.visibility == View.VISIBLE) emptyView.visibility = View.GONE
    }
}
