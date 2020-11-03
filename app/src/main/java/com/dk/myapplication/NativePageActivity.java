package com.dk.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NativePageActivity extends AppCompatActivity {
    private TextView mTvRecArg;
    private String revArgName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_page);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        String recArg = intent.getStringExtra("name");
        if (!TextUtils.isEmpty(recArg)) {
            revArgName = recArg;
        }
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NativePageActivity.class);
        context.startActivity(intent);
    }


    private void initView() {
        mTvRecArg = (TextView) findViewById(R.id.tv_rec_arg);
        mTvRecArg.setText(revArgName);
    }
}
