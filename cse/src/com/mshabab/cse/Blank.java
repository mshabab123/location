package com.mshabab.cse;

import com.mshabab.cse.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Blank extends Activity {

	Button cancel, send, shownumber;
	TextView  result, reslut_location;
	String phonenumber;
	String  presonname ;
	EditText msg1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank);
		
		Bundle extras = getIntent().getExtras();

		phonenumber = extras.getString("phone");
		presonname = extras.getString("name_preson");
		
		
		cancel = (Button) findViewById(R.id.finish_blank);
		send = (Button) findViewById(R.id.sendmsg12);
		shownumber = (Button) findViewById(R.id.shwothenumber25);
		result = (TextView) findViewById(R.id.showthereslut);
		reslut_location = (TextView) findViewById(R.id.locatins1);
		msg1 = (EditText) findViewById(R.id.editText1);

		
		reslut_location.setText(presonname);
		msg1.setText("Hi "+presonname+" ,");

		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {			
					startActivity(new Intent("com.mshabab.cse.MINUE"));

			}
	});
		
		shownumber.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				result.setText(phonenumber);


			}
	});
		
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String msg2 = msg1.getText().toString();


				startActivity(new Intent("com.mshabab.cse.MINUE"));
sendMsg(phonenumber, msg2);

			}

			
	});
		
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		finish();
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

	private void sendMsg(String phonenumber, String msg2) {
		// TODO Auto-generated method stub
	SmsManager sms= SmsManager.getDefault();
	sms.sendTextMessage(phonenumber, null, msg2, null, null);
	Toast.makeText(this, "Msg. sent to " + presonname, Toast.LENGTH_SHORT).show();

	
	}

}
