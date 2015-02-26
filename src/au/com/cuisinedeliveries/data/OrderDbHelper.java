package au.com.cuisinedeliveries.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import au.com.cuisinedeliveries.data.DataContract.OrderEntry;

public class OrderDbHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "DATA.db";
	public static int DATABASE_VERSION = 1;
	

	private static final String TEXT_TYPE = " TEXT";
	private static final String INT_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";

	public static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + OrderEntry.TABLE_NAME + " (" +
					OrderEntry._ID + " INTEGER PRIMARY KEY," +
					OrderEntry.ORDER_ID + INT_TYPE + COMMA_SEP +
					OrderEntry.RESTAURANT_ID + INT_TYPE + COMMA_SEP +
					OrderEntry.RESTAURANT_ADDR + TEXT_TYPE + COMMA_SEP +
					OrderEntry.DESTINATION + TEXT_TYPE +
					" )";

	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + OrderEntry.TABLE_NAME;
	public static final String SQL_EMPTY_ENTRIES = "DELETE FROM " + OrderEntry.TABLE_NAME;
	
	public OrderDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);

	}

}
