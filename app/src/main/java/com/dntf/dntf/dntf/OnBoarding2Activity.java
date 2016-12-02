package com.dntf.dntf.dntf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

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

        String onBoardingText = "Can Trognon use the camera to scan products ?<br/><br/>" +
                "Remember Trogon is <a href='https://en.wikipedia.org/wiki/Privacy_by_design'>private by design</a>, your data is safe.";
        onBoardingTxt = (TextView) findViewById(R.id.onBoardingTxt);
        onBoardingTxt.setClickable(true);
        onBoardingTxt.setMovementMethod(LinkMovementMethod.getInstance());
        onBoardingTxt.setText(Html.fromHtml(onBoardingText), TextView.BufferType.SPANNABLE);


        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestUserPermission.checkPermissions();
                        }
                    }
                },
                1500
        );

        onBoardingBtn = (Button) findViewById(R.id.onBoardingBtn);
        onBoardingBtn.setText("Let's Go");
        onBoardingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoarding2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
