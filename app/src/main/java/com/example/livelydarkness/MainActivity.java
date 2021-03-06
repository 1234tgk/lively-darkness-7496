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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MainActivity extends Fragment {
    private static final String TAG = "MainActivity";
    private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;
    private List<SuntimeAdapter.SuntimeModel> suntimeDataset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main, container, false);
        Context context = getContext();

        recyclerView = root.findViewById(R.id.recycler_view);
        recyclerLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(recyclerLayoutManager);
        suntimeDataset = new ArrayList<>();
        recyclerAdapter = new SuntimeAdapter(suntimeDataset);
        recyclerView.setAdapter(recyclerAdapter);

        updateDisplay();
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
        if (context == null) {
            return;
        }
        Log.i(TAG, "Updating display.");
        String raw = LogReader.getLogString(getContext());
        CalculateTime targetObj = new CalculateTime(raw);

        SortedMap<GregorianCalendar, Long> result = targetObj.organizeAndCalculate();

        suntimeDataset.clear();
        for (Map.Entry<GregorianCalendar, Long> entry : result.entrySet()) {
            suntimeDataset.add(new SuntimeAdapter.SuntimeModel(formatter.format(entry.getKey().getTime()), entry.getValue() / 1000 / 60 + " mins"));
        }
        recyclerAdapter.notifyDataSetChanged();
    }
}
