package org.techtown.study01.FirstToMain.homeMain;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private NetworkConnectionCheck networkConnectionCheck;
    public MyService() {

    }

    //////////////////////////////////////알림 채널 만들기///////////
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        String CHANNEL_ID = "channel_1";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Android test",
                NotificationManager.IMPORTANCE_LOW);

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .createNotificationChannel(channel);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build();
        startForeground(2, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //  LOLLIPOP Version 이상..
            if(networkConnectionCheck==null){
                networkConnectionCheck=new NetworkConnectionCheck(getApplicationContext());
                networkConnectionCheck.register();
            }
        }
        return START_STICKY;    // START_STICKY : 시스템에 의해 종료 되어도 다시 생성 시켜주는 것
    }// onStartCommand() ..

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {     //  LOLLIPOP Version 이상..
            if(networkConnectionCheck!=null) networkConnectionCheck.unregister();
        }
    }// onDestroy()..
}