package com.dk.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NativePageActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvRecArg;
    private String revArgName;
    private Button mBtnBackFlutterPage;

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
        mBtnBackFlutterPage = (Button) findViewById(R.id.btn_back_flutter_page);
        mBtnBackFlutterPage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_flutter_page:
                backToFlutterPage();
                break;
        }
    }

    // NativePageActivity -> FlutterPageActivity -> Flutter Page
    private void backToFlutterPage() {
        Intent backToFlutter = new Intent();
        backToFlutter.putExtra("message", "我从原生页面回来了");
        setResult(RESULT_OK, backToFlutter);
        finish();
    }
}
