package com.cspack.tweety.interfaces;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by chris on 11/6/16.
 */

public interface ZoomThumbnailInterfaceListener {
  ImageView getZoomImageView();

  void zoomImageFromThumb(final View thumbView);
}
