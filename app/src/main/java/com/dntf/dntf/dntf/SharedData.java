package com.dntf.dntf.dntf;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by franblas on 26/11/16.
 */
public class SharedData {
    final private String appPreferencesReference = "dntfPreferences";
    final private String productsReference = "products";
    final private String onBoardingReference = "onBoarding";

    private Gson gson;

    public SharedPreferences sharedPreferences;

    public SharedData(Context context) {
        gson = new Gson();
        sharedPreferences = context.getSharedPreferences(appPreferencesReference, Context.MODE_PRIVATE);
    }

    public void onBoardingDone() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(onBoardingReference, true);
        editor.commit();
    }

    public Boolean onBoardingStatus() {
        return sharedPreferences.getBoolean(onBoardingReference, false);
    }

    public ArrayList<JSONObject> getProductsList() {
        ArrayList<JSONObject> itemsList = new ArrayList<>();
        String products = sharedPreferences.getString(productsReference, null);
        Type type = new TypeToken<ArrayList<JSONObject>>() {}.getType();
        if (products != null) {
            itemsList.addAll(gson.<Collection<? extends JSONObject>>fromJson(products, type));
        }
        return itemsList;
    }

    public void addProductToList(JSONObject foodApiResult, String productName, String barcodeValue) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        ArrayList<JSONObject> itemsList = getProductsList();
        foodApiResult = FoodApi.addNowTime(foodApiResult);
        if (productName != "") {
            itemsList.add(foodApiResult);
        } else {
            try {
                JSONObject unknownProduct = new JSONObject("{\"" + FoodApi.BARCODE_SINGLE_KEY + "\": " + barcodeValue + "}");
                unknownProduct = FoodApi.addNowTime(unknownProduct);
                itemsList.add(unknownProduct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String json = gson.toJson(itemsList);
        editor.putString(productsReference, json);
        editor.commit();
    }

    public void newProductsList(ArrayList<JSONObject> products) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(products);
        editor.putString(productsReference, json);
        editor.commit();
    }

}
