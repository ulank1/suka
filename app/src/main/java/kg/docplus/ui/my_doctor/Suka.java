package kg.docplus.ui.my_doctor;

import android.app.Activity;
import android.content.Context;

import com.quickblox.videochat.webrtc.QBRTCClient;
import com.quickblox.videochat.webrtc.QBRTCSession;
import com.quickblox.videochat.webrtc.QBRTCTypes;


import java.util.ArrayList;

import kg.docplus.qbwrtc.activities.CallActivity;
import kg.docplus.qbwrtc.activities.PermissionsActivity;
import kg.docplus.qbwrtc.utils.Consts;
import kg.docplus.qbwrtc.utils.PermissionsChecker;
import kg.docplus.qbwrtc.utils.PushNotificationSender;
import kg.docplus.qbwrtc.utils.WebRtcSessionManager;

public class Suka {
    public static void suka(Context context,String name,Integer id){

        ArrayList<Integer> opponentsList = new ArrayList<>();
        opponentsList.add(id);
        QBRTCTypes.QBConferenceType conferenceType = QBRTCTypes.QBConferenceType.QB_CONFERENCE_TYPE_VIDEO;

        QBRTCClient qbrtcClient = QBRTCClient.getInstance(context);

        QBRTCSession newQbRtcSession = qbrtcClient.createNewSessionWithOpponents(opponentsList, conferenceType);

        WebRtcSessionManager.getInstance(context).setCurrentSession(newQbRtcSession);

        PushNotificationSender.sendPushMessage(opponentsList, name);

        CallActivity.start(context, false);

        permission(context,name,id);
    }


    public static void permission(Context context,String name,Integer id){

        PermissionsChecker checker = new PermissionsChecker(context);

        if (checker.lacksPermissions(Consts.PERMISSIONS)){
            PermissionsActivity.startActivity((Activity) context, false, Consts.PERMISSIONS);
        }


    }

}
