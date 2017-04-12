package com.tencent.mapsdkdemo.vector;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.OrientationListener;
import com.tencent.tencentmap.mapsdk.maps.OrientationManager;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class DirectionMapActivity extends FragmentActivity {
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private TencentMap mMap;
  
    private OrientationListener orentationListener=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diraction_demo);
        setUpMapIfNeeded();
        setUpSensorManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
   
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
        
    }
    
    private void setUpSensorManager()
    {
    	orentationListener=new OrientationListener(){

			@Override
			public void onOrientationChanged(float x, float y, float z) {
				// TODO Auto-generated method stub
				CameraPosition camerPosition =
	                    new CameraPosition.Builder()
	                            .bearing(x).tilt(45).build();
	                    CameraUpdate camerUpdate=CameraUpdateFactory.newCameraPosition(camerPosition);
	                    mMap.animateCamera(camerUpdate);
				
			}};
    	OrientationManager.getInstance(this).addOrientationListener(orentationListener);
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		OrientationManager.getInstance(this).destroy();
		super.onDestroy();
	}
    
    
    
}
