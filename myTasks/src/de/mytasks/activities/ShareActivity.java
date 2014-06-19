package de.mytasks.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.TasklistComparator;
import de.mytasks.domain.User;
import de.mytasks.service.SessionManager;
import de.mytasks.service.SimpleHttpClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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

public class ShareActivity extends Activity {

	 SessionManager session;
	
	 private Button share;
	 private Button back;
	 private TextView taskListName;
	 private EditText email;
	 private TextView shareText;
	 private static final String TAG = "ShareActivity";
	 private String resp;
	 
	 
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_share);
			taskListName = (TextView) findViewById(R.id.shareViewTasklistName);
			shareText = (TextView) findViewById(R.id.emailToShareTextView);
			email = (EditText) findViewById(R.id.shareViewEmailInput);
			back = (Button) findViewById(R.id.shareViewBackButton);
			share = (Button) findViewById(R.id.shareViewShareButton);
			share.setOnClickListener(myhandler1);
			
			session = new SessionManager(getApplicationContext());
			
		}
	 
	 View.OnClickListener myhandler1 = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
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
			    	    postParameters.add(new BasicNameValuePair("email",email.getText().toString()));
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
		    	
		    	try {

		    	    /** wait a second to get response from server */
		    	    Thread.sleep(1000);
		    	    
		    	    Log.v(TAG, resp.toString());
		    	    
		    	    boolean check = resp.contains("Shared");
					if (check != true) {
						
						JSONObject jsonObject = new JSONObject(resp);
						 
						Log.v(User.class.getName(), jsonObject.getString("id"));
						Log.v(User.class.getName(), jsonObject.getString("email"));
						Log.v(User.class.getName(), jsonObject.getString("name"));
 
						 
												
						Intent it = new Intent(getApplicationContext(),
								TasklistActivity.class);
						//it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(it);
						finish();
					}

					else {
						Toast.makeText(getApplicationContext(),
								"Wrong credential! Please try again!",
								Toast.LENGTH_LONG).show();
					}
		    	    } 
		    	catch (Exception e) {
		    	    	e.printStackTrace();
		    	 }
		    }
		  };
	 
	 	 
	  

	} 
