package com.mshabab.cse;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Welcome extends Activity {
	String useranemAll1;
	TextView usernameall2, textView1;
	public static double latitude1, longitude1;
	private LocationManager lm;
	private String provider, locations2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcom);

		useranemAll1 = Login.usernameAll;
		usernameall2 = (TextView) findViewById(R.id.usernameall2);

		usernameall2.setText(useranemAll1);

		Thread timer = new Thread() {

			public void run() {
				try {

					sleep(5000);

					lm = (LocationManager) getSystemService(LOCATION_SERVICE);
					provider = lm.getBestProvider(new Criteria(), true);
					if (provider == null) {
						onProviderDisabled(provider);
					}
					Location loc = lm.getLastKnownLocation(provider);

					/****** to set your location *****/

					double locatins1 = loc.getLongitude();
					locations2 = String.valueOf(locatins1);
					if (loc != null) {
						onLocationChanged(loc);
					}

					Log.i(locations2, "User long Clicked");

				} catch (InterruptedException e) {
					e.printStackTrace();

				} finally {

					Intent mshIntent = new Intent("com.mshabab.cse.MINUE");
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

	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		AlertDialog.Builder bulder = new AlertDialog.Builder(this);
		bulder.setTitle("GPS IS DISABLE");
		bulder.setCancelable(false);
		bulder.setPositiveButton("Enable",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent startGps = new Intent(
								android.provider.Settings.ACTION_LOCALE_SETTINGS);
						startActivity(startGps);
					}
				});
		bulder.setNegativeButton("CANCLE",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		AlertDialog aler = bulder.create();
		aler.show();
	}

	public void onLocationChanged(Location Location) {
		LatLng latlang = new LatLng(Location.getLatitude(),
				Location.getLongitude());
	}
	
	
	
	
	

	
	
	

}
