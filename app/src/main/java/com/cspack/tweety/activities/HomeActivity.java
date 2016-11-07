package com.cspack.tweety.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.TwitterClient;
import com.cspack.tweety.adapters.HomeFragmentPagerAdapter;
import com.cspack.tweety.fragments.ComposeFragment;
import com.cspack.tweety.interfaces.TweetControllerInteractionListener;
import com.cspack.tweety.interfaces.ZoomThumbnailInterfaceListener;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.util.SnackbarUtil;
import com.cspack.tweety.util.ZoomUtil;

import org.parceler.Parcels;

import static com.cspack.tweety.models.TweetListModel.PageType.HOME;
import static com.cspack.tweety.models.TweetListModel.PageType.TWEET_REPLY;

public class HomeActivity extends AppCompatActivity
    implements ComposeFragment.OnComposeListener, TweetControllerInteractionListener,
    ZoomThumbnailInterfaceListener {
  public static String TAG = HomeActivity.class.getSimpleName();
  public static final String ARG_YOUR_USER = "yourUserId";

  private ImageView toolbarBackground;
  private SnackbarUtil snackbar;
  private FloatingActionButton btnCompose;
  private UserModel user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_twitter);
    AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

    getLayoutInflater().inflate(R.layout.app_bar_home, appBarLayout, true);

    btnCompose = (FloatingActionButton) findViewById(R.id.btnCompose);
    snackbar = new SnackbarUtil(findViewById(R.id.mainLayout));
    setSupportActionBar((Toolbar) findViewById(R.id.tbMain));
    getSupportActionBar().setLogo(R.drawable.twitter_logo_white_sm);
    getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(true);
    getSupportActionBar().setTitle(R.string.twitter);

    if (!getIntent().hasExtra(ARG_YOUR_USER)) {
      finish();
      return;
    }

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
    viewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(),
        getSupportActionBar(), viewPager));
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    tabLayout.setVisibility(View.VISIBLE);
    tabLayout.setupWithViewPager(viewPager);

    user = Parcels.unwrap(getIntent().getParcelableExtra(ARG_YOUR_USER));
    btnCompose.setVisibility(View.VISIBLE);
    btnCompose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        buildTweetComposer();
      }
    });
  }

  public void buildTweetComposer() {
    ComposeFragment fragment = ComposeFragment.newInstance(HOME, "", user);
    fragment.show(getSupportFragmentManager(), "ComposeFragment");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.home, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // This shouldn't happen? (Until hamburger.)
        break;
      case R.id.item_view_profile:
        launchUserProfile(user);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onComposerCompleted(boolean tweeted, TweetModel tweet) {
    // Not all fragments does this.
    /*
    // Temporarily adds tweet but does not store it in DB (to allow refresh to work later).
    tweetAdapter.add(tweet);
    // Scroll to top.
    rvTweets.scrollToPosition(0);
    */
  }

  @Override
  public void showError(SnackbarUtil.SnackbarErrorType type, View.OnClickListener listener) {
    snackbar.show(type, listener);
  }

  private void launchUserProfile(UserModel theirUser) {
    Intent intent = new Intent(this, UserProfileActivity.class);
    intent.putExtra(UserProfileActivity.ARG_YOUR_USER, Parcels.wrap(user));
    intent.putExtra(UserProfileActivity.ARG_THEIR_USER, Parcels.wrap(theirUser));
    startActivity(intent);
  }

  @Override
  public void startAction(TweetListModel.PageType page, String pageTypeRefId) {
    switch (page) {
      case USER:
        launchUserProfile(UserModel.byId(pageTypeRefId));
        break;
      case TWEET_REPLY:
        ComposeFragment fragment = ComposeFragment.newInstance(
            TWEET_REPLY, pageTypeRefId, user);
        fragment.show(getSupportFragmentManager(), "ComposeFragment");
        break;
      default:
        showError(SnackbarUtil.SnackbarErrorType.INTERNAL, null);
    }
  }

  // Below is test code for zoom animation... it can probably be thrown out.
  private Animator mCurrentAnimator;
  private ZoomUtil zoomUtil;

  public ImageView getZoomImageView() {
    return (ImageView) findViewById(R.id.expanded_image);
  }

  public void zoomImageFromThumb(final View thumbView) {
    if (zoomUtil == null)
      zoomUtil = new ZoomUtil(findViewById(R.id.bodyFragment),
          (ImageView) findViewById(R.id.expanded_image),
          mCurrentAnimator);
    zoomUtil.runZoomImageForThumb(thumbView);
  }
}
