package de.mytasks.activities;

import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.User;

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
	private DatabaseHelper databaseHelper;
		
	@Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_register);
	      email = (EditText) findViewById(R.id.editText1);
	      password = (EditText) findViewById(R.id.editText2);
	      passwordrepeat = (EditText) findViewById(R.id.editText3);
	      username = (EditText) findViewById(R.id.editText4);
	      back = (Button)findViewById(R.id.button1);
	      register = (Button)findViewById(R.id.button2);
	      back.setOnClickListener(myhandler1);
	      register.setOnClickListener(myhandler2);
	}
	
	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	      back(v);
	    }
	  };
	  
	View.OnClickListener myhandler2 = new View.OnClickListener() {
	    public void onClick(View v) {
	      // it was the 2nd button
	    	registerOnApp(email.getText().toString(), password.getText().toString(), username.getText().toString());
	    	email.setText("");
	    	password.setText("");
	    	passwordrepeat.setText("");
	    	username.setText("");
	    }
	  };
	public void back(View view){
		  Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
	      startActivity(intent);
	   }
	
	public void register(View view){
		  Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
	      startActivity(intent);
	   }
	
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	   }
	
	private void registerOnApp(String email, String password, String username){
//		String newEmail = email.getText().toString();
//		String newPassword = password.getText().toString();
//		String newUsername = username.getText().toString();
		
		User newUser = new User(email, password, username);
		
		// get our dao
		RuntimeExceptionDao<User, Integer> userDao = getHelper().getUserRuntimeExceptionDao();
		userDao.create(newUser);
		Toast.makeText(getApplicationContext(), "succesfully registered",Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null) {
			databaseHelper.close();
			databaseHelper = null;
		}
	}
}
