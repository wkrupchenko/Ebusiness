package de.mytasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class DBHandler extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "taskDB.db";
	private static final String TABLE_TASK = "tasks";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_GEOTAG = "geotag";
	public static final String COLUMN_PICTURE = "picture";
	
	public DBHandler(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
		 	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);		 
		 
		String CREATE_TASK_TABLE = "CREATE TABLE " +
				TABLE_TASK + "("
	             + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_TITLE 
	             + " TEXT," + COLUMN_DESCRIPTION + " TEXT," + COLUMN_GEOTAG + " TEXT," + COLUMN_PICTURE + " BLOB"  + ")";
	      db.execSQL(CREATE_TASK_TABLE);	     
	   	 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
	      onCreate(db);
	}
	
	public void addTask(Task task) {

        ContentValues values = new ContentValues();        
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DESCRIPTION, task.getDescription());
        values.put(COLUMN_GEOTAG, task.getGeotag());
        values.put(COLUMN_PICTURE, task.getPicture());
 
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.insert(TABLE_TASK, null, values);
        db.close();
	}
	
	public Task findTask(int id) {
		String query = "Select * FROM " + TABLE_TASK + " WHERE " + COLUMN_ID + " =  \"" + id + "\"";
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Task task = new Task();
		
		if (cursor.moveToFirst()) {
			cursor.moveToFirst();
			task.setID(Integer.parseInt(cursor.getString(0)));
			task.setTitle(cursor.getString(1));
			task.setDescription(cursor.getString(2));
			task.setGeotag(cursor.getString(3));
			task.setPicture((cursor.getBlob(4)));
			cursor.close();
		} else {
			task = null;
		}
	        db.close();
		return task;
	}
	
	public boolean deleteTask(String tasktitle) {
		
		boolean result = false;
		
		String query = "Select * FROM " + TABLE_TASK + " WHERE " + COLUMN_TITLE + " =  \"" + tasktitle + "\"";

		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(query, null);
		
		Task task = new Task();
		
		if (cursor.moveToFirst()) {
			task.setID(Integer.parseInt(cursor.getString(0)));
			db.delete(TABLE_TASK, COLUMN_ID + " = ?",
		            new String[] { String.valueOf(task.getID()) });
			cursor.close();
			result = true;
		}
	        db.close();
		return result;
	}
	

} 
