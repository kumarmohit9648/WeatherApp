package com.mohit.weatherapp.interfaces

import androidx.fragment.app.Fragment

interface IFragmentCallbacks {
    fun updateTitle(title: String)
    fun showHamIcon()
    fun showBackArrow()
    fun showMainFragment(fragment: Fragment, tag: String?)
    fun showFragment(fragment: Fragment, tag: String?)
    fun showFragmentWithAnimation(
        fragment: Fragment,
        tag: String?,
        enterAnim: Int,
        exitAnim: Int,
        popEnterAnim: Int,
        popExitAnim: Int
    )
}