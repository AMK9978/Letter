package com.fidea.letter

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.fidea.letter.databinding.MainActivityBinding
import com.fidea.letter.ui.main.boards.BoardFragment
import com.fidea.letter.ui.main.favorites.FavoritesFragment
import com.fidea.letter.ui.main.home.HomeFragment
import com.fidea.letter.ui.main.personal.PersonalFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
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
                    fragment =
                        FavoritesFragment()
                    supportFragmentManager.popBackStack()
                    loadFragment(fragment)
                }
                R.id.boards -> {
                    fragment = BoardFragment()
                    supportFragmentManager.popBackStack()
                    loadFragment(fragment)
                }
                R.id.personal -> {
                    fragment =
                        PersonalFragment()
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
