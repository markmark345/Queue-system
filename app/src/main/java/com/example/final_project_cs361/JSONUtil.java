package com.example.final_project_cs361;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {
    private static final String TAG = "JSONUtil";

    public static JSONObject CreateJSONOBJ(MessageTransaction messageTransaction, String title, String body, String to) {
        JSONObject msgObj = new JSONObject();
        try {

            msgObj.put("to", to);
            JSONObject notifyObj = new JSONObject();
            notifyObj.put("body", body);
            notifyObj.put("title", title);

            msgObj.put("notification", notifyObj);
            JSONObject dataObj = new JSONObject();
            dataObj.put("doc_id", messageTransaction.getDocId());
            dataObj.put("name", messageTransaction.getName_());
            dataObj.put("phone", messageTransaction.getPhone_());
            dataObj.put("email", messageTransaction.getEmail_());
            dataObj.put("date", messageTransaction.getDate_());
            dataObj.put("time", messageTransaction.getTime_());
            dataObj.put("numberOfSeats", messageTransaction.getSeats_());
            dataObj.put("token", messageTransaction.getToken_());
            dataObj.put("lane", messageTransaction.getLane_());
            dataObj.put("qnum", messageTransaction.getQnum_());
            dataObj.put("merchantName", messageTransaction.getMerchantName_());
            dataObj.put("bookingStatus", messageTransaction.getBookingStatus_());
            dataObj.put("mode", messageTransaction.getMode());

            msgObj.put("data", dataObj);
        } catch (JSONException ex) {
            Log.d(TAG, "JSONException " + ex);

        } catch (Exception ex) {
            Log.d(TAG, "Exception " + ex);
        }
        return msgObj;
    }
}
