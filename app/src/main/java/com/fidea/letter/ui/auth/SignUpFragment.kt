package com.fidea.letter.ui.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.fidea.letter.MainActivity
import com.fidea.letter.R
import com.fidea.letter.Util
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.api.Temp
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SignUpFragment : Fragment() {
    private var loginViewModel: LoginViewModel? = null
    private lateinit var dialog: SweetAlertDialog
    private lateinit var body: MultipartBody.Part
    private val galleryReqCode = 100
    private val permissionCode = 101

    companion object {
        fun newInstance() = SignUpFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val view = inflater.inflate(R.layout.sign_up_fragment, container, false)
        dialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        view.image_gotoLogin.setOnClickListener {
            onLoginClick()
        }
        view.gotoLogin.setOnClickListener {
            onLoginClick()
        }

        view.cirRegisterButton.setOnClickListener {
            signUp()
        }

        view.profile_image.setOnClickListener {
            performAvatarClick()
        }

        return view
    }

    private fun performAvatarClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionChecker.checkSelfPermission(
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionCode == requestCode) {
            if (grantResults.size != 0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
            ) { //permission from popup granted
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, galleryReqCode)
    }


    private fun provideSetAvatarBody(resultUri: Uri): MultipartBody.Part? {
        if (resultUri.path == null) {
            return null
        }
        val string = resultUri.let { context!!.contentResolver.openInputStream(it).use { it!!.reader().readText() } }

        Log.i("TAG", string)
        val file = File(resultUri.path)
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData("avatar", file.name, requestFile)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }


    /**
     * Provide crop and etc for baby's avatar
     *
     * @param picUri :Uri of baby image
     */
    private fun performCrop(picUri: Uri) {
        Log.i("TAG", picUri.toString())
        val file = File(context!!.getExternalFilesDir(null), "letter")
        val ss = "profileImage.png"
        if (!file.exists()) {
            file.mkdirs()
        }
        val myPathFile = File(file.absolutePath + "/" + ss)
        val options = UCrop.Options()
        options.setToolbarTitle("برش تصویر")
        options.setToolbarColor(resources.getColor(R.color.colorPrimary))
        options.setLogoColor(resources.getColor(R.color.colorPrimary))
        options.setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        options.setActiveWidgetColor(resources.getColor(R.color.colorPrimary))
        options.setCompressionFormat(Bitmap.CompressFormat.PNG)
        options.setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        options.setToolbarWidgetColor(resources.getColor(R.color.white))
        options.setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        options.setStatusBarColor(resources.getColor(R.color.colorPrimaryDark))
        options.setActiveWidgetColor(resources.getColor(R.color.colorPrimary))
        UCrop.of(picUri, Uri.fromFile(myPathFile)).withOptions(options)
            .withAspectRatio(1f, 1f)
            .start(activity as AppCompatActivity)
    }

    private fun checkFields(): Boolean {
        if (editTextEmail.text.isEmpty() || editTextPassword.text.isEmpty()) {
            showDialog()
            return false
        }
        return true
    }

    private fun showDialog() {

    }

    private fun signUp() {
        val name: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), editTextName.text.toString())
        val password: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), editTextPassword.text.toString())
        val email: RequestBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), editTextEmail.text.toString())

        val apiInterface: APIInterface =
            context?.let {
                APIClient.getRetrofit(
                    Util.getToken(context!!),
                    Util.getCacheDir(context!!)
                )
            }!!.create(APIInterface::class.java)
        apiInterface.signUp(
            name, password, email, body
        )
            ?.enqueue(object : Callback<Temp> {
                override fun onResponse(
                    call: Call<Temp>,
                    response: Response<Temp>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!
                        gotoAfter()
                    } else {
                        getErrorEmailDialog()
                        Log.i(
                            "TAG", "Error in onResponse " + response.code() + " " +
                                    response.message()
                        )
                    }
                }

                override fun onFailure(
                    call: Call<Temp>,
                    t: Throwable
                ) {
                    Log.i(
                        "TAGd", t.message
                    )
                }
            })
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
        if (data != null) {
            Log.i("TAG", "HERE AND GRANTED")
            if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
                if (data.data != null) {
                    Glide.with(this).load(data.data).placeholder(R.drawable.ic_person_green_24dp)
                        .into(profile_image)
                    Log.i("TAG", "here:$data.data ")
                    if (data.data == null || data.data!!.path == null) {
                        return
                    }

                    body = provideSetAvatarBody(
                        data.data!!
                    )!!
                }
            }
        }
        if (resultCode == Activity.RESULT_OK && requestCode == galleryReqCode) {
            if (data != null && data.data != null) performCrop(data.data!!)
        }

    }

    private fun gotoHome() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    private fun onLoginClick() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance())
            .commitNow()
    }


    private fun getWaitingDialog() {
        if (activity!!.isFinishing) return
        if (dialog.isShowing) dialog.dismiss()
        dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        dialog.setTitle(resources.getString(R.string.wait))
        if (!activity!!.isFinishing) dialog.show()
        dialog.confirmText = resources.getString(R.string.ok)
        dialog.setConfirmClickListener { dialog.dismiss() }
    }

    private fun gotoAfter() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.container, AfterSignupFragment.newInstance())
            .commitNow()
    }

    private fun getErrorEmailDialog() {
        if (activity!!.isFinishing) return
        if (dialog.isShowing) dialog.dismiss()
        dialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        dialog.contentText = resources.getString(R.string.sign_up_error)
        if (!activity!!.isFinishing)
            dialog.show()
        dialog.confirmText = resources.getString(R.string.ok)
        dialog.setConfirmClickListener { dialog.dismiss() }
    }


}
