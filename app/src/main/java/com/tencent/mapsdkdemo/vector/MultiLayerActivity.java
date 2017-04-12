package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class MultiLayerActivity extends FragmentActivity {
	private static final String MAP_TOP = "maptop";
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */
	private TencentMap mMap;
	private FragmentManager fm;
	Button btnStart = null;
	private MapFragment mapTop;
	int icarindex = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mutillayer);
//		setUpMapIfNeeded();
		
		zoomToSpan();
		fm = getSupportFragmentManager();

		btnStart = (Button)this.findViewById(R.id.btnanimate);
		btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = fm.beginTransaction();
				mapTop = new MapFragment();
				mapTop.setOnTop(true);
				ft.add(R.id.container, mapTop , MAP_TOP);
				ft.show(mapTop);
				ft.commit();
			}});
	}

	@Override
	protected void onResume() {
		super.onResume();
//		setUpMapIfNeeded();
	}

//	private void setUpTopMapIfNeeded() {
//		// Do a null check to confirm that we have not already instantiated the
//		// map.
//		if (mapTop == null) {
//			// Try to obtain the map from the SupportMapFragment.
//			mMap = ((SupportMapFragment) getSupportFragmentManager()
//					.findFragmentById(R.id.map)).getMap();
//			// Check if we were successful in obtaining the map.
//			if (mMap != null) {
//				setUpTopMap();
//			}
//		}
//
//	}
//	
//	public void setUpTopMap(){
////		mapTop.getMap().getMapView().setOnTop(true);
//	}
	
	
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}

	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera. In this case, we just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {
	}
	
	private void zoomToSpan() {
//		Builder boundbuilder = new LatLngBounds.Builder();
//		boundbuilder.include(destCar);
//		boundbuilder.include(destTaxi);
//		boundbuilder.include(point2);
//		boundbuilder.include(point3);
//		LatLngBounds bounds = boundbuilder.build();
//		
//		mMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(bounds, 200, 0, 0, 200));
	}
	
}
