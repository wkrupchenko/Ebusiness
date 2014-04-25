package de.mytasks.activities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;

public class LoginActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText email = null;
	private EditText password = null;
	private Button login;
	private Button register;
	private DatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		email = (EditText) findViewById(R.id.loginViewEmailInput);
		password = (EditText) findViewById(R.id.loginViewPasswordInput);
		login = (Button) findViewById(R.id.loginViewLoginButton);
		register = (Button) findViewById(R.id.loginViewRegisterButton);
		saveTestData();
	}

	private void saveTestData() {

		RuntimeExceptionDao<User, Integer> userDao = getHelper()
				.getUserRuntimeExceptionDao();
		userDao.create(new User("admin", "admin", "Start123"));

		List<User> users = userDao.queryForAll();
		Log.d("demo", users.toString());
	}

	public void login(View view) {

		RuntimeExceptionDao<User, Integer> userDao = getHelper()
				.getUserRuntimeExceptionDao();
		List<User> users = userDao.queryForAll();
		for (User u : users) {
			if (email.getText().toString().equals(u.getMail())
					&& password.getText().toString()
							.equals(u.getPasswordHash())) {

				Intent intent = new Intent(this, ListViewExampleActivity.class);
				startActivity(intent);
			} else {

			}
		}
	}

	public void register(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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