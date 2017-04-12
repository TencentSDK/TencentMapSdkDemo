package com.tencent.mapsdkdemo.vector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerScrollListener;

import com.tencent.tencentmap.mapsdk.maps.MapView;

public class MapFragment extends Fragment {

	private boolean isOnTop = false;

	private MapView mapView;
	private SlidingDrawer sd;
	public MapFragment() {
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// b.a(bv, activity);
	}

	public void onInflate(Activity activity, AttributeSet attributeset,
			Bundle bundle) {
		super.onInflate(activity, attributeset, bundle);
	}

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	public View onCreateView(LayoutInflater layoutinflater,
			ViewGroup viewgroup, Bundle bundle) {
//		mapView = new MapView(this.getActivity().getBaseContext());
//		LinearLayout fl = new LinearLayout(getActivity());
//		
//		Button b = new Button(getActivity());
//		
//
//		fl.addView(mapView, new LayoutParams(-1, -1));

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.mutilfragment, viewgroup, false);
		mapView = (MapView) view.findViewById(R.id.content);
		mapView.setOnTop(isOnTop);
		sd = (SlidingDrawer) view.findViewById(R.id.drawer);
		
		sd.setOnDrawerScrollListener(new OnDrawerScrollListener() {
			
			@Override
			public void onScrollStarted() {
				Log.d("sliding", "scroll start");
				
			}
			
			@Override
			public void onScrollEnded() {
				Log.d("sliding", "scroll end");				
			}
		});
		
		return view;
	}

	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	public void onPause() {
		mapView.onPause();
		super.onPause();
	}

	public void onDestroyView() {
		mapView.onDestroy();
		super.onDestroyView();
	}

	public void onDestroy() {
		super.onDestroy();
	}

	public void onLowMemory() {
		super.onLowMemory();
	}

	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
	}

	public void setArguments(Bundle bundle) {
		super.setArguments(bundle);
	}

	public void setOnTop(boolean isOnTop) {
		this.isOnTop = isOnTop;
	}
}
