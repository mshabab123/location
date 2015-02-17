package com.mshabab.cse;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	Button login;
	Button register;
	CheckBox cb;
	EditText username;
	EditText password;
	static String usernameAll = "";

	/********************************************************/
	private ProgressDialog pDialog;
	private Dialog pfDialog;

	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://mshabab.bugs3.com/cse/login.php";
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	/********************************************************/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		cb = (CheckBox) findViewById(R.id.cb);
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		loadPrefs();

		login.setOnClickListener(this);
		register.setOnClickListener(this);

		
		boolean online_state = isOnline();
		if (!(online_state)) {
			DialogLogInF();
		}
		
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			
			
			boolean online_state = isOnline();
			if (!(online_state)) {
		    		Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

					} else {
				
			
			new AttemptLogin().execute();
			usernameAll = username.getText().toString();
			/************************************/
			savePrefs("CHECKBOX", cb.isChecked());
			if (cb.isChecked())
				savePrefs("NAME", username.getText().toString());
			savePrefs("PASS", password.getText().toString());
		}
			/**********************************/
			break;
		case R.id.register:
			Intent i = new Intent(this, Register.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}

	class AttemptLogin extends AsyncTask<String, String, String> {
		boolean Failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Attempting login...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {

			int success;
			String user1 = username.getText().toString();
			String pass1 = password.getText().toString();
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>(2);
				params.add(new BasicNameValuePair("username", user1));
				params.add(new BasicNameValuePair("password", pass1));

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST",	params);

				// check your log for json response
				Log.d("Login attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());
					Intent i = new Intent(Login.this, DoBackGournd.class);
					startActivity(i);
					savePrefs("NAME", username.getText().toString());

					// startActivity(new Intent("com.mshabab.cse.MINUE"));

					return json.getString(TAG_MESSAGE);
				} else {

					Log.d("Login Failure!", json.getString(TAG_MESSAGE));

					Intent i = new Intent(Login.this, DialogLogInF.class);
					startActivity(i);

				}
			} catch (JSONException e) {
			}
			return null;

		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();

		}

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
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.menuSweet:
			startActivity(new Intent("com.mshabab.cse516.MINUE"));
			break;
		case R.id.menuToss:
			finish();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void loadPrefs() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean cbValue = sp.getBoolean("CHECKBOX", false);
		String name = sp.getString("NAME", "");
		String pass = sp.getString("PASS", "");
		if (cbValue) {
			cb.setChecked(true);
		} else {
			cb.setChecked(false);
		}
		username.setText(name);
		password.setText(pass);
	}

	private void savePrefs(String key, boolean value) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();

	}

	private void savePrefs(String key, String value) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();

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
