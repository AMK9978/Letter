package com.fidea.letter.ui.auth

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fidea.letter.MainActivity
import com.fidea.letter.R
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Token
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    private var loginViewModel: LoginViewModel? = null

    companion object {
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //for changing status bar icon colors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        val view = inflater.inflate(R.layout.login_fragment, container, false)
        view.gotoRegister.setOnClickListener {
            onSignUpClick()
        }
        view.image_gotoRegister.setOnClickListener {
            onSignUpClick()
        }

        view.cirLoginButton.setOnClickListener {
            if (checkFields()) {
                login()
            }
        }
        return view
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

    private fun login() {
        val apiInterface: APIInterface =
            context?.let { APIClient.getRetrofit(it) }!!.create(APIInterface::class.java)
        apiInterface.login(editTextEmail.text.toString(), editTextPassword.text.toString())
            ?.enqueue(object : Callback<Token> {
                override fun onResponse(
                    call: Call<Token>,
                    response: Response<Token>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val token = response.body()!!
                        storeToken(token)
                        gotoHome()
                    } else {
                        Log.i("TAG", "Error in onResponse of Products " + response.code() +
                        " " + response.message() + " " + response.errorBody())
                    }
                }

                override fun onFailure(
                    call: Call<Token>,
                    t: Throwable
                ) {
                    Log.i(
                        "TAGd", t.message
                    )
                }
            })
    }

    @SuppressLint("ApplySharedPref")
    private fun storeToken(token: Token) {
        context!!.getSharedPreferences("pref", MODE_PRIVATE).edit().putString("token", token.access)
            .commit()
        context!!.getSharedPreferences("pref", MODE_PRIVATE).edit()
            .putString("refresh", token.refresh).commit()
    }

    private fun gotoHome() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }


    private fun onSignUpClick() {
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.container, SignUpFragment.newInstance())
            .commitNow()
    }
}
