package com.fidea.letter.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fidea.letter.util.RxBus
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Singleton

@Singleton
class SearchRepository {

    companion object {
        private var repo: SearchRepository? = null

        fun getRepo(): SearchRepository? {
            if (repo == null) {
                return SearchRepository()
            }
            return repo
        }
    }


    fun getContents(token: String?, cacheDir: File) {
        val apiInterface = APIClient.getRetrofit(token, cacheDir)!!.create(APIInterface::class.java)
        apiInterface.search()!!.enqueue(object :
            Callback<java.util.ArrayList<Item>?> {
            override fun onFailure(call: Call<java.util.ArrayList<Item>?>, t: Throwable) {
                Log.i("TAG", "Error in getting items " + t.message)
            }

            override fun onResponse(
                call: Call<java.util.ArrayList<Item>?>,
                response: Response<java.util.ArrayList<Item>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    RxBus.getNewItems().onNext(response.body()!!)
                } else {
                    Log.i("TAG", "Errtttor in getting items ${response.code()}")
                }
            }

        })
    }

    fun getFavorites(token: String?, cacheDir: File): MutableLiveData<ArrayList<Item>> {
        val data = MutableLiveData<ArrayList<Item>>()
        val apiInterface = APIClient.getRetrofit(token, cacheDir)!!.create(APIInterface::class.java)
        apiInterface.getFavorites()!!.enqueue(object :
            Callback<java.util.ArrayList<Item>?> {
            override fun onFailure(call: Call<java.util.ArrayList<Item>?>, t: Throwable) {
                Log.i("TAG", "Error in getting favorites")
            }

            override fun onResponse(
                call: Call<java.util.ArrayList<Item>?>,
                response: Response<java.util.ArrayList<Item>?>
            ) {
                if (response.isSuccessful) {
                    data.value = response.body()
                    Log.i("TAG", "HERE:" + response.body())
                } else {
                    Log.i("TAG", "Error in getting favorites ${response.code()}")
                }
            }

        })

        Log.i("TAG", "What about here? " + data.value)
        return data
    }

}