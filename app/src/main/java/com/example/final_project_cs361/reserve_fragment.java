package com.example.final_project_cs361;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reserve_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reserve_fragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "reserve_fragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Merchant> merchants = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    CardMerchantViewHolder cardMerchantViewHolder;

    public reserve_fragment() {
        // Required empty public constructor
//        getMerchantList();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment reserve_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static reserve_fragment newInstance() {
        return new reserve_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(getContext(), "onCreate", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        Log.d(TAG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_reserve_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshCustomer);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

//                mSwipeRefreshLayout.setRefreshing(true);
                // Fetching data from server
                loadRecyclerViewData();
            }
        });

//        Toast.makeText(getContext(), "onCreateView", Toast.LENGTH_SHORT).show();

        return view;
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        // Fetching data from server
        loadRecyclerViewData();
    }

    private void loadRecyclerViewData()
    {
        // Showing refresh animation before making http call
        mSwipeRefreshLayout.setRefreshing(true);

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

//                    Map<String, Object> map = document.getData();
//                    for (String key : map.keySet()) {
//                        Object value = map.get(key);
//                        Log.d(TAG, "Key: " + key + " Value: " + value);
//                    }

                    Lane lane = new Lane();
                    Map<String, Object> mapLane = (Map<String, Object>)document.get("Lanes");
                    lane.setA_( Integer.parseInt(mapLane.get("A").toString()));
                    lane.setB_( Integer.parseInt(mapLane.get("B").toString()));
                    lane.setQA_( Integer.parseInt(mapLane.get("QA").toString()));
                    lane.setQB_( Integer.parseInt(mapLane.get("QB").toString()));
                    lane.setA_timestamp_(((Timestamp) mapLane.get("a_timestamp")).toDate());
                    lane.setB_timestamp_(((Timestamp) mapLane.get("b_timestamp")).toDate());

                    merchant.setLane_(lane);
//                    for (String key : mapLane.keySet()) {
//                        Object value = mapLane.get(key);
//                        Log.d(TAG, "Lane Key: " + key + " Lane Value: " + value);
//                    }

                    merchants.add(merchant);
                }
                if (merchants.size() > 0) {
                    Log.d(TAG, "OK");
                    cardMerchantViewHolder = new CardMerchantViewHolder(getActivity(), merchants);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(cardMerchantViewHolder);
                } else {
                    Log.d(TAG, "No data");
                }
            } else {
                Log.d(TAG, "No data");
            }
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }
}