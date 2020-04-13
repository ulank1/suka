package kg.docplus.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kg.docplus.R
import kg.docplus.ui.chat.ChatActivity
import kg.docplus.ui.main.MainActivity
import kg.docplus.ui.my_doctor.DoctorActivity
import kg.docplus.ui.rating.CreateRatingActivity
import org.json.JSONObject
import java.util.*

class FMService : FirebaseMessagingService() {
    private val CHANNEL_ID = "kg.docplus"


    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
//        Log.e("NOtif","dsf"+p0.notification.toString())


        if (p0 != null) {

            var intent = p0.toIntent()
            if (p0.data != null) {

                try {
                    val data = p0.data
                    var type = "0"
                    var modal = false


                    if (p0.notification != null) {
                        Log.e("NO", p0.notification.toString())
                    }
                    data.keys.forEach { Log.e("KEYS", it) }
                    if (data.containsKey("data")) {
                        var json = JSONObject(data["data"])

                        Log.e("Json", json.toString())
                                           if (json.has("type")) {
                            type = json.getString("type")
                        }
                        if (json.has("modal")) {
                            modal = json.getBoolean("modal")
                        }



                        Log.e("Notif", "" + p0.data)
                        Log.e("Notif", "" + intent)
                        sendNotification(json.getString("body"), json.getString("title"), type, json,modal)
                    } else {
                        sendNotification(data["body"]!!, data["title"]!!, type, JSONObject(),modal)
                    }
                } catch (e: Exception) {

                }
            } else {
                Log.e("FALSE", p0.notification!!.toString())
                //sendNotification(p0.notification!!.body!!, p0.notification!!.title!!)
            }


        }
    }

    private fun sendNotification(message: String, title: String, type: String, json: JSONObject,modal:Boolean) {
        var resultIntent: Intent = when (type) {
            "1" -> {
                Intent(this, ChatActivity::class.java)
            }
            "2" -> Intent(this, MainActivity::class.java)
            else -> Intent(this, DoctorActivity::class.java)
        }
/*data = {
            "modal": True,
            "body": appointment.body,
            "title": appointment.title,
            "doctor_fullname": _doc_name,
            "service": appointment.service,
            "doctor_id": appointment.doctor_id,
            "avatar": get_avatar_profile(
                instance=appointment.patient.patient_detaiil
            ),
        }*/
        if (modal){
            resultIntent = Intent(this, CreateRatingActivity::class.java)
            resultIntent.putExtra("name", json.getString("doctor_fullname"))
            resultIntent.putExtra("avatar", json.getString("avatar"))
            resultIntent.putExtra("doctor_id", json.getString("doctor_id"))
        }

        if (type == "1") {

            resultIntent.putExtra("doc_id", json.getString("doc_id"))
            resultIntent.putExtra("patient_id", json.getString("patient_id"))
            resultIntent.putExtra("avatar", json.getString("avatar"))
            resultIntent.putExtra("speciality", json.getString("speciality"))
            resultIntent.putExtra("time", json.getString("time"))
            resultIntent.putExtra("name", json.getString("name"))
        }
        resultIntent.action = System.currentTimeMillis().toString()

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(MainActivity::class.java)
        stackBuilder.addNextIntent(resultIntent)
        val resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val random = Random()
        val notificationId = random.nextInt(9999 - 1000) + 1000

        var i: Int

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            i = NotificationManager.IMPORTANCE_HIGH
        } else {
            i = NotificationCompat.PRIORITY_MAX
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, "All Discount notifications", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(i)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.mipmap.ic_launcher)
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher)
        }
        val notification = builder.build()

        notificationManager.notify(notificationId, notification)
    }

}