package com.fidea.letter.repositories

import android.util.Log
import com.fidea.letter.util.RxBus
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Board
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Singleton

@Singleton
class BoardRepository {

    companion object {
        private var repo: BoardRepository? = null

        fun getRepo(): BoardRepository? {
            if (repo == null) {
                return BoardRepository()
            }
            return repo
        }
    }


    fun getContents(token: String?, cacheDir: File) {
        val apiInterface = APIClient.getRetrofit(token, cacheDir)!!.create(APIInterface::class.java)
        apiInterface.getBoards().enqueue(object :
            Callback<java.util.ArrayList<Board>?> {
            override fun onFailure(call: Call<java.util.ArrayList<Board>?>, t: Throwable) {
                Log.i("TAG", "Error in getting items " + t.message)
            }

            override fun onResponse(
                call: Call<java.util.ArrayList<Board>?>,
                response: Response<java.util.ArrayList<Board>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    RxBus.getNewBoards().onNext(response.body()!!)
                } else {
                    Log.i("TAG", "Errtttor in getting items ${response.code()}")
                }
            }

        })
    }

}