package com.fidea.letter.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Item
import com.fidea.letter.repositories.ItemRepository

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var contents : MutableLiveData<ArrayList<Item>>? = null
    var repo : ItemRepository? = null

    fun init(){
        if (repo != null) {
            return
        }
        repo = ItemRepository.getRepo()
        contents = repo?.getContents()
        Log.i("TAG", "Contents:${contents?.value.toString()}")
    }
}
