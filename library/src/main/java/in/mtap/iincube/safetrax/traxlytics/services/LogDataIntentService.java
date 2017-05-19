package in.mtap.iincube.safetrax.traxlytics.services;


import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.mtap.iincube.safetrax.traxlytics.model.EventModel;
import in.mtap.iincube.safetrax.traxlytics.provider.LocalStoreContract;
import in.mtap.iincube.safetrax.traxlytics.transaction.EventTransaction;
import in.mtap.iincube.safetrax.traxlytics.utils.TraxUtils;

public class LogDataIntentService extends IntentService {
  public static final String TAG = LogDataIntentService.class.getSimpleName();

  public static final String EXTRA_COMPANY = "company";
  public static final String EXTRA_USERNAME = "username";
  public static final String EXTRA_EVENT_TAG = "event_tag";
  public static final String EXTRA_EVENT_INFO = "event_info";


  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public LogDataIntentService(String name) {
    super(name);
  }

  public LogDataIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    if (intent == null) return;
    String company = intent.getStringExtra(EXTRA_COMPANY);
    String username = intent.getStringExtra(EXTRA_USERNAME);
    String tag = intent.getStringExtra(EXTRA_EVENT_TAG);
    String info = intent.getStringExtra(EXTRA_EVENT_INFO);
    long timeInMillis = System.currentTimeMillis();
    EventModel eventModel = new EventModel();
    eventModel.setAppName(TraxUtils.getApplicationName(this));
    eventModel.setAppVersion(TraxUtils.getAppVersion(this));
    eventModel.setAndroidVersion(TraxUtils.getOsVersion());
    eventModel.setDeviceModel(TraxUtils.getDeviceMake());
    eventModel.setCompany(company);
    eventModel.setUsername(username);
    eventModel.setTag(tag);
    eventModel.setInfo(info);
    eventModel.setTime(timeInMillis);
    pushToCloud(eventModel);
  }

  private void saveLocal(EventModel eventModel) {
    Uri eventUri = LocalStoreContract.EventStore.CONTENT_URI;
    ArrayList<ContentProviderOperation> batch = new ArrayList<>();
    ContentResolver contentResolver = getContentResolver();
    batch.add(ContentProviderOperation
        .newInsert(eventUri)
        .withValues(eventModel.toContentValues())
        .build());
    try {
      contentResolver.applyBatch(LocalStoreContract.CONTENT_AUTHORITY, batch);
    } catch (RemoteException | OperationApplicationException e) {
      e.printStackTrace();
    }
  }

  private void pushToCloud(EventModel eventModel) {
    List<EventModel> eventModelList = new LinkedList<>();
    eventModelList.add(eventModel);
    EventTransaction eventTransaction = new EventTransaction();
    try {
      eventTransaction.postEvent(eventModelList);
    } catch (Exception e) {
      saveLocal(eventModel);
    }
  }


  public static Intent getInstance(Context context, String company, String username, String tag,
                                   String info) {
    Intent intent = new Intent(context, LogDataIntentService.class);
    intent.putExtra(EXTRA_COMPANY, company);
    intent.putExtra(EXTRA_USERNAME, username);
    intent.putExtra(EXTRA_EVENT_TAG, tag);
    intent.putExtra(EXTRA_EVENT_INFO, info);
    return intent;
  }
}
