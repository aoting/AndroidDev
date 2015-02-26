package au.com.cuisinedeliveries;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import au.com.cuisinedeliveries.data.DataContract;
import au.com.cuisinedeliveries.services.UpdatePotentialOrdersService;

public class OrderList extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
	static final String[] PROJECTION = new String[] {DataContract.OrderEntry.RESTAURANT_ADDR};
	ProgressBar progressBar;

	private BroadcastReceiver orderUpdatedMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			String message = intent.getStringExtra("message");
			Log.d("receiver", "Got message: " + message);
		}
	};


	public SimpleCursorAdapter orderAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Start the service that update potential orders
		Intent updateIntent = new Intent(getApplicationContext(), UpdatePotentialOrdersService.class);
		startService(updateIntent);
		// Create a progress bar to display while the list loads
		progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		progressBar.setIndeterminate(true);
		ListView orderListView = (ListView)findViewById(R.id.content_frame);
		orderListView.setEmptyView(progressBar);
		orderListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
						Intent intent = new Intent(getBaseContext(), NavigationActivity.class);
						startActivity(intent);
					}
			});

		// Must add the progress bar to the root of the layout
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);

		String[] fromColumns = {DataContract.OrderEntry.RESTAURANT_ADDR};
		int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

		// Create an empty adapter we will use to display the loaded data.
		// We pass null for the cursor, then update it in onLoadFinished()
		orderAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, fromColumns, toViews, 0);
		orderListView.setAdapter(orderAdapter);

		// Prepare the loader.  Either re-connect with an existing one,
		// or start a new one.
		getLoaderManager().initLoader(0, null, this);

}

@Override
protected void onResume() {
	super.onResume();
	// Register mMessageReceiver to receive messages.
	LocalBroadcastManager.getInstance(this).registerReceiver(orderUpdatedMessageReceiver, new IntentFilter("au.com.cuisinedelivery.broadcast.orderupdated"));
}

@Override
protected void onPause() {
	super.onPause();
	LocalBroadcastManager.getInstance(this).unregisterReceiver(orderUpdatedMessageReceiver);
}

@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
}

@Override
public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
	Uri contentUri = Uri.parse(DataContract.OrderEntry.CONTENT_URI);
	return new CursorLoader(this.getApplicationContext(), contentUri, PROJECTION, null, null, null);
}

@Override
public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
	Log.i("Load finished", "load finish");
	orderAdapter.swapCursor(data);
	ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
	root.removeView(progressBar);
	return;

}

@Override
public void onLoaderReset(Loader<Cursor> arg0) {
	// TODO Auto-generated method stub
	orderAdapter.swapCursor(null);
}

//	@Override 
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        // Do something when a list item is clicked
//		Intent intent = new Intent(this, NavigationActivity.class);
//		startActivity(intent);
//    }


}
