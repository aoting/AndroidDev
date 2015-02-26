package au.com.cuisinedeliveries.data;

import android.provider.BaseColumns;

public class DataContract {
	public DataContract() {}

	public static abstract class OrderEntry implements BaseColumns {
		public static final String CONTENT_URI = "content://au.com.cuisinedeliveries.data.order.provider/order_entries";
		public static final String TABLE_NAME = "order_entries";
		public static final String ORDER_ID = "order_id";
		public static final String RESTAURANT_ID = "r_id";
		public static final String RESTAURANT_ADDR = "r_addr";
		public static final String DESTINATION = "destination";
	}
}
