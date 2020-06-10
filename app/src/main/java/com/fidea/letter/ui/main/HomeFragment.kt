package com.fidea.letter.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog.WARNING_TYPE
import com.fidea.letter.ItemActivity
import com.fidea.letter.R
import com.fidea.letter.adapters.ItemsAdapter
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Item
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var loading: Boolean = false
    private var items: ArrayList<Item>? = null
    private var adapter: ItemsAdapter? = null
    private var homeViewModel: HomeViewModel? = null
    private lateinit var dialog: SweetAlertDialog

    private var pastY = 0


    companion object {
        fun newInstance() = HomeFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        val contentRecycler = view.contentRecycler
        contentRecycler.viewTreeObserver.addOnScrollChangedListener {
            if (contentRecycler.childCount > 0 && loading) {
                Log.i("TAG", "Is true")
                if (contentRecycler.getChildAt(0).bottom <= (contentRecycler.height +
                            contentRecycler.scrollY)
                ) {
                    Log.i("TAG", "And now")
                    loading = false
                    loadItems()
                }
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        dialog = SweetAlertDialog(activity, WARNING_TYPE)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        getItems()
    }

    @SuppressLint("CheckResult")
    private fun initRecycler() {
        adapter = ItemsAdapter(context!!, homeViewModel?.contents?.value!!)
        contentRecycler.adapter = adapter
        contentRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter?.getPositionClicks()?.subscribe {
            Log.i("TAG", "HERE CLICKED")
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        }
        homeViewModel?.contents?.observe(viewLifecycleOwner, Observer {
            adapter?.notifyDataSetChanged()
        })
    }

    private fun getItems() {
        val apiInterface: APIInterface =
            context.let { it?.let { it1 -> APIClient.getRetrofit(it1) } }!!.create(APIInterface::class.java)
        apiInterface.getContent(homeViewModel!!.page++)
            ?.enqueue(object : Callback<java.util.ArrayList<Item>?> {
                override fun onResponse(
                    call: Call<java.util.ArrayList<Item>?>,
                    response: Response<java.util.ArrayList<Item>?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        homeViewModel?.contents?.value = response.body()
                        Log.i("TAG", response.body().toString())
                        initRecycler()
                        loading = true
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

    private fun loadItems() {
        val apiInterface: APIInterface =
            context.let { it?.let { it1 -> APIClient.getRetrofit(it1) } }!!.create(APIInterface::class.java)
        apiInterface.getContent(homeViewModel!!.page++)
            ?.enqueue(object : Callback<java.util.ArrayList<Item>?> {
                override fun onResponse(
                    call: Call<java.util.ArrayList<Item>?>,
                    response: Response<java.util.ArrayList<Item>?>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        homeViewModel?.contents?.value?.addAll(response.body()!!)
                        Log.i("TAG", "HERE " + response.body()!!.size)
                        adapter?.notifyDataSetChanged()
                        loading = true
                    } else {
                        Log.i("TAG", "Error in onResponse of items here " + response.code())
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
