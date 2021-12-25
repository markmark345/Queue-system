package com.example.final_project_cs361;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class QueueManager {
    private static final String TAG = "QueueManager";
    private static Map<String, Queue<MessageTransaction>> mapQueue_ = new HashMap<>();
    private final static String a = "A";
    private final static String b = "B";
//    private static int aq = 1;
//    private static int bq = 1;
    public static ArrayList<MessageTransaction> customerList = new ArrayList<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static Comparator<MessageTransaction> idComparator = new Comparator<MessageTransaction>() {
        @Override
        public int compare(MessageTransaction messageTransaction, MessageTransaction t1) {
            return (messageTransaction.getQnum_() - t1.getQnum_());
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addQueueFromDB(MessageTransaction messageTransaction) {
        Queue<MessageTransaction> q = null;
        if (messageTransaction.getLane_().equals(a)) {
            if (!mapQueue_.containsKey(a)) {
                Log.d(TAG, "not containkey A");
                q = new PriorityQueue<MessageTransaction>(idComparator);
                mapQueue_.put(a, q);
            } else {
                Log.d(TAG, "containkey A");
                q = mapQueue_.get(a);
                if (q == null) {
                    Log.d(TAG, "qa is null");
                }
            }

        } else {
            if (!mapQueue_.containsKey(b)) {
                Log.d(TAG, "not containkey B");
                q = new PriorityQueue<MessageTransaction>(idComparator);
                mapQueue_.put(b, q);
            } else {
                q = mapQueue_.get(b);
                if (q == null) {
                    Log.d(TAG, "qb is null");
                }
            }
        }
        try {
            Log.d(TAG, "q add");
            q.add(messageTransaction);
            Log.d(TAG, "q add ok");
            Log.d(TAG, "end add q size is " + q.size());
            Log.d(TAG, "q num is " + messageTransaction.getQnum_());
        } catch (Exception ex) {
            Log.d(TAG, "Error : " + ex);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static MessageTransaction add(MessageTransaction messageTransaction) {
        Log.d(TAG, "add");
        MessageTransaction messageTransaction1 = new MessageTransaction();
        messageTransaction1.setTime_(messageTransaction.getTime_());
        messageTransaction1.setEmail_(messageTransaction.getEmail_());
        messageTransaction1.setName_(messageTransaction.getName_());
        messageTransaction1.setDate_(messageTransaction.getDate_());
        messageTransaction1.setPhone_(messageTransaction.getPhone_());
        messageTransaction1.setSeats_(messageTransaction.getSeats_());
        messageTransaction1.setToken_(messageTransaction.getToken_());
        messageTransaction1.setMerchantName_(messageTransaction.getMerchantName_());
        Log.d(TAG, "seats " + messageTransaction.getSeats_() + " " + messageTransaction.getName_());
        Queue<MessageTransaction> q = null;

        Lane lane = MerchantManager.get().getLane_();
//        int aq = lane.QA_;
//        int bq = lane.QB_;
        Date a_ts = lane.a_timestamp_;
        Date b_ts = lane.a_timestamp_;
        if (messageTransaction1.getSeats_() >= 1 && messageTransaction1.getSeats_() <= lane.getA_()) {
            Log.d(TAG, "seats >= 1 && <= 4");
            if (!mapQueue_.containsKey(a)) {
                Log.d(TAG, "not containkey A");
                q = new PriorityQueue<>(idComparator);
                mapQueue_.put(a, q);
            } else {
                Log.d(TAG, "containkey A");
                q = mapQueue_.get(a);
            }

//            aq++;
            lane.QA_++;
            a_ts = new Date();
            messageTransaction1.setLane_(a);
            messageTransaction1.setQnum_(lane.QA_);
        } else {
            Log.d(TAG, "seats > 4");
            if (!mapQueue_.containsKey(b)) {
                Log.d(TAG, "not containkey B");
                q = new PriorityQueue<>(idComparator);
                mapQueue_.put(b, q);
            } else {
                q = mapQueue_.get(b);
            }
//            bq++;
            lane.QB_++;
            b_ts = new Date();
            messageTransaction1.setLane_(b);
            messageTransaction1.setQnum_(lane.QB_);
        }

        try {
            q.add(messageTransaction1);

            Log.d(TAG, "end add q size is " + q.size());

            Log.d(TAG, "q num is " + messageTransaction1.getQnum_());
        } catch (Exception ex) {
            Log.d(TAG, "Error : " + ex);
        }

        db.collection("merchants").document(MerchantManager.get().getDocId_())
            .update(
                    "Lanes.QA", lane.QA_,
                    "Lanes.QB", lane.QB_,
                    "a_timestamp", a_ts,
                    "b_timestamp", b_ts
                    )
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Updated :");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Update fail : " + e);
                }
            });
        return messageTransaction1;
    }

    public static MessageTransaction pop(String laneString) {
        Log.d(TAG, "pop ");
        if (mapQueue_ == null) {
            Log.d(TAG, "pop mapQueue is null");
            return  null;
        }
        Queue<MessageTransaction> q = mapQueue_.get(laneString);
        if (q != null) {
            Lane lane = MerchantManager.get().getLane_();

            Date a_ts = lane.a_timestamp_;
            Date b_ts = lane.a_timestamp_;
            MessageTransaction messageTransaction = q.poll();
            if (laneString.equals(a)) {
                lane.QA_--;
                a_ts = new Date();
            } else {
                lane.QB_--;
                b_ts = new Date();
            }
            db.collection("merchants").document(MerchantManager.get().getDocId_())
                    .update(
                            "Lanes.QA", lane.QA_,
                            "Lanes.QB", lane.QB_,
                            "a_timestamp", a_ts,
                            "b_timestamp", b_ts
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Updated :");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Update fail : " + e);
                        }
                    });
            return messageTransaction;
        }
        return null;
    }

    public static MessageTransaction front(String lane) {
        Queue<MessageTransaction> q = mapQueue_.get(lane);
        if (q == null) {
            return null;
        }
        return q.peek();
    }

    public static int size(String lane) {
        Queue<MessageTransaction> q = mapQueue_.get(lane);
        if (q == null) {
            return 0;
        }
        return q.size();
    }

    public static void reset() {
        db.collection("merchants").document(MerchantManager.get().getDocId_())
                .update(
                        "Lanes.QA", 0,
                        "Lanes.QB", 0,
                        "a_timestamp", new Date(),
                        "b_timestamp", new Date()
                )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        MerchantManager.get().getLane_().setQA_(0);
                        MerchantManager.get().getLane_().setQB_(0);
                        Log.d(TAG, "Updated :");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Update fail : " + e);
                    }
                });
    }

    public static void clear() {
        mapQueue_.clear();
    }
}
