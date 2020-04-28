package kg.docplus.ui.profile

import android.app.AlertDialog
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.docplus.R
import kg.docplus.base.BaseViewModel
import kg.docplus.model.get.ProfileGet
import kg.docplus.network.PostApi
import kg.docplus.ui.main.MainActivity
import javax.inject.Inject
import android.content.DialogInterface
import android.app.DatePickerDialog
import kg.docplus.utils.extension.dateToPostFormat
import java.util.*
import android.app.Dialog
import android.content.Intent
import android.view.Window
import android.widget.*
import androidx.lifecycle.MutableLiveData
import kg.docplus.App
import kg.docplus.model.get.PatientDetail
import kg.docplus.model.post.ProfilePost
import kg.docplus.ui.favorite_doctor.FavouriteActivity
import kg.docplus.ui.my_doctor.DoctorActivity
import kg.docplus.utils.UserToken
import kg.docplus.utils.extension.toast
import kg.docplus.utils.extension.validate
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ProfileViewModel : BaseViewModel(), DatePickerDialog.OnDateSetListener {


    @Inject
    lateinit var postApi: PostApi
    private var subscription: CompositeDisposable = CompositeDisposable()
    val profile: MutableLiveData<ProfileGet> = MutableLiveData()
    val avatar: MutableLiveData<String> = MutableLiveData()
    lateinit var profileGet: ProfileGet
    lateinit var birthDate: TextView
    var path: String? = null

    override fun onCleared() {
        super.onCleared()
        subscription = CompositeDisposable()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var calendar = GregorianCalendar()
        calendar.set(year, month, dayOfMonth)
        var date: Date = calendar.time

        birthDate.text = dateToPostFormat(date)
    }

    fun getProfile() {

        subscription.add(
            postApi.getProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            profileGet = result.body()!!
                            profile.value = profileGet
                            try {
                                Log.e("PRRRRR", profileGet.toString())
                                profileGet.patient_detail.gender = getGenderForId(profileGet.patient_detail.gender!!)
                                path = profileGet.patient_detail.avatar?.file.toString()
                                if (!path.isNullOrEmpty()) {
                                    avatar.value = path
                                }
                            }catch (e:Exception){

                            }
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error23456789", error)

                        }

                    },
                    {
                        hideProgress()

                        Log.e("DDddsfD2121", it.toString())
                    }

                )
        )

    }

    fun onClickSex(sex: TextView) {
        Log.e("DDD", "sdfdssex")
        val mCatsName = arrayOf("Мужской", "Женский")

        var builder = AlertDialog.Builder(App.activity!!)
        builder.setTitle("Выберите пол") // заголовок для диалога

        builder.setItems(mCatsName, DialogInterface.OnClickListener { dialog, item ->
            sex.text = mCatsName[item]
        }).show()

    }

    fun onClickBirthDate(birthDate: TextView) {
        this.birthDate = birthDate
        val dialog = DatePickerDialog(App.activity!!, this, 1990, 1, 1)
        dialog.datePicker.touchables[0].performClick()
        dialog.show()

    }

    fun onClickName(nameMain: TextView) {

        val dialog = Dialog(App.activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_name)

        val name = dialog.findViewById(R.id.name) as EditText
        val surname = dialog.findViewById(R.id.surname) as EditText
        val forename = dialog.findViewById(R.id.forename) as EditText
        val btnOk = dialog.findViewById(R.id.btn_ok) as Button
        val cancel = dialog.findViewById(R.id.cancel) as ImageView

        btnOk.setOnClickListener {
            var bool = true
            if (!name.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым")) bool = false
            if (!surname.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым")) bool = false
//            if (!forename.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым")) bool = false

            if (bool) {
                val s = surname.text.toString() + "\n" + name.text.toString() + " " + forename.text.toString()
                nameMain.text = s
                dialog.cancel()
            }

        }

        cancel.setOnClickListener { dialog.cancel() }

        dialog.show()


    }


    fun onClickAvatar() {
        var activity: MainActivity = (App.activity as MainActivity?)!!
        activity.showPickImageDialog()
    }

    fun postImage(path: String) {
        var file = File(path)
        val requestBody = RequestBody.create(MediaType.parse("multipart/data"), file)
        val multiPartBody = MultipartBody.Part
            .createFormData("file", file.name, requestBody)

        subscription.add(
            postApi.postImage(multiPartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("IMAGEDS", result.body()!!.file)
                            this.path = result.body()!!.file
                            avatar.value = this.path
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("ErrorGandon", error)

                        }

                    },
                    {
                        hideProgress()

                        Log.e("DDD", it.toString())
                    }

                )
        )

    }

    fun onClickSave(name: TextView, sex: TextView, birth: TextView) {
        var bool = true
        if (!name.validate({ s -> s.length > 3 }, "Поле не может быть пустым")) bool = false
        if (!sex.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым")) bool = false
        if (!birth.validate({ s -> s.isNotEmpty() }, "Поле не может быть пустым")) bool = false

        if (path.isNullOrEmpty()) {
            App.activity!!.toast("Выберите Фото")
            bool = false
        }

        Log.e("Name", name.text.toString())

        if (bool) {
            var textName = name.text.toString()
            val lastName = textName.split("\n")[0]
            textName = textName.split("\n")[1]
            val firstName = textName.split(" ")[0]
            val midName = textName.split(" ")[1]


            val patientDetail = PatientDetail(path, firstName, midName, lastName,getGender(sex.text.toString()),birth.text.toString())

            Log.e("Profile",patientDetail.toString())

            putProfile(ProfilePost(patientDetail))

            UserToken.saveAvatar(path!!,App.activity!!)
            UserToken.saveName("$firstName $lastName",App.activity!!)

        }

    }

    private fun getGender(sex: String): String {
        return if (sex == "Мужской") {
            "0"
        } else {
            "1"
        }
    }

    private fun getGenderForId(sex: String): String {
        return if (sex == "0") {
            "Мужской"
        } else {
            "Женский"
        }
    }


    private fun putProfile(patientDetail: ProfilePost){
        subscription.add(
            postApi.putProfile(patientDetail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe(
                    { result ->
                        hideProgress()
                        if (result.isSuccessful) {
                            Log.e("UY", result.body()!!.toString())
                            App.activity.toast("Сохранено")
                        } else {
                            var error = result.errorBody()!!.string()
                            Log.e("Error", error)

                        }

                    },
                    {
                        hideProgress()

                        Log.e("DDD", it.toString())
                    }

                )
        )
    }

    fun onClickBack(){
        (App.activity as MainActivity).onBackFromFragment()
    }

    fun onClickFavourite(){

        App.activity!!.startActivity(Intent(App.activity,FavouriteActivity::class.java))

    }
    fun onClickDoctor(){

        App.activity!!.startActivity(Intent(App.activity,DoctorActivity::class.java))

    }


}