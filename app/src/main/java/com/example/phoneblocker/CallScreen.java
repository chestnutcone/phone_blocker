package com.example.phoneblocker;

import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.util.Log;

import androidx.annotation.NonNull;

public class CallScreen extends CallScreeningService {
    public static final String TAG = CallScreen.class.getSimpleName();
    private PhoneRegex phoneRegex = new PhoneRegex();

    @Override
    public void onScreenCall(@NonNull Call.Details callDetails) {
        CallResponse.Builder responseBuilder = new CallResponse.Builder();
        Log.d(TAG, "onScreenCall: CALL RESPONSE");
        String phoneNumber = callDetails.getHandle().toString();
        Log.d(TAG, "onScreenCall: "+phoneNumber);
        phoneNumber = phoneNumber.substring(4).replace("%2B", "+"); // utf-8 country code
        Log.d(TAG, "onScreenCall: Substring "+phoneNumber);

        if (callDetails.getCallDirection() == Call.Details.DIRECTION_INCOMING) {
            Log.d(TAG, "onScreenCall: INSIDE");
            CallResponse callResponse;
            if (phoneRegex.rejectCall(getApplicationContext(), phoneNumber)) {
                Log.d(TAG, "onScreenCall: REJECTING");
                callResponse = responseBuilder.setDisallowCall(true).setRejectCall(true).setSilenceCall(true).setSkipCallLog(false).setSkipNotification(true).build();
            } else {
                Log.d(TAG, "onScreenCall: NOT REJECTING");
                callResponse = responseBuilder.setDisallowCall(false).setRejectCall(false).setSilenceCall(false).setSkipCallLog(false).setSkipNotification(false).build();
            }

            respondToCall(callDetails, callResponse);
        }
    }
}
