TeleService.java  handleCallMake - rerurn from intect call id -> Intent.ACTION_CALL {\"_callId":1} 
TeleService.java  handleCallMake - 2sim make call
TeleCall.java  toJson - callinfo id, etc...
Endpoint.js makeCall -> data==true hack


TeleService.java handleCallHangup -> add mCalls find, not currentCall for simulateneous calls

TeleCall creation uri collision input+output доновременно

отключение звонка при входящем


USSD

   TelephonyManager manager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    manager.sendUssdRequest("*100#", new TelephonyManager.UssdResponseCallback() {
        @Override
        public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
            super.onReceiveUssdResponse(telephonyManager, request, response);
        }

        @Override
        public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
            super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
        }
    }, new Handler());

    TelephonyManager manager2 = manager.createForSubscriptionId(subIdForSecondSlotFromSubscriptonManager);
    manager2.sendUssdRequest(...);



СтарыйЖ
    String encodedHash = Uri.encode("#");
String ussd = "*" + encodedHash + "12345" + encodedHash;
startActivityForResult(new Intent("android.intent.action.CALL", Uri.parse("tel:" + ussd)), 1);


RSSI

TelephonyManager.listenGemini (phoneStateListener1, state, 0) for the 1st SIM, and

TelephonyManager.listenGemini (phoneStateListener2, state, 1) for the 2nd SIM,


mobilemode.setStreamVolume(AudioManager.STREAM_RING,audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);

LAC CELL

https://www.svyaznoy.ru/catalog/phone/224/4088132/specs#mainContent jinga start 1280 8gb 7.0 1900

https://www.svyaznoy.ru/catalog/phone/224/5660909/specs# digma 800 8 0,5 8.0 1900

https://www.svyaznoy.ru/catalog/phone/224/5384717/specs# prestigio 800 8 1 8.1 1900

https://www.svyaznoy.ru/catalog/phone/224/5406995/specs# irbis 800 4 0.5 8.0 1500

https://www.svyaznoy.ru/catalog/phone/224/5610353 fly 960  8 1 8.1


https://www.avito.ru/sankt-peterburg/telefony/jinga_start_1893407608?slocation=636840

Полезные 

отдельно 
sim
radio
 voice data sms

public String getDeviceId (int slotIndex)

public String getImei () // API >=26

public String getDeviceSoftwareVersion ()


public int getDataState ()

DATA_DISCONNECTED
DATA_CONNECTING
DATA_CONNECTED
DATA_SUSPENDED
DATA_DISCONNECTING

public int getDataActivity ()

public int getCallState ()
int	the current call state. Value is CALL_STATE_IDLE, CALL_STATE_RINGING, or CALL_STATE_OFFHOOK




DATA_ACTIVITY_NONE
DATA_ACTIVITY_IN
DATA_ACTIVITY_OUT
DATA_ACTIVITY_INOUT
DATA_ACTIVITY_DORMANT


getDataNetworkType
int	the network type Value is NETWORK_TYPE_UNKNOWN, NETWORK_TYPE_GPRS, NETWORK_TYPE_EDGE, NETWORK_TYPE_UMTS, NETWORK_TYPE_CDMA, NETWORK_TYPE_EVDO_0, NETWORK_TYPE_EVDO_A, NETWORK_TYPE_1xRTT, NETWORK_TYPE_HSDPA, NETWORK_TYPE_HSUPA, NETWORK_TYPE_HSPA, NETWORK_TYPE_IDEN, NETWORK_TYPE_EVDO_B, NETWORK_TYPE_LTE, NETWORK_TYPE_EHRPD, NETWORK_TYPE_HSPAP, NETWORK_TYPE_GSM, NETWORK_TYPE_TD_SCDMA, NETWORK_TYPE_IWLAN, or NETWORK_TYPE_NR



public int getPhoneCount ()


public int getNetworkType ()
int	the NETWORK_TYPE_xxxx for current data connection. Value is NETWORK_TYPE_UNKNOWN, NETWORK_TYPE_GPRS, NETWORK_TYPE_EDGE, NETWORK_TYPE_UMTS, NETWORK_TYPE_CDMA, NETWORK_TYPE_EVDO_0, NETWORK_TYPE_EVDO_A, NETWORK_TYPE_1xRTT, NETWORK_TYPE_HSDPA, NETWORK_TYPE_HSUPA, NETWORK_TYPE_HSPA, NETWORK_TYPE_IDEN, NETWORK_TYPE_EVDO_B, NETWORK_TYPE_LTE, NETWORK_TYPE_EHRPD, NETWORK_TYPE_HSPAP, NETWORK_TYPE_GSM, NETWORK_TYPE_TD_SCDMA, NETWORK_TYPE_IWLAN, or NETWORK_TYPE_NR

public String getNetworkOperatorName ()  //!! when registered in network
public String getNetworkOperator ()

public String getNetworkCountryIso ()

public String getLine1Number ()

public String getGroupIdLevel1 ()


public String getSimOperatorName ()
Returns the Service Provider Name (SPN).
SIM state must be SIM_STATE_READY

public String getSimOperator ()
Returns the MCC+MNC (mobile country code + mobile network code) of the provider of the SIM. 5 or 6 decimal digits.

Availability: SIM state must be SIM_STATE_READY


