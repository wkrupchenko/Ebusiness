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
	      back.setOnClickListener(backHandler);
	      register.setOnClickListener(registerHandler);
	}
	
	View.OnClickListener backHandler = new View.OnClickListener() {
	    public void onClick(View v) {
	      back(v);
	    }
	  };
	  	  	  
	View.OnClickListener registerHandler = new View.OnClickListener() {
	    public void onClick(View v) {
	    	
	    	final String userameInput=username.getText().toString();
	        final String passwordInput=password.getText().toString();
	        final String passwordRepeatInput=passwordrepeat.getText().toString();
    	    final String emailText = email.getText().toString().trim();
  	      	final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
  	      	
  	      	if(!passwordInput.equals(passwordRepeatInput))
  	      	{
  	      		password.requestFocus();
  	      		password.setError("ENTERED PASSWORDS NOT EQUAL");
  	      	}
  	      	else if(userameInput.length()==0)
  	      	{
	  	    	username.requestFocus();
	  	    	username.setError("FIELD CANNOT BE EMPTY");
  	      	}
  	      	else if (!emailText.matches(emailPattern))
		    {
  	      		email.requestFocus();
  	      		email.setError("INVALID EMAIL");
		    }
  	      	else if(!userameInput.matches("[a-zA-Z0-9 ]+"))
  	      	{
               username.requestFocus();
               username.setError("ENTER ONLY ALPHABETICAL CHARACTER");
  	      	}
  	      	else if(passwordInput.length()==0)
  	      	{
               password.requestFocus();
               password.setError("FIELD CANNOT BE EMPTY");
  	      	}
  	      	else
  	      	{
	    		new Thread(new Runnable(){
	    		
		    	@Override
		    	public void run() {
		    		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		    		postParameters.add(new BasicNameValuePair("username",username.getText().toString()));
		    		postParameters.add(new BasicNameValuePair("email",email.getText().toString()));
		    	    postParameters.add(new BasicNameValuePair("password",password.getText().toString()));
		    	    
		    	    
		    	    String response = null;
		    	      try {
//		    	       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksService/register", postParameters);
		    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/register", postParameters);
		    	       String res = response.toString();
		    	       resp = res;
		    	}
		    	      
		    	      catch (Exception e) {
		    	          e.printStackTrace();
		    	         }
		    	    }
		    	
	    	}).start();
	    		
  	      	}
	    	
	    	try {

	    	    /** wait a second to get response from server */
	    	    Thread.sleep(1000);
	    	    /** Inside the new thread we cannot update the main thread
	    	    So updating the main thread outside the new thread */
	    	    	if (null != resp && !resp.isEmpty()) {
	    	    		 boolean check = resp.contains("Registered OK");	    	    		  
	    	    	       if (check == true) {
	    	    	    	   Toast.makeText(getApplicationContext(), "you've been successfully registered",Toast.LENGTH_LONG).show();
	    	    	    	   Intent it = new Intent(getApplicationContext(),TasklistActivity.class);
	    	    	     	 	startActivity(it);
	    	    	       } 
	    	    	}
	    	    
	    	    	else {
	    	    			Toast.makeText(getApplicationContext(), "Please try again!",Toast.LENGTH_SHORT).show();
	    	    	} 
	    	            
	    	   
	    	    } catch (Exception e) {
	    	    	e.printStackTrace();
	    	    	System.out.print(e.getMessage());
	    	    }
	    }
	  };
	public void back(View view){
		  Intent intent = new Intent(RegisterActivity.this, LogonActivity.class);
	      startActivity(intent);
	   }
}
