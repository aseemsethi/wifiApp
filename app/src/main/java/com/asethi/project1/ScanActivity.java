package com.asethi.project1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sony on 9/16/2017.
 */

public class ScanActivity extends AppCompatActivity {

    String serverUri = "Status: ";
    WifiManager mainWifi;
    BroadcastReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
    TextView serverText;

    private HistoryAdapter mAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        serverText = (TextView) findViewById(R.id.serverUI);

        // For ListView Adapter
        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryAdapter(new ArrayList<String>());
        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);
        // For ListView Adapter

        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (mainWifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "Enabling WiFi", Toast.LENGTH_LONG).show();
            mainWifi.setWifiEnabled(true);
        }
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        serverText.setText("Starting Scan...");

        Button conn = (Button) findViewById(R.id.connectB);
        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button disconn = (Button) findViewById(R.id.disconnectB);
        disconn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (serverUri != null) {
                }
            }
        });
    }

    class WifiReceiver extends BroadcastReceiver {
        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            serverText = (TextView) findViewById(R.id.serverUI);
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                Toast.makeText(getApplicationContext(), "WiFi Scan Results", Toast.LENGTH_LONG).show();
                System.out.println("Scan results received");
            }
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            sb.append("Wifi conn: " + wifiList.size() + "\n\n");
            for (int i = 0; i < wifiList.size(); i++) {
                sb.append(new Integer(i + 1).toString() + ". ");
                sb.append((wifiList.get(i)).toString());
                sb.append("\n\n");
                addToHistory("Already subscribed to: " + wifiList.get(i).toString(), Color.BLUE);
            }
            serverText.setText("Scan Completed");
        }
    }

    @Override
    public void onBackPressed() {
/*        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    private void addToHistory(String mainText, int color) {
        System.out.println("Aseem: " + mainText);
        mAdapter.add(mainText, color);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
