package com.dk.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class FlutterPageActivity extends AppCompatActivity {
    private static final int REQ_CODE = 13413;
    private FlutterEngine mFlutterEngine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_page);
        mFlutterEngine = createFlutterEngine();
        initFlutterFragment();
        initMethodChannel();
    }

    private FlutterEngine createFlutterEngine() {
        FlutterEngine flutterEngine = new FlutterEngine(this);
        flutterEngine.getNavigationChannel().setInitialRoute(Routes.ROUTE_MAIN + "?{\"name\":\"Dy Wade\"}");
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache.getInstance().put(Constants.FLUTTER_ENGINE_ID, flutterEngine);
        return flutterEngine;
    }


    private void initFlutterFragment() {
        FlutterFragment flutterFragment = FlutterFragment.withCachedEngine(Constants.FLUTTER_ENGINE_ID)
                //.initialRoute(Routes.ROUTE_MAIN + "?{\"name\":\"Dy Wade\"}")
                .build();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, flutterFragment).commit();
    }


    private void initMethodChannel() {
        MethodChannel methodChannel = new MethodChannel(mFlutterEngine.getDartExecutor(), Constants.CHANNEL_NATIVE);
        methodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
                switch (call.method) {
                    case "jumpToNative":// 跳转原生页面
                        if (call.hasArgument("name")) {
                            String nameArg = (String) call.argument("name");
                            Intent jumpIntent = new Intent(FlutterPageActivity.this, NativePageActivity.class);
                            jumpIntent.putExtra("name", nameArg);
                            startActivityForResult(jumpIntent, REQ_CODE);
                        }
                        break;
                    case "goBackWithResult":
                        if (call.hasArgument("message")) {
                            String message = call.argument("message");
                            Intent backIntent = new Intent();
                            backIntent.putExtra("message", message);
                            setResult(RESULT_OK, backIntent);
                            finish();
                        }
                        break;
                    case "goBack":
                        // 返回上一页
                        finish();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // NativePageActivity返回的数据
                String message = data.getStringExtra("message");
                Map<String, Object> param = new HashMap<>();
                param.put("message", message);
                // 创建MethodChannel
                MethodChannel methodChannel = new MethodChannel(mFlutterEngine.getDartExecutor(), Constants.CHANNEL_FLUTTER);
                // 调用Flutter端定义的方法
                methodChannel.invokeMethod("onActivityResult", param);
            }
        }
    }

    @Override
    public void onBackPressed() {
        MethodChannel methodChannel = new MethodChannel(mFlutterEngine.getDartExecutor(), Constants.CHANNEL_FLUTTER);
        methodChannel.invokeMethod("goBack", null);
    }
}
