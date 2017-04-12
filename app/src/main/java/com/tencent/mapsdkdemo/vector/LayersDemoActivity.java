/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.mapsdkdemo.vector;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.tencent.tencentmap.mapsdk.maps.LocationSource;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;

/**
 * Demonstrates the different base layers of a map.
 */
@SuppressLint("NewApi")
public class LayersDemoActivity extends FragmentActivity{

    private TencentMap mMap;

    private CheckBox mTrafficCheckbox;
    private CheckBox mSatelliteCheckbox;
    private CheckBox mNightCheckbox;
    private CheckBox mMyLocationCheckbox;
    private CheckBox m2DStyleCheckbox;
    private FixLocationSource locationSource;
    private Handler mainThreadHandler = new Handler();
    private Circle circle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layers_demo);

        //Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        mTrafficCheckbox = (CheckBox) findViewById(R.id.traffic);
        mSatelliteCheckbox = (CheckBox) findViewById(R.id.satellite);
        mNightCheckbox = (CheckBox) findViewById(R.id.night);
        mMyLocationCheckbox = (CheckBox) findViewById(R.id.my_location);
        

        setUpMapIfNeeded();
        locationSource = new FixLocationSource(mainThreadHandler);
        
        if(mMap != null){
            mMap.setMapType(TencentMap.MAP_MODE_NORMAL);
        }
        
        // if (mMap != null) {
        // mMap.setTrafficColor(0xffff0000, 0xff00ff00, 0xff0000ff);
        // }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (mMap != null) {
            updateTraffic();
            updateMyLocation();
            
        }
    }
    
    

    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		locationSource.stopLocating();
		
	}

	private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
        
//        circle = mMap.addCircle(new CircleOptions()
//        .center(new LatLng(39.984253, 116.307439))
//        .radius(100)
//        .strokeWidth(1)
//        .strokeColor(Color.RED)
//        .fillColor(Color.LTGRAY).zIndex(-1));
    }

    /**
     * Called when the Traffic checkbox is clicked.
     */
    public void onTrafficToggled(View view) {
        //Log.d("jiabin", "onTrafficToggled");
        updateTraffic();
    }

    private void updateTraffic() {
        //Log.d("jiabin", "updateTraffic:" + mTrafficCheckbox.isChecked());
        mMap.setTrafficEnabled(mTrafficCheckbox.isChecked());
        
    }
    
    /**
     * Called when the Satellite checkbox is clicked.
     */
    public void onSatelliteToggled(View view){
        updateSatellite();
    }
    
    private void updateSatellite() {
        mMap.setSatelliteEnabled(mSatelliteCheckbox.isChecked());
    }
    
    /**
     * Called when the Night checkbox is clicked.
     */
    public void onNightToggled(View view){
        updateNight();
    }
    
    /**
     * useless
     */
    private void updateNight() {
        //mMap.setNightEnabled(mNightCheckbox.isChecked());
    }

    /**
     * Called when the MyLocation checkbox is clicked.
     */
    public void onMyLocationToggled(View view) {
        updateMyLocation();
    }
    
    public void onMap2DStyle(View v) {
    	mMap.setDrawPillarWith2DStyle(((CheckBox)v).isChecked());
    }

    private void updateMyLocation() {
    	if(mMyLocationCheckbox.isChecked()) {
    		mMap.setLocationSource(locationSource);
    		locationSource.startToLocate();
    	}else {
    		mMap.setLocationSource(null);
    		locationSource.stopLocating();
    	}
        mMap.setMyLocationEnabled(mMyLocationCheckbox.isChecked());
    }

    private void setLayer(String layerName) {
        if (layerName.equals(getString(R.string.normal))) {
            mMap.setMapType(TencentMap.MAP_MODE_NORMAL);
        } else if (layerName.equals(getString(R.string.satellite))) {
            mMap.setMapType(TencentMap.MAP_MODE_NORMAL);
        } else {
            Log.i("LDA", "Error setting layer with name " + layerName);
        }
    }

   
    
    private static class FixLocationSource implements LocationSource {
    	
    	private Handler handler;
    	public FixLocationSource(Handler h) {
 
    		handler = h;
    	}
    	private OnLocationChangedListener locationChangedListener;
    	
    	private Runnable updateLocation = new Runnable() {
    		public void run() {
    			Location l = new Location(LocationManager.GPS_PROVIDER);
				l.setLatitude(39.984253);
				l.setLongitude(116.307439);
				l.setAccuracy(20.0f);
				l.setTime(System.currentTimeMillis());
				onLocationChanged(l);
				handler.postDelayed(this, 1000);
    		}
    	};
    	
		@Override
		public void activate(OnLocationChangedListener onlocationchangedlistener) {
			locationChangedListener = onlocationchangedlistener;
		}

		@Override
		public void deactivate() {
			locationChangedListener = null;
		}
    	
		public void startToLocate() {
			handler.post(updateLocation);
		}
		
		public void stopLocating() {
			handler.removeCallbacks(updateLocation);
		}
		

		public void onLocationChanged(Location location) {
			Log.d("xxxx", "Get new location:" + location);
			if(locationChangedListener != null) {
				locationChangedListener.onLocationChanged(location);
			}
		}
    }
}
