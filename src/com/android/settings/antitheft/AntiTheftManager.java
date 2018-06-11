package com.android.settings.antitheft;

import android.content.Context;
import android.util.Log;


public class AntiTheftManager {

    public static final String TAG = "AT_Manager";
    private static AntiTheftManager sInstance;
    private static Context mContext;

    private String action = "ANTI_THEFT_SERVICE_ACTION";
    private String packageName = "com.ndroid.ndroidclient";

    public static AntiTheftManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AntiTheftManager(context);
        }

        return sInstance;
    }

    private AntiTheftManager(Context context) {
        mContext = context;
    }


    /**
     * Public Methods
     */

    // Device Id
    public int getDeviceId() {
        Log.d(TAG, "getDeviceId()");
        return Utils.getDeviceId(mContext);
    }

    public void setDeviceId(int id) {
        Utils.storeDeviceId(mContext, id);
    }

    // Device Name
    public String getDeviceName() {
        Log.d(TAG, "getDeviceName() ");
        return Utils.getDeviceName(mContext);
    }

    public void setDeviceName(String name) {
        Log.d(TAG, "setDeviceName() " + name);
        Utils.storeDeviceName(mContext, name);
    }

    // Device Pass
    public String getDevicePass() {
        Log.d(TAG, "getDevicePass() ");
        return Utils.getDevicePass(mContext);
    }

    public void setDevicePass(String pass) {
        Log.d(TAG, "setDevicePass() " + pass);
        Utils.storeDevicePass(mContext,pass);
    }

    // AntiTheft Status
    public boolean getAntiTheftStatus() {
        Log.d(TAG, "getAntiTheftStatus() ");
        return Utils.getAntiTheftStatus(mContext);
    }

    public void setAntiTheftStatus(boolean status) {
        Log.d(TAG, "setAntiTheftStatus() " + status);
        Utils.storeAntiTheftStatus(mContext, status);
    }

    // IP Address
    public String getIpAddress() {
        return Utils.getIpAddress(mContext);
    }

    public void setIpAddress(String ip) {
        Log.d(TAG, "getIpAddress() " + ip);
        Utils.storeIpAddress(mContext, ip);
    }

    // AntiTheft Frequency
    public int getAtFrequency() {
        Log.d(TAG, "getAtFrequency() ");
        return Utils.getAtFrequency(mContext);
    }

    public void setAtFrequency(int frequency) {
        Log.d(TAG, "setAtFrequency() " + frequency);
        Utils.storeAtFrequency(mContext, frequency);
    }

    public int registerDevice(String name, String pass) {
        int lastId = Utils.getDeviceId(mContext);
        int nextId = lastId + 1;
        Utils.storeDeviceId(mContext,nextId);
        return nextId;
    }

    public void resetSettings() {
        setAntiTheftStatus(false);
        setAtFrequency(0);
        setDeviceId(0);
        setDeviceName("");
        setDevicePass("");
        setIpAddress("");
    }
}
