package kg.docplus.ui.chat

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kg.docplus.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kg.docplus.DocPlusApp
import kg.docplus.injection.ViewModelFactory
import kg.docplus.model.firebase.Message
import kg.docplus.ui.favorite_doctor.FavouriteViewModel
import kg.docplus.ui.main.filter.Filter
import kg.docplus.utils.ImagePickerHelper
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
    lateinit var doc_id:String
    lateinit var patient_id:String
    lateinit var adapter: MessageRvAdapter
    private lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        DocPlusApp.activity = this
        Glide.with(this).load(Filter.chatAvatar)
            .apply(
                RequestOptions.bitmapTransform(
                    (CropCircleTransformation())
                )
            ).into(avatar)
        viewModel = ViewModelProviders.of(this, ViewModelFactory()).get(ChatViewModel::class.java)
        setupRv()
        viewModel.avatar.observe(this,android.arch.lifecycle.Observer {
            if (it != null) {
                sendPhoto(it)
            }
        })
        doc_id = "12"
        patient_id = "15"
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

        val messageText = Message(Date().time.toString(),message.text.toString(),1)
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


}
