package com.fidea.letter

import android.app.Activity
import android.content.Context
import cn.pedant.SweetAlert.SweetAlertDialog
import java.io.File

class Util {

    companion object {


        public fun getToken(context: Context): String {
            return context.getSharedPreferences("pref", Context.MODE_PRIVATE)
                .getString("token", "").toString()
        }

        public fun getCacheDir(context: Context): File {
            return context.cacheDir
        }

        public fun getErrorDialog(dialog: SweetAlertDialog, activity: Activity) {
            if (dialog.isShowing) dialog.dismiss()
            if (activity.isFinishing) return
            val dialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            dialog.setTitle(activity.resources.getString(R.string.error))
            dialog.contentText = activity.resources.getString(R.string.general_error)
            if (!activity.isFinishing) dialog.show()
            dialog.confirmText = activity.resources.getString(R.string.ok)
            dialog.setConfirmClickListener { dialog.dismiss() }
        }

        public fun getWaitingDialog(dialog: SweetAlertDialog, activity: Activity) {
            if (dialog.isShowing) dialog.dismiss()
            if (activity.isFinishing) return
            val dialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
            dialog.setTitle(activity.resources.getString(R.string.wait))
            if (!activity.isFinishing) dialog.show()
            dialog.confirmText = activity.resources.getString(R.string.ok)
            dialog.setConfirmClickListener { dialog.dismiss() }
        }

    }

}