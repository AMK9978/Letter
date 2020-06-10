package com.fidea.letter

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fidea.letter.ui.main.BoardFragment
import com.fidea.letter.ui.main.FavoritesFragment
import com.fidea.letter.ui.main.HomeFragment
import com.fidea.letter.ui.main.PersonalFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, HomeFragment.newInstance())
                .commitNow()
        }
        bottomNavigation.setBackgroundColor(resources.getColor(R.color.white))
        bottomNavigation.setOnNavigationItemSelectedListener { item: MenuItem ->
            val fragment: Fragment
            when (item.itemId) {
                R.id.home -> {
                    fragment = HomeFragment()
                    supportFragmentManager.popBackStack()
                    loadFragment(fragment)
                }
                R.id.library -> {
                    fragment = FavoritesFragment()
                    supportFragmentManager.popBackStack()
                    loadFragment(fragment)
                }
                R.id.boards -> {
                    fragment = BoardFragment()
                    supportFragmentManager.popBackStack()
                    loadFragment(fragment)
                }
                R.id.personal -> {
                    fragment = PersonalFragment()
                    supportFragmentManager.popBackStack()
                    loadFragment(fragment)
                }
            }
            true
        }

    }

    private fun loadFragment(fragment: Fragment?) {
        val fragmentTransaction =
            supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment!!)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}
