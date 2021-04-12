package sg.halp.user.FirebaseHandler;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import sg.halp.user.ChatActivity;
import sg.halp.user.MainActivity;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class AmbulanceFCMService extends FirebaseMessagingService {

    private NotificationManager mNotificationManager;
    private String TAG = "HaoLS";

    PreferenceHelper mPreferenceHelper;
    private String mToken = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0){

            EbizworldUtils.appLogDebug(TAG, "Data: " + remoteMessage.getData());
            EbizworldUtils.appLogDebug(TAG, "Action: " + remoteMessage.getData().get(Const.NotificationParams.ACTION));
            EbizworldUtils.appLogDebug(TAG, "Type: " + remoteMessage.getData().get(Const.NotificationParams.TYPE));

            if (remoteMessage.getData().get(Const.NotificationParams.ACTION) != null &&
                    remoteMessage.getData().get(Const.NotificationParams.TYPE) != null){

                String action = remoteMessage.getData().get(Const.NotificationParams.ACTION);
                String type = remoteMessage.getData().get(Const.NotificationParams.TYPE);

                switch (action){

                    case Const.NotificationParams.SCHEDULE:{

                        Intent intent = new Intent(Const.NotificationParams.SCHEDULE);
                        intent.putExtra(Const.NotificationParams.ACTION, Const.NotificationParams.TYPE_SCHEDULE_STARTED);
                        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        EbizworldUtils.appLogDebug(TAG, "Intent sent to broadcast with action " + intent.getAction());

                        if (remoteMessage.getData().get(Const.NotificationParams.BODY) != null){

                            sendNotification(remoteMessage.getData().get(Const.NotificationParams.BODY), type);

                        }

                    }
                    break;

                    case Const.NotificationParams.TYPE_ACCOUNT_LOGOUT:{

                        Intent intent = new Intent(Const.NotificationParams.TYPE_ACCOUNT_LOGOUT);
                        intent.putExtra(Const.NotificationParams.TYPE_ACCOUNT_LOGOUT, type);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                        if (remoteMessage.getData().get(Const.NotificationParams.BODY) != null){

                            sendNotification(remoteMessage.getData().get(Const.NotificationParams.BODY), type);

                        }

                    }
                    break;

                    case Const.NotificationParams.ACTION_REQUEST:{

                        Intent intent = new Intent(Const.NotificationParams.ACTION_REQUEST);
                        intent.putExtra(Const.NotificationParams.ACTION_REQUEST, type);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                        if (remoteMessage.getData().get(Const.NotificationParams.BODY) != null){

                            sendNotification(remoteMessage.getData().get(Const.NotificationParams.BODY), type);

                        }

                    }
                    break;

                    case Const.NotificationParams.ACTION_SCHEDULE:{

                        Intent intent = new Intent(Const.NotificationParams.ACTION_SCHEDULE);
                        intent.putExtra(Const.NotificationParams.ACTION_SCHEDULE, type);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                        if (remoteMessage.getData().get(Const.NotificationParams.BODY) != null){

                            sendNotification(remoteMessage.getData().get(Const.NotificationParams.BODY), type);

                        }

                    }
                    break;

                    default:{

                        if (remoteMessage.getData().get(Const.NotificationParams.BODY) != null){

                            sendNotification(remoteMessage.getData().get(Const.NotificationParams.BODY), type);

                        }

                    }
                    break;
                }

            }

        }

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(String s) {

        mToken = s;

        storeRegistrationId(this, mToken);

        Log.d(TAG, "Token: " + mToken);

    }

    public static String getDeviceTokenManual(final Activity activity){

        final String[] token = {""};

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()){

                    EbizworldUtils.appLogDebug("HaoLS", "Get Firebase token failed: " + task.getException());

                }else {

                    token[0] = task.getResult().getToken();
                    EbizworldUtils.appLogDebug("HaoLS", "Firebase token: " + token[0]);
                    EbizworldUtils.storeRegistrationId(activity, token[0]);
                }
            }

        });

        return token[0];
    }

    private void sendNotification(String body, String type) {

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;

        if (type.equals(Const.NotificationParams.CHAT_MESSAGE)){

            intent = new Intent(this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("newques", "new_quest");

        }else {

            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }

        //Dùng cho phương thức setContenIntent(...)
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        //Dùng cho phương thức setSound(...)
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Log.e(TAG, "rec push o" + body);

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(String.valueOf(getString(R.string.notification_channel_id)), getResources().getString(R.string.notification_channel_name), importance);
            mChannel.setDescription(getString(R.string.notification_channel_name_description));
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 500});
            mNotificationManager = getSystemService(NotificationManager.class);
            mNotificationManager.createNotificationChannel(mChannel);


            if (mNotificationManager.getNotificationChannel(String.valueOf(getResources().getString(R.string.notification_channel_id))) == null) {

                mChannel = new NotificationChannel(String.valueOf(getResources().getString(R.string.notification_channel_id)), getResources().getString(R.string.notification_channel_name), importance);
                mChannel.setDescription(getResources().getString(R.string.notification_channel_name_description));
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 500});

                mNotificationManager.createNotificationChannel(mChannel);

            }

            NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this, String.valueOf(getResources().getString(R.string.notification_channel_id)))
                    .setContentTitle(getResources().getString(R.string.app_name))  // required
                    .setSmallIcon(R.drawable.ic_launcher) // required
                    .setContentText(body)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationManager.IMPORTANCE_MAX)
                    .setVibrate(new long[]{100, 500});

            try{

                mNotificationManager.notify(Integer.parseInt(getResources().getString(R.string.notification_channel_id)), mBuilder.build());

            }catch (NumberFormatException e){
                e.printStackTrace();
            }

        }else {

            //Dùng cho notificationManager.notify(...)
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{100,500})
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);

            mNotificationManager.notify(0, builder.build());

        }

    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void storeRegistrationId(Context context, String regId) {

        mPreferenceHelper = new PreferenceHelper(this);
        int appVersion = getAppVersion(context);
        Log.d(TAG, "Saving regId on app version " + appVersion);
        Log.d(TAG,"RegID "+regId);
        mPreferenceHelper.putAppVersion(appVersion);
        mPreferenceHelper.putRegisterationID(regId);
        mPreferenceHelper.putDeviceToken(regId);
    }
}
