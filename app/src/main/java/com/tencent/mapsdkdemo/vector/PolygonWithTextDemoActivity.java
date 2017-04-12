
package com.tencent.mapsdkdemo.vector;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polygon;
import com.tencent.tencentmap.mapsdk.maps.model.PolygonOptions;

import java.util.ArrayList;

/**
 * 带文字标注的多边形示例
 */
public class PolygonWithTextDemoActivity extends FragmentActivity implements OnClickListener {

    private TencentMap mMap;

    private static final LatLng latlng_yinkedasha = new LatLng(39.98192,116.306364);
    private Marker mMarkerYinke;

    private ArrayList<Polygon> mPolygonList = new ArrayList<Polygon>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.polygon_with_text_demo);

        findViewById(R.id.remove).setOnClickListener(this);
        findViewById(R.id.clear).setOnClickListener(this);
        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.addrandom).setOnClickListener(this);

        setupMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMapIfNeeded();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remove:
                removePolygonWithText();
                break;

            case R.id.clear:
                clearPolygonWithText();
                break;
            case R.id.add:
                clearPolygonWithText();
                addAllPolygonWithText();
                break;
            case R.id.addrandom:
                addDIDIpolygon();
                break;
            default:
        }
    }

    private void setupMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setupMap();
            }
        }
    }

    private void setupMap() {
//        addMarker();
//        ArrayList<ArrayList<LatLng>> pointList = getPolygonPoints();
        ArrayList<ArrayList<LatLng>> pointList = getPolygonPointsBeijing1();
        ArrayList<ArrayList<LatLng>> pointList2 = getPolygonPointsBeijing5();
//        ArrayList<ArrayList<LatLng>> pointList = getPolygonPointsGuangzhou();
//        ArrayList<ArrayList<LatLng>> pointList = getPolygonPointsShenzhen();

        int size = pointList.size();
        int size2 = pointList2.size();
        for (int i = 0; i < size; i++) {
            Polygon polygon = mMap.addPolygon(new PolygonOptions()
                    .addAll(pointList.get(i)).fillColor(Color.argb(88,169, 157, 255))
                    .strokeColor(Color.BLACK).strokeWidth(10).text("1.5X").textColor(Color.RED));
            mPolygonList.add(polygon);
        }

        for (int i = 0; i < size2; i++) {
            Polygon polygon = mMap.addPolygon(new PolygonOptions()
                    .addAll(pointList2.get(i)).fillColor(Color.argb(88, 255, 0, 0))
                    .strokeColor( Color.argb(150, 139, 0, 139)).strokeWidth(20).text("2.5X").maxTextSize(200));
            mPolygonList.add(polygon);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(40.027614,116.242218));
        builder.include(new LatLng(40.046540,116.602020));
        builder.include(new LatLng(39.810646,116.132355));
        builder.include(new LatLng(39.799041, 116.584167));

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }
    private void addMarker(){
        mMarkerYinke = mMap.addMarker(new MarkerOptions().position(latlng_yinkedasha));
        mMarkerYinke.setClickable(true);
        mMarkerYinke.setDraggable(true);
        mMarkerYinke.setIcon(BitmapDescriptorFactory.fromAsset("aa.png"));
        mMarkerYinke.setPosition(latlng_yinkedasha);
        mMarkerYinke.setTitle("银科大厦");
        mMarkerYinke.setSnippet("地址: 北京市海淀区海淀大街38号");
        mMarkerYinke.setAnchor(0.5f, 0.5f);
    }
    private  void addAllPolygonWithText(){
        addOnePolygonWithText(getPolygonPointsBeijing1(), Color.argb(120, 255, 0, 0),  Color.argb(150, 139, 0, 139), "3.7X");
        addOnePolygonWithText(getPolygonPointsBeijing2(), Color.argb(120, 124, 252, 0), Color.WHITE, "1.0X");
        addOnePolygonWithText(getPolygonPointsBeijing3(), Color.argb(120, 255, 192, 203), Color.WHITE, "1.5X");
        addOnePolygonWithText(getPolygonPointsBeijing4(), Color.argb(188, 255, 0, 0), Color.WHITE, "2.5");
        addOnePolygonWithText(getPolygonPointsBeijing5(), Color.argb(150, 139, 0, 139), Color.BLACK, "100");
        //addOnePolygonWithText(getPolygonPointsBeijing6(), Color.argb(150, 139, 222, 139), Color.WHITE, "8.5X");
//        addOnePolygonWithText(getPolygonPointsDidi(), Color.argb(150, 139, 0, 139), Color.BLACK, "30");
//        addOnePolygonWithText(getPolygonPointsDidi3D(), Color.argb(150, 139, 0, 139), Color.BLACK, "50");
//        addOnePolygonWithText(getPolygonPointsDidiBuquan(), Color.argb(150, 139, 0, 139), Color.BLACK, "70");
//        addOnePolygonWithText(getPolygonPointsDidiOut(), Color.argb(150, 139, 0, 139), Color.BLACK, "124");
    }

    private void addDIDIpolygon() {
        addOnePolygonWithText(getPolygonPointsDidi(), Color.argb(150, 139, 0, 139), Color.BLACK, "30");
        addOnePolygonWithText(getPolygonPointsDidi3D(), Color.argb(150, 139, 0, 139), Color.BLACK, "50");
        addOnePolygonWithText(getPolygonPointsDidiBuquan(), Color.argb(150, 139, 0, 139), Color.BLACK, "70");
        addOnePolygonWithText(getPolygonPointsDidiOut(), Color.argb(150, 139, 0, 139), Color.BLACK, "124");
    }
    private void removePolygonWithText() {
        if (mPolygonList.isEmpty()) {
            return;
        }

        Polygon polygon = mPolygonList.remove(0);
        if (polygon != null) {
            polygon.remove();
        }
    }

    private  void addOnePolygonWithText(ArrayList<ArrayList<LatLng>> PolygonPoints, int fillColor, int textColor, String text){
        ArrayList<ArrayList<LatLng>> pointList = PolygonPoints;
        int size = pointList.size();
        for (int i = 0; i < size; i++){
            Polygon pt = mMap.addPolygon(new PolygonOptions()
                    .addAll(pointList.get(i)).fillColor(fillColor)
                    .strokeColor(Color.BLACK).strokeWidth(3).text(text).textColor(textColor).maxTextSize(300));
            mPolygonList.add(pt);
        }
    }

    private void clearPolygonWithText() {
        for (Polygon polygon : mPolygonList) {
            polygon.remove();
        }

        mPolygonList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearPolygonWithText();
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPoints() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();

        listPoints = new ArrayList<LatLng>();
        LatLng center = new LatLng(39.981787, 116.306649);
        double halfWidth = 0.05;
        double halfHeight = 0.05;
        listPoints.add(new LatLng(center.latitude - halfHeight, center.longitude + halfWidth));
        listPoints.add(new LatLng(center.latitude + halfHeight, center.longitude + halfWidth));
        listPoints.add(new LatLng(center.latitude + halfHeight, center.longitude - halfWidth));
        listPoints.add(new LatLng(center.latitude - halfHeight, center.longitude - halfWidth));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.046540,116.188660));
        listPoints.add(new LatLng(39.592990,116.018372));
        listPoints.add(new LatLng(39.618384,116.861572));
        listPoints.add(new LatLng(40.241799,116.921997));

        list.add(listPoints);

        return list;
    }
    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing1() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.933953, 116.434982));
        listPoints.add(new LatLng(39.884839, 116.436699));
        listPoints.add(new LatLng(39.884904, 116.532829));
        listPoints.add(new LatLng(39.885234, 116.570767));
        listPoints.add(new LatLng(39.934085, 116.571282));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing2() {

        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
//        listPoints.add(new LatLng(39.970207,116.442021));
//        listPoints.add(new LatLng(39.90308, 116.439961));
//        listPoints.add(new LatLng(39.901499, 116.471203));
//        listPoints.add(new LatLng(39.968628, 116.473950));
        listPoints.add(new LatLng(39.919216,116.588974));
        listPoints.add(new LatLng(39.803789,116.586227));
        listPoints.add(new LatLng(39.839650,116.741409));
        listPoints.add(new LatLng(39.915530,116.737976));
        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.970207, 116.442021));
        listPoints.add(new LatLng(39.968628, 116.473950));
        listPoints.add(new LatLng(39.901499, 116.471203));
        listPoints.add(new LatLng(39.90308, 116.439961));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.012687, 116.467255));
        listPoints.add(new LatLng(39.989149, 116.448544));
        listPoints.add(new LatLng(39.972443, 116.47086));
        listPoints.add(new LatLng(39.998223, 116.49781));
        listPoints.add(new LatLng(40.011050,116.506233));
        listPoints.add(new LatLng(40.042335,116.473618));
        list.add(listPoints);


        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.125866,116.575928));
        listPoints.add(new LatLng(40.081224,116.598587));
        listPoints.add(new LatLng(40.042335,116.541595));
        listPoints.add(new LatLng(39.983960,116.653519));
        listPoints.add(new LatLng(40.122191,116.790848));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.849667,116.227112));
        listPoints.add(new LatLng(39.675484,116.221619));
        listPoints.add(new LatLng(39.672313,116.578674));
        listPoints.add(new LatLng(39.734650,116.641846));
        listPoints.add(new LatLng(39.738874,116.320496));
        listPoints.add(new LatLng(39.816975,116.309509));

        list.add(listPoints);


        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.724089,116.364441));
        listPoints.add(new LatLng(39.667028,116.119995));
        listPoints.add(new LatLng(39.465885,116.100769));
        listPoints.add(new LatLng(39.357662,116.375427));
        listPoints.add(new LatLng(39.542176,116.592407));
        listPoints.add(new LatLng(39.628961,116.540222));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(33.031332,110.062500));
        listPoints.add(new LatLng(33.031332,111.062500));
        listPoints.add(new LatLng(32.031332,111.062500));
        listPoints.add(new LatLng(32.031332,112.062500));
        listPoints.add(new LatLng(33.031332,112.062500));
        listPoints.add(new LatLng(33.031332,113.062500));
        listPoints.add(new LatLng(31.031332,113.062500));
        listPoints.add(new LatLng(31.031332,110.062500));
        list.add(listPoints);
        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing3() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
