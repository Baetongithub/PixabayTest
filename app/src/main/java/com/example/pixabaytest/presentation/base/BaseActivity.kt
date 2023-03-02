package com.example.pixabaytest.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.example.pixabaytest.presentation.ui.state.UIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding>(private val viewBinding: (LayoutInflater) -> VB) :
    AppCompatActivity() {

    private var binding: VB? = null
    val vb get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBinding.invoke(layoutInflater)
        setContentView(binding?.root)

        initView()
        initViewModel()
        checkInternet()
    }

    protected open fun <T> StateFlow<UIState<T>>.collectState(
        onLoading: () -> Unit,
        onError: (message: String) -> Unit,
        onSuccess: suspend (data: T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@collectState.collect {
                    when (it) {
                        is UIState.Empty -> {}
                        is UIState.Error -> {
                            onError(it.message)
                        }
                        is UIState.Loading -> {
                            onLoading()
                        }
                        is UIState.Success -> {
                            onSuccess(it.data)
                        }
                    }
                }
            }
        }
    }

    protected open fun initView() {}
    protected open fun initViewModel() {}
    protected open fun checkInternet() {}

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}