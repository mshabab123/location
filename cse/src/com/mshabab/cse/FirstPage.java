package com.mshabab.cse;

import com.mshabab.cse.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FirstPage extends Activity {

	Button butt1;
	Button butt2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firtpage);
		butt1 = (Button) findViewById(R.id.butt1);
		butt2 = (Button) findViewById(R.id.butt2);

		boolean online_state = isOnline();
		if (!(online_state)) {
			DialogLogInF();
		}

		butt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
/*******************  check the internet connection ********************/
				boolean online_state = isOnline();
				if (!(online_state)) {
				DialogLogInF();
			} else {
					
/*******************  ************************* ********************/
		
					
					startActivity(new Intent("com.mshabab.cse.LOGIN"));

				}
			}
		});
		butt2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean online_state = isOnline();
				if (!(online_state)) {
					DialogLogInF();
				} else {
					Intent i = new Intent(FirstPage.this, Welcome.class);
					startActivity(i);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater blowUP = getMenuInflater();
		blowUP.inflate(R.menu.main_menu, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menuSweet:
			startActivity(new Intent("com.mshabab.cse.MINUE"));
			break;
		case R.id.menuToss:
			finish();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void DialogLogInF() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Pleas try agin!")
				.setCancelable(false)
				.setPositiveButton("Connction Sitting",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
								startActivity(new Intent(
										Settings.ACTION_WIRELESS_SETTINGS));
								finish();
							}
						})
				.setNegativeButton("Try agian",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								boolean online_state = isOnline();
								if (!(online_state)) {
									DialogLogInF();
								}
								dialog.cancel();
								return;
							}
						});

		// Creating dialog box
		AlertDialog alert = builder.create();
		// Setting the title manually
		alert.setTitle("There is no Internet connection ");
		alert.show();
	}

}
