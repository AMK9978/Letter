package com.fidea.letter.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepo {
    private var token : MutableLiveData<Token> = MutableLiveData()

    init {

    }

    fun login(email: String, password: String) {
        val apiInterface: APIInterface =
            APIClient.getRetrofit().create(APIInterface::class.java)
        apiInterface.login(email, password)
            ?.enqueue(object : Callback<Token> {
                override fun onResponse(
                    call: Call<Token>,
                    response: Response<Token>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        token.value = response.body()!!
                    } else {
                        Log.i(
                            "TAG", "Error in onResponse of login " + response.code() +
                                    " " + response.message() + " " + response.errorBody()
                        )
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

    fun gotoHome() {

    }

    fun storeToken(token: Token) {
        // TODO: Via Util, store tokens
    }

}