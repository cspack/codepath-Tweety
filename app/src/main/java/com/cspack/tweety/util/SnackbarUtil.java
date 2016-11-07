package com.cspack.tweety.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.cspack.tweety.R;

/**
 * Created by chris on 11/1/16.
 */

public class SnackbarUtil {
  public enum SnackbarErrorType {
    OFFLINE,
    LOGIN_FAILED,
    NETWORK,
    INTERNAL,
    THROTTLED
  }
  private Snackbar snackbar;
  private View parent;

  public SnackbarUtil(View parent) {
    this.parent = parent;
  }

  public void show(SnackbarErrorType type, View.OnClickListener listener) {
    // don't show again if already showing.
    if (snackbar != null && snackbar.isShownOrQueued()) {
      return;
    }
    int snackbarStrResId = R.string.snackbar_network;
    switch (type) {
      case INTERNAL:
        snackbarStrResId = R.string.snackbar_internal;
        break;
      case LOGIN_FAILED:
        snackbarStrResId = R.string.login_failed;
        break;
      case OFFLINE:
        snackbarStrResId = R.string.snackbar_not_online;
        break;
      case NETWORK:
        snackbarStrResId = R.string.snackbar_network;
        break;
      case THROTTLED:
        snackbarStrResId = R.string.snackbar_throttled;
        break;
    }
    snackbar = Snackbar.make(parent,
        snackbarStrResId,
        Snackbar.LENGTH_INDEFINITE);
    if (listener != null) {
      snackbar.setAction(R.string.snackbar_retry, listener);
    }
    snackbar.show();
  }
}
