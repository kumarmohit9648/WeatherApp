package com.mohit.weatherapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mohit.weatherapp.R
import com.mohit.weatherapp.databinding.ActivityMainBinding
import com.mohit.weatherapp.interfaces.IFragmentCallbacks
import com.mohit.weathersdk.util.newFragmentInstance
import dagger.android.support.DaggerAppCompatActivity

class HomeActivity : DaggerAppCompatActivity(),
    IFragmentCallbacks,
    FragmentManager.OnBackStackChangedListener {

    companion object {
        const val TAG = "HomeActivity"
        const val KEY_CURRENT_FRAGMENT_TAG = "CurrentFragment"
        const val KEY_IS_MAIN = "IsMain"
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        loadInitialFragment(savedInstanceState)
        binding.fab.setOnClickListener {
            val appInfoFragment = newFragmentInstance<AppInfoFragment>()
            showFragment(appInfoFragment, AppInfoFragment.TAG)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadInitialFragment(savedInstanceState: Bundle?) {
        lateinit var baseFragment: BaseFragment
        if (savedInstanceState == null) {
            baseFragment = newFragmentInstance<LocationsFragment>(
                KEY_IS_MAIN to true,
            )
            showMainFragment(baseFragment, LocationsFragment.TAG)
        } else {
            val currentFragmentTag = savedInstanceState.getString(KEY_CURRENT_FRAGMENT_TAG)
            baseFragment = supportFragmentManager
                .findFragmentByTag(currentFragmentTag) as BaseFragment
            val bundle: Bundle? = baseFragment.arguments
            if (bundle?.getBoolean(KEY_IS_MAIN, false) == true) {
                showMainFragment(baseFragment, baseFragment.tag)
            } else {
                showFragment(baseFragment, baseFragment.tag)
            }
        }
    }

    override fun updateTitle(title: String) {
        binding.toolbar.title = title
    }

    override fun showMainFragment(fragment: Fragment, tag: String?) {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStackImmediate(first.name, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        showFragment(fragment, tag)
    }

    /**
     * Function to display the fragment without replacing all the other
     * fragments from the back stack.
     *
     * @param fragment The fragment to display.
     * @param tag      The tag of the fragment to display.
     */
    override fun showFragment(fragment: Fragment, tag: String?) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        fragmentManager.addOnBackStackChangedListener(this)
        transaction.setCustomAnimations(R.anim.fragment_load_fade_in, R.anim.fragment_load_fade_out)
        transaction.replace(R.id.rootContainer, fragment, tag)
        fragmentManager.executePendingTransactions()
        if (fragmentManager.findFragmentByTag(fragment.id.toString()) == null) {
            transaction.addToBackStack(tag)
        }
        transaction.commitAllowingStateLoss()
    }

    /**
     * Function to display the fragment without replacing all the other
     * fragments from the back stack.
     *
     * @param fragment The fragment to display.
     * @param tag      The tag of the fragment to display.
     */
    override fun showFragmentWithAnimation(
        fragment: Fragment,
        tag: String?,
        enterAnim: Int,
        exitAnim: Int,
        popEnterAnim: Int,
        popExitAnim: Int
    ) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        fragmentManager.addOnBackStackChangedListener(this)
        transaction.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        transaction.replace(R.id.rootContainer, fragment, tag)
        fragmentManager.executePendingTransactions()
        if (fragmentManager.findFragmentByTag(fragment.id.toString()) == null) {
            transaction.addToBackStack(tag)
        }
        transaction.commitAllowingStateLoss()
    }

    override fun showHamIcon() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun showBackArrow() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackStackChanged() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount > 1) {
            showBackArrow()
        } else {
            showHamIcon()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            popBackStackContent()
        } else {
            val fragment = getCurrentFragment()
            if (fragment is LocationsFragment) {
                // If fragment is instance of LocationsFragment then on back press
                // we have to finish the app because
                // there are no other screens left to display.
                finish()
            } else {
                //If the fragment is not an instance of LocationsFragment then on back press
                // we have to display Main Screen.
                showMainFragment(LocationsFragment(), LocationsFragment.TAG)
            }
        }
    }

    private fun getCurrentFragment(): Fragment? {
        val manager = supportFragmentManager
        val currentFragment = manager.getBackStackEntryAt(manager.backStackEntryCount - 1)
        return manager.findFragmentByTag(currentFragment.name)
    }

    private fun popBackStackContent() {
        val fragmentManager = supportFragmentManager
        getCurrentFragment()?.let {
            fragmentManager.popBackStackImmediate(it.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}