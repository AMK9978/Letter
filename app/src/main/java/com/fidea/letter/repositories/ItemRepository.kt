package com.fidea.letter.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fidea.letter.models.Item

class ItemRepository {

    private var items: ArrayList<Item>? = null
    private var favorites: ArrayList<Item>? = null

    companion object {
        private var repo: ItemRepository? = null

        fun getRepo(): ItemRepository? {
            if (repo == null) {
                return ItemRepository()
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

    }


}