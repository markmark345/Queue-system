package com.example.final_project_cs361;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 * แสดงรายการของหน้าร้านค้า
 */
public class CardMerchantViewHolder extends RecyclerView.Adapter<CardMerchantViewHolder.MerchantViewHolder> {
    private static final String TAG = "CardMerchantViewHolder";
    ArrayList<Merchant> merchants;
    Context context;

    CardMerchantViewHolder(Context context, ArrayList merchants) {
        this.context = context;
        this.merchants = merchants;
    }

    @NonNull
    @Override
    public MerchantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ");
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_merchant, parent, false);
        // set the view's size, margins, paddings and layout parameters
 // pass the view to View Holder
        return new MerchantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchantViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder ");
        Merchant merchant = merchants.get(position);
        Lane lane = merchant.getLane_();
//        MerchantViewHolder holder = (MerchantViewHolder)holderParent;
        holder.tvName.setText(merchant.getName_());
        holder.tvToken.setText(merchant.getToken_());
        holder.tvPhone.setText(merchant.getPhone_());
        String numQString = String.valueOf(lane.getQA_() + lane.getQB_()) + " คิว";
        holder.tvNumQ.setText(numQString);

        int pos = position;
        holder.btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Merchant merchant = merchants.get(pos);
                MerchantManager.set(merchant);
                Intent intent = new Intent(context, ReserveQueue.class);
//                intent.putExtra("merchant", merchant);
                context.startActivity(intent);
            }
        });
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderParent, int position) {
//        Merchant merchant = merchants.get(position);
//        MerchantViewHolder holder = (MerchantViewHolder)holderParent;
//        holder.tvName.setText(merchant.getName_());
//        holder.tvToken.setText(merchant.getToken_());
//        holder.tvPhone.setText(merchant.getPhone_());
//
//        holder.btBook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d(TAG, "merchant : "+ merchant.getName_());
//                Intent intent = new Intent(context, ReserveQueue.class);
//                intent.putExtra("merchant", merchant);
//                context.startActivity(intent);
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return merchants.size();
    }

    public static class MerchantViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvToken;
        TextView tvPhone;
        TextView tvNumQ;
        Button btBook;

        public MerchantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.textViewMerchantID);
            tvToken = itemView.findViewById(R.id.textViewTokenID);
            tvPhone = itemView.findViewById(R.id.textViewPhoneID);
            tvNumQ = itemView.findViewById(R.id.num_queue);
            btBook = itemView.findViewById(R.id.buttonBookID);
        }
    }
}
