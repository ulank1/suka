package kg.docplus.ui.chat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
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
import kg.docplus.utils.ImagePickerHelper
import kg.docplus.utils.extension.isTime
import kg.docplus.utils.extension.toast
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*


class ChatActivity : ImagePickerHelper() {

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

        getMessages()
        setOnClickListeners()
    }

    private fun sendPhoto(imageUrl:String) {
        val messageText = Message(Date().time.toString(),imageUrl,1)
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
    }

    private fun setupRv(){
        rv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,true) as RecyclerView.LayoutManager?
        adapter = MessageRvAdapter(this)
        rv.adapter = adapter
    }

    private fun getMessages() {

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
                    var user:Int=1
                    if (document.data["user"] !=null)
                     user= (document.data["user"] as Long).toInt()

                    messages.add(Message(time,text,user))

                }

                adapter.swapData(messages)

                Log.e("MESSSAGE",messages.toString())
            } else {
                Log.e("LLL", "Current data: null")
            }
        }

    }

    private fun sendMessage(){
        if (isTime(intent.getStringExtra("time"))) {
            val messageText = Message(Date().time.toString(), message.text.toString(), 1)
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


}
