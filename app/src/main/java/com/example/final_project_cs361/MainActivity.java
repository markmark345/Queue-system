package com.example.final_project_cs361;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleObserver;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.quickstart.fcm.R;
//import com.google.firebase.quickstart.fcm.databinding.ActivityMainBinding;


import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    // Create channel to show notifications.
//    String channelId  = getString(R.string.default_notification_channel_id);
//    String channelName = getString(R.string.default_notification_channel_name);
    private static final String TAG = "MainActivity";
    public Customer customer_;
    public Merchant merchant_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customer_ = new Customer();
        customer_.loadCustomer(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }
        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
            Utils.receiveMessageData(getIntent().getExtras());

        }

//            MessageTransaction customer = new MessageTransaction();
//
//            int mode = Integer.parseInt((String)getIntent().getExtras().get("mode"));
//            try {
//                customer.setName_((String)getIntent().getExtras().get("name"));
//                customer.setToken_((String)getIntent().getExtras().get("token"));
//                customer.setPhone_((String)getIntent().getExtras().get("phone"));
//                customer.setEmail_((String)getIntent().getExtras().get("email"));
//                customer.setSeats_(Integer.parseInt((String)getIntent().getExtras().get("numberOfSeats")));
//                customer.setDate_((String)getIntent().getExtras().get("date"));
//                customer.setTime_((String)getIntent().getExtras().get("time"));
//                customer.setMerchantName_((String)getIntent().getExtras().get("merchantName"));
//                customer.setQnum_(Integer.parseInt((String)getIntent().getExtras().get("qnum")));
//                customer.setLane_((String)getIntent().getExtras().get("lane"));
//                customer.setMode(mode);
//
//                Log.d(TAG, "Customer: " + customer.getName_());
//            } catch (Exception ex) {
//                Log.d(TAG, "Exception: " + ex);
//            }
//
//            if (mode == 1) {
//                CustomerBookingQueueManager.set(customer);
//            } else if (mode == 0) {
//                CustomerBookingNotifyManagement.add(customer);
//            }

//            for (String key : getIntent().getExtras().keySet()) {
//                Object value = getIntent().getExtras().get(key);
//                Log.d(TAG, "Key: " + key + " Value: " + value);
//            }
//        }
        // [END handle_data_extras]

        // [START log_reg_token]
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//
//                });
        // [END log_reg_token]

    }

    public void goToRegisCustomer(View view) {
//        Log.d(TAG, "Key:dddddd " );
        if (customer_ != null && customer_.isRegisterd() == true) {

            UserMode.setMode_(UserMode.Mode.CUSTOMER);
            Intent intent = new Intent(this, new_menu.class);
//            intent.putExtra("customer", customer_);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, RegisterCustomer.class);
            intent.putExtra("customer", customer_);
            startActivity(intent);
        }
    }


    public void goToRegisMerchant(View view) {
        Intent intent = new Intent(this, LoginMerchant.class);
//        intent.putExtra("customer", merchant_);
        startActivity(intent);
    }

}