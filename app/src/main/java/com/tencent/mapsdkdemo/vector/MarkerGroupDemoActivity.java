package com.tencent.mapsdkdemo.vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.BubbleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerGroup;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by didi on 16/9/18.
 */
public class MarkerGroupDemoActivity extends FragmentActivity implements View.OnClickListener {

    private static final LatLng latlng_yinkedasha = new LatLng(39.98192, 116.306364);
    private static final LatLng latlng_disanji = new LatLng(39.984253, 116.307439);
    private static final LatLng latlng_xigema = new LatLng(39.977407, 116.337194);
    private static final LatLng latlng_zhixincun = new LatLng(39.990807, 116.366172);

    private Marker mMarkerXigema;
    private Marker mMarkerYinke;
    private Marker mMarkerDisanji;

    private MarkerGroup markerGroup;

    private boolean mBubbles;

    private boolean mHidden;

    private boolean mInfoWindowEnable = true;

    private TencentMap mMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markergroup_demo);
        setUpMapIfNeeded();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
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
        mMarkerYinke = mMap.addMarker(new MarkerOptions()
                .position(latlng_yinkedasha)
                .title("银科大厦")
                .anchor(0.5f, 0.5f)
                .snippet("地址: 北京市海淀区海淀大街38号")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true).is3D(true));

        mMarkerYinke.setOnClickListener(new TencentMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("zxy", "onMarkerClick(): " + marker.getTitle());
                return true;
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

        // Creates a draggable marker. Long press to drag.
        mMarkerDisanji = mMap.addMarker(new MarkerOptions()
                .position(latlng_disanji)
                .title("第三极大厦")
                .snippet("地址: 北京市北四环西路66号")
                .draggable(true)
                .anchor(0.5f, 0.5f)
        );
//        mMarkerDisanji.setBubbleInfoWindow(true);
////        mMarkerDisanji.setOnTapMapViewBubbleHidden(true);
//        mMarkerYinke.setBubbleInfoWindow(true);
//        mMarkerYinke.setOnTapMapViewBubbleHidden(true);

//        mMarkerDisanji.setBubbleContent("brucecui");

//        mMarkerYinke.setBubbleInfoWindow(false);

        markerGroup = mMap.addMarkerGroup();
        markerGroup.addMarkerById(mMarkerDisanji.getId());
        markerGroup.addMarkerById(mMarkerYinke.getId());

        Log.d("brucecui", "markerGroup id:" + markerGroup.getId());
        List<String> idList = markerGroup.getMarkerIdList();
        for (String id : idList) {
            Log.d("brucecui", "marker id:" + id);
        }

//        boolean result1 = markerGroup.removeMarker(mMarkerYinke);
//        boolean result2 = markerGroup.removeMarker(mMarkerXigema);
//        Log.d("brucecui", "remove result:" + result1);
//        Log.d("brucecui", "remove result:" + result2);
//
//        MarkerOptions newOptions = new MarkerOptions().position(latlng_disanji)
//                .title("志新村18号")
//                .snippet("地址: 北京市北四环中路227号")
//                .draggable(true)
//                .anchor(0.5f, 0.5f);
//        boolean result3 = markerGroup.updateMarkerOptionById(mMarkerDisanji.getId(), newOptions);
//        Log.d("brucecui", "update result:" + result3);
//        markerGroup.setMarkerGroupOnTapMapBubblesHidden(true);
        markerGroup.setMarkerGroupOnTapMapInfoWindowHidden(true);
//        mMap.setOnTapMapViewInfoWindowHidden(true);、
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.show_infowindow:
                if (mMarkerYinke != null) {
                    mMarkerYinke.showInfoWindow();
                }
                if (mMarkerDisanji != null) {
                    mMarkerDisanji.showInfoWindow();
                }
                break;
            case R.id.hide_infowindow:
                if (mMarkerYinke != null) {
                    mMarkerYinke.hideInfoWindow();
                }
                if (mMarkerDisanji != null) {
                    mMarkerDisanji.hideInfoWindow();
                }
                break;
        }
    }

    public void setBubblesInfoWindow(View v) {
        mBubbles = ((CheckBox) v).isChecked();
        if (mMarkerDisanji != null) {
            mMarkerDisanji.setBubbleInfoWindow(mBubbles);
        }
        if (mMarkerYinke != null) {
            mMarkerYinke.setBubbleInfoWindow(mBubbles);
        }
    }

    public void setOnTapMapViewHidden(View v) {
        mHidden = ((CheckBox)v).isChecked();
        if(mBubbles) {
            if(markerGroup!=null) {
                markerGroup.setMarkerGroupOnTapMapBubblesHidden(mHidden);
            }
        } else {
            if(markerGroup != null) {
                markerGroup.setMarkerGroupOnTapMapInfoWindowHidden(mHidden);
            }
        }

    }

    public void setInfoWindowEnable(View v) {
        mInfoWindowEnable = ((CheckBox)v).isChecked();
        if (mMarkerDisanji != null) {
            mMarkerDisanji.setInfoWindowEnable(mInfoWindowEnable);
        }
        if (mMarkerYinke != null) {
            mMarkerYinke.setInfoWindowEnable(mInfoWindowEnable);
        }
    }
}
