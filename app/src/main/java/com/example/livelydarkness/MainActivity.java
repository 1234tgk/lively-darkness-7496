package com.example.livelydarkness;

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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Fragment {
    private static final String TAG = "MainActivity";

    private TextView targetView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_main, container, false);

        // Register button handler.
        Button button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Handling button click.");
                String raw = LogReader.getLogString(getContext());
                CalculateTime targetObj = new CalculateTime(raw);

                StringBuilder builder = new StringBuilder();

                HashMap<String, Double> result = targetObj.organizeAndCalculate();
                for (Map.Entry<String, Double> entry : result.entrySet()) {
                    builder.append("Time the user has spend outside on " + entry.getKey() + ": " + String.format("%.3f", entry.getValue()) + "\n");
                }
                targetView.setText(builder.toString());
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        targetView = (TextView) view.findViewById(R.id.display);
    }
}
