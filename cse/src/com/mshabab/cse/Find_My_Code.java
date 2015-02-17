package com.mshabab.cse;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Find_My_Code extends Activity implements OnClickListener {

	private static int numbits = 6 * 5;
	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	final static HashMap<Character, Integer> lookup = new HashMap<Character, Integer>();
	public static String gechode1 = null;
	static {
		int i = 0;
		for (char c : digits)
			lookup.put(c, i++);
	}

	String useranemAll1;
	TextView usernameall2, textView1;
	public static double latitude1, longitude1;
	String geohashcode1;

	private LocationManager lm;
	private String provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.geocode);
		Button butt250 = (Button) findViewById(R.id.done250);

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		provider = lm.getBestProvider(new Criteria(), true);
		if (provider == null) {
			onProviderDisabled(provider);
		}
		Location loca = lm.getLastKnownLocation(provider);

		/****** to set your location *****/

		double locatins1 = loca.getLatitude();
		double locatins2 = loca.getLongitude();
		useranemAll1 = Login.usernameAll;

		// double locatins2 = -77.869051;
		// double locatins1 = 40.792986;

		if (loca != null) {
			onLocationChanged(loca);
		}

		geohashcode1 = this.encode(locatins1, locatins2);
		usernameall2 = (TextView) findViewById(R.id.geohashcode);
		usernameall2.setText(geohashcode1);

		butt250.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				startActivity(new Intent("com.mshabab.cse.MINUE"));
				finish();
			}
		});
	}

	public void onProviderDisabled(String arg0) {
		AlertDialog.Builder bulder = new AlertDialog.Builder(this);
		bulder.setTitle("GPS IS DISABLE");
		bulder.setCancelable(false);
		bulder.setPositiveButton("Enable",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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

	/*********************************************************************************************************/

	public String encode(double lat, double lon) {
		BitSet latbits = getBits(lat, -90, 90);
		BitSet lonbits = getBits(lon, -180, 180);
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < numbits; i++) {
			buffer.append((lonbits.get(i)) ? '1' : '0');
			buffer.append((latbits.get(i)) ? '1' : '0');
		}
		return base32(Long.parseLong(buffer.toString(), 2));
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
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
