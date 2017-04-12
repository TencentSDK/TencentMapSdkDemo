package com.tencent.mapsdkdemo.vector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;

public class CustMapFragment extends Fragment {
	
	private static final String LOG_TAG ="CustMapFragment";
	private static CustMapFragment mCustMapFragment;
	private MapView mapView;
	private TencentMap tencentMap;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		container = (LinearLayout)inflater.inflate(R.layout.fragment_cust_layout, null);
		mapView = (MapView)container.findViewById(R.id.map);
		return container;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate");
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mapView.onStart();
		Log.d(LOG_TAG, "onStart");
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
		Log.d(LOG_TAG, "onResume");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
		Log.d(LOG_TAG, "onPause");
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mapView.onStop();
		Log.d(LOG_TAG, "onStop");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
		mCustMapFragment = null;
		Log.d(LOG_TAG, "onDestroy");
	}
	
	public TencentMap getMap() {
		if (tencentMap == null) {
			tencentMap = mapView.getMap();
		}
		return tencentMap;
	}
	
	public static synchronized CustMapFragment newInstance(Context context) {
		if (mCustMapFragment == null) {
			mCustMapFragment = new CustMapFragment();
			mCustMapFragment.onCreateView(LayoutInflater.from(context), null, null);
		}
		return mCustMapFragment;
	}
}
