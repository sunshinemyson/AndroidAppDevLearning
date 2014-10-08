package com.example.learnandroidapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrackLineView extends LinearLayout {
	private TextView mGpsStatus;
	private View mTrackLine;

	public TrackLineView(Context context) {
		super(context);
	}

	public TrackLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
}