//        listPoints.add(new LatLng(39.979225,116.306076));
//        listPoints.add(new LatLng(39.931327,116.264877));
//        listPoints.add(new LatLng(39.918689,116.296463));
//        listPoints.add(new LatLng(39.824886,116.389160));
//        listPoints.add(new LatLng(39.945016,116.393280));

        //区域过小的点
        listPoints.add(new LatLng(39.984926,116.306478));
        listPoints.add(new LatLng(39.984194,116.306661));
        listPoints.add(new LatLng(39.984137,116.308120));
        listPoints.add(new LatLng(39.984963,116.308190));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing4() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.910000,116.349678));
        listPoints.add(new LatLng(39.864162,116.353111));
        listPoints.add(new LatLng(39.848085,116.384697));
        listPoints.add(new LatLng(39.855465,116.436195));
        listPoints.add(new LatLng(39.872858,116.425209));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing5() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.884904, 116.532829));
        listPoints.add(new LatLng(39.841495,116.410446));
        listPoints.add(new LatLng(39.802734,116.535072));
        listPoints.add(new LatLng(39.885234, 116.570767));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.014468,116.270370));
        listPoints.add(new LatLng(39.953175,116.294403));
        listPoints.add(new LatLng(39.951069,116.332169));
        listPoints.add(new LatLng(39.957912,116.376457));
        listPoints.add(new LatLng(40.033398,116.296463));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsBeijing6() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.9167824294,116.435412679));
        listPoints.add(new LatLng(39.9167788339,116.435767413));
        listPoints.add(new LatLng(39.9164578837,116.435735205));
        listPoints.add(new LatLng(39.9164821051,116.435418092));

        list.add(listPoints);

        return list;
    }
    private ArrayList<ArrayList<LatLng>> getPolygonPointsGuangzhou() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(23.422428, 113.213346));
        listPoints.add(new LatLng(23.374535, 113.190858));
        listPoints.add(new LatLng(23.360825, 113.228624));
        listPoints.add(new LatLng(23.410299, 113.272741));
        listPoints.add(new LatLng(23.410299, 113.272741));
        listPoints.add(new LatLng(23.410299, 113.272912));
        listPoints.add(new LatLng(23.410299, 113.273256));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(23.217026, 113.243558));
        listPoints.add(new LatLng(23.113811, 113.224332));
        listPoints.add(new LatLng(23.054434, 113.280637));
        listPoints.add(new LatLng(23.077493, 113.394277));
        listPoints.add(new LatLng(23.10118, 113.460881));
        listPoints.add(new LatLng(23.202827, 113.371961));
        listPoints.add(new LatLng(23.202827, 113.371961));
        listPoints.add(new LatLng(23.202827, 113.371961));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(22.96129, 113.352821));
        listPoints.add(new LatLng(22.919397, 113.34604));
        listPoints.add(new LatLng(22.923903, 113.393504));
        listPoints.add(new LatLng(22.956785, 113.385694));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsShenzhen() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(22.62086, 114.016767));
        listPoints.add(new LatLng(22.600576, 114.025351));
        listPoints.add(new LatLng(22.610402, 114.04698));
        listPoints.add(new LatLng(22.63005, 114.0408));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(22.545098, 113.912054));
        listPoints.add(new LatLng(22.524168, 113.912741));
        listPoints.add(new LatLng(22.502919, 113.919607));
        listPoints.add(new LatLng(22.496575, 113.951879));
        listPoints.add(new LatLng(22.538439, 113.962866));
        listPoints.add(new LatLng(22.558097, 113.951193));
        listPoints.add(new LatLng(22.545415, 113.913084));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(22.540024, 114.017111));
        listPoints.add(new LatLng(22.524168, 114.017111));
        listPoints.add(new LatLng(22.505139, 114.062086));
        listPoints.add(new LatLng(22.521314, 114.073759));
        listPoints.add(new LatLng(22.531462, 114.074789));
        listPoints.add(new LatLng(22.526705, 114.088522));
        listPoints.add(new LatLng(22.535585, 114.130064));
        listPoints.add(new LatLng(22.550805, 114.138304));
        listPoints.add(new LatLng(22.576802, 114.132124));
        listPoints.add(new LatLng(22.551756, 113.999258));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsDidi() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.058097, 116.276711));
        listPoints.add(new LatLng(40.05941, 116.338337));
        listPoints.add(new LatLng(40.029448, 116.330956));
        listPoints.add(new LatLng(40.028922, 116.2901));
        listPoints.add(new LatLng(40.028922, 116.2901));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsDidiOut() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.021260, 116.328543));
        listPoints.add(new LatLng(39.924590, 116.212087));
        listPoints.add(new LatLng(39.897078, 116.310395));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsDidi3D() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.042045, 116.295645));
        listPoints.add(new LatLng(40.045002, 116.294684));
        listPoints.add(new LatLng(40.043620, 116.289740));
        listPoints.add(new LatLng(40.040617, 116.287106));

        list.add(listPoints);

        listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(39.994039, 116.402507));
        listPoints.add(new LatLng(39.503765, 116.691502));
        listPoints.add(new LatLng(39.772278, 117.006755));
        listPoints.add(new LatLng(39.446607, 116.287865));

        list.add(listPoints);

        return list;
    }

    private ArrayList<ArrayList<LatLng>> getPolygonPointsDidiBuquan() {
        ArrayList<ArrayList<LatLng>> list = new ArrayList<ArrayList<LatLng>>();

        ArrayList<LatLng> listPoints = new ArrayList<LatLng>();
        listPoints.add(new LatLng(40.051337, 116.251059));
        listPoints.add(new LatLng(40.027877, 116.167030));
        listPoints.add(new LatLng(39.978699, 116.205482));

        list.add(listPoints);

        return list;
    }

}


