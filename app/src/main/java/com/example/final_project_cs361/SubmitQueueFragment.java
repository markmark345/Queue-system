package com.example.final_project_cs361;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubmitQueueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmitQueueFragment extends Fragment  {

    private static final String TAG = "SubmitQueueFragment";
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button laneA, laneB;
    TextView tvNumQA, tvNumQB;
    ProgressDialog progressDialog;

    public SubmitQueueFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment SubmitQueueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubmitQueueFragment newInstance() {
        SubmitQueueFragment fragment = new SubmitQueueFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submit_queue, container, false);
        progressDialog = new ProgressDialog(getContext());

        progressDialog.setTitle("Progressing");
        progressDialog.setMessage("Updating...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        laneA = view.findViewById(R.id.button_lan_A);
        laneB = view.findViewById(R.id.button_lan_B);
        tvNumQA = view.findViewById(R.id.num_queue_a);
        tvNumQB = view.findViewById(R.id.num_queue_b);

        Lane lane = MerchantManager.get().getLane_();
        tvNumQA.setText(String.valueOf(lane.QA_));
        tvNumQB.setText(String.valueOf(lane.QB_));

        loadbookingWait();

        return view;
    }

    private void sendMessage(MessageTransaction messageTransaction) {
        progressDialog.setMessage("Sending message");
        Log.d(TAG, "sendMessage");
        String title = "Your Q is " + messageTransaction.getLane_() + messageTransaction.getQnum_();
        String body = "Your queue arrive now.";
        String to = messageTransaction.getToken_();
        messageTransaction.setMode(1);
        JSONObject msgObj = JSONUtil.CreateJSONOBJ(messageTransaction, title, body, to);
        new CallAPI().execute(FCM_MESSAGE_URL, "POST", msgObj.toString());
    }

    private void alertView( String message ) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Submit Queue")
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadbookingWait() {
        progressDialog.setTitle("Progressing");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
//        QueueManager.customerList.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Booking")
                .whereEqualTo("merchant_name", MerchantManager.get().getName_())
                .whereEqualTo("booking_status", "WAIT")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "addOnCompleteListener");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "isSuccessful");
                        QueueManager.clear();
                        int a = 0;
                        int b = 0;
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            MessageTransaction messageTransaction = new MessageTransaction();
                            messageTransaction.setDocId(document.getId());
                            messageTransaction.setName_((String) document.get("name"));
                            messageTransaction.setMerchantName_((String) document.get("merchant_name"));
                            messageTransaction.setDate_((String) document.get("date"));
                            messageTransaction.setEmail_((String) document.get("email"));
                            messageTransaction.setLane_((String) document.get("lane"));
                            messageTransaction.setBookingStatus_(Utils.ConvertBookingStatusStringToEnum((String) document.get("booking_status")));
                            messageTransaction.setSeats_(document.getLong("numberOfSeats").intValue());
                            messageTransaction.setPhone_((String) document.get("phone"));
                            messageTransaction.setQnum_(document.getLong("qnum").intValue());
                            messageTransaction.setTime_((String) document.get("time"));
                            messageTransaction.setToken_((String) document.get("token"));
                            messageTransaction.setTimeStamp_(document.getTimestamp("timestamp").toDate());

                            if (messageTransaction.getLane_().equals("A")) {
                                a++;
                                Log.d(TAG, "A : " + a);
                            } else {
                                b++;
                                Log.d(TAG, "B : " + b);
                            }
                            QueueManager.addQueueFromDB(messageTransaction);
//                            QueueManager.customerList.add(messageTransaction);
                        }
                        Log.d(TAG, "QueueManager A size " + QueueManager.size("A"));
                        Log.d(TAG, "QueueManager B size " + QueueManager.size("B"));
                        laneA.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "onClick A");
                                laneA.setEnabled(false);
                                progressDialog.show();
                                MessageTransaction messageTransaction = QueueManager.pop("A");
                                if (messageTransaction != null) {
                                    Log.d(TAG, "onClick A inside if");
                                    messageTransaction.setBookingStatus_(MessageTransaction.BookingStatus.SUCCESS);

                                    updateStatus(messageTransaction);
                                    Lane lane = MerchantManager.get().getLane_();
                                    tvNumQA.setText(String.valueOf(lane.QA_));
//                    alertView("Queue A send complete");
                                } else {
                                    progressDialog.dismiss();
                                    laneA.setEnabled(true);
                                }
                            }
                        });

                        laneB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.d(TAG, "onClick B");
                                laneB.setEnabled(false);
                                progressDialog.show();
                                MessageTransaction messageTransaction = QueueManager.pop("B");
                                if (messageTransaction != null) {
                                    Log.d(TAG, "onClick B inside if");
                                    messageTransaction.setBookingStatus_(MessageTransaction.BookingStatus.SUCCESS);
                                    updateStatus(messageTransaction);
                                    Lane lane = MerchantManager.get().getLane_();
                                    tvNumQB.setText(String.valueOf(lane.QB_));
                                    alertView("Queue B send complete");
                                } else {
                                    progressDialog.dismiss();
                                    laneA.setEnabled(true);
                                }
                            }
                        });

                        Log.d(TAG, "[End for]");
                    } else {
                        Log.d(TAG, "No data");
                    }
                    progressDialog.dismiss();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error " + e);
                        progressDialog.dismiss();
                        alertView("Error " + e);
                    }
                });
    }

    private void updateStatus(MessageTransaction messageTransaction) {
        Log.d(TAG, "updateStatus");
        progressDialog.setMessage("Updating...status");
        db.collection("Booking").document(messageTransaction.getDocId())
                .update("booking_status", messageTransaction.getBookingStatus_())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "updateStatus onSuccess");
                        sendMessage(messageTransaction);
//                        updateMerchant(lane);
                        if (!laneA.isEnabled()) {
                            laneA.setEnabled(true);
                        }
                        if (!laneB.isEnabled()) {
                            laneB.setEnabled(true);
                        }

                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Update fail : " + e);
                        progressDialog.dismiss();
                    }
                });
    }

    private void updateMerchant(Lane lane) {
        Log.d(TAG, "updateMerchant");
        progressDialog.setMessage("Updating...merchant");
        db.collection("merchants").document(MerchantManager.get().getDocId_())
                .update(
                        "Lanes.QA", lane.getQA_(),
                        "Lanes.QB", lane.getQB_(),
                        "a_timestamp", lane.getA_timestamp_(),
                        "b_timestamp", lane.getB_timestamp_()
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "updateMerchant onSuccess");
                        Log.d(TAG, "Updated :");
                        if (!laneA.isEnabled()) {
                            laneA.setEnabled(true);
                        }
                        if (!laneB.isEnabled()) {
                            laneB.setEnabled(true);
                        }
                        tvNumQA.setText(String.valueOf(lane.QA_));
                        tvNumQB.setText(String.valueOf(lane.QB_));
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Update fail : " + e);
                        progressDialog.dismiss();
                    }
                });
    }
}