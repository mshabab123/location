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
import android.widget.TextView;
import android.widget.Toast;

public class ChekIn extends Activity implements OnClickListener {

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

	String useranemAll1;
	TextView usernameall2, textView1;
	public static double latitude1, longitude1;
	String post_loc;

	private LocationManager lm;
	private String provider, locations2;

	Timer timer = new Timer();

	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	private static final String POST_COMMENT_URL = "http://mshabab.bugs3.com/cse/addlocation.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.white);

		useranemAll1 = Login.usernameAll;

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



		if (loca != null) {
			onLocationChanged(loca);
		}

		Log.i(locations2, "User long Clicked");
		post_loc = this.encode(locatins1, locatins2);
		new PostLocation().execute();

	}

	@Override
	public void onClick(View v) {
	}

	class PostLocation extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ChekIn.this);
			pDialog.setMessage("Updating the location and setup paras");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int success;

			String post_id = useranemAll1;

			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("loc", post_loc));
				params.add(new BasicNameValuePair("id", post_id));

				Log.d("request!", "starting");

				JSONObject json = jsonParser.makeHttpRequest(POST_COMMENT_URL,
						"POST", params);

				Log.d("Update the location attempt", json.toString());
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("updated lcoation!", json.toString());
					// finish();
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Updating locations: prolbme!",
							json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			} finally {

				Intent mshIntent = new Intent("com.mshabab.cse.MINUE");
				startActivity(mshIntent);
				finish();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(ChekIn.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

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

}
