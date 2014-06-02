package de.mytasks.activities;

import de.mytasks.R;
import de.mytasks.service.SessionManager;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateActivity extends Activity {
	private RatingBar ratingBar;
	private Button rate;
	private static final String TAG = "RateActivity";
	
	SessionManager session;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		
		session = new SessionManager(getApplicationContext());

		rate = (Button) findViewById(R.id.ratingViewRateButton);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		}

}
