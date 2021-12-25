package com.example.final_project_cs361;

import java.io.Serializable;
import java.util.Date;

public class MessageTransaction {
    public enum BookingStatus { // implements Serializable {
        UNINITIALIZE,
        BOOKING,
        WAIT,
        CANCEL,
        SUCCESS;
    };
    private String docId;
    private String name_;
    private String email_;
    private String phone_;
    private String token_;
    private String date_;
    private String time_;
    private String merchantName_;
    private int seats_ = 0;
    private String lane_ = "x";
    private int qnum_ = 0;
    private BookingStatus bookingStatus_;
    private int mode = 0;
    private Date timeStamp_;

    public String getDocId() {
        return docId;
    }

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

    public int getSeats_() { return seats_; }

    public String getDate_() { return date_; }

    public String getTime_() {
        return time_;
    }

    public String getLane_() {
        return lane_;
    }

    public int getQnum_() {
        return qnum_;
    }

    public String getMerchantName_() {
        return merchantName_;
    }

    public BookingStatus getBookingStatus_() {
        return bookingStatus_;
    }

    public int getMode() {
        return mode;
    }

    public Date getTimeStamp_() {
        return timeStamp_;
    }

    public void setDocId(String docId) {
        this.docId = docId;
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

    public void setSeats_(int seats_) {
        this.seats_ = seats_;
    }

    public void setDate_(String date_) {
        this.date_ = date_;
    }

    public void setTime_(String time_) {
        this.time_ = time_;
    }

    public void setBookingStatus_(BookingStatus bookingStatus_) {
        this.bookingStatus_ = bookingStatus_;
    }

    public void setLane_(String lane_) {
        this.lane_ = lane_;
    }

    public void setQnum_(int qnum_) {
        this.qnum_ = qnum_;
    }

    public void setMerchantName_(String merchantName_) {
        this.merchantName_ = merchantName_;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setTimeStamp_(Date timeStamp_) {
        this.timeStamp_ = timeStamp_;
    }

    public MessageTransaction() {

    }

    public MessageTransaction(String name, String email, String phone, String token, String date, String time, int seats, String lane_, int qnum_, BookingStatus bookingStatus_, String merchantName) {
        this.name_ = name;
        this.email_ = email;
        this.phone_ = phone;
        this.token_ = token;
        this.date_ = date;
        this.time_ = time;
        this.seats_ = seats;
        this.lane_ = lane_;
        this.qnum_ = qnum_;
        this.bookingStatus_ = bookingStatus_;
        this.merchantName_ = merchantName;
    }

}
