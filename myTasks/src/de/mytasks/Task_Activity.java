

 
package de.mytasks;

 
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import de.mytasks.DBHandler;
import de.mytasks.Task;
import de.mytasks.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Fragment;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressLint("NewApi")
public class Task_Activity extends Activity {
 EditText title;
 EditText description;
 EditText geotag; 
 ImageView picture;
 ImageButton ib;
 Bitmap bmp;
 
 
 /** Called when the activity is first created. */
 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  title = (EditText) findViewById(R.id.edit_task_title);
  description = (EditText) findViewById(R.id.edit_task_description);
  picture = (ImageView) findViewById(R.id.task_picture);
  ib =  (ImageButton) findViewById(R.id.imageButton1);
  
  ib.setOnClickListener(viewClickListener);
  
          
 }
 
 public void showTask (View view) throws NullPointerException {
	 try {
		 
		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.tomaten);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);		
		byte[] byteArray = stream.toByteArray();		 
		Task task = new Task();
		task.setTitle("Aufgabe 1");
		task.setDescription("Kaufe irgendwas");
		task.setGeotag("Aufgabe 1");
		task.setPicture(byteArray);
		
		DBHandler dbHandler = new DBHandler(this, null, null, 1);		
		dbHandler.addTask(task);
		  
		final Task other = dbHandler.findTask(Integer.parseInt(title.getText().toString()));
		title.setText(other.getTitle());
		description.setText(other.getDescription());
		Bitmap image = BitmapFactory.decodeByteArray(other.getPicture(), 0, other.getPicture().length);
		picture.setImageBitmap(image);		 
	 }
	 
	 catch (Exception e) {
		  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		  builder.setMessage(e.getMessage());
		  AlertDialog alert = builder.create();
		  alert.show();
	 }
	 
	  
	  }
 
 public void showListview(View view) 
 {
	 Intent i = new Intent(getApplicationContext(),ListViewExampleActivity.class);
	 startActivity(i);
 }
   
 OnClickListener viewClickListener
 = new OnClickListener(){

 @Override
 public void onClick(View v) {
  // TODO Auto-generated method stub
  showPopupMenu(v);
 }
  
 };

 private void showPopupMenu(View v){
	   PopupMenu popupMenu = new PopupMenu(Task_Activity.this, v);
	      popupMenu.getMenuInflater().inflate(R.menu.main_popup, popupMenu.getMenu());
	    
	      popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	   
	   @Override
	   public boolean onMenuItemClick(MenuItem item) {
	    Toast.makeText(Task_Activity.this,
	      item.toString(),
	      Toast.LENGTH_LONG).show();
	    return true;
	   }
	  });
	    
	      popupMenu.show();
	  }
 
 @Override
 public void onBackPressed() {
     Toast.makeText(getBaseContext(),"Hey digga, don't forget to save your task!!",Toast.LENGTH_LONG).show();
     finish();
     return;
 }  
 
}
 
 

