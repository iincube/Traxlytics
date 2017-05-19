package in.mtap.iincube.safetrax.traxlytics.transaction;


import okhttp3.OkHttpClient;

class HttpClientPool {
  private static final OkHttpClient INSTANCE = new OkHttpClient();

  static OkHttpClient getHttpClient() {
    return INSTANCE;
  }
}