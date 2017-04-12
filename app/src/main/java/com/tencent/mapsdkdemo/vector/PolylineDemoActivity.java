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
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.AlphaAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.AnimationListener;
import com.tencent.tencentmap.mapsdk.maps.model.EmergeAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds.Builder;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * This shows how to draw polylines on a map.
 */
public class PolylineDemoActivity extends FragmentActivity implements View.OnClickListener {

    private TencentMap mMap = null;

    private Polyline polyLine = null;

    private Marker markerStart = null;

    private Marker markerEnd = null;

    private List<LatLng> listPts = null;

    private List<Polyline> lines = new ArrayList<Polyline>();

    private List<Marker> markers = new ArrayList<Marker>();

    private Polyline bezierCurve = null;

    private Marker markerControl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polyline_demo);
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

        listPts = getArrawPoints();
        if (listPts == null || listPts.size() == 0) {
            return;
        }

        PolylineOptions options = new PolylineOptions();
        options.alpha(1.0f);
        options.setLatLngs(listPts);
        // 设置路线线宽
        options.width(12);
        options.arrow(true);
        options.lineCap(false);
        options.setLineType(PolylineOptions.LineType.LINE_TYPE_MULTICOLORLINE);
//        options.setLineType(PolylineOptions.LineType.LINE_TYPE_DOTTEDLINE);
//        options.setColorTexture("color_point_texture.png", null, 10);

        List<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(39.981787, 116.306649));

        int[] colors = new int[6];
        colors[0] = PolylineOptions.Colors.DARK_BLUE;
        colors[1] = PolylineOptions.Colors.GREEN;
        colors[2] = PolylineOptions.Colors.YELLOW;
        colors[3] = PolylineOptions.Colors.RED;
        colors[4] = PolylineOptions.Colors.LIGHT_BLUE;
        colors[5] = PolylineOptions.Colors.GREY;
        int[] indexs = new int[6];
        for (int i = 0; i < 6; i++) {
            indexs[i] = (int) (listPts.size() / 6.0 * i);
        }
//        polyLine.setColors(colors, indexs);
        options.colors(colors, indexs);
        /*
         * EmergeAnimation a = new EmergeAnimation(new LatLng(39.981787,
         * 116.306649)); a.setDuration(5000); a.setAnimationListener(new
         * AnimationListener() {
         * @Override public void onAnimationStart() { // TODO Auto-generated
         * method stub Log.d("xxxx", "onAnimationStart"); }
         * @Override public void onAnimationEnd() { // TODO Auto-generated
         * method stub Log.d("xxxx", "onAnimationEnd"); LatLng latlngEnd =
         * listPts.get(listPts.size() - 1); markerEnd = mMap.addMarker(new
         * MarkerOptions() .icon(BitmapDescriptorFactory
         * .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
         * .position(latlngEnd).title("终点")); } }); options.animation(a);
         */
        // 这就是设置自己的纹理，我这里用一个随意的文理.
        // options.setColorTexture("color_texture_hello", null, 10);
//        options.lineCap(true);
        polyLine = mMap.addPolyline(options);

        // polyLine.setArrow(true);
        // polyLine.setCustomerColorTexture("color_texture_didi.png", 2);

//        int[][] lineColor = polyLine.getColors();
//        Log.e("brucecui", "colors:" + lineColor[0]);
//        Toast.makeText(PolylineDemoActivity.this, "colors:" + lineColor[0], Toast.LENGTH_SHORT).show();
//        polyLine.addTurnArrow(10, 20);
//        polyLine.setNaviRouteLineErase(true);
//        // polyLine.setArrow(true);
//        LatLng latlngStart = listPts.get(0);
//        markerStart = mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                .position(latlngStart).title("起点"));
//         markerStart.showInfoWindow();


