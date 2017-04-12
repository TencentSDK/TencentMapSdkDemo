package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.Date;

/**
 * Created by rorbbinlan on 2015/6/13.
 */
public class LifeCircleMapViewActivity extends FragmentActivity implements
		View.OnClickListener {


	private static final LatLng latlng_disanji = new LatLng(39.984253,116.307439);

	private static final String MAP_TOP = "maptop";
	private MapFragment mapTop;
	private FragmentManager fm;

	private CustomerMapView mapView;
	private Button showFragmentButton;
	private TencentMap mMap;
	private Marker mMarkerDisanji;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lifecircle_mapview_demo);
		mapView = (CustomerMapView) findViewById(R.id.customer_mapview_map);
		findViewById(R.id.customer_mapview_button_onstart).setOnClickListener(this);
		findViewById(R.id.customer_mapview_button_onresume).setOnClickListener(this);
		findViewById(R.id.customer_mapview_button_onpause).setOnClickListener(this);
		findViewById(R.id.customer_mapview_button_onstop).setOnClickListener(this);
		mMap = mapView.getMap();
		addMarkers();
	}

	private void addMarkers() {
		// Creates a draggable marker. Long press to drag.
		mMarkerDisanji = mMap.addMarker(new MarkerOptions()
						.position(latlng_disanji)
						.title("第三极大厦")
						.snippet("地址: 北京市北四环西路66号")
						.draggable(true)
						.anchor(0.5f, 0.5f));
		mMarkerDisanji.setNaviState(true, false);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (R.id.customer_mapview_button_onstart == id) {
			mapView.onStart();
		}else if (R.id.customer_mapview_button_onresume == id) {
			mapView.onResume();
		}else if (R.id.customer_mapview_button_onpause == id) {
			mapView.onPause();
		}else if (R.id.customer_mapview_button_onstop == id) {
			mapView.onStop();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}