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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LogFragment extends Fragment {
    private static final String TAG = "LogFragment";
    private TextView rawLogTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_log, container, false);

        // Update log.
        rawLogTextView = root.findViewById(R.id.raw_log_text_view);
        updateLogText();
        Context context = getContext();
        if (context != null) {
            LocalBroadcastManager.getInstance(context).registerReceiver(
                    new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            Log.i(TAG, "Receiving local broadcast");
                            updateLogText();
                        }
                    },
                    new IntentFilter(Constants.LOCATION_UPDATE_ACTION));
        }

        return root;
    }

    private void updateLogText() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        rawLogTextView.setText(LogReader.getLogString(context));
    }
}
