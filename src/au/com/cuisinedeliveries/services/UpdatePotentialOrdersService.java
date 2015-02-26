package au.com.cuisinedeliveries.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import au.com.cuisinedeliveries.data.DataContract.OrderEntry;
import au.com.cuisinedeliveries.data.OrderDbHelper;

public class UpdatePotentialOrdersService extends IntentService {

	public UpdatePotentialOrdersService() {
		super("UpdatePotentialOrdersService");
	}

	public UpdatePotentialOrdersService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		Log.i("UpdatePotentialOrdersService", "updating PotentialOrdersService");
		updatePotentialOrdersFromServer();
	}

	// TODO: User RESTful to retrieve a list of potential orders
	private void updatePotentialOrdersFromServer() {
		OrderDbHelper orderDbHelper = new OrderDbHelper(getApplicationContext());
		// Gets the data repository in write mode
		SQLiteDatabase db = orderDbHelper.getWritableDatabase();

		/* TODO: Dummy data inserted. We should get data from server */
		db.execSQL(OrderDbHelper.SQL_EMPTY_ENTRIES);
		int oid = 1;
		
		for(int i = oid; i < 10; i++) {
			String raddr = "restaurant address ";
			String destination = "destination ";
	
			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(OrderEntry.ORDER_ID, i);
			values.put(OrderEntry.RESTAURANT_ID, i);
			values.put(OrderEntry.RESTAURANT_ADDR, raddr + i);
			values.put(OrderEntry.DESTINATION, destination + i);
	
			// Insert the new row, returning the primary key value of the new row
			long newRowId = db.insert(OrderEntry.TABLE_NAME, null , values);
			Log.i("update:", String.valueOf(newRowId));
		}
		
		LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
		Intent intent = new Intent();
		localBroadcastManager.sendBroadcast(intent);
	}

}
