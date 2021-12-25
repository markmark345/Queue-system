package com.example.final_project_cs361;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class Merchant {
    final private String merchant_fs = "merchant.txt";

    private String docId_;
    private String name_;
    private String userName_;
    private String password_;
    private String email_;
    private String phone_;
    private String token_;
    private Lane lane_;

    public Merchant() {
        name_ = "";
        email_ = "";
        phone_ = "";
        userName_ = "";
        password_ = "";
        lane_ = new Lane();
    }

    public String getDocId_() {
        return docId_;
    }

    public String getName_() {
        return name_;
    }

    public String getToken_() {
        return token_;
    }

    public String getEmail_() {
        return email_;
    }

    public String getPhone_() {
        return phone_;
    }

    public String getUserName_() {
        return userName_;
    }

    public String getPassword_() {
        return password_;
    }

    public Lane getLane_() {
        return lane_;
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

    public void setPassword_(String password_) {
        this.password_ = password_;
    }

    public void setUserName_(String userName_) {
        this.userName_ = userName_;
    }

    public void setToken_(String token_) {
        this.token_ = token_;
    }

    public void setDocId_(String docId_) {
        this.docId_ = docId_;
    }

    public void setLane_(Lane lane_) {
        this.lane_ = lane_;
    }

    public void loadMerchant(Context ctx) {
        FileInputStream fis = null;

        try {
            fis = ctx.openFileInput(merchant_fs);
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
                        userName_ = text;
                    case 5:
                        password_ = text;

                }
                i++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(Context ctx, String name, String email, String phone, String userName, String password, String token) {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("\n");
        sb.append(userName);
        sb.append("\n");
        sb.append(password);
        sb.append("\n");
        sb.append(email);
        sb.append("\n");
        sb.append(phone);
        sb.append("\n");
        sb.append(token);
        sb.append("\n");

        FileOutputStream fOut;

        try {
            fOut = ctx.openFileOutput(merchant_fs, Context.MODE_APPEND);
            fOut.write(sb.toString().getBytes());
            fOut.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//public class Merchant implements Parcelable {
//    final private String merchant_fs = "merchant.txt";
//
//    public static class Lane implements Parcelable {
//        int A_;
//        int B_;
//        int QA_;
//        int QB_;
//        Date a_timestamp_;
//        Date b_timestamp_;
//
//        public Lane(int a, int b, int qa, int qb, Date a_timestamp, Date b_timestamp) {
//            A_ = a;
//            B_ = b;
//            QA_ = qa;
//            QB_ = qb;
//            a_timestamp_ = a_timestamp;
//            b_timestamp_ = b_timestamp;
//        }
//        protected Lane(Parcel in) {
//            A_ = in.readInt();
//            B_ = in.readInt();
//            QA_ = in.readInt();
//            QB_ = in.readInt();
//            a_timestamp_ = new Date(in.readLong());
//            b_timestamp_ = new Date(in.readLong());
//        }
//
//        public static final Creator<Lane> CREATOR = new Creator<Lane>() {
//            @Override
//            public Lane createFromParcel(Parcel in) {
//                return new Lane(in);
//            }
//
//            @Override
//            public Lane[] newArray(int size) {
//                return new Lane[size];
//            }
//        };
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel parcel, int i) {
//            parcel.writeInt(A_);
//            parcel.writeInt(B_);
//            parcel.writeInt(QA_);
//            parcel.writeInt(QB_);
//            parcel.writeLong(a_timestamp_.getTime());
//            parcel.writeLong(b_timestamp_.getTime());
//        }
//
//        public int getA_() {
//            return A_;
//        }
//
//        public int getB_() {
//            return B_;
//        }
//
//        public int getQA_() {
//            return QA_;
//        }
//
//        public int getQB_() {
//            return QB_;
//        }
//
//        public Date getA_timestamp_() {
//            return a_timestamp_;
//        }
//
//        public Date getB_timestamp_() {
//            return b_timestamp_;
//        }
//
//        public void setA_(int a_) {
//            A_ = a_;
//        }
//
//        public void setB_(int b_) {
//            B_ = b_;
//        }
//
//        public void setQA_(int QA_) {
//            this.QA_ = QA_;
//        }
//
//        public void setQB_(int QB_) {
//            this.QB_ = QB_;
//        }
//
//        public void setA_timestamp_(Date a_timestamp_) {
//            this.a_timestamp_ = a_timestamp_;
//        }
//
//        public void setB_timestamp_(Date b_timestamp_) {
//            this.b_timestamp_ = b_timestamp_;
//        }
//    }
//
//    private String docId_;
//    private String name_;
//    private String userName_;
//    private String password_;
//    private String email_;
//    private String phone_;
//    private String token_;
//    private Lane lane_;
//
//    public Merchant() {
//        name_ = "";
//        email_ = "";
//        phone_ = "";
//        userName_ = "";
//        password_ = "";
//    }
//
//    protected Merchant(Parcel in) {
//        name_ = in.readString();
//        email_ = in.readString();
//        phone_ = in.readString();
//        userName_ = in.readString();
//        password_ = in.readString();
//        token_ = in.readString();
//        lane_ = Lane.CREATOR.createFromParcel(in);
//    }
//
//    public static final Parcelable.Creator<Merchant> CREATOR = new Parcelable.Creator<Merchant>() {
//        @Override
//        public Merchant createFromParcel(Parcel in) {
//            return new Merchant(in);
//        }
//
//        @Override
//        public Merchant[] newArray(int size) {
//            return new Merchant[size];
//        }
//    };
//
//    public String getDocId_() {
//        return docId_;
//    }
//
//    public String getName_() {
//        return name_;
//    }
//
//    public String getToken_() {
//        return token_;
//    }
//
//    public String getEmail_() {
//        return email_;
//    }
//
//    public String getPhone_() {
//        return phone_;
//    }
//
//    public String getUserName_() {
//        return userName_;
//    }
//
//    public String getPassword_() {
//        return password_;
//    }
//
//    public Lane getLane_() {
//        return lane_;
//    }
//
//    public void setName_(String name_) {
//        this.name_ = name_;
//    }
//
//    public void setEmail_(String email_) {
//        this.email_ = email_;
//    }
//
//    public void setPhone_(String phone_) {
//        this.phone_ = phone_;
//    }
//
//    public void setPassword_(String password_) {
//        this.password_ = password_;
//    }
//
//    public void setUserName_(String userName_) {
//        this.userName_ = userName_;
//    }
//
//    public void setToken_(String token_) {
//        this.token_ = token_;
//    }
//
//    public void setDocId_(String docId_) {
//        this.docId_ = docId_;
//    }
//
//    public void setLane_(Lane lane_) {
//        this.lane_ = lane_;
//    }
//
//    public void loadMerchant(Context ctx) {
//        FileInputStream fis = null;
//
//        try {
//            fis = ctx.openFileInput(merchant_fs);
//            InputStreamReader isr = new InputStreamReader(fis);
//            BufferedReader br = new BufferedReader(isr);
//            String text;
//
//            int i = 1;
//            while ((text = br.readLine()) != null) {
//                switch (i) {
//                    case 1:
//                        name_ = text;
//                        break;
//                    case 2:
//                        email_ = text;
//                        break;
//                    case 3:
//                        phone_ = text;
//                    case 4:
//                        userName_ = text;
//                    case 5:
//                        password_ = text;
//
//                }
//                i++;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void register(Context ctx, String name, String email, String phone, String userName, String password, String token) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(name);
//        sb.append("\n");
//        sb.append(userName);
//        sb.append("\n");
//        sb.append(password);
//        sb.append("\n");
//        sb.append(email);
//        sb.append("\n");
//        sb.append(phone);
//        sb.append("\n");
//        sb.append(token);
//        sb.append("\n");
//
//        FileOutputStream fOut;
//
//        try {
//            fOut = ctx.openFileOutput(merchant_fs, Context.MODE_APPEND);
//            fOut.write(sb.toString().getBytes());
//            fOut.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(name_);
//        parcel.writeString(email_);
//        parcel.writeString(phone_);
//        parcel.writeString(userName_);
//        parcel.writeString(password_);
//        parcel.writeString(token_);
//        parcel.writeParcelable(lane_, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
//    }
//}
