package com.example.eseva;

import android.os.Bundle;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class MainForm extends Activity {

	Button start,stop,emulate;
	String uid;
	String serverAdd;
	String pid;





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		if(b!=null){
			serverAdd=b.getString("serverAdd");
			pid=b.getString("pid");
		}
		setContentView(R.layout.activity_main_form);
		start = (Button) findViewById(R.id.butStart);
		stop = (Button) findViewById(R.id.buttonStop);
		emulate= (Button) findViewById(R.id.butEmulate);



		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(v.getContext(), PushValues.class);
				i.putExtra("serverAdd", serverAdd);
				i.putExtra("pid",pid);
				startActivity(i);
				
			}
		});



		stop.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(v.getContext(), PushValues.class);
				i.putExtra("uid", uid);
				startActivity(i);

			}
		});
	}

}
