package de.mytasks.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.service.SimpleHttpClient;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParticipantsActivity extends ListActivity {

	private List<String> emails;
	private List<String> names;
	private List<Float> ratings;
	private String resp;
	private String tasklistName;
	private Long tasklistId;
	private static final String TAG = "ParticipantsActivity";
	private ParticipantListAdapter adapter;
	private TextView headerText;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		emails = new ArrayList<String>();
		names = new ArrayList<String>();
		ratings = new ArrayList<Float>();
		
		Intent i = getIntent();
		tasklistName = i.getStringExtra("TASKLIST_NAME");
		tasklistId = i.getLongExtra("TASKLIST_ID", 0L);
		
		adapter = new ParticipantListAdapter(this,
				names, emails, ratings);
		setListAdapter(adapter);
		
		ListView lv = getListView();
		LayoutInflater inflater = getLayoutInflater();
		ViewGroup header = (ViewGroup)inflater.inflate(R.layout.group_header_participants, lv, false);
		headerText = (TextView) header.findViewById(R.id.header);
		headerText.setText(tasklistName);
		lv.addHeaderView(header);
//		lv.addHeaderView(header, null, false);

		
		getParticipants();
	}

	public void getParticipants() {
		new Thread(new Runnable() {

			@Override
			public void run() {

				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("tasklistid",
						tasklistId.toString()));

				String response = null;
				try {
					response = SimpleHttpClient.executeHttpPost(
							"http://www.iwi.hs-karlsruhe.de/eb03/getParticipants",
							postParameters);
					String res = response.toString();
					Log.v(TAG, res.toString());
					resp = res;
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}).start();

		try {

			/** wait a second to get response from server */
			Thread.sleep(1000);

			JSONArray jsonArray = new JSONArray(resp);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jobject = jsonArray.getJSONObject(i);
	    	    String rating = jobject.getString("rating");
	    	    String name = jobject.getString("name");
	    	    String email = jobject.getString("email");
	    	    
	    	    Log.v(TAG, name);
	    	    Log.v(TAG, rating);
	    	    Log.v(TAG, email);
	    	    
	    	    names.add(name);
	    	    emails.add(email);
	    	    ratings.add(Float.valueOf(rating));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// back button
	public void back(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

}
