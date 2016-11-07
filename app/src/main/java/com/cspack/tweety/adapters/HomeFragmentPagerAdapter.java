package com.cspack.tweety.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.cspack.tweety.R;
import com.cspack.tweety.fragments.TweetStreamFragment;
import com.cspack.tweety.models.TweetListModel;

import static com.cspack.tweety.models.TweetListModel.PageType.HOME;
import static com.cspack.tweety.models.TweetListModel.PageType.MENTIONS;

/**
 * Created by chris on 11/2/16.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
  final int PAGE_COUNT = 2;
  private int tabTitleIds[] = new int[] { R.string.timeline, R.string.mentions };
  private int[] imageResId = {
      R.drawable.ic_home,
      R.drawable.ic_reply
  };
  private Context context;
  private ActionBar actionBar;
  private ViewPager.OnPageChangeListener pageChangedListener =
      new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
      HomeFragmentPagerAdapter.this.actionBar.setTitle(tabTitleIds[position]);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
  };

  public HomeFragmentPagerAdapter(FragmentManager fm, ActionBar actionBar, ViewPager pager) {
    super(fm);
    this.context = pager.getContext();
    this.actionBar = actionBar;
    pager.addOnPageChangeListener(pageChangedListener);
  }

  @Override
  public int getCount() {
    return PAGE_COUNT;
  }

  @Override
  public Fragment getItem(int position) {
    TweetListModel.PageType pageType;
    if (position == 0) {
      pageType = HOME;
    } else {
      pageType = MENTIONS;
    }
    return TweetStreamFragment.newInstance(pageType, "");
  }

  @Override
  public CharSequence getPageTitle(int position) {
    Drawable image = ContextCompat.getDrawable(context, imageResId[position]);
    image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
    SpannableString sb = new SpannableString(" ");
    ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
    sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return sb;
  }
}
