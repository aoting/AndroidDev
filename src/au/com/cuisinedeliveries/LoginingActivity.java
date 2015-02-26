package au.com.cuisinedeliveries;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;
import au.com.cuisinedeliveries.services.LoginService;

public class LoginingActivity extends Activity {
	LoginService loginService;
	boolean loginServiceBound;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("au.com.cuisinedeliveries", "logining!");
     // Get the message from the intent
//        Intent intent = getIntent();
//        String message = intent.getStringExtra("au.com.cuisinedeliveries.Login.username");
        
//        Intent intent = new Intent(this, LoginService.class);
//        boolean bou = bindService(intent, loginServiceConnection, Context.BIND_AUTO_CREATE);
//        Log.i("au.com.cuisinedeliveries", String.valueOf(bou));
		
        // Create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(40);

        // Set the text view as the activity layout
        setContentView(textView);
    }
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		String username = loginService.getUsername();
//		String password = loginService.getPassword();
//		Log.i("logining onstart" , username + password);
	}


	/** Defines callbacks for service binding, passed to bindService() */
//	private ServiceConnection loginServiceConnection = new ServiceConnection() {
//
//		@Override
//		public void onServiceConnected(ComponentName className, IBinder service) {
//			// We've bound to LocalService, cast the IBinder and get LocalService instance
//			LoginBinder binder = (LoginBinder) service;
//			Log.i("au.com.cuisinedeliveries", "onServiceConnected");
//			loginService = binder.getService();
//			String username = loginService.getUsername();
//			String password = loginService.getPassword();
//			Log.i("logining onstart" , username + password);
//			loginServiceBound = true;
//		}
//
//		@Override
//		public void onServiceDisconnected(ComponentName arg0) {
//			loginServiceBound = false;
//		}
//	};
}
