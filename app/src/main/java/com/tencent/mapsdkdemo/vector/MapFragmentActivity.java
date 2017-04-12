package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapLoadedCallback;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

public class MapFragmentActivity extends FragmentActivity {

	private SupportMapFragment supportMapFragment;
	private CustMapFragment custMapFragment;
	private TextView btnSupportMapFrag;
	private TextView btnCustMapFrag;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_in_fragment);

		initView();
		setListener();
	}

	private void initView() {
		btnSupportMapFrag = (TextView)findViewById(R.id.btn_support_map_fragment);
		btnCustMapFrag = (TextView)findViewById(R.id.btn_cust_map_fragment);

		fragmentManager = getSupportFragmentManager();
		
		supportMapFragment = SupportMapFragment.newInstance(this);
		Marker marker1 = supportMapFragment.getMap()
				.addMarker(new MarkerOptions()
				.position(new LatLng(39.984129,116.307696))
				.title("SupportMapFragment"));
		marker1.showInfoWindow();
//		fragmentManager.beginTransaction().add(R.id.ll_frag_root, supportMapFragment).commit();

		custMapFragment = CustMapFragment.newInstance(this);
		Marker marker2 = custMapFragment.getMap()
				.addMarker(new MarkerOptions()
				.position(new LatLng(39.984129,116.307696))
				.title("CustMapFragment"));
		marker2.showInfoWindow();
		fragmentManager.beginTransaction().add(R.id.ll_frag_root, custMapFragment).commit();
	}

	private void setListener() {
		OnClickListener clickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.btn_support_map_fragment:
					showSupportFragment();
					break;
				case R.id.btn_cust_map_fragment:
					showCustFragment();
					break;

				default:
					break;
				}
			}
		};

		btnSupportMapFrag.setOnClickListener(clickListener);
		btnCustMapFrag.setOnClickListener(clickListener);
	}

	private void showSupportFragment() {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		if (fragmentManager.getFragments().size() == 0) {
			fragmentTransaction.add(R.id.ll_frag_root, supportMapFragment).commit();
		} else {
			fragmentTransaction.replace(R.id.ll_frag_root, supportMapFragment).commit();
		}
	}

	private void showCustFragment() {
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		TencentMap map = custMapFragment.getMap();
		map.setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				// TODO Auto-generated method stub
				
			}
			
		});
		if (fragmentManager.getFragments().size() == 0) {
			fragmentTransaction.add(R.id.ll_frag_root, custMapFragment).commit();
		} else {
			fragmentTransaction.replace(R.id.ll_frag_root, custMapFragment).commit();
		}
	}
}
