package de.mytasks.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.mytasks.R;
import de.mytasks.domain.Tasklist;
import de.mytasks.service.SessionManager;
import de.mytasks.service.SimpleHttpClient;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class RateActivity extends Activity {
	private RatingBar ratingBar;
	private Button rate;
	private static final String TAG = "RateActivity";
	private String userId;
	private Long tasklistId;
	private String resp;
	
	SessionManager session;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate);

		
		session = new SessionManager(getApplicationContext());

		rate = (Button) findViewById(R.id.ratingViewRateButton);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		
		Intent i = getIntent();
		userId = i.getStringExtra("USER_ID");
		tasklistId = i.getLongExtra("TASKLIST_ID", 0L);
		
		rate.setOnClickListener(myhandler1);
		}
	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	rateTasklist();
	    }
	};
	
	public void rateTasklist(){
		final Float numStars = ratingBar.getRating();
		final String stars = String.valueOf(numStars);
		final String stars2 = stars.replace('.', ',');
		Log.v(TAG, stars);
		Log.v(TAG, userId);
		Log.v(TAG, String.valueOf(tasklistId));
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("rate", stars2));
				postParameters.add(new BasicNameValuePair("userid",userId));
	     	    postParameters.add(new BasicNameValuePair("tasklistid",String.valueOf(tasklistId)));
	    	    

	    	    
	    	    String response = null;
	    	      try {
	    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/rate", postParameters);
	    	       String res = response.toString();
	    	       Log.v(TAG, response.toString());
//	    	       Log.v(TAG, res.toString());
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
    	    		 boolean check = resp.contains("Rated");	    	    		  
    	    	       if (check == true) {
    	    	    	   Toast.makeText(getApplicationContext(), "successfully rated",Toast.LENGTH_LONG).show();
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

}
