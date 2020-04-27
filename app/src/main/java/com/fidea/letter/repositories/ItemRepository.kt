package com.fidea.letter.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemRepository (val context: Context){

    private var items: ArrayList<Item>? = null
    private var favorites: ArrayList<Item>? = null
    companion object {
        private var repo: ItemRepository? = null

        fun getRepo(context: Context): ItemRepository? {
            if (repo == null) {
                return ItemRepository(context)
            }
            return repo
        }
    }


    fun getContents(): MutableLiveData<ArrayList<Item>> {
        setContents()
        val data = MutableLiveData<ArrayList<Item>>()
        data.value = items
        Log.i("TAG", "What about here? " + data.value)
        return data
    }

    private fun setContents() {
        items = ArrayList()
        val item1 = Item(
            1, "Redemption in Shaushenk", "The best movie in IMDB",
            "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg"
        )


        val item2 = Item(
            2, "Godfather", "The prefect classic movie",
            "https://m.media-amazon.com/images/M/MV5BM2MyNjYxNmUtYTAwNi00MTYxLWJmNWYtYzZlODY3ZTk3OTFlXkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SY1000_CR0,0,704,1000_AL_.jpg"
        )


        val item3 = Item(
            3, "The Godfather: Part II", "The best movie in 70th decade",
            "https://m.media-amazon.com/images/M/MV5BMWMwMGQzZTItY2JlNC00OWZiLWIyMDctNDk2ZDQ2YjRjMWQ0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SY1000_CR0,0,701,1000_AL_.jpg"
        )


        val item4 = Item(
            4, "The Dark Knight", "The big movie from big director",
            "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SY1000_CR0,0,675,1000_AL_.jpg"
        )


        val item5 = Item(
            5, "12 Angry Men", "An old film which I don't know that!",
            "https://m.media-amazon.com/images/M/MV5BMWU4N2FjNzYtNTVkNC00NzQ0LTg0MjAtYTJlMjFhNGUxZDFmXkEyXkFqcGdeQXVyNjc1NTYyMjg@._V1_SY1000_CR0,0,649,1000_AL_.jpg"
        )
        items?.add(item1)
        items?.add(item2)
        items?.add(item3)
        items?.add(item4)
        items?.add(item5)

    }


    private fun getItems() {
        val apiInterface: APIInterface =
            context.let { APIClient.getRetrofit(it) }!!.create(APIInterface::class.java)
        apiInterface.getContent()?.enqueue(object : Callback<java.util.ArrayList<Item>?> {
            override fun onResponse(
                call: Call<java.util.ArrayList<Item>?>,
                response: Response<java.util.ArrayList<Item>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    items = response.body()!!
                } else {
                    Log.i("TAG", "Error in onResponse of items " + response.code())
                }
            }

            override fun onFailure(
                call: Call<java.util.ArrayList<Item>?>,
                t: Throwable
            ) {
                Log.i(
                    "TAGd", t.message
                )
            }
        })
    }

    private fun getFavoriteItems() {
        val apiInterface: APIInterface =
            context.let { APIClient.getRetrofit(it) }!!.create(APIInterface::class.java)
        apiInterface.getFavorites()?.enqueue(object : Callback<java.util.ArrayList<Item>?> {
            override fun onResponse(
                call: Call<java.util.ArrayList<Item>?>,
                response: Response<java.util.ArrayList<Item>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    favorites = response.body()!!
                } else {
                    Log.i("TAG", "Error in onResponse of items " + response.code())
                }
            }

            override fun onFailure(
                call: Call<java.util.ArrayList<Item>?>,
                t: Throwable
            ) {
                Log.i(
                    "TAGd", t.message
                )
            }
        })
    }
}