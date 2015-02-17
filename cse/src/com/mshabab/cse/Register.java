package com.mshabab.cse;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {

	private EditText user, pass, description, phone;
	private Button mRegister, cancelRegister;

	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	private static final String REGISTER_RUL = "http://mshabab.bugs3.com/cse/register.php";

	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		user = (EditText) findViewById(R.id.intrname);
		pass = (EditText) findViewById(R.id.interpass);
		 description = (EditText) findViewById(R.id.inter_description);
		 phone = (EditText) findViewById(R.id.phoneregister);

		mRegister = (Button) findViewById(R.id.sipmet_register);
		cancelRegister = (Button) findViewById(R.id.cancel_register);

		mRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
			
				
				boolean online_state = isOnline();
				if (!(online_state)) {
		    		Toast.makeText(Register.this, "No internet connection", Toast.LENGTH_SHORT).show();

				} else {
				new CreateUser().execute();
				//  Intent i = new Intent( Register.this, Minue.class);
				//	 startActivity(i);
				//	 finish();
				}
			}
		});
		cancelRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				  Intent i = new Intent( Register.this, Login.class);
					 startActivity(i);
			
			}
		});
		
		
		
		
		
		
		
		
		

	}


	class CreateUser extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Register.this);
			pDialog.setMessage("Creating User...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			int success;
			String username1 = user.getText().toString();
			String password1 = pass.getText().toString();
			String description1  = description.getText().toString();
			String phone1 = phone.getText().toString();
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username1));
				params.add(new BasicNameValuePair("password", password1));
				params.add(new BasicNameValuePair("disc", description1));
				params.add(new BasicNameValuePair("phone", phone1));
				Log.d("request!", "starting");
				JSONObject json = jsonParser.makeHttpRequest(REGISTER_RUL,
						"POST", params);

				Log.d("Registering attempt", json.toString());

				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("User Created!", json.toString());
					
					
					
					Intent i = new Intent( Register.this, Login.class);
					 startActivity(i);
					 finish();
					return json.getString(TAG_MESSAGE);
					
				} else {
					Log.d("Registering Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (file_url != null) {
				Toast.makeText(Register.this, file_url, Toast.LENGTH_LONG)
						.show();
			}

		}

	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	
	

}
