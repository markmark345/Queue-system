package com.example.final_project_cs361;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MerchantMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MerchantMainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MerchantMainFragment";
    SwipeRefreshLayout mSwipeRefreshLayout;
    CardCustomerViewHolder cardCustomerViewHolder;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;

    public MerchantMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MerchantMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchantMainFragment newInstance() {
        return new MerchantMainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.merchantRecyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshMerchant);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                // Fetching data from server
                loadRecyclerViewData();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onRefresh() {
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {
        Log.d(TAG, "START loadRecyclerViewData");
        mSwipeRefreshLayout.setRefreshing(true);

        QueueManager.customerList.clear();

        db.collection("Booking")
                .whereEqualTo("merchant_name", MerchantManager.get().getName_())
                .whereEqualTo("booking_status", "UNINITIALIZE")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "addOnCompleteListener");
                    if (task.isSuccessful()) {
                        Log.d(TAG, "isSuccessful");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, (String) document.get("name"));
                            Log.d(TAG, (String) document.get("merchant_name"));
                            Log.d(TAG, (String) document.get("date"));
                            Log.d(TAG, (String) document.get("email"));

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

                            QueueManager.customerList.add(messageTransaction);
                        }

                        Log.d(TAG, "Customer List size : " + QueueManager.customerList.size());
                        cardCustomerViewHolder = new CardCustomerViewHolder(getActivity(), /*QueueManager.customerList,*/ this);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(cardCustomerViewHolder);
                        mSwipeRefreshLayout.setRefreshing(false);

                        Log.d(TAG, "[End for]");
                    } else {
                        Log.d(TAG, "No data");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error " + e);
                    }
                });

        Log.d(TAG, "END loadRecyclerViewData");
    }

}