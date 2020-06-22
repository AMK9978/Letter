package com.fidea.letter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.fidea.letter.api.APIClient
import com.fidea.letter.api.APIInterface
import com.fidea.letter.models.Item
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ItemActivity : AppCompatActivity() {

    private lateinit var dialog: SweetAlertDialog
    private lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        if (intent.extras != null && intent.extras?.get("item") != null) {
            item = intent.extras?.get("item") as Item
            setSupportActionBar(toolbar)
            Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
            movieTitle.text = item.title
            titleTxt.text = item.title
            description.text = item.description
            Log.i("TAG", movieTitle.text.toString() + " " + description.text.toString())
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_white_24dp)
            toolbar.setNavigationOnClickListener { onBackPressed() }
            dialog = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            Glide.with(this).load(item.imageUrl).placeholder(R.drawable.placeholder).into(app_bar_image)
        }

        like.setOnClickListener {
            like()
        }
        seen.setOnClickListener {
            seen()
        }

    }


    private fun like() {
        val view = findViewById<View>(android.R.id.content)
        val apiInterface: APIInterface =
            APIClient.getRetrofit(Util.getToken(this), Util.getCacheDir(this))!!
                .create(APIInterface::class.java)
        apiInterface.like(item.id)?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    dialog.dismiss()
                    Snackbar.make(view, "با موفقیت به علاقمندی هایتان افزوده شد", Snackbar.LENGTH_LONG)
                        .setAction("برگشت") {

                        }.setActionTextColor(resources.getColor(R.color.colorAccent)).show()
//                    items = response.body()!!
                } else {
                    Log.i("TAG", "Error in onResponse of Products " + response.code())
                }
            }

            override fun onFailure(
                call: Call<String?>,
                t: Throwable
            ) {
                Log.i(
                    "TAGd", t.message
                )
            }
        })
    }

    private fun seen() {
        val apiInterface: APIInterface =
            APIClient.getRetrofit(Util.getToken(this), Util.getCacheDir(this))!!
                .create(APIInterface::class.java)
        apiInterface.setAsSeen(item.id)?.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    dialog.dismiss()
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "با موفقیت به مشاهده شده هایتان افزوده شد", Snackbar.LENGTH_LONG
                    ).show()
//                    items = response.body()!!
                } else {
                    Log.i("TAG", "Error in onResponse of Products " + response.code())
                }
            }

            override fun onFailure(
                call: Call<String?>,
                t: Throwable
            ) {
                Log.i(
                    "TAGd", t.message
                )
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}
