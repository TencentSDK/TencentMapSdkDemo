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


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.Projection;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnInfoWindowClickListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMarkerClickListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMarkerDragListener;
import com.tencent.tencentmap.mapsdk.maps.model.Animation;
import com.tencent.tencentmap.mapsdk.maps.model.AnimationListener;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.RotateAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.TranslateAnimation;

/**
 * This shows how to place markers on a map.
 */
public class MarkerDemoActivity extends FragmentActivity
        implements OnMarkerClickListener, OnInfoWindowClickListener, OnMarkerDragListener {
    private static final LatLng latlng_yinkedasha = new LatLng(39.98192,116.306364);
    private static final LatLng latlng_disanji = new LatLng(39.984253,116.307439);
    private static final LatLng latlng_xigema = new LatLng(39.977407,116.337194);
    private static final LatLng latlng_fixed = new LatLng(39.980672,116.329594);

    /** Demonstrates customizing the info window and/or its contents. */
    class CustomInfoWindowAdapter implements TencentMap.MultiPositionInfoWindowAdapter {
        private final RadioGroup mOptions;

        // These a both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
            mOptions = (RadioGroup) findViewById(R.id.custom_info_window_options);
        }

        @Override
        public View[] getInfoWindow(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null;
            }
            render(marker, mWindow);

            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_window) {
                // This means that getInfoContents will be called.
                return null;
            }
            View viewPress = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            renderPress(marker, viewPress);

            return new View[]{mWindow, viewPress};
        }

        @Override
        public View[] getOverturnInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            if (mOptions.getCheckedRadioButtonId() != R.id.custom_info_contents) {
                // This means that the default info contents will be used.
                return null;
            }
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.equals(mMarkerYinke)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mMarkerXigema)) {
                badge = R.drawable.badge_nsw;
            } else if (marker.equals(mMarkerDisanji)) {
                badge = R.drawable.badge_victoria;
            } else {
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);
                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);
                snippetUi.setText(snippetText);
            } else {
                snippetUi.setText("");
            }
        }
        
        private void renderPress(Marker marker, View view) {
            int badge;
            // Use the equals() method on a Marker to check for equals.  Do not use ==.
            if (marker.equals(mMarkerYinke)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mMarkerXigema)) {
                badge = R.drawable.badge_nsw;
            } else if (marker.equals(mMarkerDisanji)) {
                badge = R.drawable.badge_victoria;
            } else {
                badge = 0;
            }
            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            titleUi.setText(title);

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            snippetUi.setText(snippet);
        }
    }

    private TencentMap mMap;

    private Marker mMarkerXigema;
    private Marker mMarkerYinke;
    private Marker mMarkerDisanji;
    private Marker mMarkerFixed;
    private TextView mTopText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_demo);

        mTopText = (TextView) findViewById(R.id.top_text);

        setUpMapIfNeeded();
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

        // Pan to see all markers in view.
        // Cannot zoom to bounds until the map has a size.
        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder()
//                            .include(PERTH)
                            .include(latlng_xigema)
