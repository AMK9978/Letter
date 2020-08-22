package com.fidea.letter.ui.auth.login

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.fidea.letter.MainActivity
import com.fidea.letter.R
import com.fidea.letter.databinding.LoginFragmentBinding
import com.fidea.letter.models.Token
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*


class LoginFragment : Fragment() {
    private val RC_SIGN_IN: Int = 105
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding

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

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        binding.root.gotoRegister.setOnClickListener(
            Navigation.createNavigateOnClickListener(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        )
        binding.root.image_gotoRegister.setOnClickListener(
            Navigation.createNavigateOnClickListener(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        )

        binding.root.cirLoginButton.setOnClickListener {
            if (checkFields()) {
                loginViewModel.login(
                    editTextEmail.text.toString(),
                    editTextPassword.text.toString()
                )
            }
        }


        loginViewModel.token.observe(viewLifecycleOwner,
            Observer { t -> t?.let { storeToken(token = it) } })


        return binding.root
    }


    private fun signIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
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

    @SuppressLint("ApplySharedPref")
    private fun storeToken(token: Token) {
        requireContext().getSharedPreferences("pref", MODE_PRIVATE).edit()
            .putString("token", token.access)
            .commit()
        requireContext().getSharedPreferences("pref", MODE_PRIVATE).edit()
            .putString("refresh", token.refresh).commit()
    }

    private fun gotoHome() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.i("TAG", "Signed in successfully")
        } catch (e: ApiException) {
            Log.i("TAG", "ERROR IN SIGN IN WITH GOOGLE")
        }
    }
}
