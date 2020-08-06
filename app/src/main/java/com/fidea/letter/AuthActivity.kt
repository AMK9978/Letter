package com.fidea.letter

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import java.io.File


class AuthActivity : AppCompatActivity() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        val token = getSharedPreferences("pref", Context.MODE_PRIVATE).getString("token", "nothing")
        if (token != "nothing") {
            Log.i("TAG", token)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                fill()
            }
        } else {
            fill()
        }

    }

    private fun fill() {
        val file = File(Environment.getExternalStorageDirectory(), "letter")
        if (!file.exists()) {
            Log.i("TAG", "Not exists:" + file.mkdirs())
        }else{
            Log.i("TAG", "exists:" + file.absolutePath)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        fill()
    }

}
