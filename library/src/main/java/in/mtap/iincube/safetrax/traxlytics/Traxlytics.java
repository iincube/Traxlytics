package in.mtap.iincube.safetrax.traxlytics;


import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

import in.mtap.iincube.safetrax.traxlytics.model.ServerConfig;

import static in.mtap.iincube.safetrax.traxlytics.Preconditions.assertNotNull;

public class Traxlytics {
  public static final String EVENT_EXCEPTION = "exception";
  public static final String EVENT_CRASH = "crash";
  public static final String EVENT_LOGIN = "login";

  private static TraxEvent instance;
  public static ServerConfig serverConfig;

  public static void init(@NonNull Application application, @NonNull ServerConfig serverConfig) {
    if (instance != null) {
      throw new IllegalStateException("Can be initialized only once!");
    }
    Traxlytics.serverConfig = serverConfig;
    instance = new TraxEvent(application);
  }

  public static void login(String company, String username) {
    checkInstanceNotNull();
    instance.onLogin(company, username);
  }

  public static void logout() {
    checkInstanceNotNull();
    instance.onLogout();
  }

  public static void logCrash(Throwable throwable) {
    checkInstanceNotNull();
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    throwable.printStackTrace(pw);
    instance.logEvent(EVENT_CRASH, sw.toString());
  }

  public static void logException(Exception exception) {
    checkInstanceNotNull();
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    exception.printStackTrace(pw);
    instance.logEvent(EVENT_EXCEPTION, sw.toString());
  }

  public static void logEvent(String event, String info) {
    checkInstanceNotNull();
    instance.logEvent(event, info);
  }

  private static void checkInstanceNotNull() {
    assertNotNull(instance, "Must call \"init\" on Traxlytics first");
  }
}
