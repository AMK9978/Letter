package com.fidea.letter

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.fidea.letter.ui.main.FavoritesFragment
import com.fidea.letter.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        setSupportActionBar(toolbar)
        toolbar.overflowIcon = resources.getDrawable(R.drawable.ic_more_vert_white_24dp)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_more, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle item selection
        return if (item.itemId == R.id.favorites) {
            favorites()
            true
        } else {
            false
        }
    }

    private fun favorites() {
//        val intent = Intent(this, FavoritesActivity::class.java )
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FavoritesFragment.newInstance())
            .commitNow()
    }


    override fun onBackPressed() {
        val f = supportFragmentManager.findFragmentById(R.id.container)
        if (f is FavoritesFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
            Log.i("TAG", "HERE WE GO AGAIN!")
        } else {
            super.onBackPressed()
//            finish()
        }
    }


}
