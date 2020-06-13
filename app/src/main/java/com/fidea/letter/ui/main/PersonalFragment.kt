package com.fidea.letter.ui.main

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.fidea.letter.BroadcastReceiver
import com.fidea.letter.R
import com.fidea.letter.Util
import kotlinx.android.synthetic.main.personal_fragment.*
import kotlinx.android.synthetic.main.personal_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*

class PersonalFragment : Fragment() {


    private val galleryReqCode = 100
    private val dialog: SweetAlertDialog? = null
    private val INTERVAL = 1000 * 60 * 2.toLong()
    private val permissionCode = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.personal_fragment, container, false)
        val avatar = view.profile_image
        avatar.setOnClickListener {
            performAvatarClick()
        }
        val share = view.share
        share.setOnClickListener {
            share()
        }

        val aboutUs = view.about_us
        aboutUs.setOnClickListener {
            performAboutUs()
        }
        val edit = view.edit
        edit.setOnClickListener {
            performEdit()
        }
        val messages = view.messages
        messages.setOnClickListener {
            performMessages()
        }
        return view
    }


    private fun performMessages() {

    }

    private fun performAboutUs() {

    }


    private fun performEdit() {

    }


    private fun performAvatarClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                // Should we show an explanation?
                shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) // Explain to the user why we need to read the contacts
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    permissionCode
                )
            } else {
                Log.i("TAG", "Permissions ")
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, galleryReqCode)
    }


    /**
     * Specify interval for Pull notification system which is using Alarm manager and
     * BroadcastReceiver
     */
    private fun setOnAlarmManager() {
        val alarmIntent: PendingIntent
        val alarmMgr: AlarmManager? = getSystemService(context!!, Context.ALARM_SERVICE.javaClass) as AlarmManager?
        val intent = Intent(context, BroadcastReceiver::class.java)
        alarmIntent = PendingIntent.getBroadcast(context!!, 0, intent, 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        alarmMgr?.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            INTERVAL, alarmIntent
        )
    }

    /**
     * Share option in drawer
     */
    private fun share() {
        ShareCompat.IntentBuilder.from(activity!!).setType("text/plain")
            .setChooserTitle("اشتراک گذاری نامه")
            .setText("https://letter4u.com").startChooser()
    }

    /**
     * Process got image in order to send it to UCrop activity or get it from UCrop and set it as
     * avatar in backend and display to user.
     *
     * @param requestCode
     * @param resultCode
     * @param data:       image file
     */
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == galleryReqCode) {
            if (data != null && data.data != null) {
                Glide.with(this).load(data.data).placeholder(R.drawable.ic_person_green_24dp)
                    .into(profile_image)
                Log.i("TAG", "here:$data.data ")
                if (data.data == null || data.data!!.path == null) {
                    return
                }
                setAvatar(
                    provideSetAvatarBody(
                        data.data!!)
                    !!)
            }
        }
    }

    private fun provideSetAvatarBody(resultUri: Uri): MultipartBody.Part? {
        if (resultUri.path == null) {
            return null
        }
        val file = File(resultUri.path)
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData("avatar", file.name, requestFile)
    }


    /**
     * Set baby avatar in backend
     *
     * @param body contains baby image
     */
    private fun setAvatar(body: MultipartBody.Part) {
//        dialog.show()
        // TODO: Use repo in order to set the avatar in server
//        val apiInterface: APIInterface =
//            APIClient.getRetrofit(this).create(APIInterface::class.java)
//        apiInterface.postBabyImage(body, defaultBaby.getId())
//            .enqueue(object : Callback<String?> {
//                override fun onResponse(
//                    call: Call<String?>,
//                    response: Response<String?>
//                ) {
//                    if (response.isSuccessful && response.body() != null) {
//                        if (dialog.isShowing) dialog.dismiss()
//                    } else {
//                        Log.i(
//                            "TAG",
//                            "Error in getting response of postBabyImage " + response.code()
//                        )
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<String?>,
//                    t: Throwable
//                ) {
//                    Log.i(
//                        "TAG",
//                        "Error in getting response of postBabyImage " + t.message
//                    )
//                }
//            })
    }

    /**
     * @param requestCode
     * @param permissions  which is reading from memory
     * @param grantResults In order to get access to user's gallery for avatar
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionCode == requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) { //permission from popup granted
                pickImageFromGallery()
            } else { //permission from popup denied
                Toast.makeText(context!!, "دسترسی الزامی است!", Toast.LENGTH_LONG).show()
            }
        }
    }
}