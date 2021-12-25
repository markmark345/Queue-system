package com.example.final_project_cs361;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Utils.receiveMessageData(remoteMessage.getData());
//
//        int mode = Integer.parseInt(remoteMessage.getData().get("mode"));
//        MessageTransaction customer = new MessageTransaction();
//        try {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            Log.d(TAG, "seat : " + remoteMessage.getData().get("numberOfSeats"));
////            int d = Integer.parseInt(remoteMessage.getData().get("qnum"));
//
//            customer.setName_(remoteMessage.getData().get("name"));
//            customer.setToken_(remoteMessage.getData().get("token"));
//            customer.setPhone_(remoteMessage.getData().get("phone"));
//            customer.setEmail_(remoteMessage.getData().get("email"));
//            customer.setSeats_(Integer.parseInt(remoteMessage.getData().get("numberOfSeats")));
//            customer.setDate_(remoteMessage.getData().get("date"));
//            customer.setTime_(remoteMessage.getData().get("time"));
//            customer.setMerchantName_(remoteMessage.getData().get("merchantName"));
//            customer.setQnum_(Integer.parseInt(remoteMessage.getData().get("qnum")));
//            customer.setLane_(remoteMessage.getData().get("lane"));
//            customer.setMode(mode);
////            customer.setBookingStatus_(CustomerBookingQueue.BookingStatus.valueOf(remoteMessage.getData().get("bookingStatus")));
//
//            Log.d(TAG, "Customer: " + customer.getName_());
//        } catch (Exception ex) {
//            Log.d(TAG, "Exception: " + ex);
//        }
//
//        if (mode == 1) {
//
//            CustomerBookingQueueManager.set(customer);
//
//        } else if (mode == 0) {
//            CustomerBookingNotifyManagement.add(customer);
//        }

        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }
    // [END receive_message]


    // [START on_new_token]
    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
