package com.example.final_project_cs361;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class RegisterCustomer extends AppCompatActivity {
    private static final String TAG = "RegisterCustomer";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> user = new HashMap<>();

    Customer customer_;
    EditText mRegisName;
    EditText mRegisEmail;
    EditText mRegisPhone;
    String token_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            customer_ = extras.getParcelable("customer");
            extras.clear();
        }

        mRegisName = (EditText)findViewById(R.id.regis_cus_name);
        mRegisEmail = (EditText)findViewById(R.id.regis_cus_email);
        mRegisPhone = (EditText)findViewById(R.id.regis_cus_phone);

        Button btnSaveCustomer = (Button) findViewById(R.id.button_register_customer);
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
        // [END log_reg_token]
    }

    public void saveCustomer(View view) {
        Intent test = new Intent(this, MainActivity.class);

        String regisName = mRegisName.getText().toString();
        String regisEmail = mRegisEmail.getText().toString();
        String regisPhone = mRegisPhone.getText().toString();

        boolean va = validate(regisName, regisPhone, regisEmail);

        if (va == true) {
            Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
            user.put("name", regisName);
            user.put("email", regisEmail);
            user.put("phone", regisPhone);
            user.put("token", token_);

            customer_.setName_(regisName);
            customer_.setEmail_(regisEmail);
            customer_.setPhone_(regisPhone);
            customer_.setToken_(token_);

            // Add a new document with a generated ID
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(null, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(null, "Error adding document", e);
                        }
                    });
            customer_.register(this, regisName, regisEmail, regisPhone, token_);
            startActivity(test);

        } else {
            Toast.makeText(getApplicationContext(), "Please check information", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validate(String regisName, String regisPhone, String regisEmail) {
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
        } else {
            return true;
        }
    }
}