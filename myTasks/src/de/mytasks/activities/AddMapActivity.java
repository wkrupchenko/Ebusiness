package de.mytasks.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import de.mytasks.R;
import de.mytasks.domain.Task;
import de.mytasks.service.SimpleHttpClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class AddMapActivity extends Activity {

	private GoogleMap map;
	private Button mBtnFind;
	private GoogleMap mMap;
	private EditText etPlace;
	private static final String TAG = "MainActivity";
	private Long taskId;
	private String taskName;
	private LatLng taskMap;
	private String resp;
//	private String latitude;
//	private String longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmap);
	
		Intent i = getIntent();
		taskId = i.getLongExtra("TaskId", 0L);
		taskName = i.getStringExtra("TaskName");
		taskMap = new LatLng(i.getDoubleExtra("Latitude", 0.0), i.getDoubleExtra("Longitude", 0.0));

		Log.v(TAG + "onCreate", "onCreate call");
		Log.v(TAG, taskId.toString());
		Log.v(TAG, taskMap.toString());
		// Getting reference to the find button
		mBtnFind = (Button) findViewById(R.id.btn_show);

		// mBtnFind.setOnClickListener(buttonHandler);

		// Getting reference to the SupportMapFragment
		MapFragment mapFragment = (MapFragment) getFragmentManager()
				.findFragmentById(R.id.map);

		// Getting reference to the Google Map
		mMap = mapFragment.getMap();

		// MapFragment mapfragment =(MapFragment)
		// getFragmentManager().findFragmentById(R.id.map);
		// mMap=mapfragment.getMap();
		// Getting reference to EditText
		etPlace = (EditText) findViewById(R.id.et_place);

		// Setting click event listener for the find button
		mBtnFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Getting the place entered

				Log.v(TAG + "onClick", "onClick call");
				String location = etPlace.getText().toString();

				if (location == null || location.equals("")) {
					Toast.makeText(getBaseContext(), "No Place is entered",
							Toast.LENGTH_SHORT).show();
					return;
				}

				String url = "https://maps.googleapis.com/maps/api/geocode/json?";

				try {
					// encoding special characters like space in the user input
					// place
					location = URLEncoder.encode(location, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				String address = "address=" + location;

				String sensor = "sensor=false";

				// url , from where the geocoding data is fetched
				url = url + address + "&" + sensor;
				Log.v(TAG + "onClick", "url is: " + url);
				// String modifiedURL= url.toString().replace(" ", "%20");

				// Instantiating DownloadTask to get places from Google
				// Geocoding service
				// in a non-ui thread
				DownloadTask downloadTask = new DownloadTask();

				// Start downloading the geocoding places
				downloadTask.execute(url);

//Hier mit dem auskommentieren beginnen
//				Log.v(TAG, "Latitude-Wert:" + latitude);
//				Log.v(TAG, "Longitude-Wert:" + longitude);
//				Log.v(TAG, taskId.toString());
//				new Thread(new Runnable(){
//					
//			    	@Override
//			    	public void run() {
//
//			    		
//			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//			    		postParameters.add(new BasicNameValuePair("latitude", latitude));
//			    		postParameters.add(new BasicNameValuePair("longitude", longitude));
//			    	    postParameters.add(new BasicNameValuePair("taskid", taskId.toString()));
//			    	    
//			    	    String response = null;
//			    	      try {
//			    	    	  response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksMainPage/addGeodata", postParameters);
////			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/getTasks", postParameters);
//			    	       String res = response.toString();
//			    	       Log.v(TAG, response.toString());
//			    	       Log.v(TAG, res.toString());
//			    	       resp = res;
//			    	}
//			    	      catch (Exception e) {
//			    	          e.printStackTrace();
//			    	          Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
//			    	      }
//			    	} 
//				}).start();
//				
//				try {
//
//					/** wait a second to get response from server */
//		    	    Thread.sleep(1000);
//		    	    /** Inside the new thread we cannot update the main thread
//		    	    So updating the main thread outside the new thread */
//		    	    	if (null != resp && !resp.isEmpty()) {
//		    	    		 boolean check = resp.contains("Map Informations updated");	    	    		  
//		    	    	       if (check == true) {
//		    	    	    	   Toast.makeText(getApplicationContext(), "Added/Changed Map Information",Toast.LENGTH_LONG).show();
//		    	    	    	   Intent it = new Intent(getApplicationContext(), ShowMapActivity.class);
//		    	    	    	   startActivity(it);
//		    	    	       } 
//		    	    	}
//		    	    	
//		    	    	else {
//		    	    			Toast.makeText(getApplicationContext(), "Failure",Toast.LENGTH_SHORT).show();
//		    	    	}
//				}
//				catch (Exception e) {
//				    	e.printStackTrace();
//				 }
//Hier dann new Thread auskommentieren


			}
			
		});

	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;

	}

	/** A class, to download Places from Geocoding webservice */
	private class DownloadTask extends AsyncTask<String, Integer, String> {

		String data = null;

		// Invoked by execute() method of this object
		@Override
		protected String doInBackground(String... url) {
			try {
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(String result) {

			ParserTask parserTask = new ParserTask();
			parserTask.execute(result);
		}

	}

	class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;
			GeocodeJSONParser parser = new GeocodeJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				/** Getting the parsed data as a an ArrayList */
				places = parser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		// Executed after the complete execution of doInBackground() method
		@Override
		protected void onPostExecute(List<HashMap<String, String>> list) {

			// Clears all the existing markers
			mMap.clear();
			int i = 0;
//			for (int i = 0; i < list.size(); i++) {

				// Creating a marker
				MarkerOptions markerOptions = new MarkerOptions();
//				HashMap<String, String> hmPlace = list.get(i);
				if(list.size()>0){
				HashMap<String, String> hmPlace = list.get(i);
				

				double lat = Double.parseDouble(hmPlace.get("lat"));
				double lng = Double.parseDouble(hmPlace.get("lng"));
				

				String name = hmPlace.get("formatted_address");
				LatLng latLng = new LatLng(lat, lng);
				markerOptions.position(latLng);
				markerOptions.title(name);

				mMap.addMarker(markerOptions);

				// Locate the first location
				if (i == 0)
					mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//			}
				
				
//				public void getTasks() throws NullPointerException {
				addMapInformation(lat, lng);
//				latitude = Double.toString(lat);
//				longitude = Double.toString(lng);
				Log.v(TAG, "Werte:" + latLng.toString());
				}
				else{
					Toast.makeText(getApplicationContext(), "No Adress Found",Toast.LENGTH_SHORT).show();
				}
//				Log.v(TAG, "Longitude-Wert:" + longitude);
//				Log.v(TAG, taskId.toString());
//				
//				  	new Thread(new Runnable(){
//						
//				    	@Override
//				    	public void run() {
//
//				    		
//				    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//				    		postParameters.add(new BasicNameValuePair("latitude", latitude));
//				    		postParameters.add(new BasicNameValuePair("longitude", longitude));
//				    	    postParameters.add(new BasicNameValuePair("taskid", taskId.toString()));
//				    	    
//				    	    String response = null;
//				    	      try {
//				    	    	  response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksMainPage/addGeodata", postParameters);
////				    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/getTasks", postParameters);
//				    	       String res = response.toString();
//				    	       Log.v(TAG, response.toString());
//				    	       Log.v(TAG, res.toString());
//				    	       resp = res;
//				    	}
//				    	      catch (Exception e) {
//				    	          e.printStackTrace();
//				    	          Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
//				    	      }
//				    	} 
//					}).start();
//					
//					try {
//
//						/** wait a second to get response from server */
//			    	    Thread.sleep(1000);
//			    	    /** Inside the new thread we cannot update the main thread
//			    	    So updating the main thread outside the new thread */
//			    	    	if (null != resp && !resp.isEmpty()) {
//			    	    		 boolean check = resp.contains("Map Informations updated");	    	    		  
//			    	    	       if (check == true) {
//			    	    	    	   Toast.makeText(getApplicationContext(), "Added/Changed Map Information",Toast.LENGTH_LONG).show();
//			    	    	    	   Intent it = new Intent(getApplicationContext(), ShowMapActivity.class);
//			    	    	    	   startActivity(it);
//			    	    	       } 
//			    	    	}
//			    	    	
//			    	    	else {
//			    	    			Toast.makeText(getApplicationContext(), "Failure",Toast.LENGTH_SHORT).show();
//			    	    	}
//					}
//					catch (Exception e) {
//					    	e.printStackTrace();
//					 }
		}
	}
	
	public void addMapInformation(final Double latitude, final Double longitude) throws NullPointerException {
//		DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
//		dfs.setDecimalSeparator('.');
//		DecimalFormat dFormat = new DecimalFormat("0,00", dfs);
		final String lat = String.valueOf(latitude);
//		Double.parseDouble(lat.replace('.', ','));
		final String lat2 = lat.replace('.', ',');
//		String latformatted = dFormat.format(latitude);
		final String lng = String.valueOf(longitude);
//		Double.parseDouble(longitude.replace(',', '.'));
		final String lng2 = lng.replace('.', ',');
//		String longformatted = dFormat.format(longitude);
		Log.v(TAG, "Latitude-Wert:" + latitude);
		Log.v(TAG, "Longitude-Wert:" + longitude);
//		Log.v(TAG, "Latitude-Formatted" + latformatted);
//		Log.v(TAG, "Longitude-Formatted" + longformatted);
		Log.v(TAG, taskId.toString());
//		latitude.toString();
//		longitude.toString();
		new Thread(new Runnable(){
			
	    	@Override
	    	public void run() {

	    		
	    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//	    		postParameters.add(new BasicNameValuePair("latitude", lat.toString()));
//	    		postParameters.add(new BasicNameValuePair("longitude", lng.toString()));
//	    		postParameters.add(new BasicNameValuePair("latitude", latitude.toString()));
//	    		postParameters.add(new BasicNameValuePair("longitude", longitude.toString()));
	    		postParameters.add(new BasicNameValuePair("latitude", lat2));
	    		postParameters.add(new BasicNameValuePair("longitude", lng2));
	    	    postParameters.add(new BasicNameValuePair("taskid", taskId.toString()));
	    	    
	    	    String response = null;
	    	      try {
//	    	    	  response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksMainPage/addGeodata", postParameters);
	    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/addGeodata", postParameters);
	    	       String res = response.toString();
	    	       Log.v(TAG, response.toString());
	    	       Log.v(TAG, res.toString());
	    	       resp = res;
	    	}
	    	      catch (Exception e) {
	    	          e.printStackTrace();
	    	          Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
	    	      }
	    	} 
		}).start();
		
		try {

			/** wait a second to get response from server */
    	    Thread.sleep(1000);
    	    /** Inside the new thread we cannot update the main thread
    	    So updating the main thread outside the new thread */
    	    	if (null != resp && !resp.isEmpty()) {
    	    		 boolean check = resp.contains("Map Informations updated");	    	    		  
    	    	       if (check == true) {
    	    	    	   Toast.makeText(getApplicationContext(), "Added/Changed Map Information",Toast.LENGTH_LONG).show();
    	    	    	   Intent it = new Intent(getApplicationContext(), ShowMapActivity.class);
//    	    	    	   it.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	    	    	   it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	    	    	   it.putExtra("TaskId", taskId);
    	    	    	   it.putExtra("TaskName", taskName);
    	    	    	   it.putExtra("Latitude", latitude);
    	    	    	   it.putExtra("Longitude", longitude);
    	    	    	   startActivity(it);
    	    	       } 
    	    	}
    	    	
    	    	else {
    	    			Toast.makeText(getApplicationContext(), "Failure",Toast.LENGTH_SHORT).show();
    	    	}
		}
		catch (Exception e) {
		    	e.printStackTrace();
		 }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}