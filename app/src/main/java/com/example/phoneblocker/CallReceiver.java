package com.example.phoneblocker;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        if (number == null)
        {
            return;
        }
        String[] blockedNumber = getPhoneNumbers(ctx);
        for(String blockedNum : blockedNumber)
        {
            if (Pattern.matches(blockedNum, number)) {
                declinePhone(ctx);
            }
        }
    }

    @Override
    protected void onIncomingCallAnswered(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start)
    {
        //
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end)
    {
        //
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start)
    {
        //
    }

    public String[] getPhoneNumbers(Context ctx){
        String yourFilePath = ctx.getFilesDir().toString();
        File myFile = new File(yourFilePath, "phone.txt");

        StringBuilder phoneText = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(myFile));
            String line;
            while ((line = br.readLine()) != null ) {
                phoneText.append(line);
            }
            br.close();

            return phoneText.toString().split(",");
        } catch (IOException e) {

        }
        return new String[0];
    }

    private void declinePhone(Context context) {
        TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        if (tm != null) {
            boolean success = tm.endCall();
            // success == true if call was terminated.
        }
    }
}