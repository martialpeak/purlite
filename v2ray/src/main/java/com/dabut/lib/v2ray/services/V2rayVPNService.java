package com.dabut.lib.v2ray.services;

import android.content.Intent;
import android.net.VpnService;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.dabut.lib.v2ray.core.V2rayCoreExecutor;
import com.dabut.lib.v2ray.model.V2rayConfigModel;
import com.dabut.lib.v2ray.utils.V2rayConstants;

public class V2rayVPNService extends VpnService {
    private V2rayCoreExecutor v2rayCoreExecutor;
    private NotificationService notificationService;
    private V2rayConstants.CONNECTION_STATES connectionState = V2rayConstants.CONNECTION_STATES.DISCONNECTED;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationService = new NotificationService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            V2rayConstants.SERVICE_COMMANDS command = (V2rayConstants.SERVICE_COMMANDS) intent.getSerializableExtra(V2rayConstants.V2RAY_SERVICE_COMMAND_EXTRA);
            if (command == V2rayConstants.SERVICE_COMMANDS.STOP_SERVICE) {
                stopSelf();
                return START_NOT_STICKY;
            }
        }

        notificationService.showNotification("PurLite VPN", "Connecting...");
        connectionState = V2rayConstants.CONNECTION_STATES.CONNECTING;

        try {
            // Start VPN tunnel using Builder
            Builder builder = new Builder();
            builder.setSession("PurLite VPN");
            builder.setMtu(1500);
            builder.addAddress("10.0.0.2", 32);
            builder.addRoute("0.0.0.0", 0);

            // Start the VPN
            startForeground(1, new android.app.Notification.Builder(this)
                    .setSmallIcon(com.dabut.lib.v2ray.R.drawable.ic_launcher)
                    .setContentTitle("PurLite VPN")
                    .setContentText("Connected")
                    .build());

            connectionState = V2rayConstants.CONNECTION_STATES.CONNECTED;
        } catch (Exception e) {
            connectionState = V2rayConstants.CONNECTION_STATES.DISCONNECTED;
            notificationService.updateNotification("Connection failed");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (v2rayCoreExecutor != null) {
            v2rayCoreExecutor.stopCore(true);
        }
        notificationService.cancelNotification();
        connectionState = V2rayConstants.CONNECTION_STATES.DISCONNECTED;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
