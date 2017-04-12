package com.tencent.mapsdkdemo.vector;

import java.util.Random;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.CancelableCallback;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnCameraChangeListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapClickListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapLoadedCallback;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.TencentMapGestureListener;

/**
 * Created by rorbbinlan on 2015/6/13.
 */
public class CustomerMapViewActivity extends FragmentActivity implements
		View.OnClickListener {


	private static final LatLng latlng_disanji = new LatLng(39.984253,116.307439);

	private static final String MAP_TOP = "maptop";
	private MapFragment mapTop;
	private FragmentManager fm;
	private int logoAncher = 0;

	private CustomerMapView mapView;
	private Button showFragmentButton;
	private TencentMap mMap;
	private Marker mMarkerDisanji;
	private Handler mHandler = new Handler();
	private FrameLayout frameLayout;
	private Marker disanji2;

	private Runnable updateIconRunnable = new Runnable() {

		private int number = 0;
		@Override
		public void run() {
			mMarkerDisanji.setTitle("当前时间：");
			mMarkerDisanji.setSnippet("number:" + number);
			mHandler.postDelayed(this, 10);
			number ++;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_mapview_demo);
		System.err.println("activity onCreate");
		mapView = (CustomerMapView) findViewById(R.id.customer_mapview_map);
		frameLayout = (FrameLayout) findViewById(R.id.framelayout);
		showFragmentButton = (Button) findViewById(R.id.customer_mapview_button);
		showFragmentButton.setOnClickListener(this);
		mMap = mapView.getMap();
		mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				Log.d("xxxx", "onMapLoaded");
				float level = mMap.calcuteZoomToSpanLevel(0, 0, 0, 0, new LatLng(40.914550,113.190765), new LatLng(40.622292,113.733215), new LatLng(40.891715,113.285522));
				Log.d("xxxx", "level:" + level);
				Log.d("yyyy3", "cameraposition:" + mMap.getCameraPosition());
			}
			
		});
		mMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng latlng) {
				Log.d("xxxx", "onMapClick");
			}
			
		});
		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition cameraposition) {
				Log.d("xxxx", "Map level:" + cameraposition.zoom);
			}
			
		});
		mMap.setOnCompassClickedListener(new TencentMap.OnCompassClickedListener() {
			
			@Override
			public void onCompassClicked() {
				// TODO Auto-generated method stub
				Log.d("xxxx", "onCompassClicked");
			}
		});
		
		mMap.addTencentMapGestureListener(new TencentMapGestureListener() {

			@Override
			public boolean onDoubleTap(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSingleTap(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onScroll(float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onLongPress(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onDown(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onUp(float x, float y) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapStable() {
				// TODO Auto-generated method stub
				Log.d("yyyy1", "cameraposition:" + mMap.getCameraPosition());
			}
			
		});
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
		// 1秒更新一下。
		mHandler.post(updateIconRunnable);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (R.id.customer_mapview_button == id) {
			/* do things you love to do
			FragmentTransaction ft = fm.beginTransaction();
			mapTop = new MapFragment();
			mapTop.setOnTop(true);
			ft.add(R.id.container, mapTop, MAP_TOP);
			ft.show(mapTop);
			ft.commit();*/
			//frameLayout.
			/*
			mMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

				@Override
				public void onMapLoaded() {
					Log.d("xxxx", "onMapLoaded");
					float level = mMap.calcuteZoomToSpanLevel(0, 0, 0, 0, new LatLng(40.914550,113.190765), new LatLng(40.622292,113.733215), new LatLng(40.891715,113.285522));
					Log.d("xxxx", "level:" + level);
					Log.d("yyyy3", "cameraposition:" + mMap.getCameraPosition());
				}
				
			});
			mMap.getUiSettings().showScaleView(new Random().nextBoolean());
			CameraPosition.Builder builder = new CameraPosition.Builder();
			builder.bearing(15);
			builder.tilt(30);
			builder.target(new LatLng(39.053318,117.684174));
			mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.build()), new CancelableCallback() {

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					Log.d("yyyy2", "cameraposition:" + mMap.getCameraPosition());
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					
				}
				
			});*/
			mMarkerDisanji.remove();
			disanji2 = mMap.addMarker(new MarkerOptions()
				.position(latlng_disanji)
				.title("第三极大厦")
				.snippet("地址: 北京市北四环西路66号")
				.draggable(true)
				.anchor(0.5f, 0.5f));
		}else if(R.id.customer_mapview_button2 == id) {
			logoAncher = (logoAncher + 1) % 6;
			//mMap.getUiSettings().setScaleViewPosition(logoAncher);
			//mMap.setLogoVisible(logoAncher % 2 == 0);
			//mMap.setLogoMarginRate(2, 0.5f);
			/*
			Intent intent = new Intent();
			intent.setClass(this, TransparentActivity.class);
			intent.setComponent(new ComponentName("com.example.androidjavatest", "com.example.androidjavatest.TransparentActivity"));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			mHandler.postDelayed(new Runnable() {public void run() {
				//mMap.requestRender();
				System.err.println("runnable request render");
			};}, 2000);*/
			//mMap.animateToNaviPosition2(mMap.getCameraPosition().target, 45, 15, 19, false);
			mMarkerDisanji.remove();

			mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
			Log.d("belli", "current scale: " + mMap.getCameraPosition().zoom);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mapView.onStart();
		System.err.println("activity onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		mapView.onRestart();
		System.err.println("activity onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
		System.err.println("activity onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		System.err.println("activity onPause");
	}

	@Override
	protected void onStop() {
		mapView.onStop();
		super.onStop();
		System.err.println("activity onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		System.err.println("activity onDestroy");
	}
}