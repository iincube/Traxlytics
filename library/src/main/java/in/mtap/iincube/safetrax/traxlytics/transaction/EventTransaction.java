package in.mtap.iincube.safetrax.traxlytics.transaction;


import com.google.gson.JsonObject;

import java.util.List;

import in.mtap.iincube.safetrax.traxlytics.Traxlytics;
import in.mtap.iincube.safetrax.traxlytics.model.EventModel;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EventTransaction extends Transaction {

  public EventTransaction() {
    super(HttpClientPool.getHttpClient());
  }


  public void postEvent(List<EventModel> eventModelList) throws Exception {
    HttpUrl httpUrl = URL.newBuilder().addPathSegment("write")
        .addQueryParameter("dbname", Traxlytics.serverConfig.getDbName())
        .addQueryParameter("colname", Traxlytics.serverConfig.getColName())
        .build();

    JsonObject authParams = new JsonObject();
    authParams.addProperty("username", Traxlytics.serverConfig.getUsername());
    authParams.addProperty("password", Traxlytics.serverConfig.getPassword());

    String postParams = "";
    for (int i = 0; i < eventModelList.size(); i++) {
      EventModel eventModel = eventModelList.get(i);
      JsonObject document = new JsonObject();
      document.addProperty("app_name", eventModel.getAppName());
      document.addProperty("app_version", eventModel.getAppVersion());
      document.addProperty("android_version", eventModel.getAndroidVersion());
      document.addProperty("device_model", eventModel.getDeviceModel());
      document.addProperty("company", eventModel.getCompany());
      document.addProperty("username", eventModel.getUsername());
      document.addProperty("event", eventModel.getTag());
      document.addProperty("event_message", eventModel.getInfo());
      document.addProperty("time", eventModel.getTime());
      if (i >= eventModelList.size() - 1) {
        postParams += document.toString();
      } else {
        postParams += document.toString() + "\n";
      }
    }

    Request request = new Request.Builder().url(httpUrl)
        .put(RequestBody.create(JSON, authParams.toString() + "\n" + postParams))
        .build();

    client.newCall(request).execute();
  }
}