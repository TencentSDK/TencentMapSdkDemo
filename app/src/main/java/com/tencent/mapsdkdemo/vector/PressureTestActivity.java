package com.tencent.mapsdkdemo.vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.tencent.map.lib.MapLanguage;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.CameraPosition;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.Polyline;
import com.tencent.tencentmap.mapsdk.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by xinyuanzhao on 2017/1/18.
 * <p>
 * 用于压力测试的demo
 */
public class PressureTestActivity extends FragmentActivity implements View.OnClickListener {

    private TencentMap mMap;

    /**
     * 压力测试定时器
     */
    private PressureTestTimer mTimer;

    private SetLanguageTester mSetLanguageTester;

    private MapTester mMapTester;

    private ElementTester mElementTester;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TencentMap.setMapConfig(TencentMap.MAP_CONFIG_LIGHT);
        setContentView(R.layout.pressure_test_demo);

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);

        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
    }

    @Override
    public void onDestroy() {
        if (mTimer != null) {
            mTimer.destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
//                pTestSetLanguage();
//                pTestMoveMap();
                pTestAddRemoveElements();
                break;

            case R.id.stop:
                stopTest();
                break;

            default:
        }
    }

    /**
     * 停止当前测试
     */
    private void stopTest() {
        if (mTimer != null) {
            mTimer.destroy();
        }
    }

    /**
     * 对TencentMap.java的setLanguage(...)接口进行压力测试
     */
    private void pTestSetLanguage() {
        if (mTimer == null) {
            mTimer = new PressureTestTimer();
        }

        mTimer.sendEmptyMessage(PressureTestTimer.MSG_ID_TEST_SET_LANGUAGE);
    }

    /**
     * 测试底图操作
     */
    private void pTestMoveMap() {
        if (mTimer == null) {
            mTimer = new PressureTestTimer();
        }

        mTimer.sendEmptyMessageDelayed(PressureTestTimer.MSG_ID_TEST_MAP, 10);
    }

    private void pTestAddRemoveElements() {
        if (mTimer == null) {
            mTimer = new PressureTestTimer();
        }

        mTimer.sendEmptyMessageDelayed(PressureTestTimer.MSG_ID_TEST_ADD_REMOVE_ELEMENTS, 10);
    }

    /**
     * 生成指定区间的随机数
     *
     * @param min 区间起点
     * @param max 区间终点
     * @return 随机数
     */
    private int randomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    /**
     * 压力测试定时器
     */
    private class PressureTestTimer extends Handler {

        /**
         * 对TencetnMap.java的setLanguage(...)接口进行压力测试的消息id
         */
        public static final int MSG_ID_TEST_SET_LANGUAGE = 0;

        /**
         * 操作底图的消息id
         */
        public static final int MSG_ID_TEST_MAP = 1;

        /**
         * 添加、删除底图覆盖物的消息id
         */
        private static final int MSG_ID_TEST_ADD_REMOVE_ELEMENTS = 2;

        /**
         * 两次接口调用的时间间隔（单位：ms）
         */
        private static final int TEST_INTERVAL = 4000;

        private static final int TEST_INTERVAL2 = 100;

        private static final int TEST_INTERVAL3 = 1000;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ID_TEST_SET_LANGUAGE:
                    testSetLanguage();
                    break;

                case MSG_ID_TEST_MAP:
                    testMap();
                    break;

                case MSG_ID_TEST_ADD_REMOVE_ELEMENTS:
                    testAddRemoveElements();
                    break;

                default:
            }
        }

        /**
         * 销毁
         */
        public void destroy() {
            removeMessages(MSG_ID_TEST_SET_LANGUAGE);
            mSetLanguageTester = null;
        }

        /**
         * 测试TencentMap.java的setLanguage(...)接口
         */
        private void testSetLanguage() {
            if (mSetLanguageTester == null) {
                mSetLanguageTester = new SetLanguageTester();
            }

            mSetLanguageTester.setLanguage();
//            mSetLanguageTester.setLanguageAndMoveMap();

            sendEmptyMessageDelayed(MSG_ID_TEST_SET_LANGUAGE, TEST_INTERVAL);
        }

        private void testMap() {
            if (mMapTester == null) {
                mMapTester = new MapTester();
            }

            mMapTester.moveMap();

            sendEmptyMessageDelayed(MSG_ID_TEST_MAP, TEST_INTERVAL2);
        }

        private void testAddRemoveElements() {
            if (mElementTester == null) {
                mElementTester = new ElementTester();
            }

            mElementTester.addRemoveElements();

            sendEmptyMessageDelayed(MSG_ID_TEST_ADD_REMOVE_ELEMENTS, TEST_INTERVAL3);
        }
    }

    /**
     * 测试TencentMap.java setLanguage(...)接口的类
     */
    private class SetLanguageTester {

        /**
         * 当前语言
         */
        private MapLanguage mLanguage = MapLanguage.LAN_CHINESE;

        private int mLatitudeRaw = 39990175;

        private int mLongitudeRaw = 116314214;

        private LatLng mPosition = null;

        /**
         * 调用setLanguage(...)接口
         */
        public void setLanguage() {
            if (mLanguage == MapLanguage.LAN_CHINESE) {
                mLanguage = MapLanguage.LAN_ENGLISH;
            } else {
                mLanguage = MapLanguage.LAN_CHINESE;
            }

            if (mMap != null) {
                mMap.setLanguage(mLanguage);
            }
        }

        /**
         * 调用setLanguage(...)接口并改变移动、旋转、放缩、倾斜地图
         */
        public void setLanguageAndMoveMap() {
            setLanguage();
            moveMap();
        }

        /**
         * 改变底图状态
         */
        private void moveMap() {
            int gap = randomInt(0, 100000);
            int lat = mLatitudeRaw + gap;
            int lon = mLongitudeRaw + gap;

            float scale = randomInt(10, 19);

            float rotate = randomInt(0, 90);

            float skew = randomInt(0, 45);

            if (mPosition == null) {
                mPosition = new LatLng(((double) lat) / 1E6, ((double) lon) / 1E6);
            } else {
                mPosition.latitude = ((double) lat) / 1E6;
                mPosition.longitude = ((double) lon) / 1E6;
            }

            CameraPosition cp = new CameraPosition(mPosition, scale, skew, rotate);

            Log.v("zxy", cp.toString());

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
        }
    }

    /**
     * 底图操作测试类
     */
    private class MapTester {

        private int mLatitudeRaw = 39990175;

        private int mLongitudeRaw = 116314214;

        private LatLng mPosition = null;

        /**
         * 移动地图
         */
        public void moveMap() {
            int gap = randomInt(0, 100000);
            int lat = mLatitudeRaw + gap;
            int lon = mLongitudeRaw + gap;

            if (mPosition == null) {
                mPosition = new LatLng(((double) lat) / 1E6, ((double) lon) / 1E6);
            } else {
                mPosition.latitude = ((double) lat) / 1E6;
                mPosition.longitude = ((double) lon) / 1E6;
            }

            Log.w("zxy", mPosition.toString());

            mMap.animateCamera(CameraUpdateFactory.newLatLng(mPosition));
        }
    }

    private class ElementTester {

        private List<Marker> mMarkers;

        private Polyline mLine;

        private Marker mMarker;

        public void addRemoveElements() {
            if (mMarker == null) {
                mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(39.984253, 116.307439)));
            }

            mMarker.setRotateAngle(new Random().nextInt(360));

//            addElements();
//
//            synchronized (this) {
//                try {
//                    wait(1000);
//                } catch (InterruptedException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            removeElements();
        }

        private void addElements() {
            Log.i("zxy", "addElements()");
            addMarkers();
            addLine();
        }

        private void removeElements() {
            Log.i("zxy", "removeElements()");
            removeMarkers();
            removeLine();
        }

        private void addMarkers() {
            if (mMarkers == null) {
                mMarkers = new ArrayList<Marker>();
            } else {
                removeMarkers();
            }

            int size = 10;

            List<LatLng> points = getLinePoints();
            List<LatLng> positions = new ArrayList<LatLng>(size);

            positions.add(points.get(0));
            positions.add(points.get(points.size() - 1));
            positions.add(points.get(10));
            positions.add(points.get(20));
            positions.add(points.get(30));
            positions.add(points.get(40));
            positions.add(points.get(50));
            positions.add(points.get(60));
            positions.add(points.get(70));
            positions.add(points.get(80));

            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

            for (int i =0; i < size; i++) {
                mMarkers.add(mMap.addMarker(new MarkerOptions().position(positions.get(i)).icon(BitmapDescriptorFactory.fromBitmap(bmp))));
            }
        }

        private void removeMarkers() {
            if (mMarkers == null) {
                return;
            }

            for (Marker marker : mMarkers) {
                marker.remove();
            }

            mMarkers.clear();
        }

        private void addLine() {
            removeLine();

            PolylineOptions opts = new PolylineOptions();
            opts.setLatLngs(getLinePoints());
            mLine = mMap.addPolyline(opts);
        }

        private void removeLine() {
            if (mLine != null) {
                mLine.remove();
            }
        }

        private List<LatLng> getLinePoints() {
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
    }

}
