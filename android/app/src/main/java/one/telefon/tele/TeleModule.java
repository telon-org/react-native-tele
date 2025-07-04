package one.telefon.tele;

/*
import android.app.Service;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;

import android.util.Log;

import com.facebook.react.bridge.*;

import one.telefon.TeleManager;
*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.*;

import android.telephony.TelephonyManager;



public class TeleModule extends ReactContextBaseJavaModule {

    private static String LOG = "telefon.one.tele.TeleModule";

    private static TeleBroadcastReceiver receiver;

    public TeleModule(ReactApplicationContext context) {
        super(context);

        // Module could be started several times, but we have to register receiver only once.
        if (receiver == null) {
            receiver = new TeleBroadcastReceiver(context);
            this.getReactApplicationContext().registerReceiver(receiver, receiver.getFilter());
        } else {
            receiver.setContext(context);
        }

        //offerReplacingDefaultDialer();
    }



    //private static TeleBroadcastReceiver receiver;
    // OLD public static ReactApplicationContext currentContext;
    /*
    public TeleModule(ReactApplicationContext context) {
        super(context);
        currentContext=context;
        // Module could be started several times, but we have to register receiver only once.
    }
    */
    
    @Override
    public String getName() {
        return "TeleModule";
    }

    /*
    @ReactMethod
    public void offerReplacingDefaultDialer() {
        Log.w(LOG, "offerReplacingDefaultDialer");
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
    
        if (telecomManager.getDefaultDialerPackage() != getPackageName()) {
          Log.w(LOG, "offerReplacingDefaultDialer->send intent");
          Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
          intent.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
          startActivityForResult(intent, RC_DEFAULT_PHONE);
          // startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER); //Different
          // code
          // Huawei/ honor : ??? manual ??? startActivityForResult(new
          // Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    
        }
    }

    @ReactMethod
    public void showApp() {
        Log.d(LOG, "showApp()");
        // Automatically start application when incoming call received.
*/
        /*
         * PowerManager.WakeLock wl = mPowerManager.newWakeLock(
         * PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE |
         * PowerManager.FULL_WAKE_LOCK, "incoming_call" ); wl.acquire(10000);
         */
/*
        Boolean mAppHidden = true;
        if (mAppHidden) {
            try {
                String ns = getApplicationContext().getPackageName();
                String cls = ns + ".MainActivity";

                Intent intent = new Intent(getApplicationContext(), Class.forName(cls));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.EXTRA_DOCK_STATE_CAR);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.putExtra("foreground", true);

                startActivity(intent);
            } catch (Exception e) {
                Log.w(LOG_TAG, "Failed to open application on received call", e);
            }
        }

        job(new Runnable() {
            @Override
            public void run() {
                // Brighten screen at least 10 seconds
                PowerManager.WakeLock wl = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.ON_AFTER_RELEASE | PowerManager.FULL_WAKE_LOCK, "incoming_call");
                wl.acquire(10000);

            }
        });

    }
*/
    @ReactMethod
    public void start(ReadableMap configuration, Callback callback) {
        int id = receiver.register(callback);
        Intent intent = TeleActions.createStartIntent(id, configuration, getReactApplicationContext());

        getReactApplicationContext().startService(intent);
    }

    /*
    @ReactMethod
    public void changeServiceConfiguration(ReadableMap configuration, Callback callback) {
        int id = receiver.register(callback);
        Intent intent = TeleActions.createSetServiceConfigurationIntent(id, configuration, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }
    */

    @ReactMethod
    public void makeCall(int accountId, String destination, ReadableMap callSettings, ReadableMap msgData,  Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createMakeCallIntent(callbackId, accountId, destination, callSettings, msgData, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void hangupCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createHangupCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void declineCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createDeclineCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void answerCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createAnswerCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void sendEnvelope(int callId, Callback callback) {
        //int callbackId = receiver.register(callback);

        String res = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) getReactApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            //if (isSimCardPresent()) {
            //res = telephonyManager.sendEnvelopeWithStatus("TESTTEST");
            //}
        } catch (SecurityException expected) {
        }

        callback.invoke(res);
	
	
        //Intent intent = TeleActions.createAnswerCallIntent(callbackId, callId, getReactApplicationContext());
        //getReactApplicationContext().startService(intent);
    }



   
    /*
    @ReactMethod
    public void holdCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createHoldCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void unholdCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createUnholdCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void muteCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createMuteCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void unMuteCall(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createUnMuteCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void useSpeaker(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createUseSpeakerCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void useEarpiece(int callId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createUseEarpieceCallIntent(callbackId, callId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void xferCall(int callId, String destination, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createXFerCallIntent(callbackId, callId, destination, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void xferReplacesCall(int callId, int destCallId, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createXFerReplacesCallIntent(callbackId, callId, destCallId, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void redirectCall(int callId, String destination, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createRedirectCallIntent(callbackId, callId, destination, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }

    @ReactMethod
    public void dtmfCall(int callId, String digits, Callback callback) {
        int callbackId = receiver.register(callback);
        Intent intent = TeleActions.createDtmfCallIntent(callbackId, callId, digits, getReactApplicationContext());
        getReactApplicationContext().startService(intent);
    }
    */


    /* OLD
    @ReactMethod
    public void getCurrentCall() {
        Log.d(LOG, "getCurrentCall()"); 
        TeleManager.getCurrentCall();
    }

    //incoming
    @ReactMethod
    public void declineCall() {
        Log.d(LOG, "declineCall()"); 
        TeleManager.reject();
    }

    @ReactMethod
    public void answerCall() {
        Log.d(LOG, "answerCall()");
        TeleManager.answer(); 
    }

    //outgoing
    @ReactMethod
    public void hangupCall() {
        Log.d(LOG, "hangupCall()"); 
        TeleManager.disconnect();
    }
    */


}






