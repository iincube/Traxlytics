package in.mtap.iincube.safetrax.traxlytics;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import in.mtap.iincube.safetrax.traxlytics.services.CrashLogRunnable;
import in.mtap.iincube.safetrax.traxlytics.services.LogDataIntentService;
import in.mtap.iincube.safetrax.traxlytics.services.SyncCloudIntentService;
import in.mtap.iincube.safetrax.traxlytics.sharedpref.SharedPref;
import in.mtap.iincube.safetrax.traxlytics.sharedpref.SharedPrefClient;

class TraxEvent {
  private static final String SHARED_PREFERENCE_NAME = "traxlytics_preferences";
  private static final String PREFERENCE_COMPANY = "pref_company";
  private static final String PREFERENCE_USERNAME = "pref_username";
  private static final int REQUEST_TRAX_SYNC = 0x011;
  private static final long ALARM_FREQUENCY = 30 * 1000;

  private Context context;

  private SharedPrefClient sharedPrefClient;

  private static Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

  private static Thread.UncaughtExceptionHandler mCaughtExceptionHandler
      = new Thread.UncaughtExceptionHandler() {
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
      Traxlytics.logCrash(ex);
      mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }
  };

  TraxEvent(Application application) {
    mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);
    this.context = application.getApplicationContext();
    sharedPrefClient = SharedPref.get(application.getApplicationContext(), SHARED_PREFERENCE_NAME);
    scheduleSync();
  }

  void logEvent(String tag, String info) {
    if (tag.equals(Traxlytics.EVENT_CRASH)) {
      CrashLogRunnable runnable = new CrashLogRunnable(context, getCompany(), getUsername(),
          tag, info, System.currentTimeMillis());
      new Thread(runnable).start();
    } else {
      context.startService(LogDataIntentService.getInstance(context, getCompany(), getUsername(),
          tag, info));
    }
  }

  void onLogin(String company, String username) {
    sharedPrefClient.putValue(PREFERENCE_COMPANY, company);
    sharedPrefClient.putValue(PREFERENCE_USERNAME, username);
  }

  void onLogout() {
    sharedPrefClient.putValue(PREFERENCE_COMPANY, "");
    sharedPrefClient.putValue(PREFERENCE_USERNAME, "");
  }

  private String getUsername() {
    return sharedPrefClient.getValue(PREFERENCE_USERNAME, "");
  }

  private String getCompany() {
    return sharedPrefClient.getValue(PREFERENCE_COMPANY, "");
  }

  private void scheduleSync() {
    Intent syncIntent = new Intent(context, SyncCloudIntentService.class);
    PendingIntent pendingSyncIntent = PendingIntent.getService(context, REQUEST_TRAX_SYNC,
        syncIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),
        ALARM_FREQUENCY, pendingSyncIntent);
  }

  private void cancelSyncService() {
    Intent syncIntent = new Intent(context, SyncCloudIntentService.class);
    PendingIntent pendingSyncIntent = PendingIntent.getService(context, REQUEST_TRAX_SYNC,
        syncIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    alarm.cancel(pendingSyncIntent);
  }
}