package com.fidea.letter.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fidea.letter.MainActivity
import com.fidea.letter.R
import com.fidea.letter.Util
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.api.Temp
import kotlinx.android.synthetic.main.sign_up_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignUpFragment : Fragment() {
    private var loginViewModel: LoginViewModel? = null
    private lateinit var dialog: SweetAlertDialog

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

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
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
        val apiInterface: APIInterface =
            context?.let {
                APIClient.getRetrofit(
                    Util.getToken(context!!),
                    Util.getCacheDir(context!!)
                )
            }!!.create(APIInterface::class.java)
        apiInterface.signUp(
            editTextName.text.toString(), editTextPassword.text.toString(),
            editTextEmail.text.toString()
        )
            ?.enqueue(object : Callback<Temp> {
                override fun onResponse(
                    call: Call<Temp>,
                    response: Response<Temp>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        response.body()!!
                        getConfirmEmailDialog()
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

    private fun getConfirmEmailDialog() {
        if (activity!!.isFinishing) return
        if (dialog.isShowing) dialog.dismiss()
        dialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        dialog.contentText = resources.getString(R.string.confirm_email)
        if (!activity!!.isFinishing)
            dialog.show()
        dialog.setCancelable(false)
        dialog.confirmText = resources.getString(R.string.ok)
        dialog.setConfirmClickListener { dialog.dismiss() }
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
