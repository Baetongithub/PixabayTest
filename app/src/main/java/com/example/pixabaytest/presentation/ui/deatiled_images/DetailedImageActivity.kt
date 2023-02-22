package com.example.pixabaytest.presentation.ui.deatiled_images

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.example.pixabaytest.R
import com.example.pixabaytest.data.model.Hit
import com.example.pixabaytest.databinding.ActivityDetailedImageBinding
import com.example.pixabaytest.databinding.BottomSheetCommentsBinding
import com.example.pixabaytest.presentation.ui.main_load_image.ImagePagerAdapter
import com.example.pixabaytest.presentation.ui.main_load_image.LoadImageViewModel
import com.example.pixabaytest.presentation.utils.Constants
import com.example.pixabaytest.presentation.utils.SavePhotoToStorage
import com.example.pixabaytest.presentation.utils.extensions.toast
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

    private val savedImagesId = arrayListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityDetailedImageBinding.inflate(layoutInflater)
        setContentView(vb.root)

        getDataInitViews()
        setUpRV()
        implCommentsPage()
    }

    private fun setUpRV() {
        vb.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = pixaAdapter
        }
    }

    private fun getDataInitViews() = with(vb) {

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


        imageDetailedView.load(hit.largeImageURL)
        tvViews.text = String.format(hit.views.toString() + "\n ${getString(R.string.views)}")
        tvLikes.text = String.format(hit.likes.toString() + "\n ${getString(R.string.likes)}")
        tvUserName.text = hit.user
        imageUserAvatar.load(hit.userImageURL)
        tvComments.text =
            String.format(getString(R.string.comments) + " (${hit.comments})")

        imageBack.setOnClickListener { finish() }
        tvUserName.setOnClickListener {
            savedImagesId.clear()
            toast(tag)
        }

        imageDownloadInToolbar.setOnClickListener {
            //downloadImage(hit, vb.imageDetailedView)

            //viewModelRoom.setPrefs(hit.id)

            tvSavedInToolbar.visibility = View.VISIBLE
            imageDownloadInToolbar.visibility = View.GONE
            toast(hit.id.toString())
        }

        savedImagesId.add(hit.id)

        if (find(savedImagesId, hit.id)) {
            toast(hit.id.toString() + " YEss")
            tvSavedInToolbar.visibility = View.VISIBLE
            imageDownloadInToolbar.visibility = View.GONE
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

    private fun find(values: List<Int>, value: Int): Boolean {
        for (e in values) {
            if (e == value) {
                return true
            }
        }
        return false
    }

    private fun downloadImage(imageView: ImageView) {
        SavePhotoToStorage.save(this, imageView)
    }

    private fun onImageClickListener(hit: Hit) {
        toast(hit.id.toString())
    }
}