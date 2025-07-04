package one.telefon.tele;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


//accounts
import android.content.ComponentName;
import java.lang.reflect.Method;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionInfo;
import android.telecom.PhoneAccountHandle;
import android.telephony.TelephonyManager;

//settings
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.System;   

public class TeleBroadcastEmiter {

    private static String TAG = "one.telefon.tele.TeleBroadcastEmiter";

    private Context context;

    public TeleBroadcastEmiter(Context context) {
        this.context = context;
    }

    public void fireStarted(Intent original/*, List<TeleAccount> accounts*/, List<TeleCall> calls/*, JSONObject settings*/) {
        try {
            JSONArray dataAccounts = new JSONArray();
            /*
            
            for (TeleAccount account : accounts) {
                dataAccounts.put(account.toJson());
            }*/

            try {
                JSONObject data1 = new JSONObject();
                SubscriptionManager mSubscriptionManager = SubscriptionManager.from(context);
                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);


                for (SubscriptionInfo sub : mSubscriptionManager.getActiveSubscriptionInfoList()) {
                    data1 = new JSONObject();
                    data1.put("mnc",sub.getMnc());
                    //29 data1.put("mncString",sub.getMncString());
                    data1.put("mnc",sub.getMcc());
                    //29 data1.put("mccString",sub.getMccString());

                    data1.put("carrierName",sub.getCarrierName());
                    data1.put("countryIso",sub.getCountryIso());
                    data1.put("displayName",sub.getDisplayName());
                    data1.put("iccid",sub.getIccId());
                    data1.put("iconTint",sub.getIconTint());

                    data1.put("number",sub.getNumber());
                    data1.put("simSlotIndex",sub.getSimSlotIndex());
                    data1.put("subscriptionId",sub.getSubscriptionId());
                    
                    data1.put("imei",telephonyManager.getDeviceId(sub.getSimSlotIndex()));
                    

                    
                    //int subscriptionId = SubscriptionManager.from(mContext).getActiveSubscriptionInfoForSimSlotIndex(slotIndex).getSubscriptionId();
                    int subscriptionId = sub.getSubscriptionId();
                    try {
                        Class c = Class.forName("android.telephony.TelephonyManager");
                        Method m = c.getMethod("getSubscriberId", new Class[] {int.class});
                        Object o = m.invoke(telephonyManager, new Object[]{subscriptionId});

                        String subscriberId = (String) o;
                        data1.put("imsi",subscriberId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    dataAccounts.put(data1);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            

            JSONArray dataCalls = new JSONArray();
            for (TeleCall call : calls) {
                dataCalls.put(call.toJson());
            }

            JSONObject dataSettings = new JSONObject();
            String androidID = System.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
            dataSettings.put("android_id", androidID);
            dataSettings.put("android_sdk", Build.VERSION.SDK_INT);
            dataSettings.put("android_security_patch", Build.VERSION.SECURITY_PATCH);
            dataSettings.put("android_codename", Build.VERSION.CODENAME);
            dataSettings.put("android_incremental", Build.VERSION.INCREMENTAL);
            dataSettings.put("android_release", Build.VERSION.RELEASE);

            dataSettings.put("manufacturer", Build.MANUFACTURER);
            dataSettings.put("model", Build.MODEL);




            JSONObject data = new JSONObject();
            data.put("accounts", dataAccounts);
            data.put("calls", dataCalls);
            data.put("settings", dataSettings);

            /*
            mTelephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
             
             mGSMIdle = (mTelephonyManager.getCallState() == TelephonyManager.CALL_STATE_IDLE);
            */

            /*
    if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
        deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    settings

    import android.provider.Settings;
import android.provider.Settings.System;   

String androidID = System.getString(this.getContentResolver(),Secure.ANDROID_ID);
Im
*/


            Intent intent = new Intent();
            intent.setAction(TeleActions.EVENT_STARTED);
            intent.putExtra("callback_id", original.getIntExtra("callback_id", -1));
            intent.putExtra("data", data.toString());

            context.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e(TAG, "Failed to send ACCOUNT_CREATED event", e);
        }
    }

    public void fireIntentHandled(Intent original, JSONObject result) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_HANDLED);
        intent.putExtra("callback_id", original.getIntExtra("callback_id", -1));
        intent.putExtra("data", result.toString());

        context.sendBroadcast(intent);
    }

    public void fireIntentHandled(Intent original) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_HANDLED);
        intent.putExtra("callback_id", original.getIntExtra("callback_id", -1));

        context.sendBroadcast(intent);
    }

    public void fireIntentHandled(Intent original, Exception e) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_HANDLED);
        intent.putExtra("callback_id", original.getIntExtra("callback_id", -1));
        intent.putExtra("exception", e.getMessage());

        context.sendBroadcast(intent);
    }

    /*
    public void fireAccountCreated(Intent original, TeleAccount account) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_ACCOUNT_CREATED);
        intent.putExtra("callback_id", original.getIntExtra("callback_id", -1));
        intent.putExtra("data", account.toJsonString());

        context.sendBroadcast(intent);
    }

    public void fireRegistrationChangeEvent(TeleAccount account) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_REGISTRATION_CHANGED);
        intent.putExtra("data", account.toJsonString());

        context.sendBroadcast(intent);
    }

    public void fireMessageReceivedEvent(TeleMessage message) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_MESSAGE_RECEIVED);
        intent.putExtra("data", message.toJsonString());

        context.sendBroadcast(intent);
    }

    */

    public void fireCallReceivedEvent(TeleCall call) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_CALL_RECEIVED);
        intent.putExtra("data", call.toJsonString());

        context.sendBroadcast(intent);
    }

    public void fireCallChanged(TeleCall call) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_CALL_CHANGED);
        intent.putExtra("data", call.toJsonString());

        context.sendBroadcast(intent);
    }

    public void fireCallTerminated(TeleCall call) {
        Intent intent = new Intent();
        intent.setAction(TeleActions.EVENT_CALL_TERMINATED);
        intent.putExtra("data", call.toJsonString());

        context.sendBroadcast(intent);
    }
}
