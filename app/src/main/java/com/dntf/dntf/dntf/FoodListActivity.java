package com.dntf.dntf.dntf;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by franblas on 22/10/16.
 */
public class FoodListActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private ListView mListView;
    private SharedData sharedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        this.setupCustomActionBar();

        sharedData = new SharedData(this);
        ArrayList<JSONObject> products = sharedData.getProductsList();

        mListView = (ListView) findViewById(R.id.recipeListView2);

        if (products.size() > 0) {
            FoodList adapter = new FoodList(this, products);
            mListView.setAdapter(adapter);
        } else {
            List<String> emptyListMessage = Arrays.asList(getString(R.string.food_list_nothing));
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, emptyListMessage);
            mListView.setAdapter(adapter);
        }

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
        actionBarTitle.setText(R.string.title_section2);

        getSupportActionBar().setCustomView(customView);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(FoodListActivity.this, MainActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 0;
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                intent = new Intent(FoodListActivity.this, AboutActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 2;
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
