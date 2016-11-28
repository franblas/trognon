package com.example.dntf.dntf;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by franblas on 22/10/16.
 */
public class RequestUserPermission {

    private Activity activity;
    private static final int REQUEST_CODE = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    public RequestUserPermission(Activity activity) {
        this.activity = activity;
    }

    public void checkPermissions() {
        // Check if we have permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_CODE
            );
        }
    }

}
