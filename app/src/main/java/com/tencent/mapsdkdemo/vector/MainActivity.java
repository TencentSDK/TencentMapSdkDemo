package com.tencent.mapsdkdemo.vector;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;

public class MainActivity extends ListActivity {
	private static final String demoPackage = "com.tencent.mapsdkdemo.vector.";

	private String[] demoClassNames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		demoClassNames=this.getResources().getStringArray(R.array.demo_activitys);
		Arrays.sort(demoClassNames);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, demoClassNames));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String strGotName = demoClassNames[position];
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = Class.forName(demoPackage + strGotName);
			Intent intent = new Intent(this, clazz);
			startActivity(intent);
		} catch (Exception e) {
		}
	}
}
