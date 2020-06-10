package com.fidea.letter

import android.app.Activity
import cn.pedant.SweetAlert.SweetAlertDialog

class Util {

    companion object {

        private fun getErrorDialog(dialog: SweetAlertDialog, activity: Activity) {
            if (dialog.isShowing) dialog.dismiss()
            if (activity.isFinishing) return
            val dialog = SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            dialog.setTitle(activity.resources.getString(R.string.error))
            dialog.contentText = activity.resources.getString(R.string.general_error)
            if (!activity.isFinishing) dialog.show()
            dialog.confirmText = activity.resources.getString(R.string.ok)
            dialog.setConfirmClickListener { dialog.dismiss() }
        }

        private fun getWaitingDialog(dialog: SweetAlertDialog, activity: Activity) {
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