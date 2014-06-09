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
import android.content.Intent;
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
	private String errorMsg;
	private static final String TAG = "LogonActivity";
//	private User user;
	TextView error;

	// Session Manager Class
	SessionManager session;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Session Manager
		session = new SessionManager(getApplicationContext());

		username = (EditText) findViewById(R.id.loginViewUsernameInput);
		password = (EditText) findViewById(R.id.loginViewPasswordInput);
		login = (Button) findViewById(R.id.loginViewLoginButton);
		register = (TextView) findViewById(R.id.loginViewRegisterButton);
		error = (TextView) findViewById(R.id.error);

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
 
						session.createLoginSession(Long.valueOf(jsonObject.getString("id")),
								jsonObject.getString("name"), jsonObject.getString("email"));
												
						Intent it = new Intent(getApplicationContext(),
								TasklistActivity.class);
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

	public void register(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
}
