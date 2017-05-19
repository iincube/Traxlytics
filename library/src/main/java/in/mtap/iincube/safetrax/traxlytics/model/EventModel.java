package in.mtap.iincube.safetrax.traxlytics.model;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static in.mtap.iincube.safetrax.traxlytics.provider.LocalStoreContract.EventStore;

public class EventModel implements Parcelable {

  public static final String[] PROJECTION = new String[]{
      EventStore.COLUMN_NAME_APP_NAME,
      EventStore.COLUMN_NAME_APP_VERSION,
      EventStore.COLUMN_NAME_ANDROID_VERSION,
      EventStore.COLUMN_NAME_DEVICE_MODEL,
      EventStore.COLUMN_NAME_COMPANY,
      EventStore.COLUMN_NAME_USERNAME,
      EventStore.COLUMN_NAME_EVENT_TAG,
      EventStore.COLUMN_NAME_EVENT_INFO,
      EventStore.COLUMN_NAME_TIME
  };

  private String appName;
  private String appVersion;
  private String androidVersion;
  private String deviceModel;
  private String company;
  private String username;
  private String tag;
  private String info;
  private long time;

  protected EventModel(Parcel in) {
    appName = in.readString();
    appVersion = in.readString();
    androidVersion = in.readString();
    deviceModel = in.readString();
    company = in.readString();
    username = in.readString();
    tag = in.readString();
    info = in.readString();
    time = in.readLong();
  }

  public EventModel() {

  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(appName);
    dest.writeString(appVersion);
    dest.writeString(androidVersion);
    dest.writeString(deviceModel);
    dest.writeString(company);
    dest.writeString(username);
    dest.writeString(tag);
    dest.writeString(info);
    dest.writeLong(time);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<EventModel> CREATOR = new Creator<EventModel>() {
    @Override
    public EventModel createFromParcel(Parcel in) {
      return new EventModel(in);
    }

    @Override
    public EventModel[] newArray(int size) {
      return new EventModel[size];
    }
  };

  public static EventModel fromCursor(Cursor cursor) {
    EventModel event = new EventModel();
    event.appName = cursor.getString(cursor.getColumnIndex(EventStore.COLUMN_NAME_APP_NAME));
    event.appVersion = cursor.getString(cursor.getColumnIndex(EventStore.COLUMN_NAME_APP_VERSION));
    event.androidVersion = cursor.getString(cursor.getColumnIndex(
        EventStore.COLUMN_NAME_ANDROID_VERSION));
    event.deviceModel = cursor.getString(cursor.getColumnIndex(
        EventStore.COLUMN_NAME_DEVICE_MODEL));
    event.company = cursor.getString(cursor.getColumnIndex(EventStore.COLUMN_NAME_COMPANY));
    event.username = cursor.getString(cursor.getColumnIndex(EventStore.COLUMN_NAME_USERNAME));
    event.tag = cursor.getString(cursor.getColumnIndex(EventStore.COLUMN_NAME_EVENT_TAG));
    event.info = cursor.getString(cursor.getColumnIndex(EventStore.COLUMN_NAME_EVENT_INFO));
    event.time = cursor.getLong(cursor.getColumnIndex(EventStore.COLUMN_NAME_TIME));
    return event;
  }

  public ContentValues toContentValues() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(EventStore.COLUMN_NAME_APP_NAME, appName);
    contentValues.put(EventStore.COLUMN_NAME_APP_VERSION, appVersion);
    contentValues.put(EventStore.COLUMN_NAME_ANDROID_VERSION, androidVersion);
    contentValues.put(EventStore.COLUMN_NAME_DEVICE_MODEL, deviceModel);
    contentValues.put(EventStore.COLUMN_NAME_COMPANY, company);
    contentValues.put(EventStore.COLUMN_NAME_USERNAME, username);
    contentValues.put(EventStore.COLUMN_NAME_EVENT_TAG, tag);
    contentValues.put(EventStore.COLUMN_NAME_EVENT_INFO, info);
    contentValues.put(EventStore.COLUMN_NAME_TIME, time);
    return contentValues;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAndroidVersion(String androidVersion) {
    this.androidVersion = androidVersion;
  }

  public String getAndroidVersion() {
    return androidVersion;
  }

  public void setDeviceModel(String deviceModel) {
    this.deviceModel = deviceModel;
  }

  public String getDeviceModel() {
    return deviceModel;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getCompany() {
    return company;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getInfo() {
    return info;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public long getTime() {
    return time;
  }
}
