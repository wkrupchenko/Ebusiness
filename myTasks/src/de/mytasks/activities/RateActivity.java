package de.mytasks.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	private RatingBar currentRating;
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
		currentRating = (RatingBar) findViewById(R.id.currentRatingBar);
		
		Intent i = getIntent();
		userId = i.getStringExtra("USER_ID");
		tasklistId = i.getLongExtra("TASKLIST_ID", 0L);
		
		rate.setOnClickListener(myhandler1);
		getCurrentRating();
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
//    	    	    	   Intent it = new Intent(getApplicationContext(),RateActivity.class);
//    	    	    	   startActivity(it);
    	    	    	   finish();
    	    	    	   startActivity(getIntent());
    	    	       } 
    	    	}
    	    	
    	    	else {
    	    			Toast.makeText(getApplicationContext(), "something went wrong, check your connection",Toast.LENGTH_SHORT).show();
    	    	} 
    	            
    	   
    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	 }
	}
	
	public void getCurrentRating(){
		Log.v(TAG, String.valueOf(tasklistId));
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	     	    postParameters.add(new BasicNameValuePair("tasklistid",String.valueOf(tasklistId)));
	    	    
	    	    String response = null;
	    	      try {
	    	       response = SimpleHttpClient.executeHttpPost("http://www.iwi.hs-karlsruhe.de/eb03/getCurrentRating", postParameters);
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
    	    
    	    Gson gson = new Gson();
    	    JsonElement jelement = new JsonParser().parse(resp);
    	    JsonObject  jobject = jelement.getAsJsonObject();
    	    jobject = jobject.getAsJsonObject("data");
    	    JsonArray jarray = jobject.getAsJsonArray("rating");
    	    jobject = jarray.get(0).getAsJsonObject();
    	    String rating = jobject.get("ratingAverage").toString();
    	    
    	    currentRating.setRating(Float.valueOf(rating));
    	            
    	    String jsonLine = "{data: {translations: [{translatedText: }]}}";
    	   
    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	 }

	}
}
