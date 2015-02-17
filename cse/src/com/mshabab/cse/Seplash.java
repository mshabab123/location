package com.mshabab.cse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Seplash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fcreen);
		Thread timer = new Thread() {

			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent mshIntent = new Intent("com.mshabab.cse516.MINUE");
					startActivity(mshIntent);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
