package com.mshabab.cse;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Minue<Sring> extends ListActivity {

	String classMenue[]= {"ChekIn","Find_My_Code","Find_preson","People","Maps"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(Minue.this, android.R.layout.simple_list_item_1, classMenue) );
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String post = classMenue[position]; 
		Class ourClass;
		try {
			
			
			
			ourClass = Class.forName("com.mshabab.cse." + post);
			boolean online_state = isOnline();
			if (!(online_state)) {
	    		Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();

			} else {
			
			Intent ourIntent = new Intent(Minue.this, ourClass);
			startActivity(ourIntent); }
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/***************************************************************/
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

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
	/*/***************************************************************/
	

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	
	

}
