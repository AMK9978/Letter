package com.fidea.letter.ui.main.home

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
import com.fidea.letter.ui.main.item.ItemActivity
import com.fidea.letter.R
import com.fidea.letter.util.RxBus
import com.fidea.letter.adapters.ItemsAdapter
import com.fidea.letter.models.Item
import com.github.ybq.android.spinkit.SpinKitView
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.home_fragment.view.*
import java.io.File

class HomeFragment : Fragment() {

    private var loading: Boolean = false
    private lateinit var contentRecycler: RecyclerView
    private lateinit var spin: SpinKitView
    private var adapter: ItemsAdapter? = null
    private lateinit var homeViewModel: HomeViewModel
    private var arrayList = arrayListOf<Item>()
    private lateinit var disposable: Disposable

    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        contentRecycler = view.contentRecycler
        spin = view.spin_kit
        homeViewModel =
            HomeViewModel(
                getToken(),
                getCacheDir()
            )
        adapter =
            ItemsAdapter(
                requireContext(),
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
                        homeViewModel.getNewItems()
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
                homeViewModel.items.value?.addAll(t)
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
        return requireContext().getSharedPreferences("pref", Context.MODE_PRIVATE)
            .getString("token", "").toString()
    }

    private fun getCacheDir(): File {
        return requireContext().cacheDir
    }

    @SuppressLint("CheckResult")
    private fun initRecycler() {
        lifecycle.addObserver(homeViewModel)
        homeViewModel.items.observe(viewLifecycleOwner, Observer { items ->
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
