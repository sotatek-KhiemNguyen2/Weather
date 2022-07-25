package com.sotatek.weather.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.sotatek.weather.data.remote.errors.ViewError
import kotlinx.coroutines.flow.collectLatest

typealias BindingInflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(private val bindingInflate: BindingInflate<VB>) :
    BaseFragmentCore() {

    lateinit var binding: VB
    abstract val viewModel: VM
    private var mLoadingDialog: Dialog? = null

    abstract fun initializeViews()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = bindingInflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted{
            viewModel.errorsFlow.collectLatest { viewError ->
                val errorMessage = when (viewError) {
                    is ViewError.ResourceError -> getString(viewError.resId)
                    is ViewError.StringError -> viewError.error
                }
                showError(errorMessage) {
                    handleError(viewError)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.progressFlow.collectLatest {
                showProgress(it)
            }
        }
    }
}