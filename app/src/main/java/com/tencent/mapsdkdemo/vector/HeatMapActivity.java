package com.tencent.mapsdkdemo.vector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.SupportMapFragment;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.HeatDataNode;
import com.tencent.tencentmap.mapsdk.maps.model.HeatNode;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlay;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlayOptions;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlayOptions.IColorMapper;
import com.tencent.tencentmap.mapsdk.maps.model.HeatOverlayOptions.OnHeatMapReadyListener;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

/**
 * This shows how to create a simple activity with a map and a marker on the
 * map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is
 * not installed/enabled/updated on a user's device.
 */
public class HeatMapActivity extends FragmentActivity {
	/**
	 * Note that this may be null if the Google Play services APK is not
	 * available.
	 */
	private TencentMap mMap;
	private HeatOverlay overlay;
	private IColorMapper colorMapper = new DidiColorMapper();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.heatmap);
		setUpMapIfNeeded();
		Button b = (Button) findViewById(R.id.btn);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				overlay.remove();
				overlay = null;
				Toast.makeText(getApplicationContext(), "热力图移除成功", Toast.LENGTH_SHORT).show();
			}

		});

		Button rm = (Button) findViewById(R.id.btnadd);
		rm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (overlay == null)
					initHeatMap();
				else
					Toast.makeText(getApplicationContext(), "热力图已经添加，如果尚未出现，请等候数据初始化", Toast.LENGTH_SHORT).show();
			}
		});

		Button update = (Button) findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {
			int i = 1;

			@Override
			public void onClick(View v) {
				if(overlay == null) return;
				i++;
				int j = i % 2;
				try {
					String path = j == 0 ? "datac" : "data2k";
					ArrayList<HeatDataNode> nodes = new ArrayList<HeatDataNode>();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(getResources().getAssets()
									.open(path)));
					String line = null;
					while ((line = br.readLine()) != null) {
						String[] lines = line.split("\t");
						if (lines.length == 3) {
							double value = Double.parseDouble(lines[2]);
							LatLng latLng = new LatLng((Double
									.parseDouble(lines[1])), (Double
									.parseDouble(lines[0])));
							nodes.add(new HeatDataNode(latLng, value));
						}
					}
					overlay.updateData(nodes);
					Toast.makeText(getApplicationContext(), "热力图数据更新中。。", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}

	}

	private void initHeatMap() {
		try {
			ArrayList<HeatDataNode> nodes = new ArrayList<HeatDataNode>();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getResources().getAssets().open("data2k")));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] lines = line.split("\t");
				if (lines.length == 3) {
					double value = Double.parseDouble(lines[2]);
					LatLng latLng = new LatLng((Double.parseDouble(lines[1])),
							(Double.parseDouble(lines[0])));
					nodes.add(new HeatDataNode(latLng, value));
				}
			}
			
//			double lat = 39.908926;
//			double lng = 116.397421;
//			double value = 10;
//			Random r = new Random();
//			for(int i = 0; i < 100; i++){
//				double a = r.nextInt(10) * 1E-4;
//				double b = r.nextInt(10) * 1E-4;
//				LatLng latLng = new LatLng(lat + a, lng + b);
//				nodes.add(new HeatDataNode(latLng, value));	
//			}
			
			HeatOverlayOptions heatOverlayOptions = new HeatOverlayOptions();
			heatOverlayOptions.nodes(nodes)
					.onHeatMapReadyListener(new OnHeatMapReadyListener() {

						@Override
						public void onHeatMapReady() {
//							Log.d("heatmap", "heatmap data ready."); 
							HeatMapActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									Toast.makeText(getApplicationContext(), "热力图数据初始化完毕", Toast.LENGTH_SHORT).show();									
								}
							});
						}
					}).radius(100) // 半径，单位是像素，这个数值越大运算量越大，默认值为18，建议设置在18-30之间
					.colorMapper(new DidiColorMapper()).heatTileGenerator(new DidiTileGenerator()); // 使用为滴滴定制的配色方案，滴滴也可以自定义这个方法来实现配色效果哈。如果不设置，默认采用的也是这个配色方案。
