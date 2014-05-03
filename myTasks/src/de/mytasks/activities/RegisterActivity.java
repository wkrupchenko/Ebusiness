package de.mytasks.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.User;
import de.mytasks.service.SimpleHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends OrmLiteBaseActivity<DatabaseHelper>{
	
	private EditText email = null;
	private EditText password = null;
	private EditText passwordrepeat = null;
	private EditText username = null;
	private Button back;
	private Button register;
	private String resp;
	private String errorMsg;
//	private DatabaseHelper databaseHelper;
		
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_register);
	      email = (EditText) findViewById(R.id.registerViewEmailInput);
	      password = (EditText) findViewById(R.id.registerViewPasswordInput);
	      passwordrepeat = (EditText) findViewById(R.id.registerViewRepeatPasswordInput);
	      username = (EditText) findViewById(R.id.registerViewUserNameInput);
	      back = (Button)findViewById(R.id.registerViewBackButton);
	      register = (Button)findViewById(R.id.registerViewRegisterButton);
	      //back.setOnClickListener(myhandler1);
	      back.setOnClickListener( myhandler1);
	      register.setOnClickListener(test);
	}
	
	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	      back(v);
	    }
	  };
	  
	  View.OnClickListener test = new View.OnClickListener() {
		    public void onClick(View v) {
		    	Intent it = new Intent(getApplicationContext(),ListViewActivity.class);
			 	startActivity(it);
		    }
		  };
	  	  	  
	View.OnClickListener myhandler2 = new View.OnClickListener() {
	    public void onClick(View v) {
	      // it was the 2nd button
	    	
	    	new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username",username.getText().toString()));
		    		postParameters.add(new BasicNameValuePair("email",email.getText().toString()));
		    	    postParameters.add(new BasicNameValuePair("password",password.getText().toString()));
		    	    
		    	    String response = null;
		    	      try {
		    	       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksRegister/show", postParameters);
		    	       String res = response.toString();
		    	       resp = res;
		    	}
		    	      catch (Exception e) {
		    	          e.printStackTrace();
		    	          errorMsg = e.getMessage();
		    	         }
		    	} 
	    	}).start();
	    	
//	    	registerOnApp(email.getText().toString(), password.getText().toString(), username.getText().toString());
//	    	email.setText("");
//	    	password.setText("");
//	    	passwordrepeat.setText("");
//	    	username.setText("");
	    	
	    	try {

	    	    /** wait a second to get response from server */
	    	    Thread.sleep(1000);
	    	    /** Inside the new thread we cannot update the main thread
	    	    So updating the main thread outside the new thread */
	    	    	if (null != resp && !resp.isEmpty()) {
	    	    		 boolean check = resp.contains("1");	    	    		  
	    	    	       if (check == true) {
	    	    	    	   Toast.makeText(getApplicationContext(), "you've been successfully registered",Toast.LENGTH_SHORT).show();
	    	    	    	   Thread.sleep(1000);
	    	    	    	   Intent it = new Intent(getApplicationContext(),TaskActivity.class);
	    	    	     	 	startActivity(it);
	    	    	       } 
	    	    		//error.setText(resp);
	    	    	}
	    	    	
	    	    	else {
//	    	    		if (null != errorMsg && !errorMsg.isEmpty()) {
//	    	    	   	       error.setText(errorMsg);
	    	    			Toast.makeText(getApplicationContext(), "something went wrong, please try again",Toast.LENGTH_SHORT).show();
//	    	    	    	  }
	    	    	} 
	    	            
	    	   
	    	    } catch (Exception e) {
//	    	     error.setText(e.getMessage());
	    	    }
	    }
	  };
	public void back(View view){
		  Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
	      startActivity(intent);
	   }
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	   }
	
//	private void registerOnApp(String email, String password, String username){
//		
//		User newUser = new User(email, password, username);
//	
//		// get our dao
//		RuntimeExceptionDao<User, Integer> userDao = getHelper().getUserRuntimeExceptionDao();
//		userDao.create(newUser);
//		Toast.makeText(getApplicationContext(), "succesfully registered",Toast.LENGTH_SHORT).show();
//	}
	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//
//		/*
//		 * You'll need this in your class to release the helper when done.
//		 */
//		if (databaseHelper != null) {
//			databaseHelper.close();
//			databaseHelper = null;
//		}
//	}
}
