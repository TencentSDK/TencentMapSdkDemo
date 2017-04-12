
package com.tencent.mapsdkdemo.vector;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMapClickListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnPolylineClickListener;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.BubbleGroup;
import com.tencent.tencentmap.mapsdk.maps.model.BubbleOptions;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds.Builder;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 公交气泡图层接口使用示例
 */
@SuppressLint("NewApi")
public class BubblesOverlayDemoActivity extends FragmentActivity implements View.OnClickListener {

    private TencentMap mMap;

    private Polyline mPolyline;

    private Marker mStartMarker;

    private Marker mEndMarker;

    private ArrayList<Marker> mTransferMarkers;

    private ArrayList<Marker> mViaMarkers;

    private BubbleGroup mBuubleGroup;

    /**
     * 非常驻气泡的id（途经站气泡）
     */
    private int mNonPermanentBubbleId = -1;

    private List<LatLng> mPoints;

    private List<Polyline> lines = new ArrayList<Polyline>();

    private List<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubbles_overlay_demo);

        findViewById(R.id.add_bubble).setOnClickListener(this);
        findViewById(R.id.remove_bubble).setOnClickListener(this);
        findViewById(R.id.add_one_bubble).setOnClickListener(this);
        findViewById(R.id.remove_one_bubble).setOnClickListener(this);

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
            case R.id.add_bubble:
                addBubbles();
                break;

            case R.id.remove_bubble:
                clearBubbles();
                break;
            case R.id.add_one_bubble:
                BubbleOptions newOpts = buildNewBubbleOptionsWithMarker(mStartMarker);
                int id = mBuubleGroup.getBubbleIds().get(0);
                Log.e("brucecui", "id:" + id);
                Log.e("brucecui", "content" + newOpts.getContent());
                mBuubleGroup.updateBubble(id, newOpts);
                break;
            case R.id.remove_one_bubble:
                break;
            default:
        }
    }

    private void setupMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setupMap();
            }
        }
    }

    private void setupMap() {
        mPoints = getArrowPoints();
        if (mPoints == null || mPoints.isEmpty()) {
            return;
        }

        addPolyline();
        addMarkers();
//        addBubbles();

        zoomToSpan(mPoints);

        mMap.setOnPolylineClickListener(new OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline line, LatLng clickPoint) {
                Log.i("zxy", "onPolylineClick(): " + clickPoint.toString());
            }
        });

        mMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng clickPosition) {
                Log.i("zxy", "onMapClick(): " + clickPosition.toString());
            }
        });
    }

    /**
     * 向底图添加线
     */
    private void addPolyline() {
        PolylineOptions options = new PolylineOptions();
        options.setLatLngs(mPoints);

        mPolyline = mMap.addPolyline(options);
        mPolyline.setArrow(true);
    }

    /**
     * 向底图添加一组marker
     */
    private void addMarkers() {
        addStartMarker();
        addEndMarker();
        addTransferMarkers();
        addViaMarkers();
    }

    /**
     * 添加起点marker
     */
    private void addStartMarker() {
        LatLng position = mPoints.get(0);
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .infoWindowEnable(false).position(position).title("起点：北京市海淀区颐和园路66号");
        mStartMarker = mMap.addMarker(options);
    }

    /**
     * 添加终点marker
     */
    private void addEndMarker() {
        LatLng position = mPoints.get(mPoints.size() - 1);
        MarkerOptions options = new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .infoWindowEnable(false).position(position).title("终点：北京市丰台区天坛东路188号").anchor(0.5f, 1);
        mStartMarker = mMap.addMarker(options);
    }

    /**
     * 添加换乘站点marker
     */
    private void addTransferMarkers() {
        int pointSize = mPoints.size();
        int[] transferPositionIndex = new int[] {
                25, 46, 68, 107
        };

        if (mTransferMarkers == null) {
            mTransferMarkers = new ArrayList<Marker>();
        } else {
            mTransferMarkers.clear();
        }

        for (int i = 0, size = transferPositionIndex.length; i < size; i++) {
            int index = transferPositionIndex[i];
            if (index < 0 || index >= pointSize) {
                continue;
            }

            LatLng position = mPoints.get(index);
            MarkerOptions options = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .infoWindowEnable(false).position(position).title("换乘站： ********");

            mTransferMarkers.add(mMap.addMarker(options));
        }
    }

    /**
     * 添加途径站点marker
     */
    private void addViaMarkers() {
        int pointSize = mPoints.size();
        int[] viaPositionIndex = new int[] {
                30, 40, 60, 90
        };

        if (mViaMarkers == null) {
            mViaMarkers = new ArrayList<Marker>();
        } else {
            mViaMarkers.clear();
        }

        for (int i = 0, size = viaPositionIndex.length; i < size; i++) {
            int index = viaPositionIndex[i];
            if (index < 0 || index >= pointSize) {
                continue;
            }

            LatLng position = mPoints.get(index);
            MarkerOptions options = new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .infoWindowEnable(false).position(position).title("途经站：********");

            mViaMarkers.add(mMap.addMarker(options));
        }
    }

    /**
     * 添加一組气泡
     */
    private void addBubbles() {
        if (mMap == null) {
            return;
        }

        mMap.clearBubbles();

        ArrayList<BubbleOptions> optionsList = new ArrayList<BubbleOptions>();

        BubbleOptions options = buildBubbleOptionsWithMarker(mStartMarker);
        if (options != null) {
            options.displayLevel(1);
            options.setOnTapHidden(false);
            optionsList.add(options);
        }

        for (Marker marker : mTransferMarkers) {
            options = buildBubbleOptions(marker);
            if (options != null) {
                options.displayLevel(1);
                options.setOnTapHidden(true);
                optionsList.add(options);
            }
        }

        options = buildBubbleOptionsWithMarker(mEndMarker);
        if (options != null) {
            options.displayLevel(1);
            options.setOnTapHidden(false);
            optionsList.add(options);
        }

//        mMap.addBubbles(optionsList);
        mBuubleGroup = mMap.addBubbleGroup(optionsList);
    }

    /**
     * 添加一个气泡
     * 
     * @param marker 气泡对应的marker
     */
    private void addBubble(Marker marker) {
        BubbleOptions options = buildBubbleOptions(marker);
        if (options == null) {
            mNonPermanentBubbleId = -1;
            return;
        }

        options.displayLevel(5);

        if (mMap != null) {
            if (mNonPermanentBubbleId >= 0) {
                mMap.removeBubble(mNonPermanentBubbleId);
            }

            mNonPermanentBubbleId = mMap.addBubble(options);
        } else {
            mNonPermanentBubbleId = -1;
        }
    }

    /**
     * 移除一个气泡
     * 
     * @param marker
     */
    private void removeBubble(Marker marker) {
        if (mMap != null) {
            mMap.removeBubble(mNonPermanentBubbleId);
        }
    }

    private void clearBubbles() {
//        if (mMap != null) {
//            mMap.clearBubbles();
//        }
        if(mBuubleGroup != null) {
            mBuubleGroup.clearBubbleGroup();
        }
    }

    /**
     * 使用marker的属性构造气泡选项
     * 
     * @param marker 气泡对应的marker
     * @return
     */
    private BubbleOptions buildBubbleOptions(Marker marker) {
        if (marker == null) {
            return null;
        }

        return new BubbleOptions().position(marker.getPosition())
        		.markerSize(marker.getWidth(this), marker.getHeight(this))
                .markerAnchor(marker.getAnchorU(), marker.getAnchorV()).content(marker.getTitle())
                .contentView(getContentView(marker.getTitle()))
                .background(getBackgroundDrawables());
    }
    
    /**
     * 直接使用marker构造气泡选项
     * 
     * @param marker 气泡对应的marker
     * @return
     */
    private BubbleOptions buildBubbleOptionsWithMarker(Marker marker) {
        if (marker == null) {
            return null;
        }

        return new BubbleOptions().marker(marker)
                .contentView(getContentView(marker.getTitle()))
                .background(getBackgroundDrawables());
    }

    private BubbleOptions buildNewBubbleOptionsWithMarker(Marker marker) {
        if (marker == null) {
            return null;
        }

        return new BubbleOptions().marker(marker)
                .contentView(getContentView("brucecui"))
                .background(getBackgroundDrawables());
    }

    private View getContentView(String content) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.custom_bubble_view, null);
        ((TextView)contentView.findViewById(R.id.content)).setText(content);

        return contentView;
    }

    private Drawable[] getBackgroundDrawables() {
        Drawable[] bgs = new Drawable[4];
        bgs[0] = getResources().getDrawable(R.drawable.bubble_station_lt);
        bgs[1] = getResources().getDrawable(R.drawable.bubble_station_rt);
        bgs[2] = getResources().getDrawable(R.drawable.bubble_station_rb);
        bgs[3] = getResources().getDrawable(R.drawable.bubble_station_lb);

        return bgs;
    }

    /**
     * 显示非常驻气泡
     * 
     * @param marker 该气泡对应的marker
     */
    private void showNonPermanentBubble(Marker marker) {
        if (mMap == null) {
            return;
        }

        if (mNonPermanentBubbleId >= 0) {
            mMap.removeBubble(mNonPermanentBubbleId);
        }

        addBubble(marker);
    }

    public boolean isViaStation(Marker marker) {
        if (mViaMarkers == null || mViaMarkers.isEmpty()) {
            return false;
        }

        for (Marker m : mViaMarkers) {
            if (m == null) {
                continue;
            }

            LatLng mp = m.getPosition();
            if (marker.equals(m)) {
                return true;
            }
        }

        return false;
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

    /**
     * 准备点串
     * 
     * @return
     */
    private List<LatLng> getArrowPoints() {
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

}
