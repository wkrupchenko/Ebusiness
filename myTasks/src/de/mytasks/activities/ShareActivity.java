package de.mytasks.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Tasklist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ShareActivity extends Activity {

	 private Button share;
	 private Button back;
	 private TextView taskListName;
	 private EditText email;
	 private TextView shareText;
	 
	 
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_share);
			taskListName = (TextView) findViewById(R.id.shareViewTasklistName);
			shareText = (TextView) findViewById(R.id.emailToShareTextView);
			email = (EditText) findViewById(R.id.shareViewEmailInput);
			back = (Button) findViewById(R.id.shareViewBackButton);
			share = (Button) findViewById(R.id.shareViewShareButton);
			
		}
	 
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
	 
	 //back button
	 public void back(View view) {
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}

	} 
