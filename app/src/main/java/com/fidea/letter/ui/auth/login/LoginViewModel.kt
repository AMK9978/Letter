package com.fidea.letter.ui.auth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Token
import com.fidea.letter.repositories.AuthRepo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.login_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val token: MutableLiveData<Token> =  MutableLiveData()
    val authRepo: AuthRepo = AuthRepo()

    init {
        Log.d("TAG", "LoginViewModel is under construction!")
    }

    fun login(email:String, password: String){
        authRepo.login(email, password)
    }





}
