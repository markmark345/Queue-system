package com.example.final_project_cs361;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterMerchant extends AppCompatActivity {
    private static final String TAG = "RegisterCustomer";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> merchantMap = new HashMap<>();
    Merchant merchant_;
    String token_;

    EditText mRegisName;
    EditText mRegisEmail;
    EditText mRegisPhone;
    EditText mRegisUserName;
    EditText mRegisPassword;
    CheckBox mRegisStoreTypeTableA, mRegisStoreTypeTableB,
            mRegisStoreTypeTableC, mRegisStoreTypeTableD,
            mRegisStoreTypeTableE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_merchant);
        merchant_ = new Merchant();
//        Log.d(TAG, "onCreate");
//        Bundle extras = getIntent().getExtras();
//        Log.d(TAG, "extras");
//        if (extras != null) {
//            merchant_ = extras.getParcelable("merchant");
//        }
//        if (merchant_ != null) {
//            Log.d(TAG, merchant_.getName_());
//        } else {
//            Log.d(TAG, "merchant is null");
//        }

        mRegisName = (EditText)findViewById(R.id.regis_store_name);
        mRegisUserName = (EditText)findViewById(R.id.regis_store_username);
        mRegisPassword = (EditText)findViewById(R.id.regis_store_password);
        mRegisPhone = (EditText)findViewById(R.id.regis_store_phone);
        mRegisEmail = (EditText)findViewById(R.id.regis_store_email);
        mRegisStoreTypeTableA = (CheckBox)findViewById(R.id.regis_store_type_table_a);
        mRegisStoreTypeTableB = (CheckBox)findViewById(R.id.regis_store_type_table_b);


//        mRegisName.setText("KFC");
//        mRegisUserName.setText("test03");
//        mRegisPassword.setText("1234");
//        mRegisPhone.setText("0837767928");
//        mRegisEmail.setText("natapatchara.anuroje@gmail.com");

        Button btnSaveCustomer = (Button) findViewById(R.id.button_register_merchant);
        btnSaveCustomer.setEnabled(false);
        // [START log_reg_token]
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
                    btnSaveCustomer.setEnabled(true);
                    // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, token_);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }

            });
    }

    public void saveMerchant(View view) {
        Intent loginMerchantIntent = new Intent(this, LoginMerchant.class);

        String regisName = mRegisName.getText().toString();
        String regisUserName = mRegisUserName.getText().toString();
        String regisPassword = mRegisPassword.getText().toString();
        String regisPhone = mRegisPhone.getText().toString();
        String regisEmail = mRegisEmail.getText().toString();

        boolean va = validate(regisName, regisUserName, regisPassword, regisPhone, regisEmail);

        if (va == true) {
            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
            Map<String, Object>lanes = new HashMap<>();

            merchantMap.put("name", regisName);
            merchantMap.put("user_name", regisUserName);
            merchantMap.put("password", regisPassword);
            merchantMap.put("phone", regisPhone);
            merchantMap.put("email", regisEmail);
            merchantMap.put("token", token_);

            if (mRegisStoreTypeTableA.isChecked()) {
                lanes.put("A", 2);
            }
            if (mRegisStoreTypeTableB.isChecked()) {
                lanes.put("B", 4);
            }

            lanes.put("QA", 0);
            lanes.put("QB", 0);
            lanes.put("a_timestamp", new Date());
            lanes.put("b_timestamp", new Date());

            merchant_.setName_(regisName);
            merchant_.setEmail_(regisEmail);
            merchant_.setPhone_(regisPhone);
            merchant_.setUserName_(regisUserName);
            merchant_.setPassword_(regisPassword);
            merchant_.setToken_(token_);

            merchantMap.put("Lanes", lanes);

            Context ctx = this;

            db.collection("merchants")
                    .add(merchantMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(null, "DocumentSnapshot added with ID: " + documentReference.getId());
                            merchant_.register(ctx, regisName, regisEmail, regisPhone, regisUserName, regisPassword, token_);
//                            loginMerchantIntent.putExtra("merchant", merchant_);
                            startActivity(loginMerchantIntent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(null, "Error adding document", e);
                            Toast.makeText(getApplicationContext(), "Error adding document", Toast.LENGTH_SHORT).show();
                        }
                    });


        } else {
            Toast.makeText(getApplicationContext(), "Please check information", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean validate(String regisName, String regisUserName, String regisPassword, String regisPhone, String regisEmail) {
        if (regisName.length() == 0) {
            mRegisName.setError("Field cannot be empty");
            mRegisName.requestFocus();
            return false;
        } else if (!regisName.matches("[a-zA-Z]+")) {
            mRegisName.setError("Enter only alphabetical character");
            mRegisName.requestFocus();
            return false;
        } else if (regisEmail.length() == 0) {
            mRegisEmail.setError("Field cannot be empty");
            mRegisEmail.requestFocus();
            return false;
        } else if (regisEmail.matches("[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+")) {
            mRegisEmail.setError("Enter valid email");
            mRegisEmail.requestFocus();
            return false;
        } else if (regisPhone.length() == 0) {
            mRegisPhone.setError("Field cannot be empty");
            mRegisPhone.requestFocus();
            return false;
        } else if (regisUserName.length() == 0) {
            mRegisUserName.setError("Field cannot be empty");
            mRegisUserName.requestFocus();
            return false;
//        } else if (!regisUserName.matches("[a-zA-Z0-9]+")) {
//            mRegisUserName.setError("Enter only alphabetical character");
//            return false;
        } else if (regisPassword.length() < 1) {
            mRegisPassword.setError("Length required lower than 9 character");
            mRegisPassword.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}