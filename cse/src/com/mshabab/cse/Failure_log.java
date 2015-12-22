package com.mshabab.cse;

import com.mshabab.cse.R;
import com.mshabab.cse.R.id;
import com.mshabab.cse.R.layout;
import com.mshabab.cse.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Failure_log extends Activity {

	Button butt1;
	Button butt2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.failure_login);
		butt1 = (Button) findViewById(R.id.agin);
		butt2 = (Button) findViewById(R.id.registeragin);

		butt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.mshabab.cse.LOGIN"));
			}
		});
		butt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent("com.mshabab.cse.GEGISTER"));
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 MenuInflater blowUP = getMenuInflater();
		 blowUP.inflate(R.menu.main_menu, menu);
			

		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.menuSweet:
			startActivity(new Intent("com.mshabab.cse.MINUE"));
			break;
		case R.id.menuToss:
			finish();
			break;
			
		}
		return super.onOptionsItemSelected(item);
	}

	

	
}
