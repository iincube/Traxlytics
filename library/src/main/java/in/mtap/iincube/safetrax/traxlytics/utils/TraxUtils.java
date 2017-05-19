package in.mtap.iincube.safetrax.traxlytics.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;

import in.mtap.iincube.safetrax.traxlytics.model.EventModel;
import in.mtap.iincube.safetrax.traxlytics.provider.LocalStoreContract;

public class TraxUtils {
  private static final String TAG = TraxUtils.class.getSimpleName();

  public static String getApplicationName(Context context) {
    ApplicationInfo applicationInfo = context.getApplicationInfo();
    int stringId = applicationInfo.labelRes;
    return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString()
        : context.getString(stringId);
  }

  public static String getAppVersion(Context context) {
    String version = "";
    try {
      PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      version = pInfo.versionName + "[" + pInfo.versionCode + "]";
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return version;
  }

  public static String getOsVersion() {
    return Build.VERSION.RELEASE;
  }

  public static String getDeviceMake() {
    return Build.MANUFACTURER + " - " + Build.MODEL;
  }

  private static boolean isOnline(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    return (netInfo != null && netInfo.isConnected());
  }

  public static boolean hasToSync(Context context) {
    ContentResolver contentResolver = context.getContentResolver();
    Uri eventUri = LocalStoreContract.EventStore.CONTENT_URI;
    Cursor cursor = contentResolver.query(eventUri, EventModel.PROJECTION, null, null,
        LocalStoreContract.EventStore._ID + " ASC");
    boolean hasLocal = false;
    if (cursor != null && cursor.getCount() > 0) {
      hasLocal = true;
      cursor.close();
    }
    return hasLocal && isOnline(context);
  }
}