package de.mytasks.domain;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.mytasks.database.DatabaseHelper;

import android.app.Activity;
import android.os.Bundle;

@DatabaseTable(tableName = "user")
public class User extends OrmLiteBaseActivity<DatabaseHelper>{
	
	@DatabaseField(generatedId = true)
	private Long user_id;
	
	@DatabaseField
	private String mail;
	
	@DatabaseField
	private String name;
	
	@DatabaseField
	private String passwordHash;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public User() {

	}

	public User(String mail, String name, String passwordHash) {
		this.mail = mail;
		this.name = name;
		this.passwordHash = passwordHash;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	
}
