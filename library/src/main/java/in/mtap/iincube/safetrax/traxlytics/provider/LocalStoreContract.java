package in.mtap.iincube.safetrax.traxlytics.provider;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import in.mtap.iincube.safetrax.traxlytics.BuildConfig;

public class LocalStoreContract {
  public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID +
      ".traxlytics.localstore";
  private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  /**
   * Constants used for the building content URI path.
   */
  static final String PATH_EVENT = "event";

  private static final String TYPE_TEXT = " TEXT";
  private static final String TYPE_INTEGER = " INTEGER";
  private static final String CONSTRAINT_NOT_NULL = " NOT NULL";
  private static final String CONSTRAINT_UNIQUE = " UNIQUE";
  private static final String COMMA_DELIMITER = ",";

  static final String[] CREATE_TABLE_QUERIES = new String[]{
      EventStore.CREATE_QUERY
  };

  static final String[] DROP_TABLE_QUERIES = new String[]{
      EventStore.DROP_QUERY
  };

  public interface EventStore extends BaseColumns {
    String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.traxlytics.event";
    String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.traxlytics.event";
    Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EVENT)
        .appendPath("all").build();
    String TABLE_NAME = "event";
    String COLUMN_NAME_APP_NAME = "app_name";
    String COLUMN_NAME_APP_VERSION = "app_version";
    String COLUMN_NAME_ANDROID_VERSION = "android_version";
    String COLUMN_NAME_DEVICE_MODEL = "device_model";
    String COLUMN_NAME_COMPANY = "company";
    String COLUMN_NAME_USERNAME = "username";
    String COLUMN_NAME_EVENT_TAG = "event_tag";
    String COLUMN_NAME_EVENT_INFO = "event_info";
    String COLUMN_NAME_TIME = "time";

    String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" + _ID + TYPE_INTEGER
        + " PRIMARY KEY" + COMMA_DELIMITER
        + COLUMN_NAME_APP_NAME + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_APP_VERSION + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_ANDROID_VERSION + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_DEVICE_MODEL + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_COMPANY + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_USERNAME + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_EVENT_TAG + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_EVENT_INFO + TYPE_TEXT + COMMA_DELIMITER
        + COLUMN_NAME_TIME + TYPE_TEXT
        + " )";
    String DROP_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
  }
}