package de.mytasks.activities;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;
import de.mytasks.service.SessionManager;
import de.mytasks.service.SimpleHttpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

@SuppressLint("NewApi")
public class TaskActivity extends Activity {
	
	private TextView tasklistTitle;
	private EditText taskName;
	private Button creatNewTask;
	private Button update;
	private ListView tasksOverviewWindow;
	private TextView task;
	private CheckBox tasksCheckBox;
	private String resp;
	private static final String TAG = "TaskActivity";
	private ArrayList<Task> allTasks = new ArrayList<Task>();
	private ArrayAdapter<Task> adapter;
	private Long tasklistId;
	private Task selectedItem;
	
	SessionManager session; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		
		session = new SessionManager(getApplicationContext());		
		tasklistTitle = (TextView) findViewById(R.id.ratingViewCurrentRatingTextView);
		taskName = (EditText) findViewById(R.id.newTasklistNameTitle);
		creatNewTask = (Button) findViewById(R.id.addTaskButton);
		tasksOverviewWindow = (ListView) findViewById(R.id.listView1);
		tasksCheckBox = (CheckBox) findViewById(R.id.check);
		update = (Button) findViewById(R.id.updateButton);
		
		tasksOverviewWindow.setOnItemClickListener(listHandler);
		tasksOverviewWindow.setOnItemLongClickListener(deleteHandler);
		tasksOverviewWindow.setClickable(true);
		tasksOverviewWindow.setLongClickable(true);
		registerForContextMenu(tasksOverviewWindow);

		Intent i = getIntent();
		String tasklistName = i.getStringExtra("Name");
		tasklistId = i.getLongExtra("ID", 0L);
		Log.v(TAG, tasklistId.toString());
		tasklistTitle.setText(tasklistName);
		
		creatNewTask.setOnClickListener(myhandler1);
		update.setOnClickListener(updateHandler);
		
		adapter = new ArrayAdapter<Task>(this,
		        android.R.layout.simple_list_item_multiple_choice, allTasks);
		
