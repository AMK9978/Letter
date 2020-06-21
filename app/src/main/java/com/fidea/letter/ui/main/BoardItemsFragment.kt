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
import androidx.recyclerview.widget.RecyclerView
import com.fidea.letter.ItemActivity
import com.fidea.letter.R
import com.fidea.letter.RxBus
import com.fidea.letter.adapters.ItemsAdapter
import com.fidea.letter.models.Item
import com.github.ybq.android.spinkit.SpinKitView
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.main_fragment.view.*
import java.io.File

class BoardItemsFragment : Fragment() {

    private var loading: Boolean = false
    private lateinit var contentRecycler: RecyclerView
    private lateinit var spin: SpinKitView
    private var adapter: ItemsAdapter? = null
    private lateinit var itemsViewModel: ItemsViewModel
    private var arrayList = arrayListOf<Item>()
    private lateinit var disposable: Disposable

    companion object {
        fun newInstance() = BoardItemsFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        contentRecycler = view.contentRecycler
        spin = view.spin_kit
        itemsViewModel = ItemsViewModel(getToken(), getCacheDir())
        adapter =
            ItemsAdapter(
                context!!,
                arrayList
            )
        contentRecycler.adapter = adapter
        contentRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initRecycler()
        contentRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (loading) {
                        Log.i("TAG", "And now")
                        spin.visibility = View.VISIBLE
                        loading = false
                        itemsViewModel.getNewItems()
                    }
                }
            }
        })
        return view
    }

    override fun onResume() {
        super.onResume()
        disposable = RxBus.getNewItems().subscribeWith(object :
            DisposableObserver<ArrayList<Item>>() {
            override fun onComplete() {

            }

            override fun onNext(t: ArrayList<Item>) {
                adapter?.itemsList!!.addAll(t)
                adapter?.notifyDataSetChanged()
                itemsViewModel.items.value?.addAll(t)
                loading = true
                spin.visibility = View.GONE
            }

            override fun onError(e: Throwable) {
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
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
        lifecycle.addObserver(itemsViewModel)
        itemsViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            // update UI
            Log.i("TAG", "EVER?$items")
            adapter?.itemsList!!.addAll(items)
            adapter?.notifyDataSetChanged()
            spin.visibility = View.GONE
            loading = true
        })
        adapter?.getPositionClicks()?.subscribe {
            Log.i("TAG", "HERE CLICKED")
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        }
        loading = true
    }

}
