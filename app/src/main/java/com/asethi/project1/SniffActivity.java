package com.asethi.project1;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.VpnService;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class SniffActivity extends VpnService implements Handler.Callback, Runnable {
    private static final String TAG = "Aseem: SniffActivity";

    private Handler mHandler;
    private Thread mThread;

    private ParcelFileDescriptor mInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The handler is only used to show messages.
        if (mHandler == null) {
            mHandler = new Handler(this);
        }

        // Stop the previous session by interrupting the thread.
        if (mThread != null) {
            mThread.interrupt();
        }

        // Start a new session by creating a new thread.
        mThread = new Thread(this, "SniffActivityThread");
        mThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mThread != null) {
            mThread.interrupt();
        }
    }

    @Override
    public boolean handleMessage(Message message) {
        if (message != null) {
            Toast.makeText(this, message.what, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public synchronized void run() {
        Log.i(TAG,"running vpnService");
        try {
            runVpnConnection();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Got " + e.toString());
        } finally {
            try {
                mInterface.close();
            } catch (Exception e) {
                // ignore
            }
            mInterface = null;

            mHandler.sendEmptyMessage(R.string.disconnected);
            Log.i(TAG, "Exiting");
        }
    }

    private boolean runVpnConnection() throws Exception {

        configure();

        FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());

        // Allocate the buffer for a single packet.
        ByteBuffer packet = ByteBuffer.allocate(32767);

        // We keep forwarding packets till something goes wrong.
        while (true) {
            // Assume that we did not make any progress in this iteration.
            boolean idle = true;

            // Read the outgoing packet from the input stream.
            int length = in.read(packet.array());
            if (length > 0) {

                Log.i(TAG,"************new packet");
                System.exit(-1);
                while (packet.hasRemaining()) {
                    Log.i(TAG,""+packet.get());
                    System.out.print((char) packet.get());
                }
                packet.limit(length);
                //  tunnel.write(packet);
                packet.clear();

                // There might be more outgoing packets.
                idle = false;
            }
            Thread.sleep(50);
        }
    }

    public String getLocalIpAddress()
    {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    Log.i(TAG,"****** INET ADDRESS ******");
                    Log.i(TAG,"address: "+inetAddress.getHostAddress());
                    Log.i(TAG,"hostname: "+inetAddress.getHostName());
                    Log.i(TAG,"address.toString(): "+inetAddress.getHostAddress().toString());
                    if (!inetAddress.isLoopbackAddress()) {
                        //IPAddresses.setText(inetAddress.getHostAddress().toString());
                        Log.i(TAG,"IS NOT LOOPBACK ADDRESS: "+inetAddress.getHostAddress().toString());
                        return inetAddress.getHostAddress().toString();
                    } else{
                        Log.i(TAG,"It is a loopback address");
                    }
                }
            }
        } catch (SocketException ex) {
            String LOG_TAG = null;
            Log.e(LOG_TAG, ex.toString());
        }

        return null;
    }

    private void configure() throws Exception {
        // If the old interface has exactly the same parameters, use it!
        if (mInterface != null) {
            Log.i(TAG, "Using the previous interface");
            return;
        }

        // Configure a builder while parsing the parameters.
        Builder builder = new Builder();
        builder.setMtu(1500);
        builder.addAddress("10.0.2.0", 24); // this needs to be 10.0.2.0 internal address
        builder.addRoute("0.0.0.0", 0); // needs to be this.
        try {
            mInterface.close();
        } catch (Exception e) {
            // ignore
        }

        mInterface = builder.establish();
    }
}