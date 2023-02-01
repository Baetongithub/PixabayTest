package com.example.pixabaytest.ui

import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.example.pixabaytest.R
import com.example.pixabaytest.data.model.Hit
import com.example.pixabaytest.databinding.ActivityDetailedImageBinding
import com.example.pixabaytest.databinding.BottomSheetCommentsBinding
import com.example.pixabaytest.ui.main_load_image.ImagePagerAdapter
import com.example.pixabaytest.ui.main_load_image.LoadImageViewModel
import com.example.pixabaytest.utils.Constants
import com.example.pixabaytest.utils.SavePhotoToStorage
import com.example.pixabaytest.utils.extensions.toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailedImageActivity : AppCompatActivity() {

    private lateinit var vb: ActivityDetailedImageBinding

    private val pixaAdapter = ImagePagerAdapter(this::onImageClickListener)
    private val gridLayoutManager = GridLayoutManager(this, 2)
    private val viewModel: LoadImageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityDetailedImageBinding.inflate(layoutInflater)
        setContentView(vb.root)

        getDataInitViews()
        setUpRV()
        implCommentsPage()
        downloadImage()
    }

    private fun setUpRV() {
        vb.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = pixaAdapter
        }
    }

    private fun getDataInitViews() {

        val hit: Hit = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Constants.SEND_HIT_EXTRA, Hit::class.java) as Hit
        } else {
            intent.getSerializableExtra(Constants.SEND_HIT_EXTRA) as Hit
        }

        val tag = hit.tags.substring(hit.tags.lastIndexOf(" ") + 1).trim()
        lifecycleScope.launch {
            viewModel.searchRepos(tag).collectLatest { pagingData ->
                pixaAdapter.submitData(pagingData)
            }
        }

        with(vb) {
            imageDetailedView.load(hit.largeImageURL)
            tvViews.text = String.format(hit.views.toString() + "\n ${getString(R.string.views)}")
            tvLikes.text = String.format(hit.likes.toString() + "\n ${getString(R.string.likes)}")
            tvUserName.text = hit.user
            imageUserAvatar.load(hit.userImageURL)
            tvComments.text =
                String.format(getString(R.string.comments) + " (${hit.comments})")

            imageBack.setOnClickListener { finish() }
            tvUserName.setOnClickListener { toast(tag) }
        }
    }

    private fun implCommentsPage() {
        vb.tvComments.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
            val dialogBinding: BottomSheetCommentsBinding = BottomSheetCommentsBinding.inflate(
                layoutInflater
            )
            bottomSheetDialog.setContentView(dialogBinding.root)
            bottomSheetDialog.show()
        }
    }

    private fun downloadImage() {
        vb.imageDownloadInToolbar.setOnClickListener {
            SavePhotoToStorage.save(this, vb.imageDetailedView)
            vb.tvSavedInToolbar.visibility = VISIBLE
            vb.imageDownloadInToolbar.visibility = GONE
        }
    }

    private fun onImageClickListener(hit: Hit) {
        toast(hit.id.toString())
    }
}
