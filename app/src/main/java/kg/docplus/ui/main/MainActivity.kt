package kg.docplus.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kg.docplus.DocPlusApp
import kg.docplus.R
import kg.docplus.model.get.ProfileGet
import kg.docplus.ui.main.home.HomeFragment
import kg.docplus.ui.main.search.FilterFragment
import kg.docplus.ui.profile.ProfileFragment
import kg.docplus.utils.ImagePickerHelper
import kotlinx.android.synthetic.main.activity_main2.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainActivity : ImagePickerHelper() {


    val pathPhoto: MutableLiveData<String> = MutableLiveData()
    var isBack = false
    lateinit var fragmentManager:FragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(3)
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
        askCameraPermission()
        replaceFragment(1)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun replaceFragment(status: Int) {
        if (!isBack) {
            var fragment: Fragment = HomeFragment()
            var tag = ""
            when (status) {
                1 -> {
                    fragment = HomeFragment()
                    tag = "home"
                }
                2 -> {
                    fragment = FilterFragment()
                    tag = "filter"
                }
                3 -> {
                    fragment = ProfileFragment()
                    tag = "profile"
                }
            }

                fragmentManager = supportFragmentManager
                val transaction = fragmentManager.beginTransaction()
                transaction.addToBackStack(tag)
                transaction.replace(R.id.frame, fragment).commit()



        }else{
            isBack = false
        }
    }

    fun selectSearch() {
        navigation.selectedItemId = R.id.navigation_search
    }

    fun onBackFromFragment(){
        onBackPressed()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        Log.e("FRAGMENT_SIZE",fragmentManager.backStackEntryCount.toString())
        if (fragmentManager.backStackEntryCount==0) finish()

        isBack = true
        val currentFragment = fragmentManager.findFragmentById(R.id.frame)

        if (currentFragment is ProfileFragment){
            navigation.selectedItemId = R.id.navigation_dashboard
        }
        if (currentFragment is FilterFragment){
            navigation.selectedItemId = R.id.navigation_search
        }
        if (currentFragment is HomeFragment){
            navigation.selectedItemId = R.id.navigation_home
        }


    }

    private fun askCameraPermission() {
        Log.e("FFFF","Dexter")
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if (report.areAllPermissionsGranted()) {
                        //once permissions are granted, launch the camera

                    } else {
                       /* Toast.makeText(
                            this@SplashActivity,
                            "All permissions need to be granted to take photo",
                            Toast.LENGTH_LONG
                        ).show()*/
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {/* ... */
                    //show alert dialog with permission options
                    AlertDialog.Builder(this@MainActivity)
                        .setTitle(
                            "Permissions Error!"
                        )
                        .setMessage(
                            "Please allow permissions to take photo with camera"
                        )
                        .setNegativeButton(
                            android.R.string.cancel
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.cancelPermissionRequest()
                        }
                        .setPositiveButton(
                            android.R.string.ok
                        ) { dialog, _ ->
                            dialog.dismiss()
                            token?.continuePermissionRequest()
                        }
                        .setOnDismissListener {
                            token?.cancelPermissionRequest()
                        }
                        .show()
                }

            }).check()

    }

    override fun setImagePath(imgpath: Uri) {

        pathPhoto.value = imgpath.path

    }

    override fun openGallery(mItemId: String) {
    }

}
