package kg.docplus.ui.main

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.quickblox.core.helper.Utils
import kg.docplus.App
import kg.docplus.R
import kg.docplus.injection.ViewModelFactory
import kg.docplus.qbwrtc.activities.PermissionsActivity
import kg.docplus.qbwrtc.utils.Consts
import kg.docplus.qbwrtc.utils.WebRtcSessionManager
import kg.docplus.ui.dialogs.TimeChooseDialog
import kg.docplus.ui.main.home.HomeFragment
import kg.docplus.ui.main.search.FilterFragment
import kg.docplus.ui.main.settings.SettingsFragment
import kg.docplus.ui.my_doctor.DoctorActivity
import kg.docplus.ui.profile.ProfileFragment
import kg.docplus.utils.ImagePickerHelper
import kg.docplus.utils.extension.isTimeDoctor
import kotlinx.android.synthetic.main.activity_main2.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ImagePickerHelper() {

    private var webRtcSessionManager: WebRtcSessionManager? = null
    private lateinit var viewModel: MainViewModel

    val pathPhoto: MutableLiveData<String> = MutableLiveData()
    var isBack = false
    lateinit var fragmentManager: FragmentManager
    var choosenService = -1

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
                replaceFragment(4)
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
        App.activity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        askCameraPermission()
        PermissionsActivity.startActivity(this, false, *Consts.PERMISSIONS)
        webRtcSessionManager = WebRtcSessionManager.getInstance(applicationContext)
        replaceFragment(1)

        //startActivity(Intent(this,CreateRatingActivity::class.java))

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(MainViewModel::class.java)
        viewModel.getDoctorFavourite()
        viewModel.myDoctor.observe(this,androidx.lifecycle.Observer {
            for (i in 0..5){
                var myDoctor = it[i]
                if (isTimeDoctor(myDoctor.date+" "+myDoctor.exact_time)){
                    startActivity(Intent(this,DoctorActivity::class.java).putExtra("service_id",myDoctor.id))
                    break
                }
            }

        })
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        Log.e("DEVICE_ID",getCurrentDeviceId())
        TimeChooseDialog(this).setUp()
        //chronoMeter()
    }

   /* private fun chronoMeter(){
        timer_chronometer_action_bar.setVisibility(View.VISIBLE)
        setBase()
        timer_chronometer_action_bar.start()
        timer_chronometer_action_bar.setOnChronometerTickListener {
            Log.e("TSGG",timer_chronometer_action_bar.text.toString())
            if (timer_chronometer_action_bar.text.toString() > "10:00")timer_chronometer_action_bar.setTextColor(Color.RED) }
    }*/

    private fun setBase(){
        val simpleDateFormat = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
        try
        {
            val startDateTime = simpleDateFormat.parse("03-07-2019 14:35:00")

//            val currentDateTime = simpleDateFormat.parse("03-07-2019 13:45:00")
            val currentDateTime = Date()


            //milliseconds
            var different = currentDateTime.time - startDateTime.time
            val secondsInMilli:Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different %= daysInMilli
            val elapsedHours = different / hoursInMilli
            different %= hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different %= minutesInMilli
            val elapsedSeconds = different / secondsInMilli

            val elapsedTimeMilliseconds = ((elapsedHours * 60 * 60 * 1000
                    + elapsedMinutes * 60 * 1000
                    + elapsedSeconds * 1000)).toInt()
//            timer_chronometer_action_bar.setBase(SystemClock.elapsedRealtime() - elapsedTimeMilliseconds)
        }
        catch (e:Exception) {
//            Utils.printLog(e.toString())
            e.printStackTrace()
        }
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
                    var bundle = Bundle()
                    bundle.putInt("service",choosenService)
                    Log.e("BUNDLE",bundle.toString())
                    fragment = FilterFragment()
                    fragment.arguments = bundle
                    choosenService = -1
                    tag = "filter"
                }
                3 -> {
                    fragment = ProfileFragment()
                    tag = "profile"
                }

                4 -> {
                    fragment = SettingsFragment()
                    tag = "settings"
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

    fun selectSearch(service:Int) {
        choosenService = service
        navigation.selectedItemId = R.id.navigation_search
    }

    fun onBackFromFragment(){
        onBackPressed()
    }

    override fun onBackPressed() {
        val currentFragment1 = fragmentManager.findFragmentById(R.id.frame)
        if (currentFragment1 is FilterFragment) {
            val lateStatus = (currentFragment1 as FilterFragment).lateStatus

            if (lateStatus==1){
                super.onBackPressed()

                replaceBottomNavigation()
            }else{
                (currentFragment1 as FilterFragment).onBack()
            }

        }else {
            super.onBackPressed()

            replaceBottomNavigation()
        }

    }

    fun replaceBottomNavigation(){
        Log.e("FRAGMENT_SIZE", fragmentManager.backStackEntryCount.toString())
        if (fragmentManager.backStackEntryCount == 0) finish()

        isBack = true
        val currentFragment = fragmentManager.findFragmentById(R.id.frame)


        if (currentFragment is ProfileFragment) {
            navigation.selectedItemId = R.id.navigation_dashboard
        }
        if (currentFragment is FilterFragment) {
            navigation.selectedItemId = R.id.navigation_search
        }
        if (currentFragment is HomeFragment) {
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


    private fun getCurrentDeviceId(): String {
        return Utils.generateDeviceId(this)
    }


}
