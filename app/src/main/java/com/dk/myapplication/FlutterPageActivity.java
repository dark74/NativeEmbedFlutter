package com.dk.myapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import io.flutter.embedding.android.FlutterFragment;

public class FlutterPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flutter_page);
        //FlutterFragment flutterFragment = FlutterFragment.createDefault();
        FlutterFragment flutterFragment = FlutterFragment.withNewEngine()
                .initialRoute(Routes.ROUTE_MAIN + "?{\"name\":\"Dy Wade\"}")
                .build();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, flutterFragment).commit();
    }
}
