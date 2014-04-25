package de.mytasks.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import android.app.Activity;
import android.os.Bundle;

@DatabaseTable(tableName = "tasklist")
public class Tasklist extends Activity{
	
	@DatabaseField(generatedId = true)
	private Long tasklist_id;
	
	@DatabaseField
	private String tasklist_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public Tasklist() {

	}

	public Tasklist(String tasklist_title) {

		this.tasklist_title = tasklist_title;
	}

	public Long getTasklist_id() {
		return tasklist_id;
	}

	public void setTasklist_id(Long tasklist_id) {
		this.tasklist_id = tasklist_id;
	}

	public String getTasklist_title() {
		return tasklist_title;
	}

	public void setTasklist_title(String tasklist_title) {
		this.tasklist_title = tasklist_title;
	}
	


}
