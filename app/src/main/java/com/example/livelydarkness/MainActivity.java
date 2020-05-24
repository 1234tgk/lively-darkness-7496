package com.example.livelydarkness;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Fragment {
    private static final String TAG = "MainActivity";

    private TextView targetView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main, container, false);

        targetView = root.findViewById(R.id.display);
        updateDisplay();
        Context context = getContext();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateDisplay();
                }
            }, new IntentFilter(Constants.LOCATION_UPDATE_ACTION));
        }

        return root;
    }

    private void updateDisplay() {
        Context context = getContext();
        if (context == null || targetView == null) {
            return;
        }
        Log.i(TAG, "Updating display.");
        String raw = LogReader.getLogString(getContext());
        CalculateTime targetObj = new CalculateTime(raw);

        StringBuilder builder = new StringBuilder();

        HashMap<String, Double> result = targetObj.organizeAndCalculate();
        for (Map.Entry<String, Double> entry : result.entrySet()) {
            builder.append("Time the user has spend outside on " + entry.getKey() + ": " + String.format("%.3f", entry.getValue()) + "\n");
        }
        targetView.setText(builder.toString());
    }
}
