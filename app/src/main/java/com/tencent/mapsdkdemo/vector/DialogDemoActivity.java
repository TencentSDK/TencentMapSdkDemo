
package com.tencent.mapsdkdemo.vector;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.map.lib.MapLanguage;
import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

public class DialogDemoActivity extends Activity implements View.OnClickListener {

    private Button actionButton = null;

    private MapView mMapView;

    private TencentMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        TencentMap.setMapConfig(TencentMap.MAP_CONFIG_LIGHT);
        setContentView(R.layout.dialog_demo);
        actionButton = (Button)findViewById(R.id.button_action);
        mMapView = (MapView)findViewById(R.id.map1);
        actionButton.setOnClickListener(this);
        mMap = mMapView.getMap();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        Log.d("yyyy", "onResume");
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        Log.d("yyyy", "onPause");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mMapView.onDestroy();
        Log.d("yyyy", "onDestroy");
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        mMapView.onRestart();
        Log.d("yyyy", "onRestart");
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        mMapView.onStart();
        // Log.d("yyyy", "onStart");
        mMapView.onResume();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        mMapView.onStop();
        Log.d("yyyy", "onResume");
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        if (R.id.button_action == id) {
            // /**
            // * AlertDialog.Builder builder = new Builder(this);
            // * builder.setMessage("确认退出吗？"); builder.setTitle("提示");
            // * builder.setPositiveButton("确认", new OnClickListener() {
            // *
            // * @Override public void onClick(DialogInterface dialog, int
            // which)
            // * { dialog.dismiss(); finish(); } });
            // * builder.setNegativeButton("取消", new OnClickListener() {
            // * @Override public void onClick(DialogInterface dialog, int
            // which)
            // * { dialog.dismiss(); } }); builder.create().show();
            // */
            // ComponentName componentName = new
            // ComponentName("com.tencent.rorbbin.androidtest",
            // "com.tencent.rorbbin.androidtest.TransparentActivity");// 这个没问题
            // Intent i = new Intent();
            // // i.setClass(this, TransparentActivity.class);
            // i.setComponent(componentName);
            // this.startActivity(i);

//            testSetMapCenterAndScale();
            testSetLanguage();
            testGetLanguage();
        }
    }

    /**
     * 测试TencentMap中的setMapCenterAndScale(...)接口
     */
    private void testSetMapCenterAndScale() {
        if (mMap != null) {
            mMap.setMapCenterAndScale(0.2f, 0.8f, 18);
        }
    }

    private MapLanguage mLanguage = MapLanguage.LAN_CHINESE;

    private void testSetLanguage() {
        if (mLanguage == MapLanguage.LAN_CHINESE) {
            mLanguage = MapLanguage.LAN_ENGLISH;
        } else {
            mLanguage = MapLanguage.LAN_CHINESE;
        }

        if (mMap != null) {
            mMap.setLanguage(mLanguage);
        }
    }

    private void testGetLanguage() {
        String msg = null;

        if (mMap != null) {
            MapLanguage lan = mMap.getLanguage();

            if (lan == MapLanguage.LAN_ENGLISH) {
                msg = "Switch to english";
            } else {
                msg = "切换到中文";
            }
        } else {
            msg = "Map not initialized...";
        }

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
