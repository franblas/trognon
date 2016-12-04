package com.dntf.dntf.dntf;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import static com.dntf.dntf.dntf.FoodApi.getProductFromCode;
import static com.dntf.dntf.dntf.FoodApi.getProductName;

/**
 * Created by franblas on 04/12/16.
 */
@RunWith(RobolectricTestRunner.class)
public class ApiTest {

    JSONObject obj = null;
    List<String> detailKeys = Arrays.asList("brands", "quantity", "countries", "ingredients_text_debug", "nutriments", "nutrition_score_debug");

    private JSONObject loadProductData() {
        JSONObject obj = null;
        try {
            obj = getProductFromCode("7622210203960");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Before
    public void setUp() throws Exception {
        obj = loadProductData();
    }

    @Test
    public void testValidateName() {
        Assert.assertEquals(getProductName(obj).equals("Ice Fresh"), true);
    }

    @Test
    public void testValidateDetailsFields() {
        for (String key : detailKeys) {
            Assert.assertEquals(obj.has(key), true);
        }
    }
}
