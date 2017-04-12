
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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.Projection;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.MultiPositionInfoWindowAdapter;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnInfoWindowClickListener;
import com.tencent.tencentmap.mapsdk.maps.TencentMap.OnMarkerClickListener;
import com.tencent.tencentmap.mapsdk.maps.model.AlphaAnimation;
import com.tencent.tencentmap.mapsdk.maps.model.Animation;
import com.tencent.tencentmap.mapsdk.maps.model.BitmapDescriptorFactory;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.LatLngBounds;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.maps.model.RotateAnimation;

/**
 * 气泡翻转示例
 */
public class InfoWindowOverturnDemoActivity extends FragmentActivity implements
        OnMarkerClickListener, OnInfoWindowClickListener {

    private static final LatLng LATLNG_YINKE = new LatLng(39.98192, 116.306364);

    private static final LatLng LATLNG_DISANJI = new LatLng(39.984253, 116.307439);

    private static final LatLng LATLNG_XIGEMA = new LatLng(39.977407, 116.337194);
    
    private static final LatLng LATLNG_UNKNOWN = new LatLng(39.98000, 116.30670);

    private TencentMap mMap;

    private Marker mMarkerXigema;

    private Marker mMarkerYinke;

    private Marker mMarkerDisanji;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_window_overturn_demo);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(mMarkerXigema)) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = mMap.getProjection();
            Point startPoint = proj.toScreenLocation(LATLNG_XIGEMA);
            startPoint.offset(0, -100);
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 1500;

            final Interpolator interpolator = new BounceInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    double time = ((double)elapsed) / duration;
                    float t = interpolator.getInterpolation((float)time);
                    double lng = t * LATLNG_XIGEMA.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * LATLNG_XIGEMA.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (time <= 1.0) {
                        handler.postDelayed(this, 16);
                    }
                }
            });
        }

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Animation animationInfowindow = new RotateAnimation(0, -360, 0, 1, 0);
        animationInfowindow.setDuration(1000);
        animationInfowindow.setInterpolator(new AccelerateDecelerateInterpolator());
        mMap.getInfoWindowAnimationManager().setInfoWindowAnimation(animationInfowindow, null);
        mMap.getInfoWindowAnimationManager().startAnimation();
    }

    /**
     * 初始化底图
     */
    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * 配置底图
     */
    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(false);

        addMarkersToMap();
        prepareOverturnAnimations();

        final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {
                    LatLngBounds bounds = new LatLngBounds.Builder().include(LATLNG_YINKE)
                            .include(LATLNG_DISANJI).include(LATLNG_XIGEMA).build();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }
            });
        }
    }

    /**
     * 添加marker
     */
    private void addMarkersToMap() {
        Marker marker = mMap.addMarker(new MarkerOptions().position(LATLNG_YINKE).title("银科大厦").anchor(0.8f, 0.5f)
                .snippet("地址: 北京市海淀区海淀大街38号")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true).is3D(true).displayLevel(2).autoOverturnInfoWindow(true));
        marker.setOnInfoWindowClickListener(this);
        marker.setOnClickListener(this);
        marker.setInfoWindowAdapter(new MPCustomInfoWindowAdapter());

        Marker marker2 = mMap.addMarker(new MarkerOptions().position(LATLNG_DISANJI).title("第三极").anchor(0.5f, 0.5f)
                .snippet("地址: 北京市海淀区北四环西路66号")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true).is3D(true).displayLevel(2).autoOverturnInfoWindow(true));
        marker2.setOnInfoWindowClickListener(this);
        marker2.setOnClickListener(this);
        marker2.setInfoWindowAdapter(new MPCustomInfoWindowAdapter());

