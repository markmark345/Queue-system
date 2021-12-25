package com.example.final_project_cs361;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Customer implements Parcelable {
    final private String customer_fs = "customer.txt";
    private static final String TAG = "MainActivity";
    private String name_;
    private String email_;
    private String phone_;
    private String token_;


    public Customer() {
        name_ = "";
        email_ = "";
        phone_ = "";
        token_ = "";
    }

    protected Customer(Parcel in) {
        name_ = in.readString();
        email_ = in.readString();
        phone_ = in.readString();
        token_ = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getName_() {
        return name_;
    }

    public String getEmail_() {
        return email_;
    }

    public String getPhone_() {
        return phone_;
    }

    public String getToken_() {
        return token_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public void setEmail_(String email_) {
        this.email_ = email_;
    }

    public void setPhone_(String phone_) {
        this.phone_ = phone_;
    }

    public void setToken_(String token_) {
        this.token_ = token_;
    }

    public void loadCustomer(Context ctx) {
        FileInputStream fis = null;

        try {
            fis = ctx.openFileInput(customer_fs);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;

            int i = 1;
            while ((text = br.readLine()) != null) {
                switch (i) {
                    case 1:
                        name_ = text;
                        break;
                    case 2:
                        email_ = text;
                        break;
                    case 3:
                        phone_ = text;
                    case 4:
                        token_ = text;
                }
                i++;
            }

        } catch (Exception e) {
            Log.d(TAG, "Exception : " + e.toString());
            e.printStackTrace();
        }
    }

    public void register(Context ctx, String name, String email, String phone, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("\n");
        sb.append(email);
        sb.append("\n");
        sb.append(phone);
        sb.append("\n");
        sb.append(token);
        sb.append("\n");

        FileOutputStream fOut;

        try {
            fOut = ctx.openFileOutput(customer_fs, Context.MODE_APPEND);
            fOut.write(sb.toString().getBytes());
            fOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isRegisterd() {
        return (!name_.trim().isEmpty()) && (!email_.trim().isEmpty()) && (!phone_.trim().isEmpty());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name_);
        parcel.writeString(email_);
        parcel.writeString(phone_);
        parcel.writeString(token_);
    }
}
