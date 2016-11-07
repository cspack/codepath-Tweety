package com.cspack.tweety.interfaces;

import android.view.View;
import android.widget.ImageView;

import com.cspack.tweety.R;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.util.SnackbarUtil;

public interface TweetControllerInteractionListener {
  void startAction(TweetListModel.PageType page, String pageTypeRefId);

  void showError(SnackbarUtil.SnackbarErrorType type, View.OnClickListener listener);
}
