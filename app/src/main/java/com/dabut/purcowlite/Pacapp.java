package com.dabut.purcowlite;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class Pacapp extends AppCompatActivity {

    private ListView listView;
    private EditText searchEditText;
    private LoadPackagesTask loadPackagesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacapp);

        listView = findViewById(R.id.listview);
        searchEditText = findViewById(R.id.search2);

        // Load installed packages
        loadPackagesTask = new LoadPackagesTask(this, listView);
        loadPackagesTask.execute();

        // Search filter
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (loadPackagesTask != null) {
                    loadPackagesTask.filterData(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
