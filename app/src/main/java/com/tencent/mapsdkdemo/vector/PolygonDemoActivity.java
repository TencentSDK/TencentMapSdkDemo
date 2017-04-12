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

import java.util.Arrays;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;

/**
 * This shows how to draw polygons on a map.
 */
public class PolygonDemoActivity extends FragmentActivity implements OnSeekBarChangeListener {
    private static final LatLng mLatlng = new LatLng(39.984186, 116.307503);

    private static final int WIDTH_MAX = 50;
    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 255;

    private TencentMap mMap;

    private Polygon mMutablePolygon;

    private SeekBar mColorBar;
    private SeekBar mAlphaBar;
    private SeekBar mWidthBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polygon_demo);

        mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
        mColorBar.setMax(HUE_MAX);
        mColorBar.setProgress(0);

        mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        mAlphaBar.setMax(ALPHA_MAX);
        mAlphaBar.setProgress(127);

        mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
        mWidthBar.setMax(WIDTH_MAX);
        mWidthBar.setProgress(10);

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
        // Create a rectangle with two rectangular holes.
        mMap.addPolygon(new PolygonOptions()
                .addAll(createCWRectangle(new LatLng(39.984186, 116.307503), 5, 5))
                /*.addHole(createCWRectangle(new LatLng(-22, 128), 1, 1))
                .addHole(createCWRectangle(new LatLng(-18, 133), 0.5, 1.5))*/
                .fillColor(Color.CYAN)
                .zIndex(0)
                .strokeColor(Color.BLUE)
                .strokeWidth(5));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Create an ellipse centered at Sydney.
        PolygonOptions options = new PolygonOptions();
        int numPoints = 400;
        float semiHorizontalAxis = 10f;
        float semiVerticalAxis = 5f;
        double phase = 2 * Math.PI / numPoints;
        for (int i = 0; i <= numPoints; i++) 
        {
        	LatLng ll = new LatLng(mLatlng.latitude + semiVerticalAxis * Math.sin(i * phase),
            		mLatlng.longitude + semiHorizontalAxis * Math.cos(i * phase));
            options.add(ll);
            builder.include(ll);
        }

        int fillColor = Color.HSVToColor(
                mAlphaBar.getProgress(), new float[] {mColorBar.getProgress(), 1, 1});
        mMutablePolygon = mMap.addPolygon(options
                .strokeWidth(mWidthBar.getProgress())
                .strokeColor(Color.BLACK)
                .zIndex(1)
                .fillColor(fillColor));

        mColorBar.setOnSeekBarChangeListener(this);
        mAlphaBar.setOnSeekBarChangeListener(this);
        mWidthBar.setOnSeekBarChangeListener(this);

        // Move the map so that it is centered on the mutable polygon.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatlng));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
    }

    /**
     * Creates a List of LatLngs that form a rectangle with the given dimensions.
     */
    private List<LatLng> createRectangle(LatLng center, double halfWidth, double halfHeight) {
        // Note that the ordering of the points is counterclockwise (as long as the halfWidth and
        // halfHeight are less than 90).
        return Arrays.asList(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude + halfWidth),
                new LatLng(center.latitude + halfHeight, center.longitude - halfWidth),
                new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));
    }
    private List<LatLng> createCWRectangle(LatLng center, double halfWidth, double halfHeight) {
      List<LatLng> rect = createRectangle(center, halfWidth, halfHeight);
//      Collections.reverse(rect);
      return rect;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mMutablePolygon == null) {
            return;
        }
        
        if (seekBar == mColorBar) {
            mMutablePolygon.setFillColor(Color.HSVToColor(
                    Color.alpha(mMutablePolygon.getFillColor()), new float[] {progress, 1, 1}));
        } else if (seekBar == mAlphaBar) {
            int prevColor = mMutablePolygon.getFillColor();
            mMutablePolygon.setFillColor(Color.argb(
                    progress, Color.red(prevColor), Color.green(prevColor),
                    Color.blue(prevColor)));
        } else if (seekBar == mWidthBar) {
            mMutablePolygon.setStrokeWidth(progress);
        }
    }
}
