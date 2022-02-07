package com.hiddenDimension.thoughtmapper.backgroud;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.hiddenDimension.thoughtmapper.R;
import com.hiddenDimension.thoughtmapper.UiDbFetcher;

//this only keeps alive the app, the one and only soul
//it's like a heart , if it stops the brain doesn't work long
public class CoreService extends Service {

    private static BroadcastReceiver br_ScreenOffReceiver;


    public static String message , sender;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    UiDbFetcher uiDbFetcher;
    Handler handler;

    Thread thread;


   public Runnable goToNextWord(){
        Runnable runnable = new Runnable() {
            @Override
            public synchronized void run() {

                uiDbFetcher.openConnection();
                registerScreenOffReceiver(uiDbFetcher.getTodayWord(),uiDbFetcher.searchWord());
                uiDbFetcher.closeConnection();


            }
        };

        handler.postDelayed(runnable,10000);


        //goToNextWord();

        return null;
    }

    @Override
    public void onCreate()
    {


        uiDbFetcher= new UiDbFetcher(getApplicationContext());

        registerScreenOffReceiver("Words", "will come here");

        handler= new Handler();

        KeyguardManager myKM = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);



        thread= new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    Log.d("thread >>>", "loo");

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if( myKM.inKeyguardRestrictedInputMode() ) {
                        // it is locked
                    } else {
                        //it is not locked
                        goToNextWord();
                    }


                }
            }
        });


       // goToNextWord();


        thread.start();

    }

    @Override
    public void onDestroy()
    {
        //unregisterReceiver(br_ScreenOffReceiver);
        //  Object m_ScreenOffReceiver = null;
        recreateService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void registerScreenOffReceiver(String title, String detail)
    {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "1234");
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)

                .setContentTitle(title)
                .setContentText(detail)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground(title,detail);

        }
        else {
            startForeground(1, notification);

             Toast.makeText(getApplicationContext(), "lolll", Toast.LENGTH_SHORT).show();
        }
        uiDbFetcher.setLatestOffset(0);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(String title , String text){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)

                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //super.onTaskRemoved(rootIntent);

           recreateService();
    }

    private void recreateService(){
        Intent intent = new Intent(getApplicationContext(), CoreService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else startService(intent);
    }
}
