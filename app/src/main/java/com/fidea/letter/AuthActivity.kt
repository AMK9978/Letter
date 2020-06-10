package com.fidea.letter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fidea.letter.ui.auth.LoginFragment

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
        val token = getSharedPreferences("pref", Context.MODE_PRIVATE).getString("token","nothing")
        if (token != "nothing"){
            Log.i("TAG", token)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance())
                .commitNow()
        }
    }

}
