//package de.mytasks.ui.main;
//
////import static de.shop.util.Konstanten.ARTIKEL_KEY;
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
////import de.shop.R;
//import de.mytasks.domain.Task;
////import de.shop.data.artikel.Artikel;
//import de.shop.service.ArtikelService;
//import de.shop.service.ArtikelService.ArtikelServiceBinder;
//import de.shop.ui.artikel.ArtikelDetails; 
//import android.widget.Toast;
//import static android.widget.Toast.LENGTH_LONG;
//import static de.shop.ui.main.Prefs.mock; 
// 
//public class Main extends Activity {
//private static final String LOG_TAG = Main.class.getSimpleName();
//
//private ArtikelServiceBinder artikelServiceBinder; 
//
//// ServiceConnection ist ein Interface: anonyme Klasse verwenden, um ein Objekt davon zu erzeugen
//private ServiceConnection artikelServiceConnection = new ServiceConnection() {
//	@Override
//	public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
//		artikelServiceBinder = (ArtikelServiceBinder) serviceBinder;
//	}
//
//	@Override
//	public void onServiceDisconnected(ComponentName name) {
//		artikelServiceBinder = null;			 
//	}
//};
// 
//
//@Override
//public void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//    setContentView(R.layout.main);
//    
//    
//    Fragment detailsFragment = null;
//    final Bundle extras = getIntent().getExtras();
//    if (extras == null) {
//    	
//    	detailsFragment = new Startseite();
//    	
//      Prefs.init(this);
//    }
//    else {
//        final Artikel artikel = (Artikel) extras.get(ARTIKEL_KEY);        
//        if (artikel != null) {
//        	Log.d(LOG_TAG, artikel.toString());
//        	
//    		final Bundle args = new Bundle(1);
//    		args.putSerializable(ARTIKEL_KEY, artikel);
//    		
//        	detailsFragment = new ArtikelDetails();
//        	detailsFragment.setArguments(args);
//        }
//                        
//    }         
//    
//    getFragmentManager().beginTransaction()
//                        .add(R.id.details, detailsFragment)
//                        .commit();
//    if (mock) {
//		Toast.makeText(this, R.string.s_mock, LENGTH_LONG).show();
//	}
//}
//
//@Override
//public void onStart() {
//	super.onStart();
//
//	Intent intent = new Intent(this, ArtikelService.class);
//	bindService(intent, artikelServiceConnection, Context.BIND_AUTO_CREATE);
//	 
//}
//
//@Override
//public void onStop() {
//	super.onStop();
//	
//	unbindService(artikelServiceConnection);	 
//}
//
//public ArtikelServiceBinder getArtikelServiceBinder() {
//	return artikelServiceBinder;
//} 
//
//}
//	
//
