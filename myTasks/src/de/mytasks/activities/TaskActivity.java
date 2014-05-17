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
import de.mytasks.service.SimpleHttpClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

@SuppressLint("NewApi")
public class TaskActivity extends Activity {
	
	private TextView tasklistTitle;
	private EditText taskName;
	private Button creatNewTask;
	private ListView tasksOverviewWindow;
	private TextView task;
	private CheckBox tasksCheckBox;
	private String resp;
	private static final String TAG = "TaskActivity";
	private ArrayList<Task> allTasks = new ArrayList<Task>();
	private ArrayAdapter<Task> adapter;
	private Long tasklistId;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);
		tasklistTitle = (TextView) findViewById(R.id.textView1);
		taskName = (EditText) findViewById(R.id.newTasklistNameTitle);
		creatNewTask = (Button) findViewById(R.id.addTaskButton);
		tasksOverviewWindow = (ListView) findViewById(R.id.listView1);
		tasksCheckBox = (CheckBox) findViewById(R.id.checkBox1);

		
		Intent i = getIntent();
		String tasklistName = i.getStringExtra("Name");
		tasklistId = i.getLongExtra("ID", 0L);
		Log.v(TAG, tasklistId.toString());
		tasklistTitle.setText(tasklistName);
		
		creatNewTask.setOnClickListener(myhandler1);
				
		adapter = new ArrayAdapter<Task>(this,
		        android.R.layout.simple_list_item_1, allTasks);
		tasksOverviewWindow.setAdapter(adapter);
		
		getTasks();
	}
	
	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username","pipi"));
		    		postParameters.add(new BasicNameValuePair("password","qqq"));
		    		postParameters.add(new BasicNameValuePair("taskname",taskName.getText().toString()));
		    	    postParameters.add(new BasicNameValuePair("tasklistid",tasklistId.toString()));
		    	    
		    	    String response = null;
		    	      try {
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
	  
  public void getTasks() throws NullPointerException {
  	new Thread(new Runnable(){
		
    	@Override
    	public void run() {

    		
    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    		postParameters.add(new BasicNameValuePair("username","pipi"));
    		postParameters.add(new BasicNameValuePair("password","qqq"));
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
	        task.setTask_id(Long.valueOf(TaskId).longValue());
	        task.setName(TaskName);
	        task.setChecked(TaskChecked);
	        task.setTasklist(Long.valueOf(TaskTasklistId).longValue());
	        if(allTasks.contains(task) == false){
	        	allTasks.add(task);
//	        	adapter.add(task);
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

	public void showListview(View view) {
		Intent i = new Intent(getApplicationContext(), ListViewActivity.class);
		startActivity(i);
	}

	OnClickListener viewClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showPopupMenu(v);
		}

	};

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
	public void onBackPressed() {
		finish();
		return;
	}

}
