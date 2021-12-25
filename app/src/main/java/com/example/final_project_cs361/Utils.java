package com.example.final_project_cs361;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Utils {

    public static Map<String, String> bundleToMap(Bundle extras) {
        Map<String, String> map = new HashMap<String, String>();

        Set<String> ks = extras.keySet();
        Iterator<String> iterator = ks.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, extras.getString(key));
        }//w  w  w.  j a  v  a  2s. co m
        return map;
    }

    public static void receiveMessageData(Bundle bundle) {
        Map<String, String> map = bundleToMap(bundle);
        receiveMessageData(map);
    }

    public static MessageTransaction.BookingStatus ConvertBookingStatusStringToEnum(String text) {
        switch (text) {
            case "UNINITIALIZE":
                return MessageTransaction.BookingStatus.UNINITIALIZE;
            case "BOOKING":
                return MessageTransaction.BookingStatus.BOOKING;
            case "WAIT":
                return MessageTransaction.BookingStatus.WAIT;
            case "CANCEL":
                return MessageTransaction.BookingStatus.CANCEL;
            case "SUCCESS":
                return MessageTransaction.BookingStatus.SUCCESS;
        }
        return MessageTransaction.BookingStatus.UNINITIALIZE;
    }

    public static String ConvertBookingStatusEnumToString(MessageTransaction.BookingStatus status) {
        switch (status) {
            case UNINITIALIZE:
                return "UNINITIALIZE";
            case BOOKING:
                return "BOOKING";
            case WAIT:
                return "WAIT";
            case CANCEL:
                return "CANCEL";
            case SUCCESS:
                return "SUCCESS";
        }
        return "UNINITIALIZE";
    }
    public static void receiveMessageData(Map<String, String> message) {
        int mode = Integer.parseInt(message.get("mode"));
        MessageTransaction bookingTransaction = new MessageTransaction();
        try {
            bookingTransaction.setDocId(message.get(""));
            bookingTransaction.setName_(message.get("name"));
            bookingTransaction.setToken_(message.get("token"));
            bookingTransaction.setPhone_(message.get("phone"));
            bookingTransaction.setEmail_(message.get("email"));
            bookingTransaction.setSeats_(Integer.parseInt(message.get("numberOfSeats")));
            bookingTransaction.setDate_(message.get("date"));
            bookingTransaction.setTime_(message.get("time"));
            bookingTransaction.setMerchantName_(message.get("merchantName"));
            bookingTransaction.setQnum_(Integer.parseInt(message.get("qnum")));
            bookingTransaction.setLane_(message.get("lane"));
            bookingTransaction.setMode(mode);

            bookingTransaction.setBookingStatus_(ConvertBookingStatusStringToEnum(message.get("bookingStatus")));

            Log.d("Utils", "Booking : " + bookingTransaction);
        } catch (Exception ex) {
            Log.d("Utils", "Exception: " + ex);
        }

        if (mode == 1) { // Send to customer
            CustomerBookingQueueManager.set(bookingTransaction);
        } else if (mode == 0) {  // Send to Merchants
//            CustomerBookingNotifyManagement.add(customer);
        }
    }
}
