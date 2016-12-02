package com.dntf.dntf.dntf;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by franblas on 22/10/16.
 */
public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private SurfaceView cameraView;
    private TextView barcodeInfo;
    private CameraSource cameraSource;
    private BarcodeDetector detector;
    private ArrayList<String> listItems = new ArrayList<>();
    private ArrayAdapter adapter;

    private SharedData sharedData;
    private String barcodeValueBuffer = "";
    private JSONObject foodApiResult = new JSONObject();

    private long TWO_SECS_DELAY = 2000;
    private String emptyListMessage = "Nothing scanned for now...";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setupCustomActionBar();

        sharedData = new SharedData(this);

        // Setup notification
        NotificationsCenter.setupAlarm(MainActivity.this);

        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        barcodeInfo = (TextView) findViewById(R.id.barcodeInfo);

        ListView mListView = (ListView) findViewById(R.id.recipeListView);

        if (listItems.size() == 0) { listItems.add(emptyListMessage); }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);

        detector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.EAN_13 | Barcode.EAN_8)
                .build();
        cameraSource = new CameraSource.Builder(this, detector)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setFacing(sharedData.getCameraFacingStatus())
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        sharedData.onBoardingDone();
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    Log.e("CAMERA SOURCE", e.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }

        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {}

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() <= 0) {
                    return;
                }

                final String barcodeValue = barcodes.valueAt(0).displayValue;

                // Check if the barcode is the same in short time interval
                // if true discard the product because it's the same detection
                if (barcodeValue.equals(barcodeValueBuffer)) {
                    return;
                }
                barcodeValueBuffer = barcodeValue;
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                barcodeValueBuffer = "";
                            }
                        },
                        TWO_SECS_DELAY
                );

                //Set barcode info on the camera when detected
                setBarcodeInfo("Found: " + barcodeValue);
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                setBarcodeInfo("");
                            }
                        },
                        TWO_SECS_DELAY
                );

                try {
                    foodApiResult = FoodApi.getProductFromCode(barcodeValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String productName = FoodApi.getProductName(foodApiResult);
                        if (listItems.get(0) == emptyListMessage) { listItems.clear(); }
                        if (productName != "") {
                            listItems.add(0, productName);
                        } else {
                            listItems.add(0, barcodeValue);
                        }
                        sharedData.addProductToList(foodApiResult, productName, barcodeValue);

                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void setCameraFlashMode() {
        Boolean flashOn = sharedData.getCameraFlashModeStatus();
        Button btn = (Button) getSupportActionBar().getCustomView().findViewById(R.id.actionBarFlashCamera);
        if (flashOn) {
            cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            sharedData.setCameraFlashModeStatus(false);
            btn.setBackground(getResources().getDrawable(R.drawable.flash_close));
        } else {
            cameraSource.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            sharedData.setCameraFlashModeStatus(true);
            btn.setBackground(getResources().getDrawable(R.drawable.flash_open));
        }
    }

    private void setCameraFacing() {
        int facingStatus = cameraSource.getCameraFacing();
        if (facingStatus == CameraSource.CAMERA_FACING_BACK) {
            sharedData.setCameraFacingStatus(CameraSource.CAMERA_FACING_FRONT);
        } else {
            sharedData.setCameraFacingStatus(CameraSource.CAMERA_FACING_BACK);
        }
        // reload activity
        finish();
        startActivity(getIntent());
    }

    private void setBarcodeInfo(final String val) {
        barcodeInfo.post(new Runnable() {
            @Override
            public void run() {
                barcodeInfo.setText(val);
            }
        });
    }

    private void setupCustomActionBar() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.nephritis)));

        View customView = getLayoutInflater().inflate(R.layout.actionbar_custom_main, null);

        final DrawerLayout drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        Button actionBarBtn = (Button) customView.findViewById(R.id.actionBarIcon);
        actionBarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        TextView actionBarTitle = (TextView) customView.findViewById(R.id.actionBarTitle);
        actionBarTitle.setText(R.string.title_section1);

        Button setFlashBtn = (Button) customView.findViewById(R.id.actionBarFlashCamera);
        setFlashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setCameraFlashMode();
                } catch (Exception e) {
                    Log.i("Flash Mode", e.getMessage());
                }

            }
        });

        Button reverseCameraBtn = (Button) customView.findViewById(R.id.actionBarReverseCamera);
        reverseCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setCameraFacing();
                } catch (Exception e) {
                    Log.i("Camera Facing", e.getMessage());
                }

            }
        });

        getSupportActionBar().setCustomView(customView);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                Intent intent = new Intent(MainActivity.this, FoodListActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 1;
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                NavigationDrawerFragment.mCurrentSelectedPosition = 2;
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true); // leave the app
    }
}
