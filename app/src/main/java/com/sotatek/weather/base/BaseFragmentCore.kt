package com.sotatek.weather.base

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.sotatek.weather.data.remote.errors.ViewError
import com.sotatek.weather.util.AlertDialogUtil
import com.sotatek.weather.util.DialogLoadingUtils

abstract class BaseFragmentCore : Fragment() {
    private var mLoadingDialog: Dialog? = null

    open fun showProgress(visible: Boolean) {
        if (visible) showLoadingDialog() else dismissLoadingDialog()
    }

    private fun showLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) return
        mLoadingDialog = DialogLoadingUtils.createLoadingDialog(requireContext())
        mLoadingDialog!!.setCanceledOnTouchOutside(false)
        mLoadingDialog!!.show()
    }

    private fun dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
    }

    open fun showError(errorMessage: String?) {
        if (errorMessage == null) return
        AlertDialogUtil.showMessage(requireContext(), errorMessage, false) {}
    }

    fun showError(errorMessage: String?, action: () -> Unit) {
        if (errorMessage == null) return
        AlertDialogUtil.showMessage(requireContext(), errorMessage, false) {
            action.invoke()
        }
    }

    open fun handleError(viewError: ViewError) = Unit

    open fun onBackPressed(): Boolean {
        return false
    }
}