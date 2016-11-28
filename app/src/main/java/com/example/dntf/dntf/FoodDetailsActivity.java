package com.example.dntf.dntf;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by franblas on 28/11/16.
 */
public class FoodDetailsActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private String noDetailsFound = "No details found...";
    private int expiredFoodStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        
        this.setupCustomActionBar();

        Bundle bExtras = getIntent().getExtras();

        String productName = "";
        JSONObject product = null;
        try {
            product = new JSONObject(bExtras.getString(FoodList.EXTRA_PRODUCT_KEY));
            productName = FoodApi.getProductName(product);
            expiredFoodStatus = bExtras.getInt(FoodList.EXTRA_EXPIRED_FOOD_STATUS_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView imageView = (ImageView) this.findViewById(R.id.foodDetailIcon);
        imageView.setColorFilter(expiredFoodStatus);
        imageView.setImageResource(R.drawable.fork_and_spoon);

        TextView foodDetailProductName = (TextView) this.findViewById(R.id.foodDetailProductName);
        foodDetailProductName.setText(productName);

        TextView foodDetailTxt = (TextView) this.findViewById(R.id.foodDetailTxt);
        foodDetailTxt.setText(Html.fromHtml(buildDetails(product)));

    }

    private String buildDetails(JSONObject product) {
        String res = "";
        if (product != null) {
            try {
                List<String> keys = Arrays.asList("brands", "countries", "quantity", "nutrition_score_debug", "nutriments", "ingredients_text_debug");
                for (String key : keys) {
                    String formatedKey = key.replace("_", " ");
                    res += "<b>" + formatedKey + "</b>: " + product.get(key).toString() + "<br/><br/>";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            res += noDetailsFound;
        }
        return res;
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
        actionBarTitle.setText(R.string.title_section4);

        getSupportActionBar().setCustomView(customView);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(FoodDetailsActivity.this, MainActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 0;
                startActivity(intent);
                break;
            case 1:
                intent = new Intent(FoodDetailsActivity.this, FoodListActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 1;
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(FoodDetailsActivity.this, AboutActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 2;
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}
