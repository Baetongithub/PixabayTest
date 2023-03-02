package com.example.pixabaytest.presentation.ui.main

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.pixabaytest.data.mappers.toHit
import com.example.pixabaytest.databinding.ActivityMainBinding
import com.example.pixabaytest.domain.model.Hit
import com.example.pixabaytest.presentation.base.BaseActivity
import com.example.pixabaytest.presentation.load_state.MyLoadStateAdapter
import com.example.pixabaytest.presentation.ui.deatiled_images.DetailedImageActivity
import com.example.pixabaytest.presentation.utils.CheckInternet
import com.example.pixabaytest.presentation.utils.Constants
import com.example.pixabaytest.presentation.utils.KeyboardHelper
import com.example.pixabaytest.presentation.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val pagingAdapter = ImagePagerAdapter(this::onItemClickListener)
    private val gridLayoutManager = GridLayoutManager(this, 2)
    private val loadStateAdapter = MyLoadStateAdapter { pagingAdapter.retry() }

    private val viewModel: LoadImageViewModel by viewModels()

    override fun initView() {
        super.initView()

        setupRecyclerView()
        loadDefaultRequestedImages()
    }

    override fun initViewModel() {
        super.initViewModel()
        vb.fabSearch.setOnClickListener {
            searchImages()
        }
    }

    override fun checkInternet() {
        super.checkInternet()
        val ccs = CheckInternet(application)
        ccs.observe(this) { isInternetOn ->
            vb.tvNoInternetMainActivity.gone = isInternetOn
        }
    }

    private fun loadDefaultRequestedImages() {

        lifecycleScope.launch {
            viewModel.getImages.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData.map { it.toHit() })
            }
        }
    }

    private fun searchImages() {
        lifecycleScope.launch {
            viewModel.findImage(vb.etSearchImage.text.toString()).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() = with(vb) {
        recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = pagingAdapter.withLoadStateFooter(footer = loadStateAdapter)

            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        KeyboardHelper.hide(this@MainActivity)
                    }
                }
            })
        }
        loadStateListener()
        centralizeRetryButton()
    }

    private fun onItemClickListener(hit: Hit) {
        val intent = Intent(this, DetailedImageActivity::class.java)
            .putExtra(Constants.HIT_EXTRA, hit)
        startActivity(intent)
    }

    private fun loadStateListener() = with(vb) {
        pagingAdapter.addLoadStateListener { loadStates ->
            //recyclerView.isVisible = loadStates.refresh is LoadState.NotLoading
            progressBar.isVisible = loadStates.refresh is LoadState.Loading
            btnRetry.isVisible = loadStates.refresh is LoadState.Error
        }
        btnRetry.setOnClickListener { pagingAdapter.retry() }
    }

    private fun centralizeRetryButton() {
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == pagingAdapter.itemCount && loadStateAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
    }
}