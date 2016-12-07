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

/**
 * Created by franblas on 02/12/16.
 */
public class OnBoarding2Activity extends AppCompatActivity {

    private TextView onBoardingTxt;
    private Button onBoardingBtn;

    private RequestUserPermission requestUserPermission = new RequestUserPermission(this);

    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        String onBoardingText = getString(R.string.onboarding2);
        onBoardingTxt = (TextView) findViewById(R.id.onBoardingTxt);
        onBoardingTxt.setClickable(true);
        onBoardingTxt.setMovementMethod(LinkMovementMethod.getInstance());
        onBoardingTxt.setText(Html.fromHtml(onBoardingText), TextView.BufferType.SPANNABLE);

        onBoardingBtn = (Button) findViewById(R.id.onBoardingBtn);
        onBoardingBtn.setText(getString(R.string.onboarding2_btn));
        onBoardingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!requestUserPermission.isGranted()) {
                    requestUserPermission.requestPermissions();
                } else {
                    NotificationsCenter.setupAlarm(OnBoarding2Activity.this); // Setup notification
                    Intent intent = new Intent(OnBoarding2Activity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestUserPermission.isGranted() || !ActivityCompat.shouldShowRequestPermissionRationale(this, RequestUserPermission.PERMISSIONS[0])) {
            NotificationsCenter.setupAlarm(OnBoarding2Activity.this); // Setup notification
            Intent intent = new Intent(OnBoarding2Activity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
