package com.example.final_project_cs361;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link user_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class user_fragment extends Fragment {
    Customer customer;

    public user_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment user_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static user_fragment newInstance() {
        return new user_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customer = new Customer();
        customer.loadCustomer(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_fragment, container, false);
        TextView tvName = view.findViewById(R.id.user_username);
        tvName.setText(customer.getName_());
        TextView tvEmail = view.findViewById(R.id.user_email);
        tvEmail.setText(customer.getEmail_());
        TextView tvPhone = view.findViewById(R.id.user_tel_num);
        tvPhone.setText(customer.getPhone_());
        return view;
    }
}