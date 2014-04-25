package de.mytasks.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
//import com.j256.ormlite.table.DatabaseTable;

import android.app.Activity;
import android.os.Bundle;

@DatabaseTable(tableName = "task")
public class Task extends Activity{
	
	@DatabaseField(generatedId = true)
	private Long task_id;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String description;
	
	@DatabaseField
	private Boolean value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public Task() {

	}

	public Task(String name, String description, Boolean value) {

		this.name = name;
		this.description = description;
		this.value = value;
	}

	public Long getTask_id() {
		return task_id;
	}

	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	
	

}
