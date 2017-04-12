package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SlidingDrawer;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class SlidingDrawerMapActivity extends FragmentActivity {
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
	SlidingDrawer sd;
	FrameLayout parent;
	
	private MapView mapView1;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mutillayer);
		
//		View view = LayoutInflater.from(this).inflate(R.layout.mutilfragment, null);
//		mapView = (MapView) view.findViewById(R.id.content);
//		mapView.setOnTop(true);
//		addContentView(view, new LayoutParams(1000, 1000));
		
		parent = (FrameLayout) findViewById(R.id.parent);
		mapView1 = (MapView) findViewById(R.id.map1);
		
		zoomToSpan();
		fm = getSupportFragmentManager();

		btnStart = (Button) this.findViewById(R.id.btnanimate);
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentTransaction ft = fm.beginTransaction();
				mapTop = new MapFragment();
				mapTop.setOnTop(true);
				ft.add(R.id.container, mapTop, MAP_TOP);
				ft.show(mapTop);
				ft.commit();
			}
		});
	}

	// private void setUpTopMapIfNeeded() {
	// // Do a null check to confirm that we have not already instantiated the
	// // map.
	// if (mapTop == null) {
	// // Try to obtain the map from the SupportMapFragment.
	// mMap = ((SupportMapFragment) getSupportFragmentManager()
	// .findFragmentById(R.id.map)).getMap();
	// // Check if we were successful in obtaining the map.
	// if (mMap != null) {
	// setUpTopMap();
	// }
	// }
	//
	// }
	//
	// public void setUpTopMap(){
	// // mapTop.getMap().getMapView().setOnTop(true);
	// }
	@Override
	protected void onResume() {
		super.onResume();
		mapView1.onResume();
//		mapView.onResume();
		// setUpMapIfNeeded();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		mapView.onPause();
		mapView1.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView1.onDestroy();
	}

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
		// Builder boundbuilder = new LatLngBounds.Builder();
		// boundbuilder.include(destCar);
		// boundbuilder.include(destTaxi);
		// boundbuilder.include(point2);
		// boundbuilder.include(point3);
		// LatLngBounds bounds = boundbuilder.build();
		//
		// mMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(bounds,
		// 200, 0, 0, 200));
	}

}
