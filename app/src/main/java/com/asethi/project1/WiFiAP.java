package com.asethi.project1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class WiFiAP extends AppCompatActivity {
    private ScanAdapter mAdapter;
    RecyclerView mRecyclerView;
    WiFiProperties wifiP;
    String ssid, bssid, level, cap;
    TextView ssidUI, bssidUI, capUI, levelUI;
    EditText passw;
    WifiManager wifiManager;
    BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifiap);
        ssidUI = (TextView) findViewById(R.id.ssid1);
        bssidUI = (TextView) findViewById(R.id.bssid1);
        capUI = (TextView) findViewById(R.id.cap1);
        levelUI = (TextView) findViewById(R.id.level1);

        Intent myIntent = getIntent(); // gets the previously created intent
        ssid = myIntent.getStringExtra("ssid");
        bssid = myIntent.getStringExtra("bssid");
        cap = myIntent.getStringExtra("cap");
        level = myIntent.getStringExtra("level");
        System.out.println("Aseem: SSID passed in Intent: " + ssid);
        ssidUI.setText(ssid);
        bssidUI.setText(bssid);
        capUI.setText(cap);
        levelUI.setText(level);

        // For ListView Adapter
        mRecyclerView = (RecyclerView) findViewById(R.id.wifiap_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ScanAdapter(new ArrayList<String>());
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        // For ListView Adapter

        Button sniff = (Button) findViewById(R.id.sniff);
        sniff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        Button wsniff = (Button) findViewById(R.id.wsniff);
        wsniff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                mAdapter.add("Need to attach external WiFi device", Color.BLUE);

            }
        });
        final Button conn = (Button) findViewById(R.id.connect);
        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passw = (EditText) findViewById(R.id.pwd);
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                broadcastReceiver = new WifiReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                registerReceiver(broadcastReceiver, intentFilter);
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                if (conn.getText() == "Disconnect") {
                    wifiManager.disconnect();
                    System.out.println("Aseem: WiFiAP Disconnecting");
                    conn.setText("Connect");
                    return;
                }
                wifiConfig.SSID = String.format("\"%s\"", ssid);
                wifiConfig.preSharedKey = String.format("\"%s\"", passw.getText());
                int netId = wifiManager.addNetwork(wifiConfig);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();
            }
        });
    }
    public class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Button cd = (Button) findViewById(R.id.connect);
            final String action = intent.getAction();
            if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                boolean connected = info.isConnected();
                if (connected) {
                    // wifi connected
                    System.out.println("Aseem: WiFiAP Connected");
                    cd.setText("Disconnect");
                    cd.setBackgroundColor(Color.GREEN);
                    //cd.setBackgroundResource(R.drawable.my_border_green);

                } else {
                    // wifi connection was lost
                    System.out.println("Aseem: WiFiAP Disconnected");
                    cd.setText("Connect");
                    cd.setBackgroundColor(Color.GRAY);
                    //cd.setBackgroundResource(R.drawable.my_border);
                }
            }
        }
    }

}