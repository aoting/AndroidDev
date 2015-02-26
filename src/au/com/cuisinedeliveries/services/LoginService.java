package au.com.cuisinedeliveries.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import au.com.cuisinedeliveries.OrderList;

/* Messenger Service */
public class LoginService extends Service {
	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	public Messenger client;

	private String username;
	private String password;

	public final static String LOGIN_PREF = "LoginPref";
	
	private int LOGIN = 1;
	private int FAILED_LOGIN = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return mMessenger.getBinder();
	}

	public boolean login(String username, String password) {
		Log.i("au.com.cuisinedeliveries.services.loginservice", "trying to autheticate:" + username + "<------>" + password);
		new LoginTask().execute();
		return true;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Save logined info at sharedPreferences
			SharedPreferences settings = getSharedPreferences(LOGIN_PREF, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("username", username);
			editor.putString("password", password);
			editor.commit();
			
			// Start the service that update potential orders
			Intent updateIntent = new Intent(getApplicationContext(), UpdatePotentialOrdersService.class);
			startService(updateIntent);

			// Send msg after login
			try {
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.arg1 = isLogInSuccess() ? LOGIN : FAILED_LOGIN;
			System.out.println("thread password" + (password == username));
			msg.obj = password;
			try {
				client.send(msg);
			} catch (android.os.RemoteException e1) {
				Log.w(getClass().getName(), "Exception sending message", e1);
			}
			return null;
		}
		
	}
	
	private boolean isLogInSuccess() {
		return username.equals(password);
	}

	/**
	 * Handler of incoming messages from clients.
	 */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			client = msg.replyTo;
			username = msg.getData().getString("username");
			password = msg.getData().getString("password");
			login(username, password);
		}
	}
	
}
