package com.dk.myapplication;

import android.app.Application;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.embedding.engine.FlutterEngineCache;
import io.flutter.embedding.engine.dart.DartExecutor;

public class MyApp extends Application {
    private FlutterEngine flutterEngine;

    @Override
    public void onCreate() {
        super.onCreate();
        // Instantiate a FlutterEngine.
        flutterEngine = new FlutterEngine(this);
        flutterEngine.getNavigationChannel().setInitialRoute(Routes.ROUTE_MAIN);
        flutterEngine.getDartExecutor().executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
        );
        FlutterEngineCache.getInstance().put(Constants.FLUTTER_ENGINE_ID, flutterEngine);
    }
}
