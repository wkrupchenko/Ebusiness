package de.mytasks.activities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.mytasks.R;

public class ShowMapActivity extends Activity {
	// Google Map
		private GoogleMap map;
		// Geodaten für Kiel
		static final LatLng KIEL = new LatLng(53.551, 9.993);

		private Button mBtnFind;
		private GoogleMap mMap;
		private EditText etPlace;
		private static final String TAG = "MainActivity";
		private Long taskId;
		private String taskName;
		private LatLng taskMap;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_show_map);
			
			Intent i = getIntent();
			taskId = i.getLongExtra("TaskId", 0L);
			taskName = i.getStringExtra("TaskName");
			taskMap = new LatLng(i.getDoubleExtra("Latitude", 0.0), i.getDoubleExtra("Longitude", 0.0));

			Log.v(TAG + "onCreate", "onCreate call");
			Log.v(TAG, taskId.toString());
			Log.v(TAG, taskMap.toString());

			 map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			 Marker marker = map.addMarker(new MarkerOptions().position(taskMap).title(taskName));
//			 Marker kiel = map.addMarker(new MarkerOptions()
//			 .position(KIEL)
//			 .title("Kiel")
//			 .snippet("Kiel is cool")
//			 .icon(BitmapDescriptorFactory
//			 .fromResource(R.drawable.ic_launcher)));
			
			 // Move the camera instantly to hamburg with a zoom of 15.
			 map.moveCamera(CameraUpdateFactory.newLatLngZoom(taskMap, 15));	
			 
			// // Zoom in, animating the camera.
			// map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		}
//		@Override
//		public void onBackPressed() {
//			finish();
//			Intent i = new Intent(getApplicationContext(), TaskActivity.class);
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(i);	
//		}

}
