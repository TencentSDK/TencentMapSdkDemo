package com.tencent.mapsdkdemo.vector;

import com.tencent.tencentmap.mapsdk.maps.model.LatLng;

public class LatLngUtil {
	/**
	 * 
	 * @param latLng 锚点的经纬度
	 * @param y 偏移的y坐标的值
	 * @param x 偏移的x坐标的值
	 * @param tilt 倾斜度0-PI/4之间
	 * @param bearing 旋转度 0-2PI之间
	 * @param zoom 缩放级别， 0-20之前
	 * @return 返回距离latLng (x,y)像素的经纬度。
	 */
	public static final LatLng getOffset(LatLng latLng, int y, int x, float tilt, float bearing, float zoom) {
		double dl = 20.0 - zoom;
		double scale = Math.pow(2, dl);
		double titlcos = Math.cos(tilt);
		double bearingcos= Math.cos(bearing);
		double bearingsin = Math.sin(bearing);
		double ry = y/titlcos;
		ry = ry * scale;
		double rx = x/titlcos;
		rx = rx * scale;
		double dx = ry * bearingsin;
		double dy = ry * bearingcos;
		dx += rx * bearingcos;
		dy += rx * bearingsin;
		double fLat = latLng.latitude;
		double fLon = latLng.longitude;
		double totalPexils = (Math.pow(2, 20.0) * 256)/2;
		double mx = (double) (fLon * totalPexils / 180);
		double my = (double) (Math.log(Math.tan((90 + fLat) * Math.PI / 360)) / (Math.PI / 180));

		my = (double) (my * totalPexils / 180);
		mx += dx;
		my += dy;
		double flonConver = mx * 180 / totalPexils;
		double flatConver = my * 180 / totalPexils;

		flatConver = 180 / Math.PI * (2 * Math.atan(Math.exp(flatConver * Math.PI / 180)) - Math.PI / 2);
		flatConver = Math.round(flatConver * 1000000)/1000000.0;
		flonConver = Math.round(flonConver * 1000000)/1000000.0;
		return new LatLng(flatConver, flonConver);
	}
}
