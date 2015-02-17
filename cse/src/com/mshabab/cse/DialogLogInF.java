package com.mshabab.cse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class DialogLogInF extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.failure_login);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage("Pleas try agin!")
				.setCancelable(false)
				.setPositiveButton("Register new",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								dialog.cancel();
								Intent i = new Intent(DialogLogInF.this,
										Register.class);
								startActivity(i);

								finish();
							}
						})
				.setNegativeButton("Again",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Action for 'NO' Button
								dialog.cancel();
								finish();

							}
						});

		// Creating dialog box
		AlertDialog alert = builder.create();
		// Setting the title manually
		alert.setTitle("Invalid ID or password");
		setContentView(R.layout.white);
		alert.show();

	}

}
