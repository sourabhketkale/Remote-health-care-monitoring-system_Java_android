package com.example.eseva;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;

import LibPack.PatientInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class ServerAddress extends Activity {


	Button but;
	String serverAdd;
	EditText server;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		if(b!=null){
			serverAdd=b.getString("serverAdd");
			
			
		}
		setContentView(R.layout.activity_server_address);
		but=(Button)findViewById(R.id.buttonConnect);
		server=(EditText)findViewById(R.id.editTextServAdd);
		but.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				serverAdd=server.getText().toString();
				callServlet();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_server_address, menu);
		return true;
	}
	void callServlet() {

		System.out.println("SERVLET CALLED");

		try {
			String urlstr = "http://"+serverAdd+":8084/WirelessBodyServer//CheckConnection";
			URL url = new URL(urlstr);
			URLConnection connection = url.openConnection();
			System.out.println("in server1");

			connection.setDoOutput(true);
			connection.setDoInput(true);

			// don't use a cached version of URL connection
			connection.setUseCaches(false);
			connection.setDefaultUseCaches(false);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			System.out.println("in server2");

			// specify the content type that binary data is sent
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			ObjectOutputStream out = new ObjectOutputStream(
					connection.getOutputStream());
			// send and serialize the object
			out.writeObject("");
			out.close();
			System.out.println("in server3");

			// define a new ObjectInputStream on the input stream
			ObjectInputStream in = new ObjectInputStream(
					connection.getInputStream());
			// receive and deserialize the object, note the cast
			boolean b = false;

			b= (Boolean) in.readObject();
			if (b) {
				System.out.println("Connection OK");
				Toast.makeText(this, "Connection OK", Toast.LENGTH_SHORT).show();

				Intent i = new Intent(this,SelectPatient.class);
				i.putExtra("serverAdd", serverAdd);
				startActivity(i);
			} else {
				Toast.makeText(this, "Please Verify Server Address", Toast.LENGTH_SHORT).show();
				System.out.println("CONNECTION ERROR");
			}

			in.close();
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

}
