package com.mshabab.cse;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Maps extends Activity implements LocationListener {
	private GoogleMap goMap;
	private String provider;
	private LocationManager lm;
	Context context = this;
	public static String Distance1 = "1000000";
	public static double latitude1, longitude1;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isGooglePlay()) {
			setContentView(R.layout.maps);

			try {
				setUpMap();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// this code for change the map type to be hybeid
			// goMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

			/****** this mehtod to make the locations on the mape ******/
			addLocations();
			goMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

				@Override
				public void onMapLongClick(LatLng latlng) {
					LayoutInflater li = LayoutInflater.from(context);
					final View v = li.inflate(R.layout.alertlayout, null);
					AlertDialog.Builder bulder1 = new AlertDialog.Builder(
							context);
					bulder1.setView(v);
					bulder1.setCancelable(false);

					bulder1.setTitle("Please inter the distance you are looking for");
					Log.i(latlng.toString(), "User long Clicked");
					latitude1 = latlng.latitude;
					longitude1 = latlng.longitude;
					bulder1.setPositiveButton("Find",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									EditText title = (EditText) v
											.findViewById(R.id.ettitle);
									Distance1 = title.getText().toString();
									
									
									if (Distance1.isEmpty())
									{}
									else{
									
									Intent i = new Intent(Maps.this,
											NearsLocations.class);
									startActivity(i);
									}
								}
							});
					bulder1.setNegativeButton("CANCLE",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});

					AlertDialog aler1 = bulder1.create();
					aler1.show();

				}
			});

		}
	}

	private void addLocations() {
	}

	private boolean isGooglePlay() {
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if (status == ConnectionResult.SUCCESS) {
			return (true);

		} else {
			((Dialog) GooglePlayServicesUtil.getErrorDialog(status, this, 30))
					.show();

		}
		return false;
	}

	private void setUpMap() {

		if (goMap == null) {
			goMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			if (goMap != null) {
				goMap.setMyLocationEnabled(true);
				lm = (LocationManager) getSystemService(LOCATION_SERVICE);
				provider = lm.getBestProvider(new Criteria(), true);
				if (provider == null) {
					onProviderDisabled(provider);
				}
				Location loc = lm.getLastKnownLocation(provider);

				/****** to set your location *****/

				// locatins1= loc.getLongitude();

				if (loc != null) {
					onLocationChanged(loc);

				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setUpMap();
		lm.requestLocationUpdates(provider, 1400, 1, this);

	}

	/************************************************************************/
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_legalnotices) {
			startActivity(new Intent(this, LegalNoticesActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location Location) {
		LatLng latlang = new LatLng(Location.getLatitude(),
				Location.getLongitude());

		goMap.moveCamera(CameraUpdateFactory.newLatLng(latlang));
		goMap.animateCamera(CameraUpdateFactory.zoomTo(10));

	}

	@Override
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

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}
}