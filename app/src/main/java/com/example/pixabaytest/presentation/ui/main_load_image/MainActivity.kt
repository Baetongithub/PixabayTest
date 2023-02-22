package com.example.pixabaytest.presentation.ui.main_load_image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.pixabaytest.databinding.ActivityMainBinding
import com.example.pixabaytest.domain.model.Hit
import com.example.pixabaytest.presentation.ui.deatiled_images.DetailedImageActivity
import com.example.pixabaytest.presentation.ui.load_state.MyLoadStateAdapter
import com.example.pixabaytest.presentation.utils.CheckInternet
import com.example.pixabaytest.presentation.utils.Constants
import com.example.pixabaytest.presentation.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding

    private val pagingAdapter = ImagePagerAdapter(this::onItemClickListener)
    private val gridLayoutManager = GridLayoutManager(this, 2)
    private val loadStateAdapter = MyLoadStateAdapter { pagingAdapter.retry() }
    private val viewModel: LoadImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        initViews()
        setupRecyclerView()
        checkInternet()

        loadDefaultRequestedImages("hello")
    }

    private fun loadDefaultRequestedImages(request: String) {

        lifecycleScope.launch {
            viewModel.findImage(request).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }

    private fun initViews() {
        vb.fabSearch.setOnClickListener {
            loadImages()
        }
    }

    private fun onItemClickListener(hit: Hit) {
        val intent = Intent(this, DetailedImageActivity::class.java)
            .putExtra(Constants.HIT_EXTRA, hit)
        startActivity(intent)
    }

    private fun loadImages() {
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
                        hideKeyBoard()
                    }
                }
            })
        }
        loadStateListener()
        centralizeRetryButton()
    }

    private fun loadStateListener() = with(vb) {
        pagingAdapter.addLoadStateListener { loadStates ->
            recyclerView.isVisible = loadStates.refresh is LoadState.NotLoading
            progressBar.isVisible = loadStates.refresh is LoadState.Loading
            tvNoInternet.isVisible = loadStates.refresh is LoadState.Error
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

    private fun checkInternet() {
        val ccs = CheckInternet(application)
        ccs.observe(this) { isInternetOn ->
            vb.tvNoInternetMainActivity.gone = isInternetOn
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) view = View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}