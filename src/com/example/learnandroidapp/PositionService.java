package com.example.learnandroidapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PositionService extends Service {
	private Thread mBkgdThread = null;

	public PositionService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {

		final String msg = intent.getStringExtra("Test");
		Toast.makeText(this, "Position Service: Show->" + msg,
				Toast.LENGTH_LONG).show();

		Log.d("MainActivity", "Position Service->create Background Thread");
		mBkgdThread = new Thread() {
			@Override
			public void run() {
				while (true) {
					// sleep(5000);
					Log.d("MainActivity", "Background Thread's Main()");
				}
			}
		};

		mBkgdThread.start();

		return super.onStartCommand(intent, flags, startid);
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Position Service Created", Toast.LENGTH_LONG)
				.show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Position Service Exit", Toast.LENGTH_LONG).show();
	}

}
