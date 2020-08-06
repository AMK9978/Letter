package com.fidea.letter.ui.main.personal

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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.fidea.letter.*
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.User
import com.fidea.letter.ui.main.about.AboutActivity
import com.fidea.letter.ui.main.edit.EditActivity
import com.fidea.letter.ui.main.inbox.MessagesActivity
import com.fidea.letter.util.Util
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.personal_fragment.*
import kotlinx.android.synthetic.main.personal_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class PersonalFragment : Fragment() {


    private val galleryReqCode = 100
    private val dialog: SweetAlertDialog? = null
    private var name: TextView? = null
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
        name = view.name
        getProfile()
        return view
    }

    private fun getProfile() {

        val apiInterface: APIInterface =
            APIClient.getRetrofit(Util.getToken(requireContext()), Util.getCacheDir(requireContext()))!!
                .create(APIInterface::class.java)
        apiInterface.getProfile()?.enqueue(object : Callback<User?> {
            override fun onFailure(call: Call<User?>, t: Throwable) {

            }

            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                name?.text = response.body()?.username
                loadImage(response.body()!!.avatar)
            }

        })
    }

    private fun loadImage(imagePath: String?) {
        if (imagePath == null) {
            return
        }
        Glide.with(requireContext())
            .load(imagePath).placeholder(R.drawable.placeholder).into(app_bar_image)
    }


    private fun performMessages() {
        val intent = Intent(context, MessagesActivity::class.java)
        startActivity(intent)
    }

    private fun performAboutUs() {
        val intent = Intent(context, AboutActivity::class.java)
        startActivity(intent)
    }


    private fun performEdit() {
        val intent = Intent(context, EditActivity::class.java)
        startActivity(intent)
    }


    private fun performAvatarClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    requireContext(),
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
     * Share option in drawer
     */
    private fun share() {
        ShareCompat.IntentBuilder.from(requireActivity()).setType("text/plain")
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
                        data.data!!
                    )
                    !!
                )
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
                Toast.makeText(requireContext(), "دسترسی الزامی است!", Toast.LENGTH_LONG).show()
            }
        }
    }
}