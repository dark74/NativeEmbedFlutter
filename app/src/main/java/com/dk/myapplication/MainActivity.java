package com.dk.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.android.FlutterView;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;
import io.flutter.embedding.engine.renderer.FlutterUiDisplayListener;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

    private static final int REQ_CODE = 12353;
    FlutterEngine mFlutterEngien;
    FlutterView mFullterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_root);
        //initFlutterEngine();
        mFlutterEngien = FlutterEngineCache.getInstance().get(Constants.FLUTTER_ENGINE_ID);
        GeneratedPluginRegistrant.registerWith(mFlutterEngien);
        //mFullterView = createFlutterView();
        //mFullterView.attachToFlutterEngine(mFlutterEngien);

        //跳转flutter 页面
        findViewById(R.id.btn_goto_flutter_fragment)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, FlutterPageActivity.class);
                        startActivityForResult(intent, REQ_CODE);
                    }
                });
    }

    private FlutterView createFlutterView() {
        FlutterView flutterView = new FlutterView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        FrameLayout flContainer = findViewById(R.id.fl_flutter);
        flContainer.addView(flutterView, layoutParams);
        flContainer.setVisibility(View.INVISIBLE);
        FlutterUiDisplayListener listener = new FlutterUiDisplayListener() {
            @Override
            public void onFlutterUiDisplayed() {
                flContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFlutterUiNoLongerDisplayed() {

            }
        };
        flutterView.addOnFirstFrameRenderedListener(listener);
        return flutterView;
    }

    private void initFlutterEngine() {
        mFlutterEngien = FlutterEngineCache.getInstance().get("flutter");
        if (mFlutterEngien == null) {
            mFlutterEngien = new FlutterEngine(this);
            mFlutterEngien.getDartExecutor().executeDartEntrypoint(
                    DartExecutor.DartEntrypoint.createDefault()
            );
            FlutterEngineCache.getInstance().put("flutter", mFlutterEngien);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFlutterEngien.getLifecycleChannel().appIsResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFlutterEngien.getLifecycleChannel().appIsInactive();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFlutterEngien.getLifecycleChannel().appIsPaused();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            String message = data.getStringExtra("message");
            Toast.makeText(this, "Flutter页面回来data:" + message, Toast.LENGTH_SHORT).show();
        }
    }
}
