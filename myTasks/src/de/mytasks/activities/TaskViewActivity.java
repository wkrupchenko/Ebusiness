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

public class TaskViewActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private Button creatNewTask;
	private Button settings;
	private Button update;
	private DatabaseHelper databaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		creatNewTask = (Button) findViewById(R.id.addTaskButton);
		settings = (Button) findViewById(R.id.shareButton);
		update = (Button) findViewById(R.id.updateButton);
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