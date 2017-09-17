package com.asethi.project1;

import java.util.ArrayList;

/**
 * Created by sony on 9/17/2017.
 */

public class WiFiProperties {
        public String mSsid;
        public String mBssid;
        public String mCap;
        public String mLevel;

        public WiFiProperties (String ssid, String bssid) {
            this.mSsid = ssid;
            this.mBssid = bssid;
        }
        public String getSSID() {
            return mSsid;
        }
        public String getBSSID() {
            return mBssid;
        }
    public String getCAP() {
        return mCap;
    }
    public String getLEVEL() {
        return mLevel;
    }
    @Override
    public boolean  equals (Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            WiFiProperties x = (WiFiProperties) object;
            if (this.mSsid.equals(x.getSSID())) {
                result = true;
            }
        }
        return result;
    }
}
