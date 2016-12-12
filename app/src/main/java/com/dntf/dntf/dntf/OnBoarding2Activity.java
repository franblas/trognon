package com.dntf.dntf.dntf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dntf.dntf.dntf.notifications.NotificationsCenter;
import com.dntf.dntf.dntf.permissions.RequestUserPermission;

/**
 * Created by franblas on 02/12/16.
 */
public class OnBoarding2Activity extends AppCompatActivity {

    private RequestUserPermission requestUserPermission = new RequestUserPermission(this);

    private void startMainActivity() {
        NotificationsCenter.setupAlarm(OnBoarding2Activity.this); // Setup notification
        Intent intent = new Intent(OnBoarding2Activity.this, MainActivity.class);
        startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        String onBoardingText = getString(R.string.onboarding2);
        TextView onBoardingTxt = (TextView) findViewById(R.id.onBoardingTxt);
        onBoardingTxt.setClickable(true);
        onBoardingTxt.setMovementMethod(LinkMovementMethod.getInstance());
        onBoardingTxt.setText(Html.fromHtml(onBoardingText), TextView.BufferType.SPANNABLE);

        Button onBoardingBtn = (Button) findViewById(R.id.onBoardingBtn);
        onBoardingBtn.setText(getString(R.string.onboarding2_btn));
        onBoardingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!requestUserPermission.isGranted()) {
                    requestUserPermission.requestPermissions();
                } else {
                    startMainActivity();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestUserPermission.isGranted() || !ActivityCompat.shouldShowRequestPermissionRationale(this, RequestUserPermission.PERMISSIONS[0])) {
            startMainActivity();
        }
    }
}
