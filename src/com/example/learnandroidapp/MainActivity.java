package com.example.learnandroidapp;

import java.util.List;

import junit.framework.Assert;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.location.GpsStatus;
import android.media.AudioManager;

/*
 import android.view.Menu;
 import android.view.MenuItem;
 */

public class MainActivity extends Activity implements GpsStatus.Listener {

	static final String TAG = "MainActiviy";
	private boolean mBkgdService = false;
	// private PositionThread<Void,Integer,Boolean> = null;

	private TextView mPosLatView = null;
	private TextView mPosLonView = null;
	private Button mBtnGetGpsInfo = null;
	private TextView mGpsInfo = null;
	private Button mBtnGetCurMode = null;
	private Button mBtnSwithNextMode = null;
	private Button mBtnTrackMovement = null;
	private LocationManager mLocationManager = null;
	private LocationProvider mLocationProvider = null;

	private OnClickListener mBtnHandler = new OnClickListener() {
		int counter = 0;

		@Override
		public void onClick(View v) {
			if (v == mBtnGetGpsInfo && mGpsInfo != null) {
				mGpsInfo.setText("");
				mGpsInfo.append(String.format("The %d times-->\n", ++counter));
				mLocationManager = (LocationManager) MainActivity.this
						.getSystemService(LOCATION_SERVICE);
				if (null != mLocationManager) {
					List<String> providers = mLocationManager.getAllProviders();
					Assert.assertEquals(true, 0 != providers.size());
					for (int i = 0; i < providers.size(); ++i) {
						Location curLocation = mLocationManager
								.getLastKnownLocation(providers.get(i));

						String detailString = new String(" --- \n");
						if (curLocation != null) {
							detailString = String.format(
									" Accuracy(%f) speed(%f) pos(%f,%f)\n",
									curLocation.getAccuracy(),
									curLocation.getSpeed(),
									curLocation.getLatitude(),
									curLocation.getLongitude());
						}
						mGpsInfo.append(providers.get(i) + detailString);
					}
					mGpsInfo.append("*************\n");
					mGpsInfo.append(String
							.format("total satellites(%d),time to cost to get location(%d)",
									mLocationManager.getGpsStatus(null)
											.getMaxSatellites(),
									mLocationManager.getGpsStatus(null)
											.getTimeToFirstFix()));

					Location curLocation = mLocationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (curLocation != null) {
						mPosLatView.setText(String.format("%f",
								curLocation.getLatitude()));
					}
				}
			}

			if (v == mBtnGetCurMode) {
				AudioManager am = (AudioManager) MainActivity.this
						.getSystemService(AUDIO_SERVICE);
				String msg = new String();

				try {
					switch (am.getRingerMode()) {
					case AudioManager.RINGER_MODE_NORMAL:
						msg = new String("normal mode");
						break;
					case AudioManager.RINGER_MODE_SILENT:
						msg = new String("Slient");
						break;
					case AudioManager.RINGER_MODE_VIBRATE:
						msg = new String("Vibrate");
					default:
						msg = new String("Error");
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}

			if (v == mBtnSwithNextMode) {

			}

			if (v == mBtnTrackMovement) {
				Intent intent = new Intent(
						MainActivity.this,
						com.example.learnandroidapp.TrackNavigationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MainActivity.this.startActivity(intent);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			mPosLatView = (TextView) findViewById(R.id.curPosLat);
			mPosLonView = (TextView) findViewById(R.id.curPosLon);
			mBtnGetGpsInfo = (Button) findViewById(R.id.btnGetGpsInfo);
			mGpsInfo = (TextView) findViewById(R.id.GpsInfo);

			mBtnGetCurMode = (Button) findViewById(R.id.BtnCurMode);
			mBtnSwithNextMode = (Button) findViewById(R.id.BtnSwitchToNextMode);
			mBtnTrackMovement = (Button) findViewById(R.id.btnTrackMovement);
			mBtnGetGpsInfo.setOnClickListener(mBtnHandler);
			mBtnGetCurMode.setOnClickListener(mBtnHandler);
			mBtnSwithNextMode.setOnClickListener(mBtnHandler);
			mBtnTrackMovement.setOnClickListener(mBtnHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			mLocationManager = (LocationManager) this
					.getSystemService(LOCATION_SERVICE);
			Assert.assertEquals(true, mLocationManager != null);
			mLocationManager.addGpsStatusListener(this);
			mLocationProvider = mLocationManager
					.getProvider(LocationManager.GPS_PROVIDER);
			Assert.assertEquals(true, mLocationProvider != null);

		} catch (SecurityException e) {
			Toast.makeText(this, "access not right: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		} catch (NullPointerException e) {
			Toast.makeText(this,
					"unknow execption" + e.getMessage() + e.toString(),
					Toast.LENGTH_LONG).show();
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
			if (gps != null) {
				promtStr = String.format("Gps Status: MaxSatellites=%d",
						gps.getMaxSatellites());
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
