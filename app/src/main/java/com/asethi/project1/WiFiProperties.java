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
}
