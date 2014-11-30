package com.example.learnandroidapp;

import java.util.List;

import junit.framework.Assert;
import GestureDetectiveSystem.GenericTwoFingerInfoData;
import GestureDetectiveSystem.GestureDetectiveSystem;
import GestureDetectiveSystem.IGestureHandler;
import GestureDetectiveSystem.ThreeFingerGestureData;
import GestureDetectiveSystem.Detectors.GestureCategory;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
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

public class MainActivity extends Activity{

	static final String TAG = "MainActiviy";

	private TextView mGestureInfo = null;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		

		// Gesture support
		Log.d("MainActiviy", "Gesture Detective System");
		//mGDS = new GestureDetectiveSystem(false);
		/*
		 * // Customized system example: // GestureDetectiveSystem
		 * mCustomizedGDS = new GestureDetectiveSystem(true); //
		 * mCustomizedGDS.customize
		 * (GestureCategory.THREE_FINGER_DRAG_GESTURE).		 */
		mGDS = new GestureDetectiveSystem(true);
		mGDS.customize(GestureCategory.THREE_FINGER_DRAG_GESTURE);
		mGesHandler = new GestureHandler();
		if (null != mGDS) {
			// mGDS.registGestureHandler(GestureCategory.two_finger_generic,
			// mGesHandler);
			mGDS.registGestureHandler(GestureCategory.THREE_FINGER_DRAG_GESTURE,
					mGesHandler);
		}
		
		mGestureInfo = (TextView)findViewById(R.id.GestureInfo);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	// Gesture Support
	private GestureDetectiveSystem mGDS = null;
	private GestureHandler mGesHandler = null;

	private class GestureHandler implements IGestureHandler {
		private int trigger = 0;
		@Override
		public void onGestureStarted(Parcelable gestureInfo) {
			trigger = 0;
			Log.e(TAG, "gesture started on callback");
			ThreeFingerGestureData gInfo = (ThreeFingerGestureData) gestureInfo;
			mGestureInfo.setText(new String("onGestureStarted"));

			// Log.v(TAG, gInfo.message);
		}

		@Override
		public void onGestureMoving(Parcelable gestureInfo) {
			trigger++;
			Log.e(TAG, "gesture moving on callback");
			ThreeFingerGestureData gInfo = (ThreeFingerGestureData) gestureInfo;
			mGestureInfo.setText(String.format("onGestureMoving(%s, diff = %d) : %d", gInfo.mDirect == 1 ? "Hor" :"Ver" , gInfo.mDiff, trigger));
			// Log.v(TAG, gInfo.message);
		}

		@Override
		public void onGestureEnd(Parcelable gestureInfo) {
			Log.e(TAG, "gesture ended on callback");
			ThreeFingerGestureData gInfo = (ThreeFingerGestureData) gestureInfo;
			mGestureInfo.setText(new String("onGestureEnd"));
			// Log.v(TAG, gInfo.message);
			trigger = 0;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mGDS.receieveMotionEvent(e);
		return super.onTouchEvent(e);
	}
}
