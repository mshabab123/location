package com.mshabab.cse;

import java.security.Provider;
import java.util.BitSet;
import java.util.HashMap;
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

import android.annotation.SuppressLint;
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

public class MapsMarkers extends Activity implements LocationListener {

	public String geocode5;
double Latit, lontit; 
	private static int numbits = 6 * 5;
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
	static {
		int i = 0;
		for (char c : digits)
			lookup.put(c, i++);
	}

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

			/****** this mehtod to make the locations on the mape ******/
			Bundle extras = getIntent().getExtras();
			geocode5 = extras.getString("msh1");
			
			
			
			addLocations(geocode5, "Your friend ",
					"If you looking to find people around this area, please use Map icon");
			
			Location loc = lm.getLastKnownLocation(provider);
			if (loc != null) {
				onLocationChanged(loc);

			}

		}
	}

	private void addLocations(String eee, String username, String descption) {

		double[] latlon = decode(eee);
Latit = latlon[0];
lontit = latlon[1];
		
		LatLng lat = new LatLng(latlon[0], latlon[1]);

		goMap.addMarker(new MarkerOptions()
				.title(username)
				.position(lat)
				.snippet(descption)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

	}

	public double[] decode(String geohash) {
		StringBuilder buffer = new StringBuilder();
		for (char c : geohash.toCharArray()) {

			int i = lookup.get(c) + 32;
			buffer.append(Integer.toString(i, 2).substring(1));
		}

		BitSet lonset = new BitSet();
		BitSet latset = new BitSet();

		// even bits
		int j = 0;
		for (int i = 0; i < numbits * 2; i += 2) {
			boolean isSet = false;
			if (i < buffer.length())
				isSet = buffer.charAt(i) == '1';
			lonset.set(j++, isSet);
		}

		// odd bits
		j = 0;
		for (int i = 1; i < numbits * 2; i += 2) {
			boolean isSet = false;
			if (i < buffer.length())
				isSet = buffer.charAt(i) == '1';
			latset.set(j++, isSet);
		}

		double lon = decode(lonset, -180, 180);
		double lat = decode(latset, -90, 90);

		return new double[] { lat, lon };
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

	@SuppressLint("NewApi")
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
		LatLng latlang = new LatLng(Latit,
				lontit);

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

	private double decode(BitSet bs, double floor, double ceiling) {
		double mid = 0;
		for (int i = 0; i < bs.length(); i++) {
			mid = (floor + ceiling) / 2;
			if (bs.get(i))
				floor = mid;
			else
				ceiling = mid;
		}
		return mid;
	}

	private BitSet getBits(double lat, double floor, double ceiling) {
		BitSet buffer = new BitSet(numbits);
		for (int i = 0; i < numbits; i++) {
			double mid = (floor + ceiling) / 2;
			if (lat >= mid) {
				buffer.set(i);
				floor = mid;
			} else {
				ceiling = mid;
			}
		}
		return buffer;
	}

	public static String base32(long i) {
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);
		if (!negative)
			i = -i;
		while (i <= -32) {
			buf[charPos--] = digits[(int) (-(i % 32))];
			i /= 32;
		}
		buf[charPos] = digits[(int) (-i)];

		if (negative)
			buf[--charPos] = '-';
		return new String(buf, charPos, (65 - charPos));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		finish();
	}

}
