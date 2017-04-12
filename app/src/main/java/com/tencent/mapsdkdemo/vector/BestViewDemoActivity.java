package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdate;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.IMapElement;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * 最佳视野相关接口demo
 */
public class BestViewDemoActivity extends FragmentActivity implements View.OnClickListener {

    private static final int PADDING_TOP = 30;

    private static final int PADDING_BOTTOM = 1000;

    private TencentMap mMap;

    private View mViewTest;

    private Marker mMarkerDisanji;

    private Marker mMarkerYinke;

    private Marker mMarkerXigema;

    private Marker mMarkerTmp;

    private static final LatLng[] LATLNGS = {
            new LatLng(40.04398, 116.28894),
            new LatLng(40.043682, 116.289119),
            new LatLng(40.043633, 116.289051),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.best_view_demo);

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();

        mViewTest = findViewById(R.id.test);
        mViewTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
//                clearMap();

                mMap.setMapPadding(0, PADDING_TOP, 0, PADDING_BOTTOM);

//                testBestViewAPIForPoint();
//                testBestViewAPIForElement();
                testBestViewAPIForElementAndPoint();

                break;
        }
    }

    /**
     * 测试最佳视野接口：以一组点几何中心为底图可视区域中心点, 同时确保所有点都在地图可视区域
     */
    private void testBestViewAPIForPoint() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0, length = LATLNGS.length; i < length; i++) {
            builder.include(LATLNGS[i]);

            addMarker(LATLNGS[i]);
        }

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBoundsRect(bounds, 0, 0, PADDING_TOP, PADDING_BOTTOM);

        mMap.animateCamera(cu);

        mViewTest.postDelayed(new Runnable() {
            @Override
            public void run() {
                addMarkerForCameraPosition();
                addMarkerForFixedPosition(0.5f, 0.1832f);
            }
        }, 1000);
    }

    /**
     * 测试最佳视野接口：以一组元素的几何中心为底图可视区域中心点, 同时确保所有点都在地图可视区域
     */
    private void testBestViewAPIForElement() {
        LatLng latlngDisanji = new LatLng(39.984253,116.307439);
        LatLng latlngYinke = new LatLng(39.98192,116.306364);
        LatLng latlngXigema = new LatLng(39.977407,116.337194);

        if (mMarkerDisanji == null) {
            mMarkerDisanji = addMarker(latlngDisanji);
        }

        if (mMarkerYinke == null) {
            mMarkerYinke = addMarker(latlngYinke);
        }

        if (mMarkerXigema == null) {
            mMarkerXigema = addMarker(latlngXigema);
        }

        ArrayList<IMapElement> elements = new ArrayList<IMapElement>(3);
        elements.add(mMarkerDisanji);
        elements.add(mMarkerYinke);
        elements.add(mMarkerXigema);

        CameraUpdate cu = CameraUpdateFactory.newElementBoundsRect(elements, 0, 0, PADDING_TOP, PADDING_BOTTOM);

        mMap.animateCamera(cu);
    }

    /**
     * 测试最佳视野接口：以一组元素的几何中心为底图可视区域中心点, 同时确保所有点都在地图可视区域
     */
    private void testBestViewAPIForElementAndPoint() {
        LatLng latlngDisanji = new LatLng(39.984253,116.307439);
        LatLng latlngYinke = new LatLng(39.98192,116.306364);
        LatLng latlngXigema = new LatLng(39.977407,116.337194);
        LatLng latlngUnknown = new LatLng(39.980672,116.329594);

        if (mMarkerDisanji == null) {
            mMarkerDisanji = addMarker(latlngDisanji);
        }

        if (mMarkerYinke == null) {
            mMarkerYinke = addMarker(latlngYinke);
        }

        if (mMarkerXigema == null) {
            mMarkerXigema = addGreenMarker(latlngXigema);
        }

        if (mMarkerTmp == null) {
            mMarkerTmp = addGreenMarker(latlngUnknown);
        }

        ArrayList<IMapElement> elements = new ArrayList<IMapElement>(2);
        elements.add(mMarkerDisanji);
//        elements.add(markerYinke);

        ArrayList<LatLng> points = new ArrayList<LatLng>(2);
        points.add(latlngXigema);
        points.add(latlngUnknown);

        CameraPosition cp = mMap.calculateZoomToSpanLevel(elements, points, 0, 0, PADDING_TOP, PADDING_BOTTOM);

        if (cp != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cp.target, cp.zoom));
        } else {
            Toast.makeText(this, "Invalid cp...", Toast.LENGTH_LONG).show();
        }
    }

    private void addMarkerForCameraPosition() {
        // 标识一下当前地图中心点的所在位置
        CameraPosition cp = mMap.getCameraPosition();
        if (cp != null) {
            addGreenMarker(cp.target);
        }
    }

    private void addMarkerForFixedPosition(float x, float y) {
        int width = mMap.getMapView().getWidth();
        int height = mMap.getMapView().getHeight();

        double dWidth = width * x;
        double dHeight = height * y;

        addFixedMarker((int)dWidth, (int)dHeight);
    }

    private Marker addMarker(LatLng position) {
        return mMap.addMarker(new MarkerOptions().position(position)
                .anchor(0.5f, 0.5f)
                .title("hahaha")
                .snippet("aaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
    }

    private Marker addGreenMarker(LatLng position) {
        return mMap.addMarker(new MarkerOptions().position(position)
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
    }

    private Marker addFixedMarker(int x, int y) {
        Marker fixedMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(39.000000, 116.000000))
                .anchor(0.5f, 1.0f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)));

        fixedMarker.setFixingPoint(x, y);
        fixedMarker.setFixingPointEnable(true);

        return fixedMarker;
    }

    private void clearMap() {
        mMap.clear();
    }

}
