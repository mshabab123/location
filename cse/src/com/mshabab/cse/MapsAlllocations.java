package com.mshabab.cse;

import java.security.Provider;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View.OnLongClickListener;
import android.widget.Toast;

public class MapsAlllocations extends Activity implements LocationListener {
	private GoogleMap goMap;
	private String provider;
	private LocationManager lm;

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
			LatLng lat = new LatLng(40.796852, -77.868965);

			goMap.addMarker(new MarkerOptions()
					.title("Frind1")
					.position(lat)
					.snippet("this for the project test")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

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
				if (loc != null) {
					onLocationChanged(loc);

				}
				goMap.setOnMapLongClickListener(OnLongClickListener());
			}
		}
	}

	private OnMapLongClickListener OnLongClickListener() {
		// TODO Auto-generated method stub
		return new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				// TODO Auto-generated method stub
				Log.i(arg0.toString(), "User long Clicked");
			}
		};
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
		// TODO Auto-generated method stub
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
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});

		AlertDialog aler = bulder.create();
		aler.show();
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}
}