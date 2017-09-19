package com.asethi.project1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class WiFiAP extends AppCompatActivity {
    private HistoryAdapter mAdapter;
    RecyclerView mRecyclerView;
    WiFiProperties wifiP;
    String ssid, bssid, level, cap;
    TextView ssidUI, bssidUI, capUI, levelUI;

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
        mAdapter = new HistoryAdapter(getApplicationContext(), new ArrayList<WiFiProperties>());
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
        Button conn = (Button) findViewById(R.id.connect);
        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

}