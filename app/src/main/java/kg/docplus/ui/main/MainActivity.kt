package kg.docplus.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.ui.main.home.HomeFragment
import kg.docplus.ui.main.search.FilterFragment
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search -> {
                replaceFragment(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onResume() {
        super.onResume()
        DocPlusApp.activity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        replaceFragment(1)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun replaceFragment(status: Int) {
        var fragment: Fragment = HomeFragment()
        var tag=""
        when (status) {
            1 -> {
                fragment = HomeFragment()
                tag="home"
            }
            2 -> {
                fragment = FilterFragment()
                tag = "filter"
            }
        }

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(tag)
        transaction.replace(R.id.frame, fragment).commit()

    }

    fun selectSearch() {
        navigation.selectedItemId = R.id.navigation_search
    }

    fun onBackFromFragment(){
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
