package de.mytasks.activities;
 
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.mytasks.R;
import de.mytasks.service.SimpleHttpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class LogonActivity extends Activity {
private EditText username = null;
private EditText password = null;
private Button login;
private String resp;
private String errorMsg;
TextView error;

 /** Called when the activity is first created. */
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_login);
  username = (EditText) findViewById(R.id.loginViewUsernameInput);
  password = (EditText) findViewById(R.id.loginViewPasswordInput);
  login = (Button) findViewById(R.id.loginViewLoginButton);
  error = (TextView) findViewById(R.id.error);
     

  login.setOnClickListener(new View.OnClickListener() {

   @Override
   public void onClick(View v) {
    /** According with the new StrictGuard policy,  running long tasks on the Main UI thread is not possible
    So creating new thread to create and execute http operations */
    new Thread(new Runnable() {

     @Override
     public void run() {
      ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
      postParameters.add(new BasicNameValuePair("username",username.getText().toString()));
      postParameters.add(new BasicNameValuePair("password",password.getText().toString()));

      String response = null;
      try {
       response = SimpleHttpClient.executeHttpPost("http://10.0.2.2:8080/mytasksLogin/show", postParameters);
       String res = response.toString();
       resp = res;
       /*
       boolean check = resp.contains("authentificated");
       if (check == true) {
    	   Intent it = new Intent(getApplicationContext(),TaskActivity.class);
     	 	startActivity(it);
       } */ 
             

      } catch (Exception e) {
       e.printStackTrace();
       errorMsg = e.getMessage();
      }
     }

    }).start();
    try {

    /** wait a second to get response from server */
    Thread.sleep(1000);
    /** Inside the new thread we cannot update the main thread
    So updating the main thread outside the new thread */
    boolean check = resp.contains("authentificated");
    	if (check == true) {
    		   		
    	    	   Intent it = new Intent(getApplicationContext(),TaskActivity.class);
    	     	 	startActivity(it);
    	     
    		//error.setText(resp);
    	}
    	
    	else {
    		//if (null != errorMsg && !errorMsg.isEmpty()) {
    	   	       error.setText("Wrong credential! Please try again!");
    	    	 // }
    	} 
            
   
    } catch (Exception e) {
     error.setText(e.getMessage());
    }
   }
  });
 }
}

