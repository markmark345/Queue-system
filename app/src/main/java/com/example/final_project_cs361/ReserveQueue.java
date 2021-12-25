package com.example.final_project_cs361;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReserveQueue extends AppCompatActivity {
    private static final String TAG = "ReserveQueue";
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";

    EditText mDialogNumber;

//    Merchant merchant_;
    String name_;
    String phone_;
    String email_;
    int numberOfSeats_;
    String tokenCustomer_;
    String date_;
    String time_;
    String tokenMerchant_;
    String merchantName_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_queue);

        mDialogNumber = (EditText) findViewById(R.id.dialog_number);
        mDialogNumber.setText("1");
        Merchant merchant = MerchantManager.get();
        try {
            TextView nameView = findViewById(R.id.name_merchant);
            nameView.setText(merchant.getName_());
            tokenMerchant_ = merchant.getToken_();
            merchantName_ = merchant.getName_();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Error " + ex, Toast.LENGTH_SHORT).show();
        }

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            try {
//                Merchant merchant = (Merchant) extras.getParcelable("merchant");
//                if (merchant != null) {
//                    try {
//                        TextView nameView = findViewById(R.id.name_merchant);
//                        nameView.setText(merchant.getName_());
//                        tokenMerchant_ = merchant.getToken_();
//                        merchantName_ = merchant.getName_();
//                    } catch (Exception ex) {
//                        Toast.makeText(getApplicationContext(), "Error " + ex, Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "merchant is null", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception ex) {
//                Toast.makeText(getApplicationContext(), "Error " + ex, Toast.LENGTH_SHORT).show();
////                Customer customer = (Customer) extras.getParcelable("merchant");
////                Log.d(TAG, "Test : " + customer.getName_());
//            }
//
//        } else {
//            Toast.makeText(getApplicationContext(), "extra is null", Toast.LENGTH_SHORT).show();
//        }

    }

    public void sendMessage(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> BookingMap = new HashMap<>();

        Button btBooking = view.findViewById(R.id.button_booking_id);
        btBooking.setEnabled(false);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy.MM.dd");;
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");;
        Customer customer = new Customer();
        customer.loadCustomer(this);

        date_ = sdfDate.format(new Date());
        time_ = sdfTime.format(new Date());
        name_ = customer.getName_();
        phone_ = customer.getPhone_();
        email_ = customer.getEmail_();
        tokenCustomer_ = customer.getToken_();
        numberOfSeats_ = Integer.parseInt(mDialogNumber.getText().toString());

        BookingMap.put("name", name_);
        BookingMap.put("phone", phone_);
        BookingMap.put("email", email_);
        BookingMap.put("token", tokenCustomer_);
        BookingMap.put("date", date_);
        BookingMap.put("time", time_);
        BookingMap.put("numberOfSeats", numberOfSeats_);
        BookingMap.put("merchant_name", merchantName_);
        BookingMap.put("qnum", 0);
        BookingMap.put("lane", "");
        BookingMap.put("booking_status", MessageTransaction.BookingStatus.UNINITIALIZE);
        BookingMap.put("timestamp", new Date());
        db.collection("Booking")
                .add(BookingMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Error adding document", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d(TAG, "sendMessage");

        sendMessage();

        alertView("Your request was sent.");

    }

    private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle( merchantName_ )
                .setIcon(R.drawable.logo)
                .setMessage(message)
//     .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//      public void onClick(DialogInterface dialoginterface, int i) {
//          dialoginterface.cancel();
//          }})
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();
    }

    public void sendMessage() {
        Log.d(TAG, "sendMessage");
//        Toast.makeText(getApplicationContext(), "sendMessage", Toast.LENGTH_SHORT).show();

        MessageTransaction messageTransaction = new MessageTransaction();
        messageTransaction.setBookingStatus_(MessageTransaction.BookingStatus.UNINITIALIZE);
        messageTransaction.setMerchantName_(merchantName_);
        messageTransaction.setLane_("");
        messageTransaction.setToken_(tokenCustomer_);
        messageTransaction.setSeats_(numberOfSeats_);
        messageTransaction.setPhone_(phone_);
        messageTransaction.setDate_(date_);
        messageTransaction.setName_(name_);
        messageTransaction.setEmail_(email_);
        messageTransaction.setTime_(time_);
        messageTransaction.setQnum_(0);
        messageTransaction.setMode(0);
        String title = name_ + " booking";
        String body = name_ + " " + numberOfSeats_ + " seat(s)";
        String to = tokenMerchant_;
        JSONObject msgObj = JSONUtil.CreateJSONOBJ(messageTransaction, title, body, to);
//        new PostMessage().execute(FCM_MESSAGE_URL, "POST", msgObj.toString());
        new CallAPI().execute(FCM_MESSAGE_URL, "POST", msgObj.toString());
//        try {
//            JSONObject msgObj = new JSONObject();
//            msgObj.put("to", tokenMerchant_);
//            JSONObject notifyObj = new JSONObject();
//            notifyObj.put("body", name_ + " " + numberOfSeats_ + " seat(s)");
//            notifyObj.put("title", name_ + " booking");
//
//            msgObj.put("notification", notifyObj);
//            JSONObject dataObj = new JSONObject();
//            dataObj.put("name", name_);
//            dataObj.put("phone", phone_);
//            dataObj.put("email", email_);
//            dataObj.put("date", date_);
//            dataObj.put("time", time_);
//            dataObj.put("numberOfSeats", numberOfSeats_);
//            dataObj.put("token", tokenCustomer_);
//            dataObj.put("merchantName", merchantName_);
//            dataObj.put("bookingStatus", CustomerBookingQueue.BookingStatus.UNINITIALIZE.toString());
//            dataObj.put("lane", "x");
//            dataObj.put("qnum", 0);
//            msgObj.put("data", dataObj);
//
//            Log.d(TAG, "tokenMerchant_ " + tokenMerchant_);
////            String result = api.doInBackground(FCM_MESSAGE_URL, "POST", msgObj.toString());
//
//            new PostMessage().execute(FCM_MESSAGE_URL, "POST", msgObj.toString());
////            Log.d(TAG, "result " + result);
////            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
//
//        } catch (JSONException ex) {
//            Log.d(TAG, "JSONException " + ex);
//            Toast.makeText(getApplicationContext(), "JSONException", Toast.LENGTH_SHORT).show();
//        } catch (Exception ex) {
//            Toast.makeText(getApplicationContext(), "Exception " + ex, Toast.LENGTH_SHORT).show();
//        }
    }

//    private class PostMessage extends AsyncTask<String, String, String> {
////        private final String TAG = "PostMessage";
//        public PostMessage() {
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            Log.d(TAG, result);
////            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
////            Intent i = new Intent(this,  reservation_fragment.class);
////            startActivity(i);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String urlString = params[0];
//            String data = params[2];
//            String method = params[1];
////        String token = params[3];
////        OutputStream out = null;
//            HttpURLConnection client = null;
//            String result = null;
//
//            Log.d(TAG, "START Http ");
//            Log.d(TAG, "params[1] " + params[1]);
//            Log.d(TAG, "params[2] " + params[2]);
////        Log.d(TAG, "params[3] " + params[3]);
//            BufferedReader reader=null;
//
//            try {
//                URL url = new URL(urlString);
//                client = (HttpURLConnection) url.openConnection();
//
//                client.setRequestMethod(method);
//                client.setRequestProperty("Content-Type","application/json");
//                client.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + "AAAAOnXuhxA:APA91bFjJAcCiK4mqakGSVjT7vNyqu70Yyed1G4Wk3HLqdDJ4-BK1LXXmMdEJHU4eSfJ6jCc7wAF9_qk60fPAJeZRjLTNzTIvJaHBVHWg5934N_qEWqLCh8JR5SdCWRrOkIVKOAh29ja");
//                client.setDoOutput(true);
//
//                OutputStreamWriter wr = new OutputStreamWriter(client.getOutputStream());
//                wr.write(data);
//                wr.flush();
//
//                reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line = null;
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//
//                result = sb.toString();
//
//
//            } catch (MalformedURLException ex) {
//                Log.d(TAG, "MalformedURLException :" + ex);
//            } catch (SocketTimeoutException ex) {
//                Log.d(TAG, "SocketTimeoutException :" + ex);
//            } catch (IOException ex) {
//                Log.d(TAG, "IOException :" + ex);
//            } finally {
//                client.disconnect();
//                Log.d(TAG, "End Http ");
//            }
//            return result;
//        }
//    }

}