package com.example.final_project_cs361;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.ArrayList;

/*
 * แสดงรายการของลูกค้าที่จองคิว
 */
public class CardCustomerViewHolder extends RecyclerView.Adapter<CardCustomerViewHolder.CustomerViewHolder> {
    private static final String TAG = "CardCustomerViewHolder";
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    ArrayList<MessageTransaction> customers;
    Context context;
    MerchantMainFragment merchantMainFragment;

    public CardCustomerViewHolder(Context context, /*ArrayList<MessageTransaction> customers,*/ MerchantMainFragment merchantMainFragment) {
        this.context = context;
//        this.customers = customers;
        this.merchantMainFragment = merchantMainFragment;

        Log.d(TAG, "CardCustomerViewHolder customers size = " + QueueManager.customerList.size());
    }


    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder ");
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_customer_booking, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // pass the view to View Holder
        return new CardCustomerViewHolder.CustomerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder ");
        MessageTransaction customer = QueueManager.customerList.get(position);

        holder.tvName.setText(customer.getName_());
        holder.tvToken.setText(customer.getToken_());
        holder.tvPhone.setText(customer.getPhone_());
        holder.tvDate.setText(customer.getDate_());
        holder.tvTime.setText(customer.getTime_());
        holder.tvSeat.setText(String.valueOf(customer.getSeats_()));

        int pos = position;
        // ส่งข้อความตอบรับ
        holder.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.btCancel.setEnabled(false);
                holder.btSubmit.setEnabled(false);
                MessageTransaction messageTransaction = customer;
                messageTransaction.setBookingStatus_(MessageTransaction.BookingStatus.WAIT);
                MessageTransaction result = QueueManager.add(messageTransaction);
                db.collection("Booking").document(messageTransaction.getDocId())
                        .update("booking_status", MessageTransaction.BookingStatus.WAIT, "lane", result.getLane_(), "qnum", result.getQnum_())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                QueueManager.customerList.remove(pos);
//                                CustomerBookingQueueManager.set(result);
                                sendMessage(result);
                                notifyDataSetChanged();
                                merchantMainFragment.onRefresh();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Update fail : " + e);
//                                QueueManager.customerList.remove(pos);
//                                MessageTransaction result = QueueManager.add(messageTransaction);
//                                CustomerBookingQueueManager.set(result);
                                notifyDataSetChanged();
                                merchantMainFragment.onRefresh();
                            }
                        });
            }
        });

        holder.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageTransaction transaction = QueueManager.customerList.get(pos);
                db.collection("Booking").document(transaction.getDocId())
                        .update("booking_status", MessageTransaction.BookingStatus.CANCEL)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                QueueManager.customerList.remove(pos);
                                notifyDataSetChanged();
                                merchantMainFragment.onRefresh();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Update fail : " + e);
                                notifyDataSetChanged();
                                merchantMainFragment.onRefresh();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return QueueManager.customerList.size();
    }

    private void sendMessage(MessageTransaction messageTransaction) {
        Log.d(TAG, "sendMessage");
        String title = "Your Q is " + messageTransaction.getLane_() + messageTransaction.getQnum_();
        Log.d(TAG, title);
        String body = "Please don't go too far!!!";
        String to = messageTransaction.getToken_();
        messageTransaction.setMode(1);
        JSONObject msgObj = JSONUtil.CreateJSONOBJ(messageTransaction, title, body, to);
        new CallAPI().execute(FCM_MESSAGE_URL, "POST", msgObj.toString());
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvToken;
        TextView tvPhone;
        TextView tvDate;
        TextView tvTime;
        TextView tvSeat;
        Button btSubmit;
        Button btCancel;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.card_cus_name);
            tvToken = itemView.findViewById(R.id.card_cus_token_id);
            tvPhone = itemView.findViewById(R.id.card_cus_phone);
            tvDate = itemView.findViewById(R.id.card_cus_date);
            tvTime = itemView.findViewById(R.id.card_cus_time);
            tvSeat = itemView.findViewById(R.id.card_cus_num_seat);
            btSubmit = itemView.findViewById(R.id.card_cus_button_submit);
            btCancel = itemView.findViewById(R.id.card_cus_button_cancel);
        }
    }
}