//                            .include(ADELAIDE)
                            .include(latlng_yinkedasha)
                            .include(latlng_disanji)
                            .build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                      mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                      mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            });
        }
    }

    private void addMarkersToMap() {
        // Uses a colored icon.
    	mMarkerYinke = mMap.addMarker(new MarkerOptions()
                .position(latlng_yinkedasha)
                .title("银科大厦")
                .anchor(0.5f, 0.5f)
                .snippet("地址: 北京市海淀区海淀大街38号")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true).is3D(true));
        mMarkerYinke.setOnClickListener(this);
        mMarkerYinke.setOnDragListener(this);
        mMarkerYinke.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMarkerYinke.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("zxy", "onInfoWindowClick(): " + marker.getTitle());
            }

            @Override
            public void onInfoWindowClickLocation(int windowWidth, int windowHigh, int x, int y) {
                Log.i("zxy", "onInfoWindowClickLocation(): " + windowWidth + "," + windowHigh + "," + x + "," + y);
            }
        });

        // Uses a custom icon.
    	mMarkerXigema = mMap.addMarker(new MarkerOptions()
                .position(latlng_xigema)
                .anchor(0.5f, 0.5f)
                .title("希格玛大厦")
                .snippet("地址: 北京市海淀区海淀区知春路49号")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
                );

        mMarkerXigema.setOnInfoWindowClickListener(new TencentMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.e("zxy", "onInfoWindowClick(): " + marker.getTitle());
            }

            @Override
            public void onInfoWindowClickLocation(int windowWidth, int windowHigh, int x, int y) {
                Log.e("zxy", "onInfoWindowClickLocation(): " + windowWidth + "," + windowHigh + "," + x + "," + y);
            }
        });

        mMarkerXigema.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        // Creates a draggable marker. Long press to drag.
    	mMarkerDisanji = mMap.addMarker(new MarkerOptions()
                .position(latlng_disanji)
                .title("第三极大厦")
                .snippet("地址: 北京市北四环西路66号")
                .draggable(true)
                .anchor(0.5f, 0.5f)
                );

        mMarkerDisanji.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        mMarkerFixed = mMap.addMarker(new MarkerOptions()
                .position(latlng_fixed)
                .title("海龙大厦")
                .snippet("地址：北京市海淀区中关村大街1号")
                .draggable(false)
                .anchor(0.5f, 0.5f));

        mMarkerFixed.setFixingPoint(360,540);
        mMarkerFixed.setFixingPointEnable(true);
        // Creates a marker rainbow demonstrating how to create default marker icons of different
        // hues (colors).
        /*int numMarkersInRainbow = 12;
        for (int i = 0; i < numMarkersInRainbow; i++) {
            mMap.addMarker(new MarkerOptions()
            .anchor(0.5f, 0.5f)
                    .position(new LatLng(
                            -30 + 10 * Math.sin(i * Math.PI / (numMarkersInRainbow - 1)),
                            135 - 10 * Math.cos(i * Math.PI / (numMarkersInRainbow - 1))))
                    .title("Marker " + i)
                    .icon(BitmapDescriptorFactory.defaultMarker(i * 360 / numMarkersInRainbow)));
        }*/
    }

    /** Called when the Clear button is clicked. */
    public void onClearMap(View view) {
        mMap.clear();
    }

    /** Called when the Reset button is clicked. */
    public void onResetMap(View view) {
        // Clear the map because we don't want duplicates of the markers.
        mMap.clear();
        addMarkersToMap();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Log.i("zxy", "onMarkerClick(): " + marker.getTitle());
        // This causes the marker at Perth to bounce into position when it is clicked.
        if (marker.equals(mMarkerXigema)) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = mMap.getProjection();
            Point startPoint = proj.toScreenLocation(latlng_xigema);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    double time=((double)elapsed) / duration;
                    float t = interpolator.getInterpolation((float)time);
                    double lng = t * latlng_xigema.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * latlng_xigema.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (time <= 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }else if(marker.equals(mMarkerFixed)) {
            LatLng target = mMap.getProjection().fromScreenLocation(new Point(360, 340));
            TranslateAnimation ta = new TranslateAnimation(target);
            ta.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    return (float)(0.5f - 2 *(0.5 - input) * (0.5 - input));
                }
            });
            ta.setDuration(1500);
            mMarkerFixed.setAnimation(ta);
            mMarkerFixed.startAnimation();
        }

        // We return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    
    Animation animationInfowindow = new RotateAnimation(0, -360, 0, 1, 0);
    @Override
    public void onInfoWindowClick(Marker marker) {
    	animationInfowindow.setDuration(1000);
		animationInfowindow.setInterpolator(new AccelerateDecelerateInterpolator());
		mMap.getInfoWindowAnimationManager().setInfoWindowAnimation(animationInfowindow, infoAnimationlistener);
	    mMap.getInfoWindowAnimationManager().startAnimation();
    }
    
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

    @Override
    public void onMarkerDragStart(Marker marker) {
        mTopText.setText("onMarkerDragStart");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mTopText.setText("onMarkerDragEnd");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        mTopText.setText("onMarkerDrag.  Current Position: " + marker.getPosition());
    }

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		this.mMap.clear();
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onInfoWindowClickLocation(int iWindowWidth, int iWindowHigh,
			int iClickX, int iClickY) {
		// TODO Auto-generated method stub
		
	}
}
