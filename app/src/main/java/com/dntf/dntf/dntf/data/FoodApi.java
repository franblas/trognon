package com.dntf.dntf.dntf.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by franblas on 23/10/16.
 */
public class FoodApi {

    final static private String apiUrl = "http://world.openfoodfacts.org";
    final static private String PRODUCT_NAME_KEY = "product_name";
    static public String BARCODE_SINGLE_KEY = "barcode_value";
    static private String ADDED_TIME_KEY = "dntf_added_time";

    static public JSONObject getProductFromCode(String code) throws JSONException {
        StringBuffer sb = new StringBuffer();
        InputStream in = null;
        JSONObject jObject = new JSONObject("{\"product\": {}}");
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(apiUrl + "/api/v0/product/" + code + ".json");
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String result = sb.toString();
            jObject = new JSONObject(result).getJSONObject("product");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            urlConnection.disconnect();
        }
        return jObject;
    }

    static public String getProductName(JSONObject foodApiResult) {
        String productName = "";
        try {
            productName = foodApiResult.getString(PRODUCT_NAME_KEY);
            productName = productName.replace("  ", " ");
            productName = productName.substring(0, 1).toUpperCase() + productName.substring(1);
        } catch (JSONException e) {
            try {
                productName = foodApiResult.getString(BARCODE_SINGLE_KEY);
            } catch (JSONException err) {
                err.printStackTrace();
            }
        }
        return productName.trim();
    }

    static public long getAddedTime(JSONObject foodApiResult) {
        long addedTime = 0;
        try {
            addedTime = foodApiResult.getLong(ADDED_TIME_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addedTime;
    }

    static public JSONObject addNowTime(JSONObject obj) {
        long now = System.currentTimeMillis();
        try {
            obj.put(ADDED_TIME_KEY, now);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}

