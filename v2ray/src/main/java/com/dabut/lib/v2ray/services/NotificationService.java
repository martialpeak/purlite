package com.dabut.lib.v2ray.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.dabut.lib.v2ray.R;

public class NotificationService {
    private static final String CHANNEL_ID = "purlite_vpn_channel";
    private static final int NOTIFICATION_ID = 1;
    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationService(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "PurLite VPN",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("PurLite VPN Connection Status");
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setAutoCancel(false);

        if (context instanceof Service) {
            ((Service) context).startForeground(NOTIFICATION_ID, builder.build());
        } else {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    public void updateNotification(String content) {
        showNotification("PurLite VPN", content);
    }

    public void cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
