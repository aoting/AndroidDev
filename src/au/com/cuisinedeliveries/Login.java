package au.com.cuisinedeliveries;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import au.com.cuisinedeliveries.services.LoginService;
import au.com.cuisinedeliveries.services.UpdateOrdersService;

public class Login extends Activity {
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger loginMessenger = new Messenger(new LoginFinishedHandler());

	public Messenger mMessenger;

	LoginService loginService;
	boolean loginServiceBound;
	
	ProgressDialog loginProgressDialog;

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection loginServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			loginServiceBound = true;
			mMessenger = new Messenger(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			loginServiceBound = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getSharedPreferences(LoginService.LOGIN_PREF, Context.MODE_PRIVATE);
		String username = settings.getString("username", "none");
		String password = settings.getString("password", "none");
		boolean logined = settings.getBoolean("logined", false);

		if (!logined || username == "none" || password == "none") {
			Intent intent = new Intent(this, LoginService.class);
			bindService(intent, loginServiceConnection, Context.BIND_AUTO_CREATE);
			setContentView(R.layout.login);
		} else {
			Intent startUpdate = new Intent(this, UpdateOrdersService.class);
			startService(startUpdate);
			Intent intent = new Intent(this, OrderList.class);
			startActivity(intent);
		}
	}

	protected void onDestroy() {
		super.onDestroy();
		unbindService(loginServiceConnection);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void login(View view) {
		Log.i("au.com.cuisinedeliveries", "login click");
		loginProgressDialog = new ProgressDialog(this);
		loginProgressDialog.setTitle("Logging");
		loginProgressDialog.setMessage("Wait while verifying your account...");
		loginProgressDialog.show();
		EditText input = (EditText)findViewById(R.id.user_name);
		final String username = input.getText().toString();
		input = (EditText)findViewById(R.id.password);
		final String password = input.getText().toString();

		Log.i("au.com.cuisinedeliveries", "trying to login with: " + username + " ----- " + password);

		// We want to monitor the service for as long as we are
		// connected to it.
		try {
			Message msg = Message.obtain(null, 1);
			Bundle data = new Bundle();
			data.putString("username", username);
			data.putString("password", password);
			msg.setData(data);
			msg.replyTo = loginMessenger;
			mMessenger.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
			// In this case the service has crashed before we could even
			// do anything with it; we can count on soon being
			// disconnected (and then reconnected if it can be restarted)
			// so there is no need to do anything here.
		}

	}

	/**
	 * Handler of incoming messages from service.
	 */
	class LoginFinishedHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Log.i("Login activity", "receive msg");
			SharedPreferences settings = getSharedPreferences(LoginService.LOGIN_PREF, Context.MODE_PRIVATE);
			String username = settings.getString("username", "none");
			String password = settings.getString("password", "none");
			Log.i("Login activity message handler", username + password);
			Log.i("Login activity message handler", Integer.toString(msg.arg1));
			loginProgressDialog.cancel();
			if (msg.arg1 == 1) {
				Intent intent = new Intent(getApplicationContext(), OrderList.class);
				startActivity(intent);
			}
		}
	}
}
