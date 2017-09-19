package com.asethi.project1;

/**
 * Created by sony on 9/16/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asethi.project1.WiFiProperties;
import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{

    private ArrayList<String> history;
    static private ArrayList<WiFiProperties> wifiP = new ArrayList<>();
    private Context mContext;

    //Store Colors here indexed by position
    ArrayList<Integer> myColors = new ArrayList<Integer>();
    int colorVar = Color.BLACK;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public TextView mBssid, mCap, mLevel;
        LinearLayout rootView;//newly added field
        LinearLayout postView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.ssid);
            mBssid = (TextView) v.findViewById(R.id.bssid);
            mCap = (TextView) v.findViewById(R.id.cap);
            mLevel = (TextView) v.findViewById(R.id.level);
            rootView=(LinearLayout)v.findViewById(R.id.rootView);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (wifiP.size() == 0) {
                        System.out.println("Aseem: WiFiP size is 0");
                        Toast.makeText(v.getContext(), "Scan in progress... "
                                , Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(v.getContext(), "Clicked: " +wifiP.get(getAdapterPosition()).mSsid
                            , Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(v.getContext(), WiFiAP.class);
                    intent.putExtra("ssid", wifiP.get(getAdapterPosition()).mSsid);
                    intent.putExtra("bssid", wifiP.get(getAdapterPosition()).mBssid);
                    intent.putExtra("cap", wifiP.get(getAdapterPosition()).mCap);
                    intent.putExtra("level", wifiP.get(getAdapterPosition()).mLevel);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public HistoryAdapter(Context context, ArrayList<WiFiProperties> dataSet){
        //history = dataSet;
        wifiP = dataSet;
        mContext = context;
    }
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create View
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_row, parent, false);
        return new ViewHolder(v);
    }

    // size shows as 0, when called from Resume, since the wifiP has already been cleared at that point
    // It is best to check for wifip.size in onClick and not proceed if the size is 0.
    // That gives time for the background scan to complete.
    public void clear() {
        int size = wifiP.size();
        System.out.println("Aseem: recyclerView cleared size" + size);
        wifiP.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void add(WiFiProperties data, int color){
        colorVar = color;
        myColors.add(color);
        if (wifiP.contains(data)) {
            System.out.println("Aseem: duplicate data" + data.mSsid);
            return;
        }
        wifiP.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.mTextView.setTextColor(myColors.get(position));
        //holder.mTextView.setText(history.get(position));
        WiFiProperties wifiPt = wifiP.get(position);
        holder.mBssid.setText(wifiPt.getBSSID());
        holder.mTextView.setText(wifiPt.getSSID());
        holder.mCap.setText(wifiPt.getCAP());
        holder.mLevel.setText(wifiPt.getLEVEL());
    }

    @Override
    public int getItemCount() {
        return wifiP.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

}