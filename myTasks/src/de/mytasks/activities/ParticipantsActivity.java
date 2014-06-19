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
import de.mytasks.domain.User;
import de.mytasks.service.SessionManager;
import de.mytasks.service.SimpleHttpClient;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class ParticipantsActivity extends ListActivity {
	
	
	SessionManager session;

	private List<String> emails;
	private List<String> names;
	private List<Float> ratings;
	private String resp;
	private String tasklistName;
	private Long tasklistId;
	private Button shareTasklistButton;
	private static final String TAG = "ParticipantsActivity";
	private ParticipantListAdapter adapter;
	private TextView headerText;
	private Button shareButton;
	private EditText shareEmail;
	private TextView shareText;
	
	

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		session = new SessionManager(getApplicationContext());
		
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
		
		ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.group_footer_participants, lv, false);
		shareText = (TextView) footer.findViewById(R.id.shareEmailText);
		shareEmail = (EditText) footer.findViewById(R.id.shareTasklitEmailInsertField);
		shareButton = (Button) footer.findViewById(R.id.shareTasklistButton);
		lv.addFooterView(footer);
		shareButton.setOnClickListener(myhandler1);
		
		getParticipants();
	}
	
	 View.OnClickListener myhandler1 = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	if(emails.contains(shareEmail.getText().toString()))
	  	      	{
		    		shareEmail.requestFocus();
		    		shareEmail.setError("User already subscribed");
	  	      	}
	  	      	else
	  	      	{
		    	new Thread(new Runnable(){
		    		
			    	@Override
			    	public void run() {
			    		Intent i = getIntent();			    		 
			    		final Long tasklistId = i.getLongExtra("TASKLIST_ID", 0L);    		 
			    		
			    		User user = new User();
			    		List<String> userd = session.getUserDetails();
			    		user.setName(userd.get(0));
			    		user.setPassword(userd.get(1));
			    		user.setId(userd.get(2));		    		 
			    		 		    		
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
			    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));
			    	    postParameters.add(new BasicNameValuePair("email",shareEmail.getText().toString()));
			    	    postParameters.add(new BasicNameValuePair("tasklistid",tasklistId.toString()));
			    	    
			    	    String response = null;
			    	      try {
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/shareTasklist", postParameters);
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
	  	      	}
		    	
		    	try {

		    	    /** wait a second to get response from server */
		    	    Thread.sleep(1000);
		    	    
		    	    Log.v(TAG, resp.toString());
		    	    
		    	    boolean check = resp.contains("Shared");
					if (check == true) {
						Intent i = getIntent();
						tasklistName = i.getStringExtra("TASKLIST_NAME");
						tasklistId = i.getLongExtra("TASKLIST_ID", 0L);
						Intent intent2 = new Intent(getApplicationContext(), ParticipantsActivity.class);
		                intent2.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		                intent2.putExtra("TASKLIST_NAME", "Share Tasklist: " + tasklistName);
		                intent2.putExtra("TASKLIST_ID", tasklistId);
		                startActivity(intent2);
					}

					else {
						Toast.makeText(getApplicationContext(),
								"Something went wrong! Please try again!",
								Toast.LENGTH_LONG).show();
					}
		    	    } 
		    	catch (Exception e) {
		    	    	e.printStackTrace();
		    	 }
		    }
		  };
	
		 
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
		 
	// back button
	public void back(View view) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

}