//        // 监听被点击的事件
//        mMap.setOnPolylineClickListener(new LineClickedListener());
//        mMap.setMapScreenCenterProportion(0.5f, 0.5f);
//        mMap.setOnMapClickListener(new OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latlng) {
//                Log.d("xxxx", "onMapClick");
//
//            }
//
//        });
//        List<List<LatLng>> sets = this.getPointsSet();
//        for (int i = 0; i < sets.size(); i++) {
//            PolylineOptions opts = new PolylineOptions();
//            List<LatLng> pts = sets.get(i);
//            opts.alpha(1.0f);
//            opts.setLatLngs(pts);
//            opts.color(i + 1);
//            Polyline l = mMap.addPolyline(opts);
//            lines.add(l);
//            Marker m = null;
//            if (i == 0) {
//                m = mMap.addMarker(new MarkerOptions()
//                        .icon(BitmapDescriptorFactory
//                                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                        .position(pts.get(0)).title("第" + i + "个点"));
//                markers.add(m);
//            }
//            m = mMap.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                    .position(pts.get(pts.size() - 1)).title("第" + (i + 1) + "个点"));
//            markers.add(m);
//        }


//        // bezier曲线
//        PolylineOptions bezierops = new PolylineOptions();
////        bezierops.setLineType(PolylineOptions.LineType.LINE_TYPE_DOTTEDLINE);
////        bezierops.setColorTexture("color_point_texture.png", null, 10);
//        bezierops.color(1);
//        List<LatLng> bezierPoints = new ArrayList<LatLng>();
//        bezierPoints.add(new LatLng(39.989521,116.369125));
//        bezierPoints.add(new LatLng(39.966747,116.349125));
//        bezierops.setLatLngs(bezierPoints);
//
//        //bezier曲线二阶测试
//        List<LatLng> control = new ArrayList<LatLng>();
//        control.add(new LatLng(39.987618,116.378405));
//        control.add(new LatLng(39.977618,116.358405));
//        bezierops.setBezierInfo(3,control, false);

//        //bezier曲线一阶测试
//        bezierops.setBezierInfo(1,null, false);

//        // bezier 曲线三阶测试
//        List<LatLng> control = new ArrayList<LatLng>();
//        control.add(new LatLng(39.987618,116.378405));
//        control.add(new LatLng(39.983007,116.380217));
//        bezierops.setBezierInfo(3,control, false);
//        mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                .position(control.get(0)).title("控制点1"));
//        mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                .position(control.get(1)).title("控制点2"));

//        mMap.addPolyline(bezierops);
//        mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                .position(bezierPoints.get(0)).title("起点"));
//        mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
//                .position(bezierPoints.get(1)).title("终点"));
//        zoomToSpan(bezierPoints);
    }

    private int mBezierBetweeness = 1;
    private Polyline mBezierLine;
    private float mBezierWidth = 1.0f;

    private void addBezierLine() {

        if (mBezierLine != null) {
            mBezierLine.remove();
        }

        List<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.981940, 116.306360));//银科
        listPoints.add(new LatLng(40.081224, 116.598587));//首都机场*/
        //listPoints.add(new LatLng(40.084376,116.414566));//1

        List<LatLng> ctlPoints = new ArrayList<LatLng>();
        ctlPoints.add(new LatLng(40.058629, 116.413193));//东小口
        ctlPoints.add(new LatLng(40.019201, 116.251831));
        ctlPoints.add(new LatLng(40.064672, 116.445808));

        //ctlPoints.add(new LatLng(40.060994,116.362381));//1

        PolylineOptions options = new PolylineOptions();
        options.setLatLngs(listPoints);
        //options.setBezierInfo(2,ctlPoints,false);
        switch (mBezierBetweeness) {
            case 1:
                options.setBezierInfo(mBezierBetweeness, null, false);
                break;
            case 2:
                options.setBezierInfo(mBezierBetweeness, ctlPoints.subList(0, 1), false);
                break;
            case 3:
                options.setBezierInfo(mBezierBetweeness, ctlPoints.subList(0, mBezierBetweeness - 1), false);
                break;
        }

        options.setLineType(PolylineOptions.LineType.LINE_TYPE_MULTICOLORLINE);
        options.width(mBezierWidth);
