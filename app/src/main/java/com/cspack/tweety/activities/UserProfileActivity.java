package com.cspack.tweety.activities;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.TwitterClient;
import com.cspack.tweety.adapters.HomeFragmentPagerAdapter;
import com.cspack.tweety.fragments.ComposeFragment;
import com.cspack.tweety.fragments.TweetStreamFragment;
import com.cspack.tweety.interfaces.TweetControllerInteractionListener;
import com.cspack.tweety.interfaces.ZoomThumbnailInterfaceListener;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.util.SnackbarUtil;
import com.cspack.tweety.util.TweetHeaderUtil;
import com.cspack.tweety.util.ZoomUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;

import static com.cspack.tweety.models.TweetListModel.PageType.HOME;
import static com.cspack.tweety.models.TweetListModel.PageType.TWEET_REPLY;
import static com.cspack.tweety.models.TweetListModel.PageType.USER;

public class UserProfileActivity extends AppCompatActivity
    implements ComposeFragment.OnComposeListener, TweetControllerInteractionListener,
    ZoomThumbnailInterfaceListener {
  public static String TAG = UserProfileActivity.class.getSimpleName();
  public static final String ARG_YOUR_USER = "yourUser";
  public static final String ARG_THEIR_USER = "theirUser";

  private CollapsingToolbarLayout toolbarLayout;
  private Toolbar toolbar;
  private ImageView toolbarBackground;
  private TwitterClient client;
  private SnackbarUtil snackbar;
  private FloatingActionButton btnCompose;
  private UserModel yourUser;
  private UserModel theirUser;
  private ImageView ivAvatar;
  private ImageView ivCollapsedToolbarAvatar;
  private TextView tvFollowing;
  private TextView tvFollowers;
  private TextView tvTagLine;
  private TextView tvName;
  private TextView tvScreenName;
  private RelativeLayout collapsedToolbarTitle;
  private TextView tvCollapsedToolbarTitleText;
  private TextView tvCollapsedToolbarSubtitleText;

  private AppBarLayout appBarLayout;
  private MenuItem followMenuItem;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_twitter);
    initToolbar();

    btnCompose = (FloatingActionButton) findViewById(R.id.btnCompose);
    snackbar = new SnackbarUtil(findViewById(R.id.mainLayout));
    ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
    tvFollowing = (TextView) findViewById(R.id.tvFollowing);
    tvFollowers = (TextView) findViewById(R.id.tvFollowers);
    tvTagLine = (TextView) findViewById(R.id.tvTagLine);
    tvName = (TextView) findViewById(R.id.tvProfileName);
    tvScreenName = (TextView) findViewById(R.id.tvProfileScreenName);


    client = TwitterApplication.getRestClient();

    if (!getIntent().hasExtra(ARG_YOUR_USER) || !getIntent().hasExtra(ARG_THEIR_USER)) {
      finish();
      return;
    }

    // Lookup User from DB.
    yourUser = Parcels.unwrap(getIntent().getParcelableExtra(ARG_YOUR_USER));
    theirUser = Parcels.unwrap(getIntent().getParcelableExtra(ARG_THEIR_USER));
    setUserMenu(theirUser);

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction()
          .replace(R.id.bodyFragment, TweetStreamFragment.newInstance(USER, theirUser.getId()))
          .commit();
    }
    btnCompose.setVisibility(View.VISIBLE);
    btnCompose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        buildTweetComposer();
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.user, menu);
    followMenuItem = menu.findItem(R.id.item_follow);
    setupMenuItems();
    return true;
  }

  private void initToolbar() {
    appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
    if (appBarLayout.getChildCount() == 0) {
      getLayoutInflater().inflate(R.layout.app_bar_user, appBarLayout, true);
    }
    toolbarLayout = (CollapsingToolbarLayout) appBarLayout.findViewById(R.id.toolbarLayout);
    toolbar = (Toolbar) appBarLayout.findViewById(R.id.tbMain);
    toolbarBackground = (ImageView) findViewById(R.id.ivBackground);
    collapsedToolbarTitle = (RelativeLayout) getLayoutInflater().inflate(
        R.layout.app_bar_title, toolbar, false);
    tvCollapsedToolbarTitleText = (TextView) collapsedToolbarTitle.findViewById(R.id.tvProfileName);
    tvCollapsedToolbarSubtitleText = (TextView) collapsedToolbarTitle.findViewById(
        R.id.tvProfileScreenName);
    ivCollapsedToolbarAvatar = (ImageView) collapsedToolbarTitle.findViewById(R.id.ivAvatar);
    // This call is loaded with bad things... find a better solution for it.
    /*
    tvCollapsedToolbarTitle.setTextAppearance(
        this,
        android.support.v7.appcompat.R.style.Base_TextAppearance_AppCompat_Widget_ActionBar_Title_Inverse);
    */
    toolbar.addView(collapsedToolbarTitle);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayUseLogoEnabled(false);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    // Back button disabled because it looks bad on dark backgrounds.
    // TODO: Reenable after finding a way to skin the up arrow.
    // getSupportActionBar().setDisplayShowHomeEnabled(true);
    // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    appBarLayout.removeOnOffsetChangedListener(offsetChangedListener);
    offsetChangedListener =
        new AppBarLayout.OnOffsetChangedListener() {
          boolean isShow = false;
          int scrollRange = -1;

          @Override
          public void onOffsetChanged(final AppBarLayout appBarLayout, final int verticalOffset) {
            runOnUiThread(new Runnable() {
              @Override
              public void run() {
                if (scrollRange == -1) {
                  scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                  collapsedToolbarTitle.setVisibility(View.VISIBLE);
                  isShow = true;
                } else if (isShow) {
                  collapsedToolbarTitle.setVisibility(View.INVISIBLE);
                  isShow = false;
                }
              }

            });
          }
        };
    appBarLayout.addOnOffsetChangedListener(offsetChangedListener);
  }
  private AppBarLayout.OnOffsetChangedListener offsetChangedListener;

  private void setUserMenu(UserModel user) {
    String title = user.getName();
    toolbarLayout.setTitle(title);
    tvCollapsedToolbarTitleText.setText(title);
    tvCollapsedToolbarSubtitleText.setText("@" + user.getScreenName());
    TweetHeaderUtil.PopulateUserToolbarLayout(user, toolbarLayout);
    String profileBackground = user.getProfileBackgroundImageUrlHttps();
    if (profileBackground != null) {
      Picasso.with(this).load(profileBackground).into(toolbarBackground);
    }
    String avatar = user.getProfileImageUrlBigger();
    if (avatar != null) {
      ivAvatar.setVisibility(View.VISIBLE);
      ivCollapsedToolbarAvatar.setVisibility(View.VISIBLE);
      Picasso.with(this).load(avatar).transform(new RoundedCornersTransformation(10, 0))
          .into(ivAvatar);
      Picasso.with(this).load(avatar).transform(new RoundedCornersTransformation(10, 0))
          .into(ivCollapsedToolbarAvatar);
    } else {
      ivAvatar.setVisibility(View.GONE);
      ivCollapsedToolbarAvatar.setVisibility(View.GONE);
    }
    tvName.setText(user.getName());
    tvScreenName.setText("@" + user.getScreenName());
    tvTagLine.setText(user.getTagLine());

    // TODO(technical-debt): This doesn't work well for i18n (you should use "%s Followers").
    SpannableStringBuilder followingSpan = new SpannableStringBuilder();
    followingSpan.append(NumberFormat.getNumberInstance().format(user.getFriendCount()));
    followingSpan.append(" ");
    followingSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, followingSpan.length(),
        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    followingSpan.append(getString(R.string.following));
    SpannableStringBuilder followerSpan = new SpannableStringBuilder();
    followerSpan.append(NumberFormat.getNumberInstance().format(user.getFollowerCount()));
    followerSpan.append(" ");
    followerSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, followerSpan.length(),
        SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
    followerSpan.append(getString(R.string.followers));
    tvFollowers.setText(followingSpan);
    tvFollowing.setText(followerSpan);

    setupMenuItems();
  }

  public void setupMenuItems() {
    if (followMenuItem == null) return;
    if (!theirUser.getId().equals(yourUser.getId())) {
      followMenuItem.setVisible(true);
      if (theirUser.isFollowing() || theirUser.isFollowRequestSent()) {
        followMenuItem.setIcon(R.drawable.ic_user_minus);
      }
    }
  }
  public void buildTweetComposer() {
    ComposeFragment fragment = ComposeFragment.newInstance(USER, theirUser.getId(), yourUser);
    fragment.show(getSupportFragmentManager(), "ComposeFragment");
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // Treat up as a back button press?
        onBackPressed();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onComposerCompleted(boolean tweeted, TweetModel tweet) {
    // Not all fragments do this.
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

  @Override
  public void onBackPressed() {
    int count = getFragmentManager().getBackStackEntryCount();
    if (count == 0) {
      finish();
    } else {
      String newUserId =
          getSupportFragmentManager().getBackStackEntryAt(
              getSupportFragmentManager().getBackStackEntryCount() - 1)
          .getName();
      theirUser = UserModel.byId(newUserId);
      setUserMenu(theirUser);
      getFragmentManager().popBackStack();
    }
  }

  private void launchUserProfile(UserModel newUser) {
    setUserMenu(newUser);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.bodyFragment, TweetStreamFragment.newInstance(USER, newUser.getId()))
        .addToBackStack(newUser.getId())
        .commit();
  }
  @Override
  public void startAction(TweetListModel.PageType page, String pageTypeRefId) {
    switch (page) {
      case USER:
        if (pageTypeRefId.equals(theirUser.getId())) {
          return;
        }
        if (pageTypeRefId.charAt(0) == '@') {
          UserModel user = UserModel.byScreenName(pageTypeRefId.substring(1));
          if (user == null) {
            // You need to find this user if it isn't familiar.
            TwitterApplication.getRestClient().lookupScreenName(pageTypeRefId.substring(1),
                new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    if (response.length() != 1) {
                      showError(SnackbarUtil.SnackbarErrorType.INTERNAL, null);
                    } else {
                      try {
                        UserModel user = new UserModel(response.getJSONObject(0));
                        user.save();
                        launchUserProfile(user);
                      } catch (JSONException e) {
                        e.printStackTrace();
                        showError(SnackbarUtil.SnackbarErrorType.INTERNAL, null);
                      }
                    }
                  }
                });
          } else {
            launchUserProfile(user);
          }
        } else {
          launchUserProfile(UserModel.byId(pageTypeRefId));
        }
        break;
      case TWEET_REPLY:
        ComposeFragment fragment = ComposeFragment.newInstance(
            TWEET_REPLY, pageTypeRefId, yourUser);
        fragment.show(getSupportFragmentManager(), "ComposeFragment");
        break;
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