//         mMap.addMarker(new
//         MarkerOptions().position(LATLNG_UNKNOWN).title("这是哪里呢")
//         .anchor(0.5f, 0.5f).snippet("地址: 北京市海淀区海淀大街38号")
//         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//         .draggable(true).is3D(true).displayLevel(2).autoOverturnInfoWindow(true));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        this.mMap.clear();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onInfoWindowClickLocation(int iWindowWidth, int iWindowHigh, int iClickX,
            int iClickY) {
        // TODO Auto-generated method stub
    }

    /**
     * 准备气泡翻转动画
     */
    private void prepareOverturnAnimations() {
        Animation animAppear = new AlphaAnimation(0, 1);
        animAppear.setDuration(500);
        animAppear.setInterpolator(new AccelerateDecelerateInterpolator());

        Animation animDisappear = new AlphaAnimation(1, 0);
        animDisappear.setDuration(500);
        animDisappear.setInterpolator(new AccelerateDecelerateInterpolator());

        mMap.getInfoWindowAnimationManager().setInfoWindowAppearAnimation(animAppear);
        mMap.getInfoWindowAnimationManager().setInfoWindowDisappearAnimation(animDisappear);
    }

    /**
     * 自定义的气泡控件适配器
     */
    // private class CustomInfoWindowAdapter implements InfoWindowAdapter {
    //
    // private final View mWindow;
    //
    // private final View mContents;
    //
    // private final View mViewPress;
    //
    // CustomInfoWindowAdapter() {
    // mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
    // mContents = getLayoutInflater().inflate(R.layout.custom_info_contents,
    // null);
    // mViewPress = getLayoutInflater().inflate(R.layout.custom_info_window,
    // null);
    // }
    //
    // @Override
    // public View getInfoWindow(Marker marker) {
    // render(marker, mWindow);
    // return mWindow;
    // }
    //
    // @Override
    // public View getInfoContents(Marker marker) {
    // render(marker, mContents);
    // return mContents;
    // }
    //
    // @Override
    // public View getInfoWindowPressState(Marker marker) {
    // renderPress(marker, mViewPress);
    // return mViewPress;
    // }
    //
    // private void render(Marker marker, View view) {
    // int badge = 0;
    // if (marker.equals(mMarkerYinke)) {
    // badge = R.drawable.badge_qld;
    // } else if (marker.equals(mMarkerXigema)) {
    // badge = R.drawable.badge_nsw;
    // } else if (marker.equals(mMarkerDisanji)) {
    // badge = R.drawable.badge_victoria;
    // } else {
    // badge = 0;
    // }
    // ((ImageView)view.findViewById(R.id.badge)).setImageResource(badge);
    //
    // String title = marker.getTitle();
    // TextView titleUi = ((TextView)view.findViewById(R.id.title));
    // if (title != null) {
    // SpannableString titleText = new SpannableString(title);
    // titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
    // titleText.length(), 0);
    // titleUi.setText(titleText);
    // } else {
    // titleUi.setText("");
    // }
    //
    // String snippet = marker.getSnippet();
    // TextView snippetUi = ((TextView)view.findViewById(R.id.snippet));
    // if (snippet != null && snippet.length() > 12) {
    // SpannableString snippetText = new SpannableString(snippet);
    // snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
    // snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12,
    // snippet.length(), 0);
    // snippetUi.setText(snippetText);
    // } else {
    // snippetUi.setText("");
    // }
    // }
    //
    // private void renderPress(Marker marker, View view) {
    // int badge = 0;
    // if (marker.equals(mMarkerYinke)) {
    // badge = R.drawable.badge_qld;
    // } else if (marker.equals(mMarkerXigema)) {
    // badge = R.drawable.badge_nsw;
    // } else if (marker.equals(mMarkerDisanji)) {
    // badge = R.drawable.badge_victoria;
    // } else {
    // badge = 0;
    // }
    // ((ImageView)view.findViewById(R.id.badge)).setImageResource(badge);
    //
    // String title = marker.getTitle();
    // TextView titleUi = ((TextView)view.findViewById(R.id.title));
    // titleUi.setText(title);
    //
    // String snippet = marker.getSnippet();
    // TextView snippetUi = ((TextView)view.findViewById(R.id.snippet));
    // snippetUi.setText(snippet);
    // }
    //
    // }

    /**
     * 可翻转的自定义气泡控件适配器
     */
    private class MPCustomInfoWindowAdapter implements MultiPositionInfoWindowAdapter {
        private final View mWindow;

        private final View mContents;

        private final View mWindowOverturn;

        private final View mViewPress;

        private final View mViewOverturnPress;

        MPCustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
            mWindowOverturn = getLayoutInflater().inflate(R.layout.custom_info_window_overturn,
                    null);
            mViewPress = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mViewOverturnPress = getLayoutInflater().inflate(R.layout.custom_info_window_overturn,
                    null);
        }

        @Override
        public View[] getInfoWindow(Marker marker) {
            render(marker, mWindow);
            renderPress(marker, mViewPress);
            return new View[] {
                    mWindow, mViewPress
            };
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        @Override
        public View[] getOverturnInfoWindow(Marker marker) {
            render(marker, mWindowOverturn);
            renderPress(marker, mViewOverturnPress);
            return new View[] {
                    mWindowOverturn, mViewOverturnPress
            };
        }

        private void render(Marker marker, View view) {
            int badge = 0;
            if (marker.equals(mMarkerYinke)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mMarkerXigema)) {
                badge = R.drawable.badge_nsw;
            } else if (marker.equals(mMarkerDisanji)) {
                badge = R.drawable.badge_victoria;
            } else {
                badge = 0;
            }
            ((ImageView)view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView)view.findViewById(R.id.title));
            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
                titleUi.setText(titleText);
            } else {
                titleUi.setText("");
            }

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView)view.findViewById(R.id.snippet));
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
            int badge = 0;
            if (marker.equals(mMarkerYinke)) {
                badge = R.drawable.badge_qld;
            } else if (marker.equals(mMarkerXigema)) {
                badge = R.drawable.badge_nsw;
            } else if (marker.equals(mMarkerDisanji)) {
                badge = R.drawable.badge_victoria;
            } else {
                badge = 0;
            }
            ((ImageView)view.findViewById(R.id.badge)).setImageResource(badge);

            String title = marker.getTitle();
            TextView titleUi = ((TextView)view.findViewById(R.id.title));
            titleUi.setText(title);

            String snippet = marker.getSnippet();
            TextView snippetUi = ((TextView)view.findViewById(R.id.snippet));
            snippetUi.setText(snippet);
        }

    }

}
