package com.dntf.dntf.dntf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dntf.dntf.dntf.data.SharedData;

/**
 * Created by franblas on 27/11/16.
 */
public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);

        SharedData sharedData = new SharedData(this);
        if (sharedData.isOnBoardingDone()) {
            Intent intent = new Intent(OnBoardingActivity.this, MainActivity.class);
            startActivity(intent);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        String onBoardingText = getString(R.string.onBoarding);
        TextView onBoardingTxt = (TextView) findViewById(R.id.onBoardingTxt);
        onBoardingTxt.setClickable(true);
        onBoardingTxt.setMovementMethod(LinkMovementMethod.getInstance());
        onBoardingTxt.setText(Html.fromHtml(onBoardingText), TextView.BufferType.SPANNABLE);

        Button onBoardingBtn = (Button) findViewById(R.id.onBoardingBtn);
        onBoardingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OnBoardingActivity.this, OnBoarding2Activity.class);
                startActivity(intent);
            }
        });

    }
}