//			 heatOverlayOptions.colorMapper(new StandardColorMapper());
			// 标准的配色方案

			overlay = mMap.addHeatOverlay(heatOverlayOptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is where we can add markers or lines, add listeners or move the
	 * camera. In this case, we just add a marker near Africa.
	 * <p>
	 * This should only be called once and when we are sure that {@link #mMap}
	 * is not null.
	 */
	private void setUpMap() {
		mMap.addMarker(new MarkerOptions().position(
				new LatLng(39.961629, 116.355343)).title("Marker"));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.961629, 116.355343), 16));

		initHeatMap();
	}

	public class DidiColorMapper implements IColorMapper {

		@Override
		public int colorForValue(double value) {
			int alpha, red, blue, green;
			if (value > 1)
				value = 1;
			value = Math.sqrt(value);

			float a = 20000;

			red = 255;
			green = 119;
			blue = 3;
			if (value > 0.7) {
				green = 78;
				blue = 1;
			}

			if (value > 0.6) {
				alpha = (int) (a * Math.pow((value - 0.7), 3) + 240);
			} else if (value > 0.4) {
				alpha = (int) (a * Math.pow((value - 0.5), 3) + 200);
			} else if (value > 0.2) {
				alpha = (int) (a * Math.pow((value - 0.3), 3) + 160);
			} else {
				alpha = (int) (700 * value);
			}
			if (alpha > 255) {
				alpha = 255;
			}

			return Color.argb(alpha, red, green, blue);
		}
	}

	public class StandardColorMapper implements IColorMapper {

		// This number should be between 0 and 1
		private static final float kSBAlphaPivotX = 0.3f;// 0.333;

		// This number should be between 0 and MAX_ALPHA
		private static final float kSBAlphaPivotY = 0.5f;

		// This number should be between 0 and 1
		private static final float kSBMaxAlpha = 0.85f;

		@Override
		public int colorForValue(double value) {
			int alpha, red, blue, green;

			if (value > 1)
				value = 1;
			value = Math.sqrt(value);

			if (value < kSBAlphaPivotY) {
				alpha = (int) (255 * value * kSBAlphaPivotY / kSBAlphaPivotX);
			} else {
				alpha = (int) (255 * (kSBAlphaPivotY + ((kSBMaxAlpha - kSBAlphaPivotY) / (1 - kSBAlphaPivotX))
						* (value - kSBAlphaPivotX)));
			}

			// formula converts a number from 0 to 1.0 to an rgb color.
			// uses MATLAB/Octave colorbar code
			if (value <= 0) {
				red = green = blue = alpha = 0;
			} else if (value < 0.125) {
				red = green = 0;
				blue = (int) (255 * 4 * (value + 0.125));
			} else if (value < 0.375) {
				red = 0;
				green = (int) (255 * 4 * (value - 0.125));
				blue = 255;
			} else if (value < 0.625) {
				red = (int) (255 * 4 * (value - 0.375));
				green = 255;
				blue = (int) (255 * (1 - 4 * (value - 0.375)));
			} else if (value < 0.875) {
				red = 255;
				green = (int) (255 * (1 - 4 * (value - 0.625)));
				blue = 0;
			} else {
				red = (int) (255 * Math.max(1 - 4 * (value - 0.875), 0.5f));
				green = blue = 0;
			}
			return Color.argb(alpha, red, green, blue);
		}
	}
	
	public static class DidiTileGenerator implements HeatOverlayOptions.HeatTileGenerator {

		/**
		 * 这里生成一个热点向周围辐射的矩阵。
		 * 这个实现是目前热力图默认的实现。
		 */
		@Override
		public float[] generateFadeOutMatrix(int radius) {
			float[] fadeOutMatrix = new float[2 * radius * 2 * radius];
			for (int i = 0; i < 2 * radius; i++) {
				for (int j = 0; j < 2 * radius; j++) {
					float distance = (float) Math.sqrt((i - radius) * (i - radius) + (j - radius) * (j - radius));
					float scaleFactor = 1 - distance / radius;
					if (scaleFactor < 0) {
						scaleFactor = 0;
					} else {
						scaleFactor = (float) ((Math.exp(-distance / 10.0) - Math.exp(-radius / 10.0)) / Math.exp(0));
					}
					fadeOutMatrix[j * 2 * radius + i] = scaleFactor;
				}
			}
			return fadeOutMatrix;
		}

		/**
		 * 这里是根据一个256x256瓦片内的热点生成一个瓦片的图。
		 * nodes是与这个Tile相关的node，它的点可能不在这个Tile之内，但也会通过半径辐射影响这个Tile.
		 * fadeOutMatrix就是函数generateFadeOutMatrix生成的矩阵。
		 * radius 热力图的辐射半径。
		 * tileSize，这个瓦片的大小，目前为256.
		 * mapper， 热力值对应的颜色的映射,为HeatOverlayOptions.colorMapper设置的值
		 */
		@Override
		public int[] generateHeatTile(List<HeatNode> nodes, float[] fadeOutMatrix, int radius, int tileSize, IColorMapper mapper) {
			int rows = tileSize;
			int columns = tileSize;
			float[] pointValues = new float[tileSize * tileSize]; // 256*;
			int[] colors = new int[tileSize *tileSize]; // 256KB

			if (nodes != null && !nodes.isEmpty()) {
				for (HeatNode node : nodes) {
					double value = node.getValue();
					double x = node.getX();
					double y = node.getY();
					if (value > 0) { // don't bother with 0 or negative values
						// iterate through surrounding pixels and increase
						for (int i = 0; i < 2 * radius; i++) {
							for (int j = 0; j < 2 * radius; j++) {
								// find the array index
								int column = (int) (x - radius + i);
								int row = (int) (y - radius + j);
								// make sure this is a valid array index
								if (row >= 0 && column >= 0 && row < rows
										&& column < columns) {
									int index = columns * row + column;
									pointValues[index] += value
											* fadeOutMatrix[j * 2
													* radius + i];
								}
							}
						}
					}
				}
				for (int i = 0; i < columns * rows; i++) {
					if (pointValues[i] > 0) {
						colors[i] = mapper.colorForValue(pointValues[i]);
					}
				}
			}
			return colors;
		}
		
	}
}
