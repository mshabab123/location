package com.mshabab.cse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class Online_state extends Activity {



	
public  void Online_state1(){
		
				boolean online_state = isOnline();
				while (!(online_state)) {
					DialogLogInF();
					online_state = isOnline();
				}
		}



	public   boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public  void DialogLogInF() {

		AlertDialog.Builder builder = new AlertDialog.Builder(null);

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
								// Action for 'NO' Button
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
