package in.gohelper.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import in.gohelper.activity.OtpVerifyActivity;


public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    OtpVerifyActivity main;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "sms");
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderAddress = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    Log.d(TAG, "Received SMS: " + message + ", Sender: " + senderAddress);
                    if (!senderAddress.contains("goHelp")) {
                        return;
                    }
                    String verificationCode = getVerificationCode(message);

                    Log.d(TAG, "OTP received: " + verificationCode);
                    if (main != null) {
                        main.receiveSms(verificationCode);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: ");
            e.printStackTrace();
        }
    }

    private String getVerificationCode(String message) {

        String[] code = message.split(" ");
        return code[3];
    }


    public void setMainActivityHandler(OtpVerifyActivity main) {
        this.main = main;
    }


}

