package de.mytasks.activities;

import java.lang.reflect.Array;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.TasklistComparator;
import de.mytasks.domain.User; 
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
	private static final String TAG = "TasklistActivity";
	private List<Tasklist> allTasklists = new ArrayList<Tasklist>();
	private ArrayAdapter<Tasklist> adapter;
	private Tasklist selectedItem;
	private PopupWindow popupWindow;
	private EditText newTasklistName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasklists);
		
		// Session class instance
        session = new SessionManager(getApplicationContext());
        
        if (!session.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(getApplicationContext(), LogonActivity.class);
			
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						 
			startActivity(i);
			finish();
		}
                		
		creatNewTask = (Button) findViewById(R.id.addTasklistButton);		 
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
        
}

	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		// Get User Information		    		 
		    		
		    		User user = new User();
		    		List<String> userd = session.getUserDetails();
		    		user.setName(userd.get(0));
		    		user.setPassword(userd.get(1));
		    		user.setId(userd.get(2));		    		 
		    		 		    		
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
		    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));
		    	    postParameters.add(new BasicNameValuePair("userid",user.getId()));
		    	    
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
	    	    Comparator<Tasklist> comp = new TasklistComparator();
	    	    Collections.sort(allTasklists, comp); 
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
		    		//Looper.prepare();
		    		User user = new User();
		    		List<String> userd = session.getUserDetails();
		    		user.setName(userd.get(0));
		    		user.setPassword(userd.get(1));
		    		user.setId(userd.get(2));
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
		    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));		    		 		    	    
		    		postParameters.add(new BasicNameValuePair("tasklistname",taskListName.getText().toString()));
		    	    postParameters.add(new BasicNameValuePair("ownerid",user.getId()));
		    	    
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
	    	    	    	   it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("Name", "Tasks from: "+ selectedItem.getName());
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
				
				User user = new User();
	    		List<String> userd = session.getUserDetails();
	    		user.setName(userd.get(0));
	    		user.setPassword(userd.get(1));
	    		user.setId(userd.get(2));	    		
	    		 
	    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
	    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));				 	    		 
	    	    postParameters.add(new BasicNameValuePair("tasklistid",tl.getId().toString()));
 	    	    
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
    	    	    	   it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
		
		Intent i = new Intent(getApplicationContext(), LogonActivity.class);		
		
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 
		getApplicationContext().startActivity(i);
		finish();			            
	}
	
	public void editTasklistName(){
		popupWindow = new PopupWindow();
		try {
			// We need to get the instance of the LayoutInflater
			LayoutInflater inflater = (LayoutInflater) TasklistActivity.this
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.screen_popup,
			(ViewGroup) findViewById(R.id.popup_element));
			popupWindow = new PopupWindow(layout, 700, 500, true);
			popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

			Button btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
			Button btnChangePopup = (Button) layout.findViewById(R.id.btn_commit_popup);
			newTasklistName = (EditText) layout.findViewById(R.id.newNameEditText);
			btnClosePopup.setOnClickListener(cancel_button_click_listener);
			btnChangePopup.setOnClickListener(change_name_button_click_listener);

			} catch (Exception e) {
			e.printStackTrace();
			}
	}
	private OnClickListener change_name_button_click_listener = new OnClickListener() {
		public void onClick(View v) {
			new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		Looper.prepare();
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		
		    		User user = new User();
		    		List<String> userd = session.getUserDetails();
		    		user.setName(userd.get(0));
		    		user.setPassword(userd.get(1));
		    		user.setId(userd.get(2));
		    		
		    		postParameters.add(new BasicNameValuePair("username",user.getName()));
		    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));
		    		postParameters.add(new BasicNameValuePair("tasklistname",newTasklistName.getText().toString()));
		    		postParameters.add(new BasicNameValuePair("archived","0"));
		    	    postParameters.add(new BasicNameValuePair("tasklistid",selectedItem.getId().toString()));
		    	    
		    	    String response = null;
		    	      try {
		    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/updateTasklist", postParameters);
		    	       String res = response.toString();
		    	       Log.v(TAG, response.toString());
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
	    	    		 boolean check = resp.contains("Tasklist updated");	    	    		  
	    	    	       if (check == true) {
	    	    	    	   Toast.makeText(getApplicationContext(), "Tasklist successfully renamed",Toast.LENGTH_LONG).show();
	    		    	    	popupWindow.dismiss();
	    		    	    	finish();
	    		    	    	startActivity(getIntent());
	    	    	       } 
	    	    	}
	    	    	
	    	    	else {
	    	    			Toast.makeText(getApplicationContext(), "something went wrong, check your connection",Toast.LENGTH_SHORT).show();
	    	    	}  
	    	    } catch (Exception e) {
	    	    	
	    	 }
		}
	};
	
	private OnClickListener cancel_button_click_listener = new OnClickListener() {
		public void onClick(View v) {
			popupWindow.dismiss();
		}
	};
	
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
        menu.setHeaderTitle("OPTIONS");
        getMenuInflater().inflate(R.menu.actions , menu);
        
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
 
        switch(item.getItemId()){
            case R.id.context_menu_edit: 
                editTasklistName();
                break;
            case R.id.context_menu_delete:
            	deleteTasklist(selectedItem);
            	Toast.makeText(this, "Delete...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.context_menu_share:
                Intent intent2 = new Intent(getApplicationContext(), ParticipantsActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent2.putExtra("TASKLIST_NAME", "Share Tasklist: " + selectedItem.getName());
                intent2.putExtra("TASKLIST_ID", selectedItem.getId());
                startActivity(intent2);
                break;
            case R.id.context_menu_rate:
            	User user = new User();
	    		List<String> userd = session.getUserDetails();
	    		user.setName(userd.get(0));
	    		user.setPassword(userd.get(1));
	    		user.setId(userd.get(2));
                Intent intent = new Intent(getApplicationContext(), RateActivity.class);
                intent.putExtra("USER_ID", user.getId());
    			intent.putExtra("TASKLIST_ID", selectedItem.getId());
    			intent.putExtra("TASKLIST_NAME", "Rate Tasklist: " + selectedItem.getName());
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