package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.UiSettings;

public class LogoAndScaleActivity extends FragmentActivity implements View.OnClickListener{

    private TencentMap mMap;
    private UiSettings mUiSettings;

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_and_scale);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);

        et1 = (EditText)findViewById(R.id.et1);
        et2 = (EditText)findViewById(R.id.et2);
        et3 = (EditText)findViewById(R.id.et3);
        et4 = (EditText)findViewById(R.id.et4);
        et5 = (EditText)findViewById(R.id.et5);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onDestroy() {
        //clearPolygonWithText();
        super.onDestroy();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        //mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
        //mMap.setLogoAnchor(1);
        mUiSettings.setZoomControlsEnabled(false);
        //mMap.setDrawPillarWith2DStyle(true);
        //mUiSettings.showScaleView(false);
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLogoAnchor(){
        if(!checkReady()){
            return;
        }

        String anchor = et1.getText().toString();
        if(TextUtils.isEmpty(anchor)){
            Toast.makeText(getApplicationContext(), "please input", Toast.LENGTH_SHORT).show();
            return;
        }

        mMap.setLogoAnchor(Integer.parseInt(anchor));

    }

    private void setLogoAnchorWithMargin(){
        if(!checkReady()){
            return;
        }

        String anchor = et1.getText().toString();
        String top = et2.getText().toString();
        String bottom = et3.getText().toString();
        String left = et4.getText().toString();
        String right = et5.getText().toString();
        if(TextUtils.isEmpty(anchor) || TextUtils.isEmpty(top) || TextUtils.isEmpty(bottom) || TextUtils.isEmpty(left) || TextUtils.isEmpty(right)){
            Toast.makeText(getApplicationContext(), "please input", Toast.LENGTH_SHORT).show();
            return;
        }

        mMap.setLogoAnchorWithMargin(Integer.parseInt(anchor),Integer.parseInt(top),Integer.parseInt(bottom),Integer.parseInt(left),Integer.parseInt(right));

    }

    private void setScaleAnchor(){
        if(!checkReady()){
            return;
        }

        String anchor = et1.getText().toString();
        if(TextUtils.isEmpty(anchor)){
            Toast.makeText(getApplicationContext(), "please input", Toast.LENGTH_SHORT).show();
            return;
        }

        mMap.setScaleAnchor(Integer.parseInt(anchor));

    }

    private void setScaleAnchorWithMargin(){
        if(!checkReady()){
            return;
        }

        String anchor = et1.getText().toString();
        String top = et2.getText().toString();
        String bottom = et3.getText().toString();
        String left = et4.getText().toString();
        String right = et5.getText().toString();
        if(TextUtils.isEmpty(anchor) || TextUtils.isEmpty(top) || TextUtils.isEmpty(bottom) || TextUtils.isEmpty(left) || TextUtils.isEmpty(right)){
            Toast.makeText(getApplicationContext(), "please input", Toast.LENGTH_SHORT).show();
            return;
        }

        mMap.setScaleAnchorWithMargin(Integer.parseInt(anchor), Integer.parseInt(top), Integer.parseInt(bottom), Integer.parseInt(left), Integer.parseInt(right));

    }

//    private void setLogoLeftMargin(){
//        if(!checkReady()){
//            return;
//        }
//        String s1 = et1.getText().toString();
//        if(TextUtils.isEmpty(s1)){
//            return;
//        }
//        mMap.setLogoLeftMargin(Integer.parseInt(s1));
//    }

//    private void setLogoBottomMargin(){
//        if(!checkReady()){
//            return;
//        }
//        String s1 = et1.getText().toString();
//        if(TextUtils.isEmpty(s1)){
//            return;
//        }
//        mMap.setLogoBottomMargin(Integer.parseInt(s1));
//    }

//    private void setLogoMarginRate(){
//
//        if(!checkReady()){
//            return;
//        }
//        String s1 = et1.getText().toString();
//        if(TextUtils.isEmpty(s1)){
//            return;
//        }
//        String s2 = et2.getText().toString();
//        if(TextUtils.isEmpty(s2)){
//            return;
//        }
//
//        mMap.setLogoMarginRate(Integer.parseInt(s1), Float.parseFloat(s2));
//    }

    boolean isShowScale = false;
    private void showScale(){
        if(!checkReady()){
            return;
        }
        mUiSettings.showScaleView(isShowScale);
        if(isShowScale){
            isShowScale = false;
        }else{
            isShowScale = true;
        }
    }

    private void getScaleState(){
        if(!checkReady()){
            return;
        }
        boolean state = mUiSettings.isScaleVisable();
        Toast.makeText(getApplicationContext(),"isShow:" + state,Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn1:
                setLogoAnchor();
                break;
            case R.id.btn2:
                setLogoAnchorWithMargin();

                break;
            case R.id.btn3:
                setScaleAnchor();
                break;
            case R.id.btn4:
                setScaleAnchorWithMargin();
                break;
            case R.id.btn5:
                showScale();
                break;
            case R.id.btn6:
                getScaleState();
                break;
        }
    }
}
