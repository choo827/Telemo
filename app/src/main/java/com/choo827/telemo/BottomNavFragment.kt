package com.choo827.telemo


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_bottom_nav.*


/**
 * A simple [Fragment] subclass.
 *
 */
class BottomNavigationDrawerFragment : RoundedBottomSheetDialogFragment(),
    NavigationView.OnNavigationItemSelectedListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)

        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Bottom Navigation Drawer menu item clicks
        when (item.itemId) {
            R.id.nav1 -> {
                val gotoSetting = Intent(context, SettingActivity::class.java)
                startActivity(gotoSetting)
            }
        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener(this)
    }
}
