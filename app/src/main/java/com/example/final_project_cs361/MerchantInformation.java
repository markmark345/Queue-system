package com.example.final_project_cs361;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MerchantInformation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MerchantInformation extends Fragment {
    private static final String TAG = "SubmitQueueFragment";
    ProgressDialog progressDialog;

    public MerchantInformation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment MerchantInformation.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchantInformation newInstance() {
        return new MerchantInformation();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_information, container, false);
        TextView tvName = view.findViewById(R.id.restaurant_name);
        tvName.setText(MerchantManager.get().getName_());
        TextView tvEmail = view.findViewById(R.id.restaurant_email);
        tvEmail.setText(MerchantManager.get().getEmail_());
        TextView tvPhone = view.findViewById(R.id.restaurant_tel);
        tvPhone.setText(MerchantManager.get().getPhone_());
        Button button = view.findViewById(R.id.restaurant_reset);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Updating..."); // Setting Message
        progressDialog.setTitle("Progressing"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); //

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick");
                progressDialog.show();
                QueueManager.reset();
//                alertView("Your queue was reset");
                Log.d(TAG, "query booking");
                db.collection("Booking")
                        .whereNotEqualTo("booking_status", "SUCCESS")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.d(TAG, "onSuccess");
                                WriteBatch batch = db.batch();
                                Log.d(TAG, "Start for");
                                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                    Log.d(TAG, "document : " + document.getId());
                                    DocumentReference docRef = db.collection("Booking").document(document.getId());
                                    batch.update(docRef, "booking_status", "CANCEL");
                                }
                                Log.d(TAG, "End for");
                                batch.commit()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d(TAG, "batch commit success");
                                                progressDialog.dismiss();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d(TAG, "batch commit failed");
                                                progressDialog.dismiss();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure");
                                progressDialog.dismiss();
                            }
                        });

            }
        });

        return view;
    }

    private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle( "Q" )
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
}