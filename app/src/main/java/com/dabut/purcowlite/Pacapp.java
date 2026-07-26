package com.dabut.purcowlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Pacapp extends AppCompatActivity {

    private ListView listView;
    private EditText searchEditText;
    private AppListAdapter adapter;
    private List<AppInfo> appInfoList;
    private SharedPreferences prefs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacapp);

        listView = findViewById(R.id.listview);
        searchEditText = findViewById(R.id.search2);

        prefs = getSharedPreferences("com.dabut.purnetvray", Context.MODE_MULTI_PROCESS);

        appInfoList = new ArrayList<>();
        adapter = new AppListAdapter(this, R.layout.listview_item_row, appInfoList);
        listView.setAdapter(adapter);

        // Load installed apps in background
        new LoadPackagesTask(this, appInfoList, adapter, prefs).execute();

        // Search filter
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Save on back press
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AppInfo appInfo = appInfoList.get(position);
            appInfo.setSelected(!appInfo.isSelected());
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSelectedPackages();
    }

    private void saveSelectedPackages() {
        Set<String> selectedPackages = new HashSet<>();
        for (AppInfo appInfo : appInfoList) {
            if (appInfo.isSelected()) {
                selectedPackages.add(appInfo.getPackageName());
            }
        }
        prefs.edit().putStringSet("selectedPackages", selectedPackages).apply();
    }
}
