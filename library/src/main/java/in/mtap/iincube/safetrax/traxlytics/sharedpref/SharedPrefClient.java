package in.mtap.iincube.safetrax.traxlytics.sharedpref;


import android.support.annotation.CheckResult;

public interface SharedPrefClient extends PrefHolder {

  /**
   * Clears all data from the shared preferences
   * of name {@link #getPreferenceName()} that is used by this instance.
   */
  void clearAll();

  /** @return Preference name used for this instance. */
  @CheckResult String getPreferenceName();
}
