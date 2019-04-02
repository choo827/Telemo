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
import kotlinx.android.synthetic.main.fragment_bottom_nav.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class BottomNavigationDrawerFragment : RoundedBottomSheetDialogFragment(),
    NavigationView.OnNavigationItemSelectedListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)

        view.expanded_menu.setOnClickListener {
            this.dismiss()
        }

        view.setting.setOnClickListener {
            val gotoSetting = Intent(context, SettingActivity::class.java)
            startActivity(gotoSetting)
        }
        return view
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Bottom Navigation Drawer menu item clicks
        when (item.itemId) {
            R.id.nav1 -> switchToMainFragment()
            R.id.nav2 -> switchToStarFragment()
        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener(this)
    }

    private fun switchToMainFragment() {
        val manager = fragmentManager
        manager!!.beginTransaction().replace(R.id.content_layout, MainFragment()).commit()
    }

    private fun switchToStarFragment() {
        val manager = fragmentManager
        manager!!.beginTransaction().replace(R.id.content_layout, StarFragment()).commit()
    }
}
