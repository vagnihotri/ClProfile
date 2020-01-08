package com.example.clprofile;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.clevertap.android.sdk.ActivityLifecycleCallback;
import com.clevertap.android.sdk.CleverTapAPI;

import io.branch.referral.Branch;

public class CLApplication extends Application {

    @Override
    public void onCreate() {
        CleverTapAPI.setUIEditorConnectionEnabled(true);//Set to false in production
        CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG); //Set to OFF in production

        //Branch.getAutoInstance(getApplicationContext());

        //initAppsFlyer();

        ActivityLifecycleCallback.register(this);
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("test", "test", importance);
            channel.setDescription("test");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
