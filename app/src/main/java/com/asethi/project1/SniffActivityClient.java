package com.asethi.project1;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

public class SniffActivityClient extends Activity implements View.OnClickListener {
    private TextView mServerAddress;
    private TextView mServerPort;
    private TextView mSharedSecret;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.form);

        mServerAddress = (TextView) findViewById(R.id.address);
        mServerPort = (TextView) findViewById(R.id.port);
        mSharedSecret = (TextView) findViewById(R.id.secret);
        */
        findViewById(R.id.connect).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, 0);
        } else {
            onActivityResult(0, RESULT_OK, null);
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
            String prefix = getPackageName();
            Intent intent = new Intent(this, SniffActivity.class);
            startService(intent);
        }
    }
}
