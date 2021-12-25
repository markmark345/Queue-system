package com.example.final_project_cs361;

import android.util.Log;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class DBUtils {
    private static final String TAG = "DBUtils";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ArrayList<Merchant> getMerchants() {
        ArrayList<Merchant> merchants = new ArrayList<>();
        db.collection("merchants")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    merchants.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Merchant merchant = new Merchant();
                        merchant.setName_((String) document.get("name"));
                        merchant.setPhone_((String) document.get("phone"));
                        merchant.setToken_((String) document.get("token"));
                        merchants.add(merchant);
                    }
                } else {
                    Log.d(TAG, "No data");
                }
            });
        return merchants;
    }
}
