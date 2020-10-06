package com.example.phoneblocker;

import android.content.Context;
import android.provider.ContactsContract;
import android.telecom.TelecomManager;
import java.util.Date;
import java.util.regex.Pattern;

public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallReceived(Context ctx, String number, Date start)
    {
        if (number == null)
        {
            return;
        }
        PhoneRegex phoneRegex = new PhoneRegex();
        if (phoneRegex.checkExist(ctx, number, PhoneRegex.typeReject) && !phoneRegex.checkExist(ctx, number, PhoneRegex.typeExcept)) {
            declinePhone(ctx);
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

    private void declinePhone(Context context) {
        TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        if (tm != null) {
            boolean success = tm.endCall();
            // success == true if call was terminated.
        }
    }
}