package com.example.final_project_cs361;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;
import java.util.Map;

public class LoginMerchant extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "LoginMerchant";
    private String token_ = "";
    EditText mUserName, mPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_merchant);
        mUserName = (EditText)findViewById(R.id.log_merchant_user);
        mPassword = (EditText)findViewById(R.id.log_merchant_Password);

        mUserName.setText("test04");
        mPassword.setText("1234");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("...");
        progressDialog.setTitle("Progressing");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    token_ = task.getResult();
                    Log.d(TAG, token_);
                }
            });
    }

    public void SigninMerchant(View view) {
        Intent intent = new Intent(this, RegisterMerchant.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void login(View view) {
        progressDialog.show();
        String userName = mUserName.getText().toString(), password = mPassword.getText().toString();
        Log.d(TAG, "start log-in " + userName);
        db.collection("merchants").whereEqualTo("user_name", userName).whereEqualTo("password", password)
            .get()
            .addOnCompleteListener(task -> {
                Log.d(TAG, "onComplete ");
                if (task.isSuccessful()) {
                    Log.d(TAG, "isSuccessful ");
                    boolean haveResult = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        haveResult = true;
                        String pwd = (String) document.get("password");
                        if (pwd.equals(password)) {
                            String token = (String) document.get("token");
                            if (token_.equals(token)) {
                                Merchant merchant = new Merchant();
                                merchant.setName_((String) document.get("name"));
                                merchant.setEmail_((String) document.get("email"));
                                merchant.setPhone_((String) document.get("phone"));
                                merchant.setUserName_((String) document.get("user_name"));
                                merchant.setToken_(token);
                                merchant.setDocId_(document.getId());
                                Lane lane = new Lane();
                                Map<String, Object> mapLane = (Map<String, Object>)document.get("Lanes");
                                lane.setA_( Integer.parseInt(mapLane.get("A").toString()));
                                lane.setB_( Integer.parseInt(mapLane.get("B").toString()));
                                lane.setQA_( Integer.parseInt(mapLane.get("QA").toString()));
                                lane.setQB_( Integer.parseInt(mapLane.get("QB").toString()));
                                lane.setA_timestamp_(((Timestamp) mapLane.get("a_timestamp")).toDate());
                                lane.setB_timestamp_(((Timestamp) mapLane.get("b_timestamp")).toDate());
                                merchant.setLane_(lane);

                                MerchantManager.set(merchant);

                                UserMode.setMode_(UserMode.Mode.MERCHANT);
                                progressDialog.dismiss();
                                Intent intent = new Intent(this, MerchantMainActivity.class);
                                startActivity(intent);
                            } else {
                                mUserName.setError("Invalid user name or password");
                                progressDialog.dismiss();
                            }

                        } else {
                            mPassword.setError("Invalid password");
                            progressDialog.dismiss();
                        }
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    if (!haveResult) {
                        mUserName.setError("Invalid user name or password");
                        progressDialog.dismiss();
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    mUserName.setError("Invalid user name or password");
                    progressDialog.dismiss();
                }
            });
    }
}