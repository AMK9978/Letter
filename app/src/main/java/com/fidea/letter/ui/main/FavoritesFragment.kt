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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fidea.letter.ItemActivity
import com.fidea.letter.R
import com.fidea.letter.adapters.ItemsAdapter
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Item
import kotlinx.android.synthetic.main.main_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class FavoritesFragment : Fragment() {
    private var favorites: ArrayList<Item>? = null
    private lateinit var dialog: SweetAlertDialog
    private var adapter: ItemsAdapter? = null
    private var homeViewModel: HomeViewModel? = null
    private lateinit var viewModel: FavoritesViewModel


    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.favorites_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FavoritesViewModel::class.java)
        // TODO: Use the ViewModel
        dialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        getFavoriteItems()
    }


    @SuppressLint("CheckResult")
    private fun initRecycler() {
        adapter = context?.let { ItemsAdapter(it, homeViewModel?.contents?.value!!) }
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

    private fun getFavoriteItems() {
        val apiInterface: APIInterface =
            context.let { it?.let { it1 -> APIClient.getRetrofit(it1) } }!!.create(APIInterface::class.java)
        apiInterface.getFavorites()?.enqueue(object : Callback<ArrayList<Item>?> {
            override fun onResponse(
                call: Call<ArrayList<Item>?>,
                response: Response<ArrayList<Item>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    favorites = response.body()!!
                    initRecycler()
                } else {
                    Log.i("TAG", "Error in onResponse of items " + response.code())
                }
            }

            override fun onFailure(
                call: Call<ArrayList<Item>?>,
                t: Throwable
            ) {
                Log.i(
                    "TAGd", t.message
                )
            }
        })
    }
}
