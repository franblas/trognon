package com.dntf.dntf.dntf;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by franblas on 28/11/16.
 */
public class AboutActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private TextView aboutTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        this.setupCustomActionBar();

        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String aboutText = "About Trognon ...<br/><br/>" +
                "Trognon is a small app to help you to remind the food you bought before it reaches the expiration date. " +
                "Just scan barcodes of important food and Trognon will add it to your list and send notifications before food expired.<br/><br/>" +
                "Trogon is <a href='https://en.wikipedia.org/wiki/Privacy_by_design'>private by design</a>, your list remains on your phone and is never used by any servers or analysers.<br/><br/>" +
                "The app is using <a href='http://world.openfoodfacts.org/'>open food facts</a> database to get informations on products.<br/><br/>" +
                "Version " + version;
        aboutTxt = (TextView) findViewById(R.id.aboutTxt);
        aboutTxt.setClickable(true);
        aboutTxt.setMovementMethod(LinkMovementMethod.getInstance());
        aboutTxt.setText(Html.fromHtml(aboutText));
    }

    private void setupCustomActionBar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nephritis)));
        View customView = getLayoutInflater().inflate(R.layout.actionbar_custom, null);

        final DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        Button actionBarBtn = (Button) customView.findViewById(R.id.actionBarIcon);
        actionBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        TextView actionBarTitle = (TextView) customView.findViewById(R.id.actionBarTitle);
        actionBarTitle.setText(R.string.title_section3);

        getSupportActionBar().setCustomView(customView);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 0;
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(AboutActivity.this, FoodListActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 1;
                startActivity(intent);
                break;
            case 2:
                break;
            default:
                break;
        }

    }
}
