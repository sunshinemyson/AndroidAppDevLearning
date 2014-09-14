package com.example.learnandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/*
 import android.view.Menu;
 import android.view.MenuItem;
 */

public class MainActivity extends Activity {

	private boolean mBkgdService = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (!mBkgdService) {
			Log.d("MainActivity","onCreate");
			startService(new Intent(this,
					com.example.learnandroidapp.PositionService.class));

			mBkgdService = true;
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mBkgdService) {
			Log.d("MainActivity","onPause");
			stopService(new Intent(this,
					com.example.learnandroidapp.PositionService.class));

			mBkgdService = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mBkgdService) {
			Log.d("MainActiviy","onResume");
			Intent msg = new Intent(this,
					com.example.learnandroidapp.PositionService.class);

			Bundle b = new Bundle();
			b.putString("Text", "MainActivity::onResume()");
			msg.putExtras(b);

			startService(msg);

			mBkgdService = true;
		}
	}
}
