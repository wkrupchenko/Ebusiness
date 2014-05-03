package de.mytasks.activities;

 
import java.io.ByteArrayOutputStream;

import de.mytasks.R;
import de.mytasks.database.DatabaseHelper;
import de.mytasks.domain.Task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

@SuppressLint("NewApi")
public class TaskActivity extends Activity {
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
	 Intent i = new Intent(getApplicationContext(),ListViewActivity.class);
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
	   PopupMenu popupMenu = new PopupMenu(TaskActivity.this, v);
	      popupMenu.getMenuInflater().inflate(R.menu.main_popup, popupMenu.getMenu());
	    
	      popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
	   
	   @Override
	   public boolean onMenuItemClick(MenuItem item) {
	    Toast.makeText(TaskActivity.this,
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
 
 

