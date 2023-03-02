package com.example.pixabaytest.presentation.ui.deatiled_images

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.example.pixabaytest.R
import com.example.pixabaytest.databinding.ActivityDetailedImageBinding
import com.example.pixabaytest.databinding.BottomSheetCommentsBinding
import com.example.pixabaytest.domain.model.Hit
import com.example.pixabaytest.presentation.ui.main.ImagePagerAdapter
import com.example.pixabaytest.presentation.ui.main.LoadImageViewModel
import com.example.pixabaytest.presentation.utils.Constants
import com.example.pixabaytest.presentation.utils.SavePhotoToStorage
import com.example.pixabaytest.presentation.utils.extensions.toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.Serializable

@ExperimentalPagingApi
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
    }

    private fun setUpRV() {
        vb.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = pixaAdapter
        }
    }

    private fun getDataInitViews() = with(vb) {

        val hit = intent?.customGetSerializable<Hit>(Constants.HIT_EXTRA)

        if (hit != null) {

            // load similar images
            val tag = hit.tags.substring(hit.tags.lastIndexOf(" ") + 1).trim()
            lifecycleScope.launch {
                viewModel.findImage(tag).collectLatest { pagingData ->
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
                toast(tag)
            }

            downloadImage(hitId = hit.id.toString(), imageDetailedView)
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
//
//    private fun find(values: List<Int>, value: Int): Boolean {
//        for (e in values) {
//            if (e == value) {
//                return true
//            }
//        }
//        return false
//    }

    private fun downloadImage(hitId: String, imageView: ImageView) = with(vb) {
        imageDownloadInToolbar.setOnClickListener {
            tvSavedInToolbar.visibility = View.VISIBLE
            imageDownloadInToolbar.visibility = View.GONE
            toast(hitId)
        }
        SavePhotoToStorage.save(this@DetailedImageActivity, imageView)
    }

    private fun onImageClickListener(hitEntity: Hit) {
        toast(hitEntity.id.toString())
    }

    @Suppress("DEPRECATION")
    inline fun <reified T : Serializable> Intent.customGetSerializable(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializableExtra(key, T::class.java)
        } else {
            getSerializableExtra(key) as? T
        }
    }
}