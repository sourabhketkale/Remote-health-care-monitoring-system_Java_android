package com.example.eseva;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class PushValues extends Activity {
	String serverAdd;
	int cnt;
	String pid;
	Button start;
	Button ok;
	Button nok;
	ProgressBar p;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_values);
		Bundle b = getIntent().getExtras();
		if(b!=null){
			serverAdd=b.getString("serverAdd");
			pid=b.getString("pid");
			Toast.makeText(this, "PID :"+pid, Toast.LENGTH_LONG).show();
		}
		start=(Button)findViewById(R.id.buttonStartNow);
		ok=(Button)findViewById(R.id.buttonOk);
		nok=(Button)findViewById(R.id.buttonNOk);
		p=(ProgressBar)findViewById(R.id.progressBar1);

		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			callServlet(100, 80);
			}
		});
		nok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				callServlet(150, 105);
			}
		});

		start.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendValues();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_push_values, menu);
		return true;
	}

	void callServlet(int heart,int temp) {

		System.out.println("SERVLET CALLED");

		try {
			String urlstr = "http://"+serverAdd+":8084/WirelessBodyServer//PushPatientValues";
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
			out.writeObject(heart+","+temp+","+pid);

			out.close();
			System.out.println("in server3");

			// define a new ObjectInputStream on the input stream
			ObjectInputStream in = new ObjectInputStream(
					connection.getInputStream());
			// receive and deserialize the object, note the cast
			boolean b = false;

			b= (Boolean) in.readObject();

			System.out.println("Return :"+b);

			in.close();
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}

	void sendValues()	{
		int pvalue=0;

		for(int i =0;i<100;i++){
			Random r  = new Random();
			Random r1  = new Random();
			int temp=r.nextInt(128);
			int temp1=r1.nextInt(128);
			System.out.println("VALUE :"+temp);
			System.out.println("VALUE :"+temp1);
			callServlet(temp,temp1);
			p.setProgress(pvalue);
			pvalue=pvalue+5;
			if(pvalue==100){
				pvalue=0;
			}
		}
		Toast.makeText(this, "ALL VALUE UPDATED TO SERVER", Toast.LENGTH_LONG).show();
	}

}
