package com.cspack.tweety.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cspack.tweety.R;
import com.cspack.tweety.databinding.ListTwitterBinding;
import com.cspack.tweety.databinding.TweetHeaderBinding;
import com.cspack.tweety.interfaces.TweetControllerInteractionListener;
import com.cspack.tweety.interfaces.ZoomThumbnailInterfaceListener;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * Util for abstracting implementation details of Tweet UIs.
 * This is probably poorly named since it does way more than just the headers.
 */

public class TweetHeaderUtil {
  public static void PopulateTweetHeaderBinding(UserModel user, TweetHeaderBinding binding) {
    binding.imageAuthor.setImageDrawable(null);
    int color =TweetHeaderUtil.getProfileBackgroundColor(binding.tweetHeader.getContext(), user);
    binding.tweetHeader.setBackgroundColor(ColorUtils.setAlphaComponent(color, 120));
    String profileImageUrl = user.getProfileImageUrlBigger();
    if (profileImageUrl != null) {
      Picasso.with(binding.imageAuthor.getContext()).load(profileImageUrl)
          .transform(new RoundedCornersTransformation(10, 0)).into(binding.imageAuthor);
    }
    PopulateAuthorTextView(user, binding.tvAuthor);
  }

  public static void PopulateTweetBinding(TweetModel tweet,
                                          boolean showImage,
                                          boolean showFooterRow,
                                          final ListTwitterBinding binding,
                                          final TweetControllerInteractionListener listener) {
    PopulateTweetHeaderBinding(tweet.getUser(), binding.tweetHeaderInclude);
    binding.tweetHeaderInclude.tvTweetAge.setText(
        TimestampUtil.GetRelativeTimeAgo(tweet.getCreatedAt()));
    binding.tweetPhoto.setImageDrawable(null);
    final String tweetPhotoUrl = tweet.getFirstPhotoUrl();
    if (showImage && tweetPhotoUrl != null) {
      Picasso.with(binding.tweetPhoto.getContext()).load(tweetPhotoUrl).transform(
          new RoundedCornersTransformation(10, 10)).into(binding.tweetPhoto);

      final Context context = binding.tweetPhoto.getContext();
      if (context instanceof ZoomThumbnailInterfaceListener) {
        final ZoomThumbnailInterfaceListener zoomListener = (ZoomThumbnailInterfaceListener) context;
        binding.tweetPhoto.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Picasso.with(context).load(tweetPhotoUrl).into(zoomListener.getZoomImageView(),
                new Callback() {
                  @Override
                  public void onSuccess() {
                    zoomListener.zoomImageFromThumb(binding.tweetPhoto);
                  }

                  @Override
                  public void onError() {}
                });
          }
        });
      }
    }
    binding.tvTweetText.setText(tweet.getText());
    // Don't highlight tweet text if we can't handle clicks.
    if (listener != null) {
      new PatternEditableBuilder().
          addPattern(Pattern.compile("\\@(\\w+)"), R.color.twitter_primary,
              new PatternEditableBuilder.SpannableClickedListener() {
                @Override
                public void onSpanClicked(String text) {
                  // Do something here
                  listener.startAction(TweetListModel.PageType.USER, text);
                }
              }).into(binding.tvTweetText);
    }
    if (showFooterRow) {
      binding.footerRow.setVisibility(View.VISIBLE);
      binding.tvRetweets.setText(NumberFormat.getInstance().format(tweet.getRetweetCount()));
      binding.tvRetweets.setChecked(tweet.isRetweeted());
      binding.tvFavorites.setText(NumberFormat.getInstance().format(tweet.getFavoritesCount()));
      binding.tvFavorites.setChecked(tweet.isFavorited());
    } else {
      binding.footerRow.setVisibility(View.GONE);
    }
  }

  public static void PopulateAuthorTextView(UserModel user, TextView textView) {
    String name = user.getName();
    String screenName = user.getScreenName();
    if (name.isEmpty() || screenName.isEmpty()) {
      return;
    }
    SpannableStringBuilder ssb = new SpannableStringBuilder(name);
    ssb.setSpan(new AbsoluteSizeSpan(textView.getResources()
            .getDimensionPixelSize(R.dimen.author_text_size)), 0,
        ssb.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    ssb.append(Html.fromHtml("<br/>"));
    ssb.append("@" + screenName);
    ssb.setSpan(new StyleSpan(Typeface.BOLD), name.length() + 1, ssb.length(),
        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    ssb.setSpan(new AbsoluteSizeSpan(textView.getResources()
            .getDimensionPixelSize(R.dimen.author_screenname_text_size)), name.length() + 1,
        ssb.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    textView.setText(ssb);
  }

  public static int getProfileBackgroundColor(Context context, UserModel user) {
    int backgroundColorInt = -1;
    try {
      String backgroundColor = user.getProfileBackgroundColor();
      backgroundColorInt = ContextCompat.getColor(context, R.color.twitter_primary);
      if (backgroundColor != null) {
        backgroundColorInt = Color.parseColor("#" + backgroundColor);
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return backgroundColorInt;
  }

  public static void PopulateUserToolbarLayout(UserModel user,
                                               CollapsingToolbarLayout toolbarLayout) {
    try {
      int backgroundColorInt = getProfileBackgroundColor(toolbarLayout.getContext(), user);
      toolbarLayout.setBackgroundColor(backgroundColorInt);
      toolbarLayout.setContentScrimColor(backgroundColorInt);
      float[] hsl = new float[3];
      ColorUtils.colorToHSL(backgroundColorInt, hsl);
      hsl[2] = 1.0f - hsl[2]; // inverse value from background.
      int textColorInt = ColorUtils.HSLToColor(hsl);
      toolbarLayout.setExpandedTitleColor(textColorInt);
      toolbarLayout.setCollapsedTitleTextColor(textColorInt);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
  }
}
