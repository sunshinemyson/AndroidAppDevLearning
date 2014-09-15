package com.example.learnandroidapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.location.GpsStatus;

/*
 import android.view.Menu;
 import android.view.MenuItem;
 */

public class MainActivity extends Activity
	implements GpsStatus.Listener{

	private boolean mBkgdService = false;
	//private PositionThread<Void,Integer,Boolean> = null;

	private TextView mPosLatView = null;
	private TextView mPosLonView = null;
	private LocationManager mLocationManager = null;
	private LocationProvider mLocationProvider = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPosLatView = (TextView) findViewById(R.id.curPosLat);
		mPosLonView = (TextView) findViewById(R.id.curPosLon);
		try{
		mLocationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		mLocationManager.addGpsStatusListener(this);
		mLocationProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
		Location curLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		mPosLatView.setText(String.format("%3.5f", curLocation.getLatitude()));
		mPosLonView.setText(String.format("%3.5f", curLocation.getLongitude()));
		} catch (SecurityException e){
			Toast.makeText(this, "access not right", Toast.LENGTH_LONG).show();
		}
	

		if (!mBkgdService) {
			Log.d("MainActivity", "onCreate");
			startService(new Intent(this,
					com.example.learnandroidapp.PositionService.class));

			mBkgdService = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mBkgdService) {
			Log.d("MainActivity", "onPause");
			stopService(new Intent(this,
					com.example.learnandroidapp.PositionService.class));

			mBkgdService = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mBkgdService) {
			Log.d("MainActiviy", "onResume");
			Intent msg = new Intent(this,
					com.example.learnandroidapp.PositionService.class);

			Bundle b = new Bundle();
			b.putString("Text", "MainActivity::onResume()");
			msg.putExtras(b);

			startService(msg);

			mBkgdService = true;
		}
	}

	@Override
	public void onGpsStatusChanged(int event) {
		String promtStr = null;
		switch (event) {
		case GpsStatus.GPS_EVENT_STARTED:
			promtStr = new String("GPS Started");
			break;
		case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
			GpsStatus gps = mLocationManager.getGpsStatus(null);
			if(gps != null ){
				promtStr = String.format("Gps Status: MaxSatellites=%d", gps.getMaxSatellites());
			}
			break;
		case GpsStatus.GPS_EVENT_STOPPED:
			promtStr = new String("GPS GPS_EVENT_STOPPED");
			break;
		case GpsStatus.GPS_EVENT_FIRST_FIX:
			promtStr = new String("GPS GPS_EVENT_FIRST_FIX");			
			break;
		default:
			promtStr = new String("Nothing happen");
			break;
		}
		Toast.makeText(this, promtStr, Toast.LENGTH_LONG).show();
	}
}
