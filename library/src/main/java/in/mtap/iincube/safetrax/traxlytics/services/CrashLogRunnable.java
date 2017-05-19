package in.mtap.iincube.safetrax.traxlytics.services;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import in.mtap.iincube.safetrax.traxlytics.model.EventModel;
import in.mtap.iincube.safetrax.traxlytics.provider.LocalStoreContract;
import in.mtap.iincube.safetrax.traxlytics.transaction.EventTransaction;
import in.mtap.iincube.safetrax.traxlytics.utils.TraxUtils;

public class CrashLogRunnable implements Runnable {

  private static final String TAG = CrashLogRunnable.class.getSimpleName();
  private Context context;
  private String company;
  private String username;
  private String tag;
  private String info;
  private long time;

  public CrashLogRunnable(Context context, String company, String username, String tag,
                          String info, long time) {
    this.context = context;
    this.company = company;
    this.username = username;
    this.tag = tag;
    this.info = info;
    this.time = time;
  }

  @Override
  public void run() {
    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
    EventModel eventModel = new EventModel();
    eventModel.setAppName(TraxUtils.getApplicationName(context));
    eventModel.setAppVersion(TraxUtils.getAppVersion(context));
    eventModel.setAndroidVersion(TraxUtils.getOsVersion());
    eventModel.setDeviceModel(TraxUtils.getDeviceMake());
    eventModel.setUsername(username);
    eventModel.setCompany(company);
    eventModel.setTag(tag);
    eventModel.setInfo(info);
    eventModel.setTime(time);
    pushToCloud(eventModel);
  }

  private void saveLocal(EventModel eventModel) {
    Uri eventUri = LocalStoreContract.EventStore.CONTENT_URI;
    ArrayList<ContentProviderOperation> batch = new ArrayList<>();
    ContentResolver contentResolver = context.getContentResolver();
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
      e.printStackTrace();
    }
  }
}
