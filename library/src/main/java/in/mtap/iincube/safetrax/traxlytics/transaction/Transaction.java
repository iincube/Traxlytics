package in.mtap.iincube.safetrax.traxlytics.transaction;

import in.mtap.iincube.safetrax.traxlytics.Traxlytics;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

abstract class Transaction {
  static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  static final HttpUrl URL = HttpUrl.parse(Traxlytics.serverConfig.getUrl());
  OkHttpClient client;

  Transaction(OkHttpClient client) {
    this.client = client;
  }
}
