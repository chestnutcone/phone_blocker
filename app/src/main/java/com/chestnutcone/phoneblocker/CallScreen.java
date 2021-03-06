package com.chestnutcone.phoneblocker;

import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.util.Log;
import android.widget.Switch;

import androidx.annotation.NonNull;

public class CallScreen extends CallScreeningService {
    public static final String TAG = CallScreen.class.getSimpleName();
    private PhoneRegex phoneRegex = new PhoneRegex();

    @Override
    public void onScreenCall(@NonNull Call.Details callDetails) {
        CallResponse callResponse;
        CallResponse.Builder responseBuilder = new CallResponse.Builder();
        Log.d(TAG, "onScreenCall: "+callDetails.getHandle()+" eof");
        if (callDetails.getHandle() == null) {
            if (phoneRegex.getUnknownCallerSettings(getApplicationContext())) {
                callResponse = responseBuilder.setDisallowCall(true).setRejectCall(true).setSilenceCall(true).setSkipCallLog(false).setSkipNotification(true).build();
            } else {
                callResponse = responseBuilder.setDisallowCall(false).setRejectCall(false).setSilenceCall(false).setSkipCallLog(false).setSkipNotification(false).build();
            }
            respondToCall(callDetails, callResponse);
        } else {
            String phoneNumber = callDetails.getHandle().toString();
            phoneNumber = phoneNumber.substring(4).replace("%2B", "+"); // utf-8 country code

            if (callDetails.getCallDirection() == Call.Details.DIRECTION_INCOMING) {
                if (phoneRegex.rejectCall(getApplicationContext(), phoneNumber)) {
                    callResponse = responseBuilder.setDisallowCall(true).setRejectCall(true).setSilenceCall(true).setSkipCallLog(false).setSkipNotification(true).build();
                } else {
                    callResponse = responseBuilder.setDisallowCall(false).setRejectCall(false).setSilenceCall(false).setSkipCallLog(false).setSkipNotification(false).build();
                }
                respondToCall(callDetails, callResponse);
            }
        }
    }
}
