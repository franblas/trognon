package com.example.dntf.dntf;

import android.app.Activity;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by franblas on 01/11/16.
 */
public class FoodList extends ArrayAdapter<JSONObject> {

    private final Activity context;
    private final ArrayList<JSONObject> products;
    private SharedData sharedData;
    private long now;

    static protected String EXTRA_PRODUCT_KEY = "product";
    static protected String EXTRA_EXPIRED_FOOD_STATUS_KEY = "expiredFoodStatus";

    public FoodList(Activity context, ArrayList<JSONObject> products) {
        super(context, R.layout.food_list, products);
        this.context = context;
        this.products = products;
        sharedData = new SharedData(this.context);
        now = System.currentTimeMillis();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.food_list, null, true);

        long addedTime = FoodApi.getAddedTime(products.get(position));
        final int expiredFoodStatus = ExpiredFoodLogic.getColor(now, addedTime);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.foodIcon);
        imageView.setColorFilter(expiredFoodStatus);
        imageView.setImageResource(R.drawable.fork_and_spoon);

        TextView productNameTxt = (TextView) rowView.findViewById(R.id.productName);
        productNameTxt.setText(FoodApi.getProductName(products.get(position)));
        productNameTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FoodDetailsActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = -1;
                intent.putExtra(EXTRA_PRODUCT_KEY, products.get(position).toString());
                intent.putExtra(EXTRA_EXPIRED_FOOD_STATUS_KEY, expiredFoodStatus);
                context.startActivity(intent);
                return;
            }
        });

        TextView addedTimeTxt = (TextView) rowView.findViewById(R.id.addedTime);
        String relativeTime = DateUtils.getRelativeTimeSpanString(addedTime, now, DateUtils.DAY_IN_MILLIS).toString();
        addedTimeTxt.setText(relativeTime);

        Button delBtn = (Button) rowView.findViewById(R.id.delBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                products.remove(position);
                sharedData.newProductsList(products);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }
}