//        Log.e("brucecui", "bezier order:" + mBezierBetweeness);
        mBezierLine = mMap.addPolyline(options);
//        zoomToSpan(listPoints);
//        mBezierLine.setArrow(true);
    }

    /**
     * 把地图视图缩放到刚好显示路线
     *
     * @param listPts
     */
    private void zoomToSpan(List<LatLng> listPts) {
        if (listPts == null) {
            return;
        }
        int iPtSize = listPts.size();
        if (iPtSize <= 0) {
            return;
        }

        Builder boundbuilder = new LatLngBounds.Builder();
        LatLng latlngPt = null;
        for (int i = 0; i < iPtSize; i++) {
            latlngPt = listPts.get(i);
            if (latlngPt == null) {
                continue;
            }
            boundbuilder.include(latlngPt);
        }
        LatLngBounds bounds = boundbuilder.build();

        mMap.animateCamera(CameraUpdateFactory.newLatLngBoundsRect(bounds, 10, 10, 0, 0));
        // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 110));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.start_alpha_animation == id) {
            AlphaAnimation a = new AlphaAnimation(1.0f, 0.3f);
            a.setDuration(1000);
            a.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart() {
                    // TODO Auto-generated method stub
                    Log.d("xxxx", "onAnimationStart");
                }

                @Override
                public void onAnimationEnd() {
                    // TODO Auto-generated method stub
                    Log.d("xxxx", "onAnimationEnd");
                }

            });
            if (polyLine != null) {
                polyLine.startAnimation(a);
            }
        } else if (R.id.start_emerge_animation == id) {
            EmergeAnimation a = new EmergeAnimation(new LatLng(39.976328, 116.328622));
            a.setDuration(5000);
            a.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart() {
                    // TODO Auto-generated method stub
                    Log.d("xxxx", "onAnimationStart");
                }

                @Override
                public void onAnimationEnd() {
                    // TODO Auto-generated method stub
                    Log.d("xxxx", "onAnimationEnd");
                }

            });
            if (polyLine != null) {
                polyLine.startAnimation(a);
            }
        } else if (R.id.change_bezier == id) {
            mBezierBetweeness++;
            if (mBezierBetweeness > 3) {
                mBezierBetweeness = 1;
            }
            mBezierWidth++;
            if (mBezierWidth > 10) {
                mBezierWidth = 1.0f;
            }
            addBezierLine();
        } else if (R.id.navi_route_erase == id) {
            testLineCap();
        }
    }

    private void testLineCap() {
        polyLine.setLineCap(true);
    }

    /*
     * @Override public boolean onKeyUp(int keyCode, KeyEvent event) { // TODO
     * Auto-generated method stub zoomToSpan(listPts); return
     * super.onKeyUp(keyCode, event); }
     */

    private static final class LineClickedListener implements TencentMap.OnPolylineClickListener {
        @Override
        public void onPolylineClick(Polyline line, LatLng clickedPoint) {
            Log.d("xxxx", "Clicked at polyline" + line.getId() + " at (" + clickedPoint + ")");
            EmergeAnimation a = new EmergeAnimation(clickedPoint);
            a.setDuration(5000);
            if (line != null) {
                line.startAnimation(a);
            }
        }
    }

    private List<LatLng> getPoints() {
        List<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.981787, 116.306649));
        listPoints.add(new LatLng(39.982021, 116.306739));
        listPoints.add(new LatLng(39.982351, 116.306883));
        listPoints.add(new LatLng(39.98233, 116.306047));
        listPoints.add(new LatLng(39.982324, 116.305867));
        listPoints.add(new LatLng(39.981918, 116.305885));
        listPoints.add(new LatLng(39.981298, 116.305921));
        listPoints.add(new LatLng(39.981091, 116.305939));
        listPoints.add(new LatLng(39.980506, 116.305975));
        listPoints.add(new LatLng(39.980148, 116.306002));
        listPoints.add(new LatLng(39.980121, 116.306002));
        listPoints.add(new LatLng(39.979708, 116.306038));
        listPoints.add(new LatLng(39.979205, 116.306074));
        listPoints.add(new LatLng(39.979205, 116.306074));
        listPoints.add(new LatLng(39.978813, 116.306101));
        listPoints.add(new LatLng(39.978015, 116.306182));
        listPoints.add(new LatLng(39.977299, 116.306227));
        listPoints.add(new LatLng(39.976996, 116.306245));
        listPoints.add(new LatLng(39.976913, 116.306245));
        listPoints.add(new LatLng(39.97597, 116.306308));
        listPoints.add(new LatLng(39.97575, 116.306326));
        listPoints.add(new LatLng(39.97564, 116.306335));
        listPoints.add(new LatLng(39.975178, 116.306371));
        listPoints.add(new LatLng(39.975185, 116.306514));
        listPoints.add(new LatLng(39.97564, 116.306272));
        listPoints.add(new LatLng(39.975633, 116.306272));
        listPoints.add(new LatLng(39.975715, 116.307997));
        listPoints.add(new LatLng(39.975819, 116.311545));
        listPoints.add(new LatLng(39.975936, 116.314878));
        listPoints.add(new LatLng(39.975998, 116.317528));
        listPoints.add(new LatLng(39.976025, 116.318785));
        listPoints.add(new LatLng(39.976108, 116.321714));
        listPoints.add(new LatLng(39.976259, 116.326843));
        listPoints.add(new LatLng(39.976328, 116.328622));
        listPoints.add(new LatLng(39.976397, 116.330356));
        listPoints.add(new LatLng(39.9765, 116.333967));
        listPoints.add(new LatLng(39.976459, 116.341019));
        listPoints.add(new LatLng(39.976473, 116.341674));
        listPoints.add(new LatLng(39.976473, 116.341944));
        listPoints.add(new LatLng(39.976473, 116.342546));
        listPoints.add(new LatLng(39.976479, 116.345295));
        listPoints.add(new LatLng(39.976197, 116.353829));
        listPoints.add(new LatLng(39.976459, 116.369926));
        listPoints.add(new LatLng(39.97672, 116.381353));
        listPoints.add(new LatLng(39.976748, 116.382314));
        listPoints.add(new LatLng(39.976851, 116.388045));
        listPoints.add(new LatLng(39.976892, 116.393597));
        listPoints.add(new LatLng(39.976906, 116.394199));
        listPoints.add(new LatLng(39.976906, 116.394298));
        listPoints.add(new LatLng(39.976996, 116.405949));
        listPoints.add(new LatLng(39.977016, 116.407692));
        listPoints.add(new LatLng(39.97701, 116.417564));
        listPoints.add(new LatLng(39.97701, 116.417564));
        listPoints.add(new LatLng(39.977127, 116.417591));
        listPoints.add(new LatLng(39.977127, 116.417582));
        listPoints.add(new LatLng(39.969017, 116.417932));
        listPoints.add(new LatLng(39.968549, 116.417977));
        listPoints.add(new LatLng(39.9666, 116.418094));
        listPoints.add(new LatLng(39.965099, 116.418193));
        listPoints.add(new LatLng(39.963957, 116.418256));
        listPoints.add(new LatLng(39.961533, 116.418301));
        listPoints.add(new LatLng(39.959343, 116.418301));
        listPoints.add(new LatLng(39.95422, 116.418732));
        listPoints.add(new LatLng(39.952375, 116.418858));
        listPoints.add(new LatLng(39.952106, 116.418876));
        listPoints.add(new LatLng(39.95192, 116.418849));
        listPoints.add(new LatLng(39.951693, 116.418696));
        listPoints.add(new LatLng(39.951528, 116.418525));
        listPoints.add(new LatLng(39.951383, 116.41822));
        listPoints.add(new LatLng(39.95128, 116.417941));
        listPoints.add(new LatLng(39.951239, 116.417609));
        listPoints.add(new LatLng(39.951218, 116.417312));
        listPoints.add(new LatLng(39.951218, 116.417088));
        listPoints.add(new LatLng(39.951197, 116.416899));
        listPoints.add(new LatLng(39.951115, 116.416675));
        listPoints.add(new LatLng(39.950984, 116.416513));
        listPoints.add(new LatLng(39.950839, 116.416378));
        listPoints.add(new LatLng(39.950639, 116.41627));
        listPoints.add(new LatLng(39.950426, 116.416217));
        listPoints.add(new LatLng(39.950095, 116.416243));
        listPoints.add(new LatLng(39.948835, 116.416486));
        listPoints.add(new LatLng(39.948697, 116.416486));
        listPoints.add(new LatLng(39.945557, 116.416648));
        listPoints.add(new LatLng(39.941686, 116.416791));
        listPoints.add(new LatLng(39.941005, 116.4168));
        listPoints.add(new LatLng(39.938442, 116.416944));
        listPoints.add(new LatLng(39.936045, 116.417016));
        listPoints.add(new LatLng(39.933662, 116.417142));
        listPoints.add(new LatLng(39.929247, 116.417295));
        listPoints.add(new LatLng(39.927683, 116.417393));
        listPoints.add(new LatLng(39.926553, 116.417438));
        listPoints.add(new LatLng(39.924583, 116.417492));
        listPoints.add(new LatLng(39.924369, 116.417492));
        listPoints.add(new LatLng(39.921779, 116.417573));
        listPoints.add(new LatLng(39.919044, 116.417654));
        listPoints.add(new LatLng(39.917404, 116.417708));
        listPoints.add(new LatLng(39.917287, 116.417717));
        listPoints.add(new LatLng(39.916233, 116.417825));
        listPoints.add(new LatLng(39.913904, 116.417807));
        listPoints.add(new LatLng(39.91254, 116.41786));
        listPoints.add(new LatLng(39.911258, 116.417905));
        listPoints.add(new LatLng(39.910459, 116.417923));
        listPoints.add(new LatLng(39.908557, 116.418049));
        listPoints.add(new LatLng(39.908337, 116.418058));
        listPoints.add(new LatLng(39.90824, 116.418067));
        listPoints.add(new LatLng(39.90669, 116.418148));
        listPoints.add(new LatLng(39.904795, 116.418283));
        listPoints.add(new LatLng(39.903416, 116.418265));
        listPoints.add(new LatLng(39.901218, 116.418408));
        listPoints.add(new LatLng(39.900805, 116.418417));
        listPoints.add(new LatLng(39.900805, 116.418426));
        listPoints.add(new LatLng(39.901335, 116.417968));
        listPoints.add(new LatLng(39.901342, 116.417968));
        listPoints.add(new LatLng(39.901342, 116.418004));
        listPoints.add(new LatLng(39.901197, 116.418193));
        listPoints.add(new LatLng(39.901204, 116.418426));
        listPoints.add(new LatLng(39.901218, 116.418552));
        listPoints.add(new LatLng(39.901087, 116.418624));
        listPoints.add(new LatLng(39.901053, 116.41884));
        listPoints.add(new LatLng(39.901004, 116.419028));
        listPoints.add(new LatLng(39.900922, 116.419388));
        listPoints.add(new LatLng(39.900839, 116.419774));
        listPoints.add(new LatLng(39.900749, 116.420043));
        listPoints.add(new LatLng(39.900722, 116.420178));
        listPoints.add(new LatLng(39.900667, 116.42034));
        listPoints.add(new LatLng(39.900619, 116.420519));
        listPoints.add(new LatLng(39.900557, 116.420744));
        listPoints.add(new LatLng(39.900515, 116.420915));
        listPoints.add(new LatLng(39.900488, 116.421067));
        listPoints.add(new LatLng(39.900467, 116.421274));
        listPoints.add(new LatLng(39.900467, 116.421301));
        listPoints.add(new LatLng(39.900467, 116.421301));
        listPoints.add(new LatLng(39.900674, 116.428856));
        listPoints.add(new LatLng(39.900681, 116.429287));
        listPoints.add(new LatLng(39.900674, 116.429287));
        listPoints.add(new LatLng(39.900694, 116.429745));
        listPoints.add(new LatLng(39.900736, 116.43173));
        listPoints.add(new LatLng(39.900729, 116.433132));
        listPoints.add(new LatLng(39.900729, 116.433267));
        listPoints.add(new LatLng(39.900743, 116.433545));

        return listPoints;
    }

    private List<LatLng> getArrawPoints() {
        List<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.981787, 116.306649));
        listPoints.add(new LatLng(39.982021, 116.306739));
        listPoints.add(new LatLng(39.982351, 116.306883));
        listPoints.add(new LatLng(39.98233, 116.306047));
        listPoints.add(new LatLng(39.982324, 116.305867));
        listPoints.add(new LatLng(39.981918, 116.305885));
        listPoints.add(new LatLng(39.981298, 116.305921));
        listPoints.add(new LatLng(39.981091, 116.305939));
        listPoints.add(new LatLng(39.980506, 116.305975));
        listPoints.add(new LatLng(39.980148, 116.306002));
        listPoints.add(new LatLng(39.980121, 116.306002));
        listPoints.add(new LatLng(39.979708, 116.306038));
        listPoints.add(new LatLng(39.979205, 116.306074));
        listPoints.add(new LatLng(39.979205, 116.306074));
        listPoints.add(new LatLng(39.978813, 116.306101));
        listPoints.add(new LatLng(39.978015, 116.306182));
        listPoints.add(new LatLng(39.977299, 116.306227));
        listPoints.add(new LatLng(39.976996, 116.306245));
        listPoints.add(new LatLng(39.976913, 116.306245));
        listPoints.add(new LatLng(39.97597, 116.306308));
        listPoints.add(new LatLng(39.97575, 116.306326));
        listPoints.add(new LatLng(39.97564, 116.306335));
        listPoints.add(new LatLng(39.975178, 116.306371));
        listPoints.add(new LatLng(39.975185, 116.306514));
        listPoints.add(new LatLng(39.97564, 116.306272));
        listPoints.add(new LatLng(39.975633, 116.306272));
        listPoints.add(new LatLng(39.975715, 116.307997));
        listPoints.add(new LatLng(39.975819, 116.311545));
        listPoints.add(new LatLng(39.975936, 116.314878));
        listPoints.add(new LatLng(39.975998, 116.317528));
        listPoints.add(new LatLng(39.976025, 116.318785));
        listPoints.add(new LatLng(39.976108, 116.321714));
        listPoints.add(new LatLng(39.976259, 116.326843));
        listPoints.add(new LatLng(39.976328, 116.328622));
        listPoints.add(new LatLng(39.976397, 116.330356));
        listPoints.add(new LatLng(39.9765, 116.333967));
        listPoints.add(new LatLng(39.976459, 116.341019));
        listPoints.add(new LatLng(39.976473, 116.341674));
        listPoints.add(new LatLng(39.976473, 116.341944));
        listPoints.add(new LatLng(39.976473, 116.342546));
        listPoints.add(new LatLng(39.976479, 116.345295));
        listPoints.add(new LatLng(39.976197, 116.353829));
        listPoints.add(new LatLng(39.976459, 116.369926));
        listPoints.add(new LatLng(39.97672, 116.381353));
        listPoints.add(new LatLng(39.976748, 116.382314));
        listPoints.add(new LatLng(39.976851, 116.388045));
        listPoints.add(new LatLng(39.976892, 116.393597));
        listPoints.add(new LatLng(39.976906, 116.394199));
        listPoints.add(new LatLng(39.976906, 116.394298));
        listPoints.add(new LatLng(39.976996, 116.405949));
        listPoints.add(new LatLng(39.977016, 116.407692));
        listPoints.add(new LatLng(39.97701, 116.417564));
        listPoints.add(new LatLng(39.97701, 116.417564));
        listPoints.add(new LatLng(39.977127, 116.417591));
        listPoints.add(new LatLng(39.977127, 116.417582));
        listPoints.add(new LatLng(39.969017, 116.417932));
        listPoints.add(new LatLng(39.968549, 116.417977));
        listPoints.add(new LatLng(39.9666, 116.418094));
        listPoints.add(new LatLng(39.965099, 116.418193));
        listPoints.add(new LatLng(39.963957, 116.418256));
        listPoints.add(new LatLng(39.961533, 116.418301));
        listPoints.add(new LatLng(39.959343, 116.418301));
        listPoints.add(new LatLng(39.95422, 116.418732));
        listPoints.add(new LatLng(39.952375, 116.418858));
        listPoints.add(new LatLng(39.952106, 116.418876));
        listPoints.add(new LatLng(39.95192, 116.418849));
        listPoints.add(new LatLng(39.951693, 116.418696));
        listPoints.add(new LatLng(39.951528, 116.418525));
        listPoints.add(new LatLng(39.951383, 116.41822));
        listPoints.add(new LatLng(39.95128, 116.417941));
        listPoints.add(new LatLng(39.951239, 116.417609));
        listPoints.add(new LatLng(39.951218, 116.417312));
        listPoints.add(new LatLng(39.951218, 116.417088));
        listPoints.add(new LatLng(39.951197, 116.416899));
        listPoints.add(new LatLng(39.951115, 116.416675));
        listPoints.add(new LatLng(39.950984, 116.416513));
        listPoints.add(new LatLng(39.950839, 116.416378));
        listPoints.add(new LatLng(39.950639, 116.41627));
        listPoints.add(new LatLng(39.950426, 116.416217));
        listPoints.add(new LatLng(39.950095, 116.416243));
        listPoints.add(new LatLng(39.948835, 116.416486));
        listPoints.add(new LatLng(39.948697, 116.416486));
        listPoints.add(new LatLng(39.945557, 116.416648));
        listPoints.add(new LatLng(39.941686, 116.416791));
        listPoints.add(new LatLng(39.941005, 116.4168));
        listPoints.add(new LatLng(39.938442, 116.416944));
        listPoints.add(new LatLng(39.936045, 116.417016));
        listPoints.add(new LatLng(39.933662, 116.417142));
        listPoints.add(new LatLng(39.929247, 116.417295));
        listPoints.add(new LatLng(39.927683, 116.417393));
        listPoints.add(new LatLng(39.926553, 116.417438));
        listPoints.add(new LatLng(39.924583, 116.417492));
        listPoints.add(new LatLng(39.924369, 116.417492));
        listPoints.add(new LatLng(39.921779, 116.417573));
        listPoints.add(new LatLng(39.919044, 116.417654));
        listPoints.add(new LatLng(39.917404, 116.417708));
        listPoints.add(new LatLng(39.917287, 116.417717));
        listPoints.add(new LatLng(39.916233, 116.417825));
        listPoints.add(new LatLng(39.913904, 116.417807));
        listPoints.add(new LatLng(39.91254, 116.41786));
        listPoints.add(new LatLng(39.911258, 116.417905));
        listPoints.add(new LatLng(39.910459, 116.417923));
        listPoints.add(new LatLng(39.908557, 116.418049));
        listPoints.add(new LatLng(39.908337, 116.418058));
        listPoints.add(new LatLng(39.90824, 116.418067));
        listPoints.add(new LatLng(39.90669, 116.418148));
        listPoints.add(new LatLng(39.904795, 116.418283));
        listPoints.add(new LatLng(39.903416, 116.418265));
        listPoints.add(new LatLng(39.901218, 116.418408));
        listPoints.add(new LatLng(39.900805, 116.418417));
        listPoints.add(new LatLng(39.900805, 116.418426));
        listPoints.add(new LatLng(39.901335, 116.417968));
        listPoints.add(new LatLng(39.901342, 116.417968));
        listPoints.add(new LatLng(39.901342, 116.418004));
        listPoints.add(new LatLng(39.901197, 116.418193));
        listPoints.add(new LatLng(39.901204, 116.418426));
        listPoints.add(new LatLng(39.901218, 116.418552));
        listPoints.add(new LatLng(39.901087, 116.418624));
        listPoints.add(new LatLng(39.901053, 116.41884));
        listPoints.add(new LatLng(39.901004, 116.419028));
        listPoints.add(new LatLng(39.900922, 116.419388));
        listPoints.add(new LatLng(39.900839, 116.419774));
        listPoints.add(new LatLng(39.900749, 116.420043));
        listPoints.add(new LatLng(39.900722, 116.420178));
        listPoints.add(new LatLng(39.900667, 116.42034));
        listPoints.add(new LatLng(39.900619, 116.420519));
        listPoints.add(new LatLng(39.900557, 116.420744));
        listPoints.add(new LatLng(39.900515, 116.420915));
        listPoints.add(new LatLng(39.900488, 116.421067));
        listPoints.add(new LatLng(39.900467, 116.421274));
        listPoints.add(new LatLng(39.900467, 116.421301));
        listPoints.add(new LatLng(39.900467, 116.421301));
        listPoints.add(new LatLng(39.900674, 116.428856));
        listPoints.add(new LatLng(39.900681, 116.429287));
        listPoints.add(new LatLng(39.900674, 116.429287));
        listPoints.add(new LatLng(39.900694, 116.429745));
        listPoints.add(new LatLng(39.900736, 116.43173));
        listPoints.add(new LatLng(39.900729, 116.433132));
        listPoints.add(new LatLng(39.900729, 116.433267));
        listPoints.add(new LatLng(39.900743, 116.433545));
        return listPoints;
    }

    private List<List<LatLng>> getPointsSet() {
        List<List<LatLng>> sets = new ArrayList<List<LatLng>>();
        List<LatLng> list = new ArrayList<LatLng>();
        list.add(new LatLng(39.967648, 116.275177));
        list.add(new LatLng(39.961859, 116.275864));
        list.add(new LatLng(39.931854, 116.274490));
        list.add(new LatLng(39.926062, 116.275864));
        list.add(new LatLng(39.910263, 116.274490));
        sets.add(list);
        list = new ArrayList<LatLng>();
        list.add(new LatLng(39.910263, 116.274490));
        list.add(new LatLng(39.890246, 116.274490));
        list.add(new LatLng(39.874439, 116.279984));
        list.add(new LatLng(39.850194, 116.284103));
        list.add(new LatLng(39.844395, 116.285477));
        list.add(new LatLng(39.833850, 116.288223));
        sets.add(list);

        list = new ArrayList<LatLng>();
        list.add(new LatLng(39.833850, 116.288223));
        list.add(new LatLng(39.830686, 116.310883));
        list.add(new LatLng(39.831214, 116.343155));
        list.add(new LatLng(39.830159, 116.345215));
        list.add(new LatLng(39.831741, 116.358261));
        list.add(new LatLng(39.830686, 116.369934));
        sets.add(list);
        return sets;
    }

    /**
     * 测试插点接口
     */
    private void testInsertPoint() {
        polyLine.insertPoint(20, listPts.get(20));
        Toast.makeText(this, polyLine.getNaviRouteLineVisibleRect().toString(), Toast.LENGTH_SHORT).show();
    }

}
