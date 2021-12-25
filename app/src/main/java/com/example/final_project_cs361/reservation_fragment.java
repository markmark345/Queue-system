package com.example.final_project_cs361;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reservation_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reservation_fragment extends Fragment {
    private static final String TAG = "reservation_fragment";
    TextView mName, mNumQueue;

    public reservation_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment reservation_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static reservation_fragment newInstance() {
        return  new reservation_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_fragment, container, false);

        mName = view.findViewById(R.id.reserve_name_merchant);
        mNumQueue = view.findViewById(R.id.reserve_num_queue);

//        Log.d(TAG, CustomerBookingQueueManager.get().getName_());
        if (!CustomerBookingQueueManager.isNull()) {
            Log.d(TAG, "is not Null");
            String a = String.valueOf(CustomerBookingQueueManager.get().getLane_()) + String.valueOf(CustomerBookingQueueManager.get().getQnum_()) ;
            mName.setText(CustomerBookingQueueManager.get().getMerchantName_());
            mNumQueue.setText(a);
        } else {
            Log.d(TAG, "isNull");
            mName.setText("-");
            mNumQueue.setText("0");
        }

        return view;
    }
}