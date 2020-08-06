package com.fidea.letter.ui.auth.after_signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fidea.letter.R
import com.fidea.letter.ui.auth.login.LoginFragment
import kotlinx.android.synthetic.main.after_sign_up_fragment.view.*


class AfterSignupFragment : Fragment() {


    companion object {
        fun newInstance() =
            AfterSignupFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.after_sign_up_fragment, container, false)
        val confirmed = view.confirmed
        confirmed.setOnClickListener {
            onLoginClick()
            onDestroy()
        }

        return view
    }


    private fun onLoginClick() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance())
            .commitNow()
    }


}
