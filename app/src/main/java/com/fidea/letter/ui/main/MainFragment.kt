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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var adapter: ItemsAdapter? = null
    private var mainViewModel: MainViewModel? = null
    private lateinit var dialog: SweetAlertDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
        dialog = SweetAlertDialog(activity, WARNING_TYPE)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel!!.init()
        mainViewModel?.contents?.observe(viewLifecycleOwner, Observer {
            adapter?.notifyDataSetChanged()
        })
        initRecycler()
    }

    @SuppressLint("CheckResult")
    private fun initRecycler() {
        adapter = context?.let { ItemsAdapter(it, mainViewModel?.contents?.value!!) }
        contentRecycler.adapter = adapter
        contentRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter?.getPositionClicks()?.subscribe {
            Log.i("TAG", "HERE CLICKED")
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("item", it)
            startActivity(intent)
        }
    }

    private fun getItems() {
        val apiInterface: APIInterface =
            context?.let { APIClient.getRetrofit(it) }!!.create(APIInterface::class.java)
        getWaitingDialog()
        apiInterface.getContent()?.enqueue(object : Callback<ArrayList<Item>?> {
            override fun onResponse(
                call: Call<ArrayList<Item>?>,
                response: Response<ArrayList<Item>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    dialog.dismiss()
//                    items = response.body()!!
                } else {
                    getErrorDialog()
                    Log.i("TAG", "Error in onResponse of Products " + response.code())
                }
            }

            override fun onFailure(
                call: Call<ArrayList<Item>?>,
                t: Throwable
            ) {
                Log.i(
                    "TAGd", t.message
                )
                getErrorDialog()
            }
        })
    }


    private fun getErrorDialog() {
        if (dialog.isShowing) dialog.dismiss()
        if (activity!!.isFinishing) return
        dialog = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        dialog.setTitle(resources.getString(R.string.error))
        dialog.contentText = resources.getString(R.string.general_error)
        if (!activity!!.isFinishing) dialog.show()
        dialog.confirmText = resources.getString(R.string.ok)
        dialog.setConfirmClickListener { dialog.dismiss() }
    }

    private fun getWaitingDialog() {
        if (activity!!.isFinishing) return
        if (dialog.isShowing) dialog.dismiss()
        dialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        dialog.setTitle(resources.getString(R.string.wait))
        if (!activity!!.isFinishing) dialog.show()
        dialog.confirmText = resources.getString(R.string.ok)
        dialog.setConfirmClickListener { dialog.dismiss() }
    }

}
