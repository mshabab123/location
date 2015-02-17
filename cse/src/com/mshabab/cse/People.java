package com.mshabab.cse;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class People extends ListActivity {

	public String name_preson;
	public String phone_preson;

	public double distance250;
	int d;
	String distance1, locations2;
	int distance22;
	double location1, location2;
	public String geocode5;
	double Latit, lontit;
	private String provider, loc260;
	private LocationManager lm;

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

	private ProgressDialog pDialog;

	private static final String LOCATION_URL = "http://mshabab.bugs3.com/cse/locations.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_POSTS = "posts";
	private static final String TAG_loc = "loc";
	private static final String TAG_user = "user";
	private static final String TAG_times = "times";
	private static final String TAG_desc = "desc";
	private static final String TAG_phone = "phone";

	private JSONArray mComments = null;
	private ArrayList<HashMap<String, String>> mCommentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// note that use read_comments.xml instead of our single_post.xml

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		provider = lm.getBestProvider(new Criteria(), true);
		if (provider == null) {
			onProviderDisabled(provider);
		}
		Location loca = lm.getLastKnownLocation(provider);

		/****** to set your location *****/

		location1 = loca.getLatitude();
		location2 = loca.getLongitude();

		setContentView(R.layout.locations);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// loading the comments via AsyncTask
		new LoadComments().execute();
	}

	public void addComment(View v) {
		// if you wnat to add button to updata the list
	}

	public void updateJSONdata() {

		mCommentList = new ArrayList<HashMap<String, String>>();

		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(LOCATION_URL);

		try {

			mComments = json.getJSONArray(TAG_POSTS);

			// looping through all posts according to the json object returned
			for (int i = 0; i < mComments.length(); i++) {
				JSONObject c = mComments.getJSONObject(i);

				// gets the content of each tag
				String loc1 = c.getString(TAG_loc);
				String user1 = c.getString(TAG_user);
				String times1 = c.getString(TAG_times);
				String desc1 = c.getString(TAG_desc);
				String phone1 = c.getString(TAG_phone);

				Log.i(loc1.toString(), "mshabab12");

				double[] latlon = decode(loc1);
				Latit = latlon[0];
				lontit = latlon[1];

				distance250 = CalculationByDistance(location1, location2,
						Latit, lontit);

				int d = (int) distance250;
				loc1 = String.valueOf(d);

				// creating new HashMap
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(TAG_loc, loc1);
				map.put(TAG_user, user1);
				map.put(TAG_times, times1);
				map.put(TAG_desc, desc1);
				map.put(TAG_phone, phone1);

				// adding HashList to ArrayList
				mCommentList.add(map);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updateList() {

		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
				R.layout.single_location, new String[] { TAG_user, TAG_loc,
						TAG_desc, TAG_times }, new int[] { R.id.location,
						R.id.username, R.id.descoiption, R.id.times1 });

		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			// ************************************************************************************************************

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				phone_preson = mCommentList.get(position).get("phone");
				name_preson = mCommentList.get(position).get("user");
				Intent i = new Intent(People.this, Blank.class);
				i.putExtra("phone", phone_preson);
				i.putExtra("name_preson", name_preson);
				startActivity(i);
finish();

			}

		});
	}

	// ************************************************************************************************************

	public class LoadComments extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(People.this);
			pDialog.setMessage("Loading Locations...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			updateJSONdata();
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			updateList();
		}
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

	public static double CalculationByDistance(double initialLat,
			double initialLong, double finalLat, double finalLong) {
		/* PRE: All the input values are in radians! */

		double latDiff = finalLat - initialLat;
		double longDiff = finalLong - initialLong;
		double earthRadius = 6371; // In Km if you want the distance in km

		double distance = 2
				* earthRadius
				* Math.asin(Math.sqrt(Math.pow(Math.sin(latDiff / 2.0), 2)
						+ Math.cos(initialLat) * Math.cos(finalLat)
						* Math.pow(Math.sin(longDiff / 2), 2)));

		return distance;

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

}
