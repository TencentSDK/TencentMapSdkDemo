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


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.InfoWindowAnimationManager;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnInfoWindowClickListener;
import com.tencent.tencentmap.mapsdk.maps.model.AlphaAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.Animation;
import com.tencent.tencentmap.mapsdk.maps.model.AnimationListener;
import com.tencent.tencentmap.mapsdk.maps.model.AnimationSet;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.RotateAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.ScaleAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.TranslateAnimation;

/**
 * This shows how to place markers on a map.
 */
public class InfoWindowAnimationDemoActivity extends FragmentActivity {
    private static final LatLng latlng_yinkedasha = new LatLng(39.98192,116.306364);
    private static final LatLng latlng_disanji = new LatLng(39.984253,116.307439);
    private static final LatLng latlng_xigema = new LatLng(39.977407,116.337194);

    private TencentMap mMap;

    private Marker mMarkerXigema;
    private Marker mMarkerYinke;
    private Marker mMarkerDisanji;
    private RadioGroup radioAnimate = null;
	private CheckBox checkSingleBubbleHidden = null;
    
    
    Animation animationInfowindow = new RotateAnimation(0, -360, 1, 0, 0);
	AnimationListener infoAnimationlistener = new AnimationListener() {

		@Override
		public void onAnimationStart() {
			// TODO Auto-generated method stub
			Log.e("lhx","infowindow animation start");
		}

		@Override
		public void onAnimationEnd() {
			// TODO Auto-generated method stub
			Log.e("lhx","infowindow animation end");
		}
		
	};
	OnInfoWindowClickListener infoWindowClicklistener = new OnInfoWindowClickListener() {

		@Override
		public void onInfoWindowClick(Marker marker) {
			// TODO Auto-generated method stub
			animationInfowindow.setDuration(1000);
			animationInfowindow.setInterpolator(new AccelerateDecelerateInterpolator());
			mMap.getInfoWindowAnimationManager().setInfoWindowAnimation(animationInfowindow, infoAnimationlistener);
		    mMap.getInfoWindowAnimationManager().startAnimation();
		}

		@Override
		public void onInfoWindowClickLocation(int iWindowWidth,
				int iWindowHigh, int iClickX, int iClickY) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
    Animation animateScaleAppear = new ScaleAnimation(0, 1, 0, 1);
    Animation animateScaleDis = new ScaleAnimation(1, 0, 1, 0);
    Animation aniMove = new TranslateAnimation(null);
    
    Animation animateAlphaDisAppear = new AlphaAnimation(1, 0) ;
    Animation animateAlphaAppear = new AlphaAnimation(0, 1) ;
    
    AnimationSet animateSetDisAppear = new AnimationSet(true) ;
    AnimationSet animateSetAppear = new AnimationSet(true) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infowindow_animation);
        setUpMapIfNeeded();
		checkSingleBubbleHidden = (CheckBox) this.findViewById(R.id.infowindow_ontap_visible);
//		checkSingleBubbleHidden.setOnCheckedChangeListener(new onCreate);
        radioAnimate = (RadioGroup) this.findViewById(R.id.infowindow_animate);
        radioAnimate.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if( mMap == null) {
					return ;
				}
				InfoWindowAnimationManager infoAnimateManager = mMap.getInfoWindowAnimationManager();
				if(infoAnimateManager == null) {
					return ;
				}
				int iCheckedID = radioAnimate.getCheckedRadioButtonId();
				
				if( R.id.infowindow_no_animate == iCheckedID) {
					infoAnimateManager.setInfoWindowDisappearAnimation(null) ;
					infoAnimateManager.setInfoWindowMovingAnimation(null) ;
					infoAnimateManager.setInfoWindowAppearAnimation(null) ;
				}
				else if(R.id.infowindow_scale_animate == iCheckedID) {
					animateScaleDis.setDuration(500) ;
					animateScaleAppear.setDuration(1000) ;
					animateScaleAppear.setInterpolator(new BounceInterpolator()) ;
					
					infoAnimateManager.setInfoWindowDisappearAnimation(animateScaleDis) ;
					infoAnimateManager.setInfoWindowMovingAnimation(null) ;
					infoAnimateManager.setInfoWindowAppearAnimation(animateScaleAppear) ;
				}
				else if( R.id.infowindow_translate_animate == iCheckedID) {
					aniMove.setDuration(800) ;
//					aniMove.setInterpolator(new OvershootInterpolator()) ;
					aniMove.setInterpolator(new AnticipateOvershootInterpolator()) ;
					
					infoAnimateManager.setInfoWindowDisappearAnimation(null) ;
					infoAnimateManager.setInfoWindowMovingAnimation(aniMove) ;
					infoAnimateManager.setInfoWindowAppearAnimation(null) ;
				}
				else if( R.id.infowindow_alpha_animate == iCheckedID) {
					animateAlphaDisAppear.setDuration(500) ;
					animateAlphaAppear.setDuration(500) ;
					
					infoAnimateManager.setInfoWindowDisappearAnimation(animateAlphaDisAppear) ;
					infoAnimateManager.setInfoWindowMovingAnimation(null) ;
					infoAnimateManager.setInfoWindowAppearAnimation(animateAlphaAppear) ;
				}
				else if( R.id.infowindow_animateset_animate == iCheckedID){
					animateSetDisAppear.cleanAnimation();
					animateSetAppear.cleanAnimation();
					
					animateSetDisAppear.addAnimation(animateAlphaDisAppear) ;
					animateSetDisAppear.addAnimation(animateScaleDis) ;
					
					animateSetAppear.addAnimation(animateAlphaAppear) ;
					animateSetAppear.addAnimation(animateScaleAppear) ;
					
					animateSetDisAppear.setDuration(800);
					animateSetAppear.setDuration(800);
					animateSetDisAppear.setInterpolator(new BounceInterpolator());
					animateSetAppear.setInterpolator(new BounceInterpolator());
					
					infoAnimateManager.setInfoWindowDisappearAnimation(animateSetDisAppear) ;
					infoAnimateManager.setInfoWindowMovingAnimation(null) ;
					infoAnimateManager.setInfoWindowAppearAnimation(animateSetAppear) ;
				}
			}});
        radioAnimate.check(R.id.infowindow_no_animate);
	}

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();
    }

    private void addMarkersToMap() {
        // Uses a colored icon.
    	mMarkerYinke = mMap.addMarker(new MarkerOptions()
                .position(latlng_yinkedasha)
                .title("银科大厦")
                .anchor(0.5f, 0.5f)
                .snippet("地址: 北京市海淀区海淀大街38号")
                .draggable(false));

        // Uses a custom icon.
    	mMarkerXigema = mMap.addMarker(new MarkerOptions()
                .position(latlng_xigema)
                .anchor(0.5f, 0.5f)
                .title("希格玛大厦")
                .snippet("地址: 北京市海淀区海淀区知春路49号")
                );

        // Creates a draggable marker. Long press to drag.
    	mMarkerDisanji = mMap.addMarker(new MarkerOptions()
                .position(latlng_disanji)
                .title("第三极大厦")
                .snippet("地址: 北京市北四环西路66号")
                .draggable(false)
                .anchor(0.5f, 0.5f)
                );

    	LatLngBounds bounds = new LatLngBounds.Builder()
        .include(latlng_xigema)
        .include(latlng_yinkedasha)
        .include(latlng_disanji)
        .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

	public void setOnTapMapViewInfoWindowHidden(View v) {
		mMap.setOnTapMapViewInfoWindowHidden(((CheckBox)v).isChecked());
	}
}
