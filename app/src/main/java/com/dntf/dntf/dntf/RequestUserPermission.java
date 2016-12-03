package com.dntf.dntf.dntf;

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
    protected static String[] PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    public RequestUserPermission(Activity activity) {
        this.activity = activity;
    }

    public Boolean isGranted() {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        return (permission == PackageManager.PERMISSION_GRANTED);
    }

    public void requestPermissions() {
        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS,
                REQUEST_CODE
        );
    }

}
