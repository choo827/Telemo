package com.choo827.telemo


import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_bottom_nav.*
import kotlinx.android.synthetic.main.fragment_bottom_nav.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class BottomNavigationDrawerFragment : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_nav, container, false)

        view.expanded_menu.setOnClickListener {
            this.dismiss()
        }

        view.setting.setOnClickListener {

        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            // Bottom Navigation Drawer menu item clicks
            when (menuItem.itemId) {
                R.id.nav1 -> context!!.toast("1번 클릭")
                R.id.nav2 -> context!!.toast("2번 클릭")
//                R.id.app_bar_setting -> context!!.toast("setting")
//                android.R.id.home -> this.dismiss()
            }

            true
        }
    }

    // This is an extension method for easy Toast call
    fun Context.toast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM, 0, 600)
        toast.show()
    }

}
