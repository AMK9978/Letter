package com.fidea.letter.util

import android.app.Activity
import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fidea.letter.R
import java.io.File

class Util {

    companion object {
        private const val SHARED_PREFERENCE_NAME = "PREF_app"
        private const val TOKEN = "PREF_token"
        private const val DEFAULT_VALUE = ""


        fun getToken(context: Context): String {
            return context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
                .getString(TOKEN, DEFAULT_VALUE).toString()
        }

        fun getCacheDir(context: Context): File {
            return context.cacheDir
        }

        fun getErrorDialog(dialog: SweetAlertDialog, activity: Activity) {
            if (dialog.isShowing) dialog.dismiss()
            if (activity.isFinishing) return
            val dialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            dialog.setTitle(activity.resources.getString(R.string.error))
            dialog.contentText = activity.resources.getString(R.string.general_error)
            if (!activity.isFinishing) dialog.show()
            dialog.confirmText = activity.resources.getString(R.string.ok)
            dialog.setConfirmClickListener { dialog.dismiss() }
        }

        fun getWaitingDialog(dialog: SweetAlertDialog, activity: Activity): SweetAlertDialog? {
            if (dialog.isShowing) dialog.dismiss()
            if (activity.isFinishing) return null
            val dialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
            dialog.setTitle(activity.resources.getString(R.string.wait))
            if (!activity.isFinishing) dialog.show()
            dialog.confirmText = activity.resources.getString(R.string.ok)
            dialog.setConfirmClickListener { dialog.dismiss() }
            return dialog
        }

    }

}