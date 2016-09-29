package com.example.eseva;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import LibPack.PatientInfo;
import LibPack.QueryInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class SelectPatient extends Activity {



	Button cont;
	String pid;
	ListView list;
	ArrayAdapter<String> listAdapter;
	String serverAdd;
	Vector<PatientInfo> allPatient;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_patient);
		Bundle b = getIntent().getExtras();
		if(b!=null){
			serverAdd=b.getString("serverAdd");
			Toast.makeText(this, "ADD :"+serverAdd, Toast.LENGTH_SHORT).show();
		}
		list=(ListView)findViewById(R.id.listView1);
		listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
		
		
		cont=(Button)findViewById(R.id.buttonCont);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				pid=allPatient.elementAt(arg2).pid;
			}
		});

		cont.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(v.getContext(),MainForm.class);
				i.putExtra("serverAdd", serverAdd);
				i.putExtra("pid",pid);
				startActivity(i);
			}
		});
		callServlet();
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_select_patient, menu);
		return true;
	}
	void callServlet() {

		System.out.println("SERVLET CALLED");

		try {
			String urlstr = "http://"+serverAdd+":8084/WirelessBodyServer//GetPatientServ";
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

			allPatient=(Vector<PatientInfo>)in.readObject();
			for(int i =0;i<allPatient.size();i++){
				listAdapter.add(allPatient.elementAt(i).fname+"  "+allPatient.elementAt(i).lname);
			}
			list.setAdapter(listAdapter);
			
			
			in.close();
		} catch (Exception e) {
			System.out.println("Error:" + e);
		}
	}
	
}
