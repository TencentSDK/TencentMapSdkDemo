
package com.tencent.mapsdkdemo.vector;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 承载MapView的Activity。
 */
public class ScreenShotDemoActivity extends Activity {

    /**
     * map view，铺在界面最底部的view
     */
    public MapView mapview = null;

    public TencentMap tencentMap = null;

    private ImageView imgView = null;

    private Handler handScreen = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            if (msg == null || msg.obj == null) {
                return;
            }
            Bitmap bmpScreen = (Bitmap)msg.obj;
            imgView.setImageBitmap(bmpScreen);

        }

    };

    final CameraPosition camerPosition_GUGONG = new CameraPosition.Builder()
            .target(new LatLng(39.91822, 116.397165)).zoom(14.5f).bearing(200).tilt(50).build();

    final CameraPosition camerPosition_YINKE = new CameraPosition.Builder()
            .target(new LatLng(31.226407, 121.48298)).zoom(17.5f).bearing(0).tilt(25).build();

    private Runnable runScreenShot = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            tencentMap.getScreenShot(handScreen, Config.ARGB_8888);
        }

    };

    // TencentMap.OnTrafficUpdateListener trafficUpdateListener = new
    // TencentMap.OnTrafficUpdateListener() {
    //
    // @Override
    // public void onTrafficUpdate(String city) {
    // Log.e("zxy", "onTrafficUpdate(): " + city);
    // // TODO Auto-generated method stub\
    // handScreen.postDelayed(runScreenShot, 2000);
    //
    // }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.screenshot);
        mapview = (MapView)findViewById(R.id.map);
        tencentMap = mapview.getMap();

        // tencentMap.setOnTrafficUpdate(trafficUpdateListener);
        tencentMap.setTrafficEnabled(true);

        imgView = (ImageView)this.findViewById(R.id.imgview);
        imgView.setScaleType(ScaleType.CENTER);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mapview.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mapview.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mapview.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 1, 0, "截屏坐标1");
        menu.add(0, 2, 0, "截屏坐标2");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:
                setCoord1();
                handScreen.postDelayed(runScreenShot, 2000);
                break;
            case 2:
                setCoor2();
                handScreen.postDelayed(runScreenShot, 2000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCoord1() {
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(camerPosition_GUGONG);
        tencentMap.moveCamera(update);
    }

    private void setCoor2() {
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(camerPosition_YINKE);
        tencentMap.moveCamera(update);
    }
}
