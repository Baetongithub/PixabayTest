package com.example.pixabaytest.ui.main_load_image

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.pixabaytest.databinding.ActivityMainBinding
import com.example.pixabaytest.databinding.ItemLoadStateBinding
import com.example.pixabaytest.data.model.Hit
import com.example.pixabaytest.ui.DetailedImageActivity
import com.example.pixabaytest.ui.main_load_image.load_state.FooterLoadStateAdapter
import com.example.pixabaytest.ui.main_load_image.load_state.FooterViewHolder
import com.example.pixabaytest.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var vb: ActivityMainBinding
    private val pagingAdapter = ImagePagerAdapter(this::onItemClickListener)
    private var footerLoadStateHolder: FooterViewHolder? = null
    private val gridLayoutManager = GridLayoutManager(this, 2)
    private val viewModel: LoadImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)

        initViews()
        observeLoadState()

        lifecycleScope.launch {
            viewModel.searchRepos("hello").collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
        setUpRV()
    }

    private fun initViews() {

        vb.fabSearch.setOnClickListener {
            loadImages()
        }
    }

    private fun onItemClickListener(hit: Hit) {
        val intent = Intent(this, DetailedImageActivity::class.java)
            .putExtra(Constants.SEND_HIT_EXTRA, hit)
        startActivity(intent)
    }

    private fun loadImages() {
        lifecycleScope.launch {
            viewModel.searchRepos(vb.etSearchImage.text.toString()).collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }
//
//        viewModel.liveModel(vb.etSearchImage.text.toString(), page).observe(this) {
//            list.addAll(it.hits)
//            adapter.notifyItemInserted(list.size - 1)
//        }


    private fun setUpRV() {
        vb.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = this@MainActivity.pagingAdapter
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        hideKeyBoard()
                    }
                }
            })
        }
    }

    private fun observeLoadState() {
        //also use adapter.addLoadStateListener
        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest { state ->
                // main indicator in the center of the screen
                footerLoadStateHolder?.bind(state.refresh)
            }
        }
        vb.recyclerView.adapter =
            pagingAdapter.withLoadStateFooter(FooterLoadStateAdapter(pagingAdapter::retry))
        footerLoadStateHolder = FooterViewHolder(ItemLoadStateBinding.inflate(layoutInflater)) {
            pagingAdapter.retry()
        }

        // to centralize the retry button
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == pagingAdapter.itemCount && pagingAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
    }

    private fun hideKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) view = View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}