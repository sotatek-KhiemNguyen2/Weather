package com.sotatek.weather.util

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.LinearLayout
import com.sotatek.weather.R

object DialogLoadingUtils {

    @JvmStatic
    fun createLoadingDialog(
            context: Context
    ): Dialog {
        val inflater = LayoutInflater.from(context)
        val loadingView = inflater.inflate(R.layout.dialog_loading, null)
        val llLoadingContent =
                loadingView.findViewById<LinearLayout>(R.id.ll_loadingContent)

        val loadingDialog = Dialog(context, R.style.LoadingDialog)
        loadingDialog.setCancelable(true)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.setContentView(
                llLoadingContent,
                LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                )
        )
        val window = loadingDialog.window
        val lp: WindowManager.LayoutParams
        if (window != null) {
            lp = window.attributes
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.setGravity(Gravity.CENTER)
            window.attributes = lp
        }
        loadingDialog.show()
        return loadingDialog
    }

}