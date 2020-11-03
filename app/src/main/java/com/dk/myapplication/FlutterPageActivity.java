package com.dk.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import io.flutter.embedding.android.FlutterFragment;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class FlutterPageActivity extends AppCompatActivity {

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
                            startActivity(jumpIntent);
                        }
                        break;
                }
            }
        });
    }
}
