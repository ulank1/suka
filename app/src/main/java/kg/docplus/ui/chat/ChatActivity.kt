package kg.docplus.ui.chat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kg.docplus.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kg.docplus.App
import kg.docplus.injection.ViewModelFactory
import kg.docplus.model.firebase.Message
import kg.docplus.ui.main.filter.Filter
import kg.docplus.ui.notification.PayboxActivity
import kg.docplus.utils.ImagePickerHelper
import kg.docplus.utils.extension.isTime
import kg.docplus.utils.extension.toast
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*
import kotlin.collections.HashMap


class ChatActivity : ImagePickerHelper() {
    private var isVideoActive = true
    override fun setImagePath(imgpath: Uri) {
        Log.e("sdfsdf",imgpath.path)
        viewModel.postImage(imgpath.path!!)
    }

    override fun openGallery(mItemId: String) {
    }

    var db = FirebaseFirestore.getInstance()
    var doc_id:String = "12"
    var patient_id:String = "15"
    lateinit var adapter: MessageRvAdapter
    private lateinit var viewModel: ChatViewModel
    var video_price = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        App.activity = this
        Filter.chatAvatar = intent.getStringExtra("avatar")

        name.text = intent.getStringExtra("name").toString()
        Glide.with(this).load(intent.getStringExtra("avatar"))
            .apply(
                RequestOptions.bitmapTransform(
                    (CropCircleTransformation())
                )
            ).into(avatar)

        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(ChatViewModel::class.java)
         setupRv()
        viewModel.avatar.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                sendPhoto(it)
            }
        })

        doc_id = intent.getStringExtra("doc_id").toString()
        patient_id = intent.getStringExtra("patient_id").toString()
        specialities.text = intent.getStringExtra("speciality").toString()

        viewModel.confirm.observe(this,androidx.lifecycle.Observer { video_price = it.calculated_price })
        getMessages()
        setOnClickListeners()
    }

    private fun sendPhoto(imageUrl:String) {
        val messageText = Message(Date().time.toString(),imageUrl,1,null,false)
        message.setText("")
        db.collection("chat").document(doc_id).collection(patient_id)
            .add(messageText)
            .addOnSuccessListener { documentReference ->
                Log.e("TRUE", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.e("FALSE", "Error adding document", e)
            }
    }

    private fun setOnClickListeners(){
        avatar.setOnClickListener { startActivity(Intent(this,ImageActivity::class.java).putExtra("image",Filter.chatAvatar)) }
        send_message.setOnClickListener { sendMessage() }
        attachment_photo.setOnClickListener { showPickImageDialog() }
        video.setOnClickListener { if (isVideoActive){
            sendMessage(ChatConstants.request_video,"Пациент хочет переключиться на видеозвонок")
            viewModel.confirmVideo(intent.getIntExtra("id",0))
        }}
    }

    private fun setupRv(){
        rv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,true) as RecyclerView.LayoutManager?
        adapter = MessageRvAdapter(this)
        rv.adapter = adapter
    }

    private fun getMessages() {
        Log.e("LLLDD",doc_id+" "+patient_id)

        val docRef=db.collection("chat").document(doc_id).collection(patient_id).orderBy("time", Query.Direction.DESCENDING)
        docRef.addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Log.w("FAIL", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                var messages:ArrayList<Message> = ArrayList()

                for (document in snapshot) {
                    Log.e("GET", "${document.id} => ${document.data}")
                    val text:String = document.data["message"] as String
                    val time:String = document.data["time"] as String
                    val viewed:Boolean? = document.data["_viewed"] as Boolean?
                    val status:String? = document.data["status"] as String?
                    var user:Int=1
                    if (document.data["user"] !=null)
                     user= (document.data["user"] as Long).toInt()

                    Log.e("VIEWED", viewed.toString())
                    if (viewed.toString()=="false"){
                        if (status==ChatConstants.video_success){
                            showAlertSuccess()
                            resendMessage(ChatConstants.video_success,document.id)
                        }else if(status==ChatConstants.video_cancel){
                            resendMessage(ChatConstants.video_cancel,document.id)
                            showAlertCancel()
                        }else if(status==ChatConstants.request_video){
                            isVideoActive = !isTime(time.toLong())
                        }
                    }
                    messages.add(Message(time,text,user,status,viewed))

                }
                adapter.swapData(messages)

                Log.e("MESSSAGE",messages.toString())
            } else {
                Log.e("LLL", "Current data: null")
            }
        }

    }

    private fun showAlertSuccess() {
        var dialog = VideoSuccessDialog(this)
        dialog.setUp(intent.getStringExtra("name").toString(),intent.getStringExtra("avatar").toString(),video_price)
        var btnPay:Button = dialog.findViewById(R.id.btn_pay)
        btnPay.setOnClickListener {

           startActivityForResult(Intent(this, PayboxActivity::class.java).putExtra("price", video_price),1020)

        }
    }

    private fun showAlertCancel() {
        var dialog = VideoCancelDialog(this)
        dialog.setUp(intent.getStringExtra("name").toString(),intent.getStringExtra("avatar").toString())
    }

    private fun sendMessage(){
        if (isTime(intent.getStringExtra("time"))) {
            val messageText = Message(Date().time.toString(), message.text.toString(), 1,null,null)
            viewModel.sendPush(messageText.message, doc_id, patient_id,intent.getStringExtra("time"))
            message.setText("")
            db.collection("chat").document(doc_id).collection(patient_id)
                .add(messageText)
                .addOnSuccessListener { documentReference ->
                    Log.e("TRUE", "DocumentSnapshot added with ID: $documentReference")
                }
                .addOnFailureListener { e ->
                    Log.e("FALSE", "Error adding document", e)
                }
        }else{
            toast("Вы не можете отправлять сообщение")
        }
    }

    private fun sendMessage(text:String,notification:String){

        Log.e("ISTIME",isTime(intent.getStringExtra("time")).toString())
        if (isTime(intent.getStringExtra("time"))) {

            val messageText = Message(Date().time.toString(), "", 1,text,false)
            message.setText("")

            viewModel.sendPush(notification,doc_id,patient_id,intent.getStringExtra("time").toString())


            db.collection("chat").document(doc_id).collection(patient_id)
                    .add(messageText)
                    .addOnSuccessListener { documentReference ->
                        //                        Log.e("TRUE", "DocumentSnapshot added with ID: $documentReference")
                    }
                    .addOnFailureListener { e ->
                        //                        Log.e("FALSE", "Error adding document", e)
                    }
        }else{
            toast("Вы не можете отправлять сообщение")
        }
    }

    private fun resendMessage(text:String,id:String){

        Log.e("ISTIME",isTime(intent.getStringExtra("time")).toString())
        if (isTime(intent.getStringExtra("time"))) {

            val messageText = Message(Date().time.toString(), "", 1,text,true)
            db.collection("chat").document(doc_id).collection(patient_id)
                    .document(id)
                    .set(messageText)
                    .addOnSuccessListener { documentReference ->
                        //                        Log.e("TRUE", "DocumentSnapshot added with ID: $documentReference")
                    }
                    .addOnFailureListener { e ->
                        //                        Log.e("FALSE", "Error adding document", e)
                    }
        }else{
            toast("Вы не можете отправлять сообщение")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1020){
            if (resultCode== Activity.RESULT_OK){
//                viewModel.createAppointment()
            }else{
                if (data!=null) {
                    toast(data.getStringExtra("error"))
                }
            }
        }
    }


}
