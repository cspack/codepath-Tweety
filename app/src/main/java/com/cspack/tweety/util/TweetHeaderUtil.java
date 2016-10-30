package com.cspack.tweety.util;

import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.cspack.tweety.R;
import com.cspack.tweety.models.UserModel;

/**
 * Created by chris on 10/29/16.
 */

public class TweetHeaderUtil {
  public static void PopulateAuthorTextView(UserModel user, TextView textView) {
    String name = user.getName();
    String screenName = user.getScreenName();
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
}
