package de.mytasks.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Tasklist;
import de.mytasks.service.SimpleHttpClient;

public class TasklistActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText taskListName;
	private Button creatNewTask;
	private Button settings;
	private Button update;
	private DatabaseHelper databaseHelper;
	private ListView tasklistOverviewWindow;
	private String resp;
	private ArrayList<String> list = new ArrayList<String>();
	private static final String TAG = "TasklistActivity";
	private List<Tasklist> allTasklists = new ArrayList<Tasklist>();
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasklists);
		creatNewTask = (Button) findViewById(R.id.addTaskButton);
		settings = (Button) findViewById(R.id.shareButton);
		update = (Button) findViewById(R.id.updateButton);
		taskListName = (EditText) findViewById(R.id.newTasklistNameTitle);
		tasklistOverviewWindow = (ListView) findViewById(R.id.tasklistOverviewWindow);
		update.setOnClickListener(myhandler1);
		creatNewTask.setOnClickListener(myhandler2);
		
//		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, list);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list);

		tasklistOverviewWindow.setAdapter(adapter);
		list.add("Test");
	
	}
	
	View.OnClickListener myhandler2 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username","pipi"));
		    		postParameters.add(new BasicNameValuePair("password","qqq"));
		    		postParameters.add(new BasicNameValuePair("tasklistname",taskListName.getText().toString()));
		    	    postParameters.add(new BasicNameValuePair("ownerid","14"));
		    	    
		    	    String response = null;
		    	      try {
//		    	       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksRegister/show", postParameters);
//		    	       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksService/register", postParameters);
//		    	       response = SimpleHttpClient.executeHttpPost("http://iwi-w-eb03:8080/mytasksService/register", postParameters);
		    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/createTasklist", postParameters);
		    	       String res = response.toString();
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
	    	    		 boolean check = resp.contains("Created");	    	    		  
	    	    	       if (check == true) {
	    	    	    	   Toast.makeText(getApplicationContext(), "New Tasklist successfully created",Toast.LENGTH_LONG).show();
	    	    	    	   Intent it = new Intent(getApplicationContext(),TasklistActivity.class);
	    	    	    	   startActivity(it);
	    	    	       } 
	    	    	}
	    	    	
	    	    	else {
	    	    			Toast.makeText(getApplicationContext(), "something went wrong, check your connection",Toast.LENGTH_SHORT).show();
	    	    	} 
	    	            
	    	   
	    	    } catch (Exception e) {
	    	    	
	    	 }
	    }
	  };
	  
	  View.OnClickListener myhandler1 = new View.OnClickListener() {
		    public void onClick(View v) {
		    	
		    	new Thread(new Runnable(){
		    		
			    	@Override
			    	public void run() {
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    		postParameters.add(new BasicNameValuePair("username","pipi"));
			    		postParameters.add(new BasicNameValuePair("password","qqq"));
			    	    postParameters.add(new BasicNameValuePair("userid","14"));
			    	    
			    	    String response = null;
			    	      try {
//			    	       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksRegister/show", postParameters);
//			    	       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksService/register", postParameters);
//			    	       response = SimpleHttpClient.executeHttpPost("http://iwi-w-eb03:8080/mytasksService/register", postParameters);
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/getTasklist", postParameters);
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
		    	    
		    	    Gson gson = new Gson();
		    	    
		    	    JSONArray jsonArray = new JSONArray(resp);
		    	    
		    	    
		    	    for (int i = 0; i < jsonArray.length(); i++) {
		    	        JSONObject jsonObject = jsonArray.getJSONObject(i);
		    	        Tasklist tasklist = new Tasklist();
		    	        String TasklistId = jsonObject.getString("id");
		    	        String TasklistName = jsonObject.getString("name");
		    	        String TasklistOwner = jsonObject.getString("ownerId");
		    	        tasklist.setId(Long.valueOf(TasklistId).longValue());
		    	        tasklist.setName(TasklistName);
		    	        tasklist.setOwnerId(Long.valueOf(TasklistOwner).longValue());
		    	        allTasklists.add(tasklist);
		    	        Log.i(Tasklist.class.getName(), jsonObject.getString("name"));
		    	      }
		    	    
		    	    for(Tasklist t : allTasklists){
		    	    	list.add(t.getName());
		    	    }
		    	    
		    	    
		    	    tasklistOverviewWindow.setAdapter(adapter);
		    	    
		    	    
//		    	    JSONObject responseObj = null;
//					Log.v(TAG, "JSON Object NULL");
//		    	    /** Inside the new thread we cannot update the main thread
//		    	    So updating the main thread outside the new thread */
//		    	   
//		    	    		
//					// parse and display JSON response
//    	    		// create JSON object from JSON string
//					responseObj = new JSONObject(resp);
//					Log.v(TAG, "JSON Object Not NULL");
//					
//					boolean success = responseObj.getBoolean("success");
//					String s = new Boolean(success).toString();
//					Log.v(TAG, s);
//					if (success) {
//						
//						String tasklistInfo = responseObj
//								.getString("TasklistInfo");
//						Log.v(TAG, tasklistInfo);
//					
//						// create java object from the JSON object
//						Tasklist tasklist = gson.fromJson(tasklistInfo,
//								Tasklist.class);
//						Log.v(TAG, tasklist.toString());
//						
//						String name;
//						name = tasklist.getName();
//						list.add(name);
//					}
//		    	    	
//		    	    	else {
//		    	    		Log.v(TAG, "leider nicht geklappt");
//							list.add("leider nicht geklappt");
//		    	    	} 
		    	    } 
		    	catch (Exception e) {
		    	    	e.printStackTrace();
		    	 }
		    }
		  };


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tasklist_group, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null) {
			databaseHelper.close();
			databaseHelper = null;
		}
	}
}