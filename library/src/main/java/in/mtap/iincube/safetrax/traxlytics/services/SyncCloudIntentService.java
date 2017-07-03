package in.mtap.iincube.safetrax.traxlytics.services;


import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import in.mtap.iincube.safetrax.traxlytics.model.EventModel;
import in.mtap.iincube.safetrax.traxlytics.provider.LocalStoreContract;
import in.mtap.iincube.safetrax.traxlytics.transaction.EventTransaction;
import in.mtap.iincube.safetrax.traxlytics.utils.TraxUtils;

public class SyncCloudIntentService extends IntentService {

  private static final String TAG = SyncCloudIntentService.class.getSimpleName();

  /**
   * Creates an IntentService.  Invoked by your subclass's constructor.
   *
   * @param name Used to name the worker thread, important only for debugging.
   */
  public SyncCloudIntentService(String name) {
    super(name);
  }

  public SyncCloudIntentService() {
    super(TAG);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    try {
      syncToCloud(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void syncToCloud(Context context) throws Exception {
    if (!TraxUtils.hasToSync(context)) return;
    ContentResolver contentResolver = context.getContentResolver();
    Uri eventUri = LocalStoreContract.getContentUri(LocalStoreContract.getAuthority(context),
        LocalStoreContract.EventStore.TABLE_NAME);
    Cursor cursor = contentResolver.query(eventUri, EventModel.PROJECTION, null, null,
        LocalStoreContract.EventStore._ID + " ASC");
    List<EventModel> eventModelList = new LinkedList<>();
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      do {
        EventModel eventModel = EventModel.fromCursor(cursor);
        eventModelList.add(eventModel);
      } while (cursor.moveToNext());
      cursor.close();
    }
    if (eventModelList.size() > 0) {
      EventTransaction eventTransaction = new EventTransaction();
      eventTransaction.postEvent(eventModelList);
      contentResolver.delete(eventUri, null, null);
    }
  }
}