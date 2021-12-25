package com.example.final_project_cs361;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalDateTime;
import java.util.Date;

public class MerchantMainActivity extends AppCompatActivity {
//    private final String TAG = "MerchantMenu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d(TAG, "onCreate");

        setContentView(R.layout.activity_merchant_main);

        BottomNavigationView navigationView;
        navigationView= findViewById(R.id.merchant_bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.merchant_body_container,MerchantMainFragment.newInstance()).commit();
        navigationView.setSelectedItemId(R.id.MerchantHomeID);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.MerchantHomeID:
                        fragment = MerchantMainFragment.newInstance();//new MerchantHome();
                        break;
                    case R.id.MerchantSubmitQueue:
                        fragment = SubmitQueueFragment.newInstance();
                        break;
                    case R.id.MerchantInformationID:
                        fragment = MerchantInformation.newInstance();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.merchant_body_container,fragment).commit();
                return true;
            }
        });
    }
}