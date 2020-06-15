package com.fidea.letter.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fidea.letter.ItemActivity
import com.fidea.letter.R
import com.fidea.letter.adapters.ItemsAdapter
import com.fidea.letter.models.Item
import kotlinx.android.synthetic.main.favorites_fragment.*
import java.io.File

class FavoritesFragment : Fragment() {

    private var adapter: ItemsAdapter? = null
    private lateinit var itemsViewModel: ItemsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.favorites_fragment, container, false)
        itemsViewModel = ItemsViewModel(getToken(), getCacheDir())
        initRecycler()
        return view
    }

    private fun getToken(): String {
        return context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
            .getString("token", "").toString()
    }

    private fun getCacheDir(): File {
        return context!!.cacheDir
    }

    @SuppressLint("CheckResult")
    private fun initRecycler() {
        itemsViewModel.favorites.observe(viewLifecycleOwner, Observer { items ->
            if (adapter == null) {
                adapter =
                    ItemsAdapter(
                        context!!,
                        items
                    )
                favoritesRecycler.adapter = adapter
                favoritesRecycler.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter?.getPositionClicks()?.subscribe {
                    Log.i("TAG", "HERE CLICKED")
                    val intent = Intent(context, ItemActivity::class.java)
                    intent.putExtra("item", it)
                    startActivity(intent)
                }
            } else {
                adapter?.notifyDataSetChanged()
            }
        })
    }
}
