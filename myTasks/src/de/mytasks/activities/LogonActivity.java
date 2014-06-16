package de.mytasks.activities;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import de.mytasks.R;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;
import de.mytasks.service.SessionManager;
import de.mytasks.service.SimpleHttpClient;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
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
	private TextView register;
	private String resp;
	private Boolean connected;
	private String errorMsg;
	private static final String TAG = "LogonActivity";
//	private User user;
	TextView error;

	// Session Manager Class
	SessionManager session;

	private boolean connectionAvailable() {
	    boolean connected = false;
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
	            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
	        //we are connected to a network
	        connected = true;
	    }
	    return connected;
	}
	
	private boolean serviceAvailable() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				 
				try {
					
					String url = "http://www.iwi.hs-karlsruhe.de/eb03/";					 
					connected = SimpleHttpClient.executePing(url);

				} catch (Exception e) {
					e.printStackTrace();
					errorMsg = e.getMessage();
				}
			}

		}).start();
		
		try {
			 
			Thread.sleep(1000);			
			 
			} catch (Exception e) {
			e.printStackTrace();
			error.setText(e.getMessage());
		}
		
		if (connected == true) {
			return true;
		}
		
		else {
			return false;
		}
	}	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		error = (TextView) findViewById(R.id.error);

		// Session Manager
		session = new SessionManager(getApplicationContext());
		
		//System.out.println(connectionAvailable());
		
		if (session.isLoggedIn()==true && connectionAvailable() == true) {
			Intent it = new Intent(getApplicationContext(),
					TasklistActivity.class);
			//it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(it);
			finish();
		}
		
		/*else if (session.isLoggedIn()==true && connectionAvailable() == false) {
			error.setText("Service temporarily unavailable... Please try again later");
			 
		}
		*/
		
		else {		

		username = (EditText) findViewById(R.id.loginViewUsernameInput);
		password = (EditText) findViewById(R.id.loginViewPasswordInput);
		login = (Button) findViewById(R.id.loginViewLoginButton);
		register = (TextView) findViewById(R.id.loginViewRegisterButton);
		//error = (TextView) findViewById(R.id.error);

		// Toast.makeText(getApplicationContext(), "User Login Status: " +
		// session.isLoggedIn(), Toast.LENGTH_LONG).show();

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * According with the new StrictGuard policy, running long tasks
				 * on the Main UI thread is not possible So creating new thread
				 * to create and execute http operations
				 */
				new Thread(new Runnable() {

					@Override
					public void run() {
						ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
						postParameters.add(new BasicNameValuePair("username",
								username.getText().toString()));
						postParameters.add(new BasicNameValuePair("password",
								password.getText().toString()));

						String response = null;
						try {
							 
							response = SimpleHttpClient
									.executeHttpPost(
											"http://www.iwi.hs-karlsruhe.de/eb03/login",
											postParameters);
							String res = response.toString();
							resp = res;

						} catch (Exception e) {
							e.printStackTrace();
							errorMsg = e.getMessage();
						}
					}

				}).start();
				try {

					/** wait a second to get response from server */
					Thread.sleep(1000);
					/**
					 * Inside the new thread we cannot update the main thread So
					 * updating the main thread outside the new thread
					 */

					boolean check = resp.contains("Error");
					if (check != true) {
						
						JSONObject jsonObject = new JSONObject(resp);
						 
						Log.v(User.class.getName(), jsonObject.getString("id"));
						Log.v(User.class.getName(), jsonObject.getString("email"));
						Log.v(User.class.getName(), jsonObject.getString("name"));
 
						session.createLoginSession(jsonObject.getString("id"),
								jsonObject.getString("name"),password.getText().toString());
												
						Intent it = new Intent(getApplicationContext(),
								TasklistActivity.class);
						//it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(it);
						finish();
					}

					else {
						Toast.makeText(getApplicationContext(),
								"Wrong credential! Please try again!",
								Toast.LENGTH_LONG).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
					error.setText(e.getMessage());
				}
			}
		});
		}
	}
	
	

	public void register(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}	
	 
}
