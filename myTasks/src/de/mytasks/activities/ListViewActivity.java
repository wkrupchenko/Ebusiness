package de.mytasks.activities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
 
import com.j256.ormlite.support.ConnectionSource;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.widget.ExpandableListView;
import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;
import de.mytasks.domain.Tasklist;
import de.mytasks.domain.User;

public class ListViewActivity extends Activity {
  // more efficient than HashMap for mapping integers to objects
  SparseArray<Group> groups = new SparseArray<Group>();
  DatabaseHelper databaseHelper; 
     
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.list_main);
    
     
    fillData();
    
    ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
    MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,
        groups);
    listView.setAdapter(adapter);
  }

  public void fillData() {
	  
	  databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
	   
	  RuntimeExceptionDao<Task, Integer> taskDao = databaseHelper.getTaskRuntimeExceptionDao();
	  
	  	 	   
		/*taskDao.create(new Task("Waesche waschen", "waschen, schleudern", true));
		taskDao.create(new Task("Wohnung putzen", "putzen", true));
		taskDao.create(new Task("Max abholen", "Max abholen", true));
		taskDao.create(new Task("Hausis waschen", "Hausis machen", true));*/
	  
		List<Task> taskList = taskDao.queryForAll();
		   
    for (int j = 0; j < 4; j++) {
      Group group = new Group("Tasklist " + j);
      for(Task task : taskList) {
    	  	group.children.add((task.getName().toString()));		     
		}       
      groups.append(j, group);
    }
  }

} 