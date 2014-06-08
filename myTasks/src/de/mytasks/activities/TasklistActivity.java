package de.mytasks.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;
import de.mytasks.service.GetCurrentUserInformation;
import de.mytasks.service.SessionManager;
import de.mytasks.service.SimpleHttpClient;

public class TasklistActivity extends OrmLiteBaseActivity<DatabaseHelper> {
	
	// Session Manager Class
    SessionManager session;

	private EditText taskListName;
	private Button creatNewTask;
	private Button settings;
	private Button update;
	private MenuItem delete;
	private ListView tasklistOverviewWindow;
	private String resp;
//	private ArrayList<String> list = new ArrayList<String>();
	private static final String TAG = "TasklistActivity";
	private List<Tasklist> allTasklists = new ArrayList<Tasklist>();
	private ArrayAdapter<Tasklist> adapter;
	private Tasklist selectedItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasklists);
		
		// Session class instance
        session = new SessionManager(getApplicationContext());
		
		creatNewTask = (Button) findViewById(R.id.addTaskButton);
		settings = (Button) findViewById(R.id.shareButton);
		update = (Button) findViewById(R.id.updateButton);
		delete = (MenuItem) findViewById(R.id.context_menu_delete);
		taskListName = (EditText) findViewById(R.id.newTasklistNameTitle);
		tasklistOverviewWindow = (ListView) findViewById(R.id.tasklistOverviewWindow);
		update.setOnClickListener(myhandler1);
		creatNewTask.setOnClickListener(myhandler2);
		tasklistOverviewWindow.setOnItemClickListener(listHandler);
		tasklistOverviewWindow.setOnItemLongClickListener(deleteHandler);
//		delete.setOnMenuItemClickListener(menuItemClickListener);
		tasklistOverviewWindow.setLongClickable(true);
		
		adapter = new ArrayAdapter<Tasklist>(this,
				android.R.layout.simple_list_item_1, allTasklists);

		tasklistOverviewWindow.setAdapter(adapter);
		registerForContextMenu(tasklistOverviewWindow);
		
		update.callOnClick();
		
		/**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();
         
        // get user data from session
//        List<String> user = session.getUserDetails();
         
        // name an 1. Position in der Liste
//        String name = user.get(0);
        
     // For Testing we display in the EditText the user data
//        taskListName.setText(name);
	
	}

	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		// Get User Information via GetCurrentUserInformation Class
		    		// which gives the user the following Information:
		    		// name, email and ID
		    		// Maybe this should be done out of the thread...
//		    		User user = new User();
//		    		GetCurrentUserInformation ui = new GetCurrentUserInformation();
//		    		user = ui.getUserInformation();
		    		 		    		
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username","pipi"));
//		    		postParameters.add(new BasicNameValuePair("username",user.getName()));
		    		postParameters.add(new BasicNameValuePair("password","qqq"));
		    	    postParameters.add(new BasicNameValuePair("userid","14"));
		    	    
		    	    String response = null;
		    	      try {
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
	    	        if(allTasklists.contains(tasklist) == false){
		    	        allTasklists.add(tasklist);
//		    	        list.add(tasklist.getName());
	    	        }
	    	        Log.i(Tasklist.class.getName(), jsonObject.getString("name"));
	    	      }  
	    	    tasklistOverviewWindow.setAdapter(adapter);
	    	    } 
	    	catch (Exception e) {
	    	    	e.printStackTrace();
	    	 }
	    }
	  };

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

	ListView.OnItemClickListener listHandler = new ListView.OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedItem = (Tasklist) tasklistOverviewWindow.getItemAtPosition(arg2);
			Log.v(TAG, selectedItem.getId().toString());
			Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
//			intent.putExtra("Name", "Taskliste: "+ tasklistOverviewWindow.getAdapter().getItem(arg2).toString());
			intent.putExtra("Name", "Taskliste: "+ selectedItem.getName());
			intent.putExtra("ID", selectedItem.getId());
			startActivity(intent);	
		}
	  };
	  
	  
	ListView.OnItemLongClickListener deleteHandler = new ListView.OnItemLongClickListener(){
		  
		  @Override
	      public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long row_id) {
			selectedItem = (Tasklist) tasklistOverviewWindow.getItemAtPosition(position);
			Log.v(TAG, selectedItem.getId().toString());
			return false;
	        }
	  };
	  
	public void deleteTasklist(final Tasklist tl){
		new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	    		postParameters.add(new BasicNameValuePair("username","pipi"));
	    		postParameters.add(new BasicNameValuePair("password","qqq"));
	    	    postParameters.add(new BasicNameValuePair("tasklistid",tl.getId().toString()));
//	    	    postParameters.add(new BasicNameValuePair("tasklistid","200004"));

	    	    
	    	    String response = null;
	    	      try {
	    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/deleteTasklist", postParameters);
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
    	    		 boolean check = resp.contains("OK");	    	    		  
    	    	       if (check == true) {
    	    	    	   Toast.makeText(getApplicationContext(), "Tasklist deleted",Toast.LENGTH_LONG).show();
    	    	    	   Intent it = new Intent(getApplicationContext(),TasklistActivity.class);
    	    	    	   startActivity(it);
    	    	       } 
    	    	}
    	    	
    	    	else {
    	    			Toast.makeText(getApplicationContext(), "something went wrong, check your connection",Toast.LENGTH_SHORT).show();
    	    	} 
    	            
    	   
    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	 }
	}
	  
	public void logout(){
		// Clear the session data
        // This will clear all session data and 
        // redirect user to LoginActivity
		 
		session.logoutUser(); 
			            
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tasklist_group, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.logout:
	            logout();	            
	            return true;
	        case R.id.tasklists_update:
	        	update.callOnClick();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("TEST");
        getMenuInflater().inflate(R.menu.actions , menu);
        
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
 
        switch(item.getItemId()){
            case R.id.context_menu_edit:
                Toast.makeText(this, "Edit...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_menu_delete:
            	deleteTasklist(selectedItem);
            	Toast.makeText(this, "Delete...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_menu_share:
                Toast.makeText(this, "Share...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_menu_rate:
                Toast.makeText(this, "Rate...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                intent.putExtra("USER_ID", "14");
    			intent.putExtra("TASKLIST_ID", selectedItem.getId());
    			startActivity(intent);	
                break;
 
        }
        return true;
    }
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}