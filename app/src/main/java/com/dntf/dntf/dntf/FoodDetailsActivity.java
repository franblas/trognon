package com.dntf.dntf.dntf;

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

import com.dntf.dntf.dntf.data.FoodApi;
import com.dntf.dntf.dntf.fragments.FoodList;
import com.dntf.dntf.dntf.fragments.NavigationDrawerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by franblas on 28/11/16.
 */
public class FoodDetailsActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private int expiredFoodStatus;
    private String productName = "";
    private String details = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        
        this.setupCustomActionBar();

        final Bundle bExtras = getIntent().getExtras();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                JSONObject product = null;
                try {
                    product = new JSONObject(bExtras.getString(FoodList.EXTRA_PRODUCT_KEY));
                    productName = FoodApi.getProductName(product);
                    expiredFoodStatus = bExtras.getInt(FoodList.EXTRA_EXPIRED_FOOD_STATUS_KEY);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                details = buildDetails(product);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageView imageView = (ImageView) FoodDetailsActivity.this.findViewById(R.id.foodDetailIcon);
                        imageView.setColorFilter(expiredFoodStatus);
                        imageView.setImageResource(R.drawable.dntf_logo_dark);

                        TextView foodDetailProductName = (TextView) FoodDetailsActivity.this.findViewById(R.id.foodDetailProductName);
                        foodDetailProductName.setText(productName);

                        TextView foodDetailTxt = (TextView) FoodDetailsActivity.this.findViewById(R.id.foodDetailTxt);
                        foodDetailTxt.setText(Html.fromHtml(details));
                    }
                });

            }
        });
        thread.start();

    }

    private String buildDetails(JSONObject product) {
        String noDetailsFound = getString(R.string.details_notfound);
        String res = "";
        List<String> keys = Arrays.asList("brands", "quantity", "countries", "code", "ingredients_text_debug", "nutriments", "nutrition_score_debug");
        List<String> formattedKeys = Arrays.asList(getString(R.string.details_brand), getString(R.string.details_quantity), getString(R.string.details_country), getString(R.string.details_code), getString(R.string.details_ingredients), getString(R.string.details_nutriments), getString(R.string.details_nutrition));
        if (product != null) {
            try {
                for (int i=0; i<keys.size(); i++) {
                    String key = keys.get(i);
                    String formattedKey = formattedKeys.get(i);
                    String addToRes = "<b>" + formattedKey + "</b>: &nbsp;";
                    switch (formattedKey) {
                        case "Ingredients":
                            addToRes += ingredientsFormatter(product.get(key).toString());
                            break;
                        case "Nutriments":
                            addToRes += "<br/>" + nutrimentsFormatter(product.get(key).toString());
                            break;
                        default:
                            addToRes += product.get(key).toString();
                            break;

                    }
                    res += addToRes + "<br/><br/>";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            res += noDetailsFound;
        }
        return res;
    }

    private String ingredientsFormatter(String ingredients) {
        return ingredients.trim().replace("_", "").replace(":", "");
    }

    private String nutrimentsFormatter(String nutriments) {
        String firstStep = nutriments.replace("{nameValuePairs={", "").replace("}}", "");
        String[] keyValuePairs = firstStep.split(",");
        Map<String, String> units = new HashMap<>();
        for (String k : keyValuePairs) {
            if (k.contains("_unit")) {
                String[] kv = k.split("=");
                units.put(kv[0].trim().replace("_unit", ""), kv[1]);
            }
        }

        String res = "";
        for (String k : keyValuePairs) {
            if (!k.contains("_unit")) {
                String[] kv = k.split("=");
                if (kv.length < 2) {
                    continue;
                }
                String key = kv[0].trim();
                String value = kv[1];
                String unit = units.get(key.split("_")[0]);
                if (unit == null) {
                    unit = "";
                }
                res += "&nbsp;&nbsp; &bull; &nbsp;<i>" + key.replace("_", " ") + "</i>: &nbsp;" + value + unit + "<br/>";
            }
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
