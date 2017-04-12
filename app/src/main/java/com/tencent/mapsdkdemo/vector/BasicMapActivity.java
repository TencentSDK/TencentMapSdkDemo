package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
public class BasicMapActivity extends FragmentActivity {
	private static final String MAP_TOP = "maptop";
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */
	private TencentMap mMap;
	private FragmentManager fm;
	
	private Button bv1;
	private Button bv2;
	private Button bv3;
	
	private SupportMapFragment mapTop;
	int icarindex = 0;

	private MapView mapView1;
	private MapView mapView2;
	private MapView mapView3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basic_demo);
		// setUpMapIfNeeded();

		// zoomToSpan();

		bv1 = (Button) findViewById(R.id.bv1);
		bv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//mapView2.setLeft(mapView2.getLeft() - 5);
				//mapView2.setRight(mapView2.getRight() + 5);
//				mapView1.setOnTop(true);
//				mapView2.setOnTop(false);
//				mapView3.setOnTop(false);
			}
		});
		
		bv2 = (Button) findViewById(R.id.bv2);
		bv2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mapView1.setOnTop(false);
				mapView2.setOnTop(true);
				mapView3.setOnTop(false);
			}
		});
		
		bv3 = (Button) findViewById(R.id.bv3);
		bv3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mapView1.setOnTop(false);
				mapView2.setOnTop(false);
				mapView3.setOnTop(true);
			}
		});
		
		mapView3 = (MapView) findViewById(R.id.map3);
		mapView2 = (MapView) findViewById(R.id.map2);
		mapView1 = (MapView) findViewById(R.id.map1);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView2.onResume();
		mapView3.onResume();
		mapView1.onResume();
		// setUpMapIfNeeded();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView2.onPause();
		mapView3.onPause();
		mapView1.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView3.onDestroy();
		mapView2.onDestroy();
		mapView1.onDestroy();
	}
	
	

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mapView2.onStart();
		mapView3.onStart();
		mapView1.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mapView2.onStop();
		mapView3.onStop();
		mapView1.onStop();
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
