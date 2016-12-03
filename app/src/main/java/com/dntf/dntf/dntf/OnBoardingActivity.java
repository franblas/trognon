package com.dntf.dntf.dntf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by franblas on 27/11/16.
 */
public class OnBoardingActivity extends AppCompatActivity {

    private TextView onBoardingTxt;
    private Button onBoardingBtn;
    private SharedData sharedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        sharedData = new SharedData(this);
        if (sharedData.isOnBoardingDone()) {
            Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        String onBoardingText = "Welcome to Trognon !<br/><br/>" +
                "Trognon is a small app to help you to remind the food you bought before it reaches the expiration date. " +
                "Just scan barcodes of important food and Trognon will add it to your list and send notifications before food expired.<br/><br/>" +
                "Trogon is <a href='https://en.wikipedia.org/wiki/Privacy_by_design'>private by design</a>, your data remains on your phone and is never used by any servers or analysers.<br/><br/>" +
                "To be able to scan products, the app need the permission to use the camera, let's enable it on the next screen.";
        onBoardingTxt = (TextView) findViewById(R.id.onBoardingTxt);
        onBoardingTxt.setClickable(true);
        onBoardingTxt.setMovementMethod(LinkMovementMethod.getInstance());
        onBoardingTxt.setText(Html.fromHtml(onBoardingText), TextView.BufferType.SPANNABLE);

        onBoardingBtn = (Button) findViewById(R.id.onBoardingBtn);
        onBoardingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingActivity.this, OnBoarding2Activity.class);
                startActivity(intent);
            }
        });

    }
}