public ServiceState getServiceState () API>=26


public int getPhoneType ()
Returns a constant indicating the device phone type. This indicates the type of radio used to transmit voice calls.

Returns
int	
See also:

PHONE_TYPE_NONE
PHONE_TYPE_GSM
PHONE_TYPE_CDMA
PHONE_TYPE_SIP




public boolean setOperatorBrandOverride (String brand)
public boolean setLine1NumberForDisplay (String alphaTag, 
                String number)

public boolean isWorldPhone ()

public boolean isVoiceCapable ()
public boolean isSmsCapable ()

public boolean isNetworkRoaming ()

public int getVoiceNetworkType ()

int	Value is NETWORK_TYPE_UNKNOWN, NETWORK_TYPE_GPRS, NETWORK_TYPE_EDGE, NETWORK_TYPE_UMTS, NETWORK_TYPE_CDMA, NETWORK_TYPE_EVDO_0, NETWORK_TYPE_EVDO_A, NETWORK_TYPE_1xRTT, NETWORK_TYPE_HSDPA, NETWORK_TYPE_HSUPA, NETWORK_TYPE_HSPA, NETWORK_TYPE_IDEN, NETWORK_TYPE_EVDO_B, NETWORK_TYPE_LTE, NETWORK_TYPE_EHRPD, NETWORK_TYPE_HSPAP, NETWORK_TYPE_GSM, NETWORK_TYPE_TD_SCDMA, NETWORK_TYPE_IWLAN, or NETWORK_TYPE_NR

public String getSubscriberId ()

public int getSimState ()

int	Value is SIM_STATE_UNKNOWN, SIM_STATE_ABSENT, SIM_STATE_PIN_REQUIRED, SIM_STATE_PUK_REQUIRED, SIM_STATE_NETWORK_LOCKED, SIM_STATE_READY, SIM_STATE_NOT_READY, SIM_STATE_PERM_DISABLED, SIM_STATE_CARD_IO_ERROR, SIM_STATE_CARD_RESTRICTED, android.telephony.TelephonyManager.SIM_STATE_LOADED, or android.telephony.TelephonyManager.SIM_STATE_PRESENT


public String getSimSerialNumber ()







getCellLocation() api<26
public List<CellInfo> getAllCellInfo () api>=26


CellSignalStrength
int	SIGNAL_STRENGTH_GOOD
int	SIGNAL_STRENGTH_GREAT
int	SIGNAL_STRENGTH_MODERATE
int	SIGNAL_STRENGTH_NONE_OR_UNKNOWN
int	SIGNAL_STRENGTH_POOR
abstract int	getDbm()
Get the technology-specific signal strength in dBm, which is the signal strength of the pilot signal or equivalent.

abstract int	getLevel()
Retrieve an abstract level value for the overall signal quality.


NeighboringCellInfo



GsmCellLocation
public void setLacAndCid (int lac, 
                int cid)




https://github.com/CellularPrivacy/Android-IMSI-Catcher-Detector



После TeleEndpoint Call - статистика по дропнутым ТЕЛЕ пакетам и SIP пакетам в лог звонка
Logger в Endpoint



Bad audio recording quality on some devices
Reported that audio quality recorded on the microphone is bad and the speed is twice what it should be, it only happens on some devices. It could be fixed by setting audio mode via AudioManager to MODE_IN_COMMUNICATION in the application, e.g:

AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
int original_mode = am.getMode();

/* Set audio mode before using audio device, for example before making/answering a SIP call */
am.setMode(AudioManager.MODE_IN_COMMUNICATION);
...
/* Restore back to the original mode after finished with audio device */
am.setMode(original_mode);



       String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state == null) {

            //Outgoing call
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.e("tag", "Outgoing number : " + number);

        } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

            Log.e("tag", "EXTRA_STATE_OFFHOOK");

        } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

            Log.e("tag", "EXTRA_STATE_IDLE");

        } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            //Incoming call
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e("tag", "Incoming number : " + number);

        } else {
            Log.e("tag extra ", state);
        }






        #define PJMEDIA_HAS_SRTP 1
#define PJSIP_HAS_TLS_TRANSPORT 1

ue was because pjsua_start(); was not called

3

Something silly: I needed to call

pjsua.start()



To handle it user need to include $(NDKRoot)/sources/android/support/include in the CFLAGS (user.mak).
APP_PLATFORM=android-17


You should use "-DANDROID_STL=c++_static" instead of -DANDROID_STL=gnustl_static



https://stackoverflow.com/questions/2513713/how-to-use-3g-connection-in-android-application-instead-of-wi-fi
https://stackoverflow.com/questions/25931334/send-request-over-mobile-data-when-wifi-is-on-android-l


TRANSPORT_CELLULAR



https://github.com/viaduck/openssl-cmake



https://trac.pjsip.org/repos/wiki/Getting-Started/Android#OpenSSLSupport


https://mi-house.ru/naushniki/naushniki-vkladyshi/adapter-dlya-naushnikov-xiaomi-mi-bluetooth-audio-receiver-ypjsq01jy.html?ymclid=15900717811726762190800002&utm_source=yandex.market&utm_medium=cpc&utm_campaign=61_naushniki&utm_term=YPJSQ01JY&utm_content=76_naushniki-vkladyshi