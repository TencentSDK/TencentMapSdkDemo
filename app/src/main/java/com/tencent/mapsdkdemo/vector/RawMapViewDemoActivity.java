package com.tencent.mapsdkdemo.vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.tencent.tencentmap.mapsdk.maps.MapView;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapLongClickListener;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;
import com.tencent.tencentmap.mapsdk.maps.model.VisibleRegion;

/**
 * 承载MapView的Activity。
 */
public class RawMapViewDemoActivity extends Activity
     {

    /**
     * map view，铺在界面最底部的view
     */
    public MapView mapview = null;
    public TencentMap sosoMap = null;
    View viewTip = null;
    private Marker MarkerMiddle;
    private Marker Markertemp;
	private Handler mHandler = new Handler();
    
    private Runnable updateIconRunnable = new Runnable() {

		private int number = 0;
		@Override
		public void run() {
			MarkerMiddle.setTitle("等待：");
			MarkerMiddle.setSnippet("number:" + number);
			mHandler.postDelayed(this, 30);
			number ++;
		}
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapview=new MapView(this);
        setContentView(mapview);
        
        sosoMap=mapview.getMap();
        sosoMap.setDrawPillarWith2DStyle(true);
        new Handler().postDelayed(new Runnable()
		{  
		    public void run()
		    {  
		         //execute the task
				if (mapview.getMap().getProjection() == null) {
					return;
				}

		    	VisibleRegion vg = mapview.getMap().getProjection().getVisibleRegion();
		        Log.d("farLeft", vg.farLeft.toString());
		        Log.d("farRight", vg.farRight.toString());
		        Log.d("nearLeft", vg.nearLeft.toString());
		        Log.d("nearRight", vg.nearRight.toString());
		        Log.d("latLngBounds-northeast", vg.latLngBounds.northeast.toString());
		        Log.d("latLngBounds-southwest", vg.latLngBounds.southwest.toString());
		    }  
		 }, 3000); 
        
        
        sosoMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			
			@Override
			public void onMapLongClick(LatLng latlng) {
				sosoMap.addMarker(new MarkerOptions().position(latlng));
				sosoMap.getInfoWindowAnimationManager().setInfowindowBackColor(0x000000);
				sosoMap.getInfoWindowAnimationManager().setInfowindowBackEnable(true);
				sosoMap.getInfoWindowAnimationManager().setInfowindowBackSacle(100, 80);
				Log.d("缩放级别："+sosoMap.getCameraPosition().zoom+"，当前纬度："+latlng.latitude, "metersPerPixe:"+sosoMap.getProjection().metersPerPixel(latlng.latitude));
			}
		});
        
       /* new Handler().postDelayed(new Runnable()
		{  
		    public void run()
		    {  
		         //execute the task
		    	mapview.onDestroy();
		    	Log.d("xxx", ""+sosoMap.isDestroyed());
		    	
		    }  
		 }, 8000); 
        
        new Handler().postDelayed(new Runnable()
		{  
		    public void run()
		    {  
		         //execute the task
		    	Log.d("xxx", ""+sosoMap.getVersion());
		    	Log.d("xxx", "xxx");
		    	
		    }  
		 }, 10000); */
        
        
        addTest();
        
        
    }
    
    private void addTest() {
    	LatLng start = new LatLng(39.970345,116.322298);
    	LatLng end = new LatLng(39.982682,116.307111);
    	LatLng middle = new LatLng((start.latitude+end.latitude)/2, (start.longitude+end.longitude)/2);
    	sosoMap.addPolyline(new PolylineOptions().add(start, end).road(false));
    	sosoMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).position(start).title("起点"));
    	Markertemp = sosoMap.addMarker(new MarkerOptions().position(end).title("终点"));
    	//.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
    	MarkerMiddle = sosoMap.addMarker(new MarkerOptions().position(middle).title("test"));
    	// 1秒更新一下。
		//mHandler.post(updateIconRunnable);
	}
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapview.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapview.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapview.onDestroy();
	}

	@Override
	protected void onStart() {
		mapview.onStart();
		super.onStart();
	}

	@Override
	protected void onStop() {
		mapview.onStop();
		super.onStop();
	}
	
	

}
