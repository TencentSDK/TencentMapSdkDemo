
package com.tencent.mapsdkdemo.vector;


import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnCameraChangeListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapLongClickListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMarkerDragListener;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.Circle;
import com.tencent.tencentmap.mapsdk.maps.model.CircleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * This shows how to draw circles on a map.
 */
public class CircleDemoActivity extends FragmentActivity implements OnSeekBarChangeListener,
        OnMarkerDragListener, OnMapLongClickListener, OnCameraChangeListener {
    private static final LatLng DISANJI = new LatLng(39.984253,116.307439);
    private static final LatLng JICHANG = new LatLng(40.081749,116.597900);
    private static final double DEFAULT_RADIUS = 10000;
    public static final double RADIUS_OF_EARTH_METERS = 6371009;

    private static final int WIDTH_MAX = 50;
    private static final int HUE_MAX = 360;
    private static final int ALPHA_MAX = 255;
    private static final int RADIUS_MAX = 100;
    private static final int FIX_R_PIXEL = 100;

    private TencentMap mMap;

    private List<DraggableCircle> mCircles = new ArrayList<DraggableCircle>(1);

    private SeekBar mColorBar;
    private SeekBar mAlphaBar;
    private SeekBar mWidthBar;
    private SeekBar mRadiusBar;
    private int mStrokeColor;
    private int mFillColor;
    private double mRadiusOrg;
    private double mRadius;
    private Marker mMarkerDisanji;
    
    private Circle fixSizeCircle;

    private class DraggableCircle {
        private final Marker centerMarker;
        private final Marker radiusMarker;
        private final Circle circle;
        private double radius;
        public DraggableCircle(LatLng center, double radius) {
            this.radius = radius;
            mRadius = radius;
            mRadiusOrg = mRadius;
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            centerMarker.setOnDragListener(CircleDemoActivity.this);

            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(toRadiusLatLng(center, radius))
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public DraggableCircle(LatLng center, LatLng radiusLatLng) {
            this.radius = toRadiusMeters(center, radiusLatLng);
            mRadius = radius;
            mRadiusOrg = mRadius;
            centerMarker = mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .draggable(true));
            centerMarker.setOnDragListener(CircleDemoActivity.this);

            radiusMarker = mMap.addMarker(new MarkerOptions()
                    .position(radiusLatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_AZURE)));
            circle = mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(radius)
                    .strokeWidth(mWidthBar.getProgress())
                    .strokeColor(mStrokeColor)
                    .fillColor(mFillColor));
        }
        public boolean onMarkerMoved(Marker marker) {
            if (marker.equals(centerMarker)) {
                circle.setCenter(marker.getPosition());
                radiusMarker.setPosition(toRadiusLatLng(marker.getPosition(), radius));
                return true;
            }
            if (marker.equals(radiusMarker)) {
                 radius = toRadiusMeters(centerMarker.getPosition(), radiusMarker.getPosition());
                 circle.setRadius(radius);
                 return true;
            }
            return false;
        }
        public void onStyleChange() {
            circle.setStrokeWidth(mWidthBar.getProgress());
            circle.setFillColor(mFillColor);
            circle.setStrokeColor(mStrokeColor);
            circle.setRadius(mRadius);
        }
    }

    /** Generate LatLng of radius marker */
    private static LatLng toRadiusLatLng(LatLng center, double radius) {
        double radiusAngle = Math.toDegrees(radius / RADIUS_OF_EARTH_METERS) /
                Math.cos(Math.toRadians(center.latitude));
        return new LatLng(center.latitude, center.longitude + radiusAngle);
    }

    private static double toRadiusMeters(LatLng center, LatLng radius) {
        float[] result = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                radius.latitude, radius.longitude, result);
        return result[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_demo);

        mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
        mColorBar.setMax(HUE_MAX);
        mColorBar.setProgress(0);

        mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        mAlphaBar.setMax(ALPHA_MAX);
        mAlphaBar.setProgress(127);

        mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
        mWidthBar.setMax(WIDTH_MAX);
        mWidthBar.setProgress(1);
        
        mRadiusBar = (SeekBar) findViewById(R.id.radiusSeekBar);
        mRadiusBar.setMax(RADIUS_MAX);
        mRadiusBar.setProgress(10);
        setUpMapIfNeeded();
        
        mMap.addCircle(new CircleOptions()
        .center(DISANJI)
        .radius(1000)
        .strokeWidth(mWidthBar.getProgress())
        .strokeColor(mStrokeColor)
        .fillColor(mFillColor));
        double fixR = metersPerPixel(JICHANG.latitude, mMap.getCameraPosition().zoom);
        fixSizeCircle = mMap.addCircle(new CircleOptions()
        .center(JICHANG).radius(FIX_R_PIXEL * fixR).fillColor(Color.BLUE));
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
        mColorBar.setOnSeekBarChangeListener(this);
        mAlphaBar.setOnSeekBarChangeListener(this);
        mWidthBar.setOnSeekBarChangeListener(this);
        mRadiusBar.setOnSeekBarChangeListener(this);

        mMap.setOnMapLongClickListener(this);
        mMap.setOnCameraChangeListener(this);

        mFillColor = Color.HSVToColor(
                mAlphaBar.getProgress(), new float[] {mColorBar.getProgress(), 1, 1});
        mStrokeColor = Color.BLACK;

        DraggableCircle circle = new DraggableCircle(DISANJI, DEFAULT_RADIUS);
        mCircles.add(circle);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DISANJI, 10.0f));
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
        if (seekBar == mColorBar) {
            mFillColor = Color.HSVToColor(Color.alpha(mFillColor), new float[] {progress, 1, 1});
        } else if (seekBar == mAlphaBar) {
            mFillColor = Color.argb(progress, Color.red(mFillColor), Color.green(mFillColor),
                  Color.blue(mFillColor));
        }else if(seekBar == mRadiusBar) {
        	mRadius = mRadiusOrg * ( 1 + progress/100.0);
        }

        for (DraggableCircle draggableCircle : mCircles) {
            draggableCircle.onStyleChange();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        onMarkerMoved(marker);
    }

    private void onMarkerMoved(Marker marker) {
        for (DraggableCircle draggableCircle : mCircles) {
            if (draggableCircle.onMarkerMoved(marker)) {
                break;
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        // We know the center, let's place the outline at a point 3/4 along the view.
        View view = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getView();
        LatLng radiusLatLng = mMap.getProjection().fromScreenLocation(new Point(
            view.getHeight() * 3 / 4, view.getWidth() * 3 / 4));

        // ok create it
        DraggableCircle circle = new DraggableCircle(point, radiusLatLng);
        mCircles.add(circle);
    }

	@Override
	public void onCameraChange(CameraPosition cameraposition) {
		Log.d("xxxx", "zoom" + cameraposition.zoom);
		double newR = metersPerPixel(JICHANG.latitude, cameraposition.zoom) * FIX_R_PIXEL;
		fixSizeCircle.setRadius(newR);
	}
	
	private static final int EQUATOR_METERS = 40076000; 
	private static final int TILE_BMP_SIZE = 256;
	
	public static double metersPerPixel(double lat, double zoom) {
		double  worldPixels = Math.pow(2.0, zoom) * TILE_BMP_SIZE;
		 // 赤道上第个像素点代表的米数。
        double metersPerPixelOnEquator = EQUATOR_METERS / worldPixels;
        
        // 再算出对应的纬度上的一像素代表多少米就行了。
        double mpp = metersPerPixelOnEquator * Math.cos(lat * Math.PI / 180);
        return mpp;

	}
}
