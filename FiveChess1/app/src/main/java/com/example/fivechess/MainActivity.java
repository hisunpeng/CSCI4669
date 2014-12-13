package com.example.fivechess;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class MainActivity extends Activity {
	GameView gameview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gameview = (GameView) this.findViewById(R.id.gameview);
		gameview.setTextView((TextView) this.findViewById(R.id.text));
	}
}
