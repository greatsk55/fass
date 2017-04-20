package com.thechange.fass.fass.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thechange.fass.fass.R;
import com.thechange.fass.fass.service.ClipboardService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ClipboardService 시작
        Intent intent = new Intent(this, ClipboardService.class);
        startService(intent);
    }
}