		tasksOverviewWindow.setAdapter(adapter);
		tasksOverviewWindow.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		getTasks();
	}
	
	View.OnClickListener updateHandler = new View.OnClickListener() {
		 public void onClick(View v) {
		    	
		    	new Thread(new Runnable(){
		    		
			    	@Override
			    	public void run() {
			    		
			    		User user = new User();
			    		List<String> userd = session.getUserDetails();
			    		user.setName(userd.get(0));
			    		user.setPassword(userd.get(1));
			    		user.setId(userd.get(2));
			    		
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
			    		postParameters.add(new BasicNameValuePair("password",user.getPassword())); 
			    	    postParameters.add(new BasicNameValuePair("tasklistid",tasklistId.toString()));
			    	    
			    	    String response = null;
			    	      try {
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/getTasks", postParameters);
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
		    	    
		    	    Log.v(TAG, resp.toString());
		    	    
		    	    JSONArray jsonArray = new JSONArray(resp);
		    	    
		    	    
		    	    for (int i = 0; i < jsonArray.length(); i++) {
		    	        JSONObject jsonObject = jsonArray.getJSONObject(i);
		    	        Task task = new Task();
		    	        String TaskId = jsonObject.getString("id");
		    	        String TaskName = jsonObject.getString("name");
		    	        Integer TaskChecked = jsonObject.getInt("checked");
		    	        String TaskTasklistId = jsonObject.getString("tasklist");
		    	        Double Latitude = jsonObject.getDouble("latitude");
		    	        Double Longitude = jsonObject.getDouble("longitude");
		    	        task.setTask_id(Long.valueOf(TaskId).longValue());
		    	        task.setName(TaskName);
		    	        task.setChecked(TaskChecked);
		    	        task.setTasklist(Long.valueOf(TaskTasklistId).longValue());
		    	        task.setLatitude(Latitude);
		    	        task.setLongitude(Longitude);
		    	        if(allTasks.contains(task) == false){
		    	        	allTasks.add(task);
		    	        }
		    	        for (int a = 0; a < allTasks.size(); a++) {
		    	        	if(allTasks.get(a).getChecked()==1){
		            			tasksOverviewWindow.setItemChecked(a, true);
		    	        	}
		    	        }
		    	        
		    	        Log.i(Task.class.getName(), jsonObject.getString("name"));
		    	      }	    
		    	 } 
		    	catch (Exception e) {
		    	    	e.printStackTrace();
		    	 }
		    }
	};
	
	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		
		    		User user = new User();
		    		List<String> userd = session.getUserDetails();
		    		user.setName(userd.get(0));
		    		user.setPassword(userd.get(1));
		    		user.setId(userd.get(2));
		    		
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
		    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));
		    		postParameters.add(new BasicNameValuePair("taskname",taskName.getText().toString()));
		    	    postParameters.add(new BasicNameValuePair("tasklistid",tasklistId.toString()));
		    	    
		    	    String response = null;
		    	      try {
//		    	    	  response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksMainPage/createTask", postParameters);
		    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/createTask", postParameters);
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
	    	    		 boolean check = resp.contains("Created");	    	    		  
	    	    	       if (check == true) {
	    	    	    	   Toast.makeText(getApplicationContext(), "New Tasklist successfully created",Toast.LENGTH_LONG).show();
//	    	    	    	   Intent it = new Intent(getApplicationContext(),TaskActivity.class);
	    	    	     	   //it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//	    	    	     	   startActivity(it);
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
	  
  public void getTasks() throws NullPointerException {
  	new Thread(new Runnable(){
		
    	@Override
    	public void run() {
    		
    		User user = new User();
    		List<String> userd = session.getUserDetails();
    		user.setName(userd.get(0));
    		user.setPassword(userd.get(1));
    		user.setId(userd.get(2));
    		
    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
    		postParameters.add(new BasicNameValuePair("password",user.getPassword())); 
    	    postParameters.add(new BasicNameValuePair("tasklistid",tasklistId.toString()));
    	    
    	    String response = null;
    	      try {
    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/getTasks", postParameters);
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
	        Task task = new Task();
	        String TaskId = jsonObject.getString("id");
	        String TaskName = jsonObject.getString("name");
	        Integer TaskChecked = jsonObject.getInt("checked");
	        String TaskTasklistId = jsonObject.getString("tasklist");
	        Double Latitude = jsonObject.getDouble("latitude");
	        Double Longitude = jsonObject.getDouble("longitude");
	        task.setTask_id(Long.valueOf(TaskId).longValue());
	        task.setName(TaskName);
	        task.setChecked(TaskChecked);
	        task.setTasklist(Long.valueOf(TaskTasklistId).longValue());
	        task.setLatitude(Latitude);
	        task.setLongitude(Longitude);
	        if(allTasks.contains(task) == false){
	        	allTasks.add(task);
//	        	adapter.add(task);
	        }
	        for (int a = 0; a < allTasks.size(); a++) {
	        	if(allTasks.get(a).getChecked()==1){
        			tasksOverviewWindow.setItemChecked(a, true);
	        	}
	        }
	        
	        Log.i(Task.class.getName(), jsonObject.getString("name"));
	      }	    
	 } 
	catch (Exception e) {
	    	e.printStackTrace();
	 }

		}

	public void showTask(View view) throws NullPointerException {
		try {

		}

		catch (Exception e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(e.getMessage());
			AlertDialog alert = builder.create();
			alert.show();
		}

	}
	
	ListView.OnItemClickListener listHandler = new ListView.OnItemClickListener(){
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			
			selectedItem = (Task) tasksOverviewWindow.getItemAtPosition(arg2);
			
			SparseBooleanArray checked=tasksOverviewWindow.getCheckedItemPositions();
			if(checked.get(arg2)){
				Toast.makeText(getApplicationContext(), "Selected", Toast.LENGTH_SHORT).show();
				final Long value = (long) 1;
				new Thread(new Runnable(){
		    		
			    	@Override
			    	public void run() {
			    		
			    		User user = new User();
			    		List<String> userd = session.getUserDetails();
			    		user.setName(userd.get(0));
			    		user.setPassword(userd.get(1));
			    		user.setId(userd.get(2));
			    		
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
			    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));			    		    		
			    		postParameters.add(new BasicNameValuePair("taskname",selectedItem.getName()));
			    		postParameters.add(new BasicNameValuePair("taskcheckbox",value.toString()));
			    	    postParameters.add(new BasicNameValuePair("taskid",selectedItem.getTask_id().toString()));
			    	    
			    	    String response = null;
			    	      try {
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/updateTask", postParameters);
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
		    	    		 boolean check = resp.contains("Task updated");	    	    		  
		    	    	       if (check == true) {
		    	    	    	   Toast.makeText(getApplicationContext(), "Task updated",Toast.LENGTH_LONG).show();
		    	    	       } 
		    	    	}
		    	    	
		    	    	else {
		    	    			Toast.makeText(getApplicationContext(), "Failure",Toast.LENGTH_SHORT).show();
		    	    	} 
		    	            
		    	   
		    	    } catch (Exception e) {
		    	    	
		    	 }
			}else {
	            Toast.makeText(getApplicationContext(), "Not selected", Toast.LENGTH_SHORT).show();
	            final Long value = (long) 0;
				new Thread(new Runnable(){
		    		
			    	@Override
			    	public void run() {
			    		
			    		User user = new User();
			    		List<String> userd = session.getUserDetails();
			    		user.setName(userd.get(0));
			    		user.setPassword(userd.get(1));
			    		user.setId(userd.get(2));
			    		
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    		postParameters.add(new BasicNameValuePair("username",user.getName())); 
			    		postParameters.add(new BasicNameValuePair("password",user.getPassword()));			    		 
			    		postParameters.add(new BasicNameValuePair("taskname",selectedItem.getName()));
			    		postParameters.add(new BasicNameValuePair("taskcheckbox",value.toString()));
			    	    postParameters.add(new BasicNameValuePair("taskid",selectedItem.getTask_id().toString()));
			    	    
			    	    String response = null;
			    	      try {
//			    	    	  response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksMainPage/updateTask", postParameters);
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/updateTask", postParameters);
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
		    	    		 boolean check = resp.contains("Task updated");	    	    		  
		    	    	       if (check == true) {
		    	    	    	   Toast.makeText(getApplicationContext(), "Task updated",Toast.LENGTH_LONG).show();
		    	    	       } 
		    	    	}
		    	    	
		    	    	else {
		    	    			Toast.makeText(getApplicationContext(), "Failure",Toast.LENGTH_SHORT).show();
		    	    	} 
		    	            
		    	   
		    	    } catch (Exception e) {
		    	    	
		    	 }
	        }
		}
	  };
	  
		ListView.OnItemLongClickListener deleteHandler = new ListView.OnItemLongClickListener(){
			  
			  @Override
		      public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long row_id) {
				selectedItem = (Task) tasksOverviewWindow.getItemAtPosition(position);
				Log.v(TAG, selectedItem.getTask_id().toString());
				Log.v(TAG, selectedItem.getLatitude().toString());
				Log.v(TAG, selectedItem.getLongitude().toString());
				return false;
		        }
		  };
		  
		  public void deleteMapInformation(final Long id) throws NullPointerException {
			  	new Thread(new Runnable(){
					
			    	@Override
			    	public void run() {

			    		
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    	    postParameters.add(new BasicNameValuePair("taskid", id.toString()));
			    	    
			    	    String response = null;
			    	      try {
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/removeGeodata", postParameters);
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
		    	    		 boolean check = resp.contains("Geodata deleted");	    	    		  
		    	    	       if (check == true) {
		    	    	    	   Toast.makeText(getApplicationContext(), "Map Information succesfully removed",Toast.LENGTH_LONG).show();
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

		  public void deleteTask(final Long id) throws NullPointerException {
			  	new Thread(new Runnable(){
					
			    	@Override
			    	public void run() {

			    		
			    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			    	    postParameters.add(new BasicNameValuePair("taskid", id.toString()));
			    	    
			    	    String response = null;
			    	      try {
			    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/deleteTask", postParameters);
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
		    	    Thread.sleep(2000);
		    	    /** Inside the new thread we cannot update the main thread
		    	    So updating the main thread outside the new thread */
		    	    	if (null != resp && !resp.isEmpty()) {
		    	    		 boolean check = resp.contains("OK");	    	    		  
		    	    	       if (check == true) {
		    	    	    	   //Intent it = new Intent(getApplicationContext(),TaskActivity.class);
		    	    	     	   //it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		    	    	    	   
		    	    	     	   startActivity(getIntent());
		    	    	     	   finish();
		    	    	    	   //Toast.makeText(getApplicationContext(), "Task deleted",Toast.LENGTH_LONG).show();
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
	
	private void updateTaskActivityView(){
	   Intent it = new Intent(getApplicationContext(),TaskActivity.class);
 	   it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 	   startActivity(it);    	 
	}
	
	private void showPopupMenu(View v) {
		PopupMenu popupMenu = new PopupMenu(TaskActivity.this, v);
		popupMenu.getMenuInflater().inflate(R.menu.main_popup,
				popupMenu.getMenu());

		popupMenu
				.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(TaskActivity.this, item.toString(),
								Toast.LENGTH_LONG).show();
						return true;
					}
				});

		popupMenu.show();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Map Information");
        getMenuInflater().inflate(R.menu.map_actions , menu);
        
    }
	@Override
    public boolean onContextItemSelected(MenuItem item) {
 
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
 
        switch(item.getItemId()){
            case R.id.context_menu_add_map_info:
                Toast.makeText(this, "Loading Map...", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(getApplicationContext(), AddMapActivity.class);
                it.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    			it.putExtra("TaskId", selectedItem.getTask_id());
    			it.putExtra("TaskName", selectedItem.getName());
    			it.putExtra("Latitude", selectedItem.getLatitude());
    			it.putExtra("Longitude", selectedItem.getLongitude());
				startActivity(it);
                break;
            case R.id.context_menu_show_map_info:
//            	deleteTasklist(selectedItem);
            	Toast.makeText(this, "Loading Map...", Toast.LENGTH_SHORT).show();
            	if(selectedItem.getLatitude() !=0 && selectedItem.getLongitude()!=0){
		        	Intent it2 = new Intent(getApplicationContext(), ShowMapActivity.class);
		        	it2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					it2.putExtra("TaskId", selectedItem.getTask_id());
					it2.putExtra("TaskName", selectedItem.getName());
					it2.putExtra("Latitude", selectedItem.getLatitude());
					it2.putExtra("Longitude", selectedItem.getLongitude());
					startActivity(it2);
            	}
            	else{
            		Toast.makeText(this, "No Map Information, please Add Map Information", Toast.LENGTH_LONG).show();
            	}
                break;
            case R.id.context_menu_delete_map_info:
                Toast.makeText(this, "Removing Map Information...", Toast.LENGTH_SHORT).show();
                deleteMapInformation(selectedItem.getTask_id());
                selectedItem.setLatitude(0.0);
                selectedItem.setLongitude(0.0);
//                getTasks();
                break;
            case R.id.context_menu_delete_task:
                deleteTask(selectedItem.getTask_id());
                break;
 
        }
        return true;
    }

	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(), TasklistActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

}
