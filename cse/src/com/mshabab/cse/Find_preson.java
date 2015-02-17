package com.mshabab.cse;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class Find_preson extends Activity implements OnClickListener {
	public  String  gechode1 ="df";
	String mshabab10;
	EditText geocode;
	Button butt250;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_person);
		butt250 = (Button) findViewById(R.id.done250);
	 geocode = (EditText) findViewById(R.id.int250);


		butt250.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				 gechode1 = geocode.getText().toString();
				 gechode1 =gechode1.replaceAll("\\p{Z}","");
				 gechode1=gechode1.trim();

				 		 
				 Intent i = new Intent(Find_preson.this, MapsMarkers.class);
				 i.putExtra("msh1", gechode1);
					startActivity(i);
					finish();
				
			}
		});
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
