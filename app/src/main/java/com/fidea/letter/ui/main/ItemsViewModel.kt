package com.fidea.letter.ui.main

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fidea.letter.models.Item
import com.fidea.letter.repositories.ItemRepository
import java.io.File
import javax.inject.Inject

class ItemsViewModel @Inject constructor(
    private val token: String?,
    private val cacheDir: File
) : ViewModel(), LifecycleObserver {

    private var page = 0
    var items: MutableLiveData<ArrayList<Item>> = MutableLiveData()

    init {
        ItemRepository.getRepo()!!.getContents(token, cacheDir, page++)
    }

    val favorites: MutableLiveData<ArrayList<Item>> = MutableLiveData<ArrayList<Item>>()

    fun getNewItems() {
        ItemRepository.getRepo()!!.getContents(token, cacheDir, page++)
    }

    fun getFavorites() {
        favorites.value?.addAll(ItemRepository.getRepo()!!.getFavorites(token, cacheDir).value!!)
    }
}
