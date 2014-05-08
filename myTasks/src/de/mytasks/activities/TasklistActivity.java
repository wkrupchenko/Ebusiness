package de.mytasks.activities;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;

public class TasklistActivity extends OrmLiteBaseActivity<DatabaseHelper> {

	private EditText taskListName;
	private Button creatNewTask;
	private Button settings;
	private Button update;
	private DatabaseHelper databaseHelper;
	private ListView tasklistOverwiewWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		creatNewTask = (Button) findViewById(R.id.addTaskButton);
		settings = (Button) findViewById(R.id.shareButton);
		update = (Button) findViewById(R.id.updateButton);
		taskListName = (EditText) findViewById(R.id.newTasklistNameTitle);
		tasklistOverwiewWindow = (ListView) findViewById(R.id.tasklistOverwiewWindow);
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