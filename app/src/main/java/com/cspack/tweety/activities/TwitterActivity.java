package com.cspack.tweety.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.cspack.tweety.BuildConfig;
import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.TwitterClient;
import com.cspack.tweety.adapters.TweetListAdapter;
import com.cspack.tweety.fragments.ComposeFragment;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.util.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import cz.msebera.android.httpclient.Header;

import static com.cspack.tweety.models.TweetListModel.PageType.HOME;
import static com.cspack.tweety.models.TweetListModel.PageType.USER;

public class TwitterActivity extends AppCompatActivity
    implements TweetListAdapter.TweetAdapterListener, ComposeFragment.OnComposeListener {
  public static String TAG = TwitterActivity.class.getSimpleName();
  public static final String ARG_YOUR_USER = "yourUserId";
  public static final String ARG_PAGE_TYPE = "pageType";
  public static final String ARG_PAGE_TYPE_REF_ID = "pageTypeRefId";

  private TweetListAdapter tweetAdapter;
  private RecyclerView rvTweets;
  private CollapsingToolbarLayout toolbarLayout;
  private Toolbar toolbar;
  private ImageView toolbarBackground;
  private TwitterClient client;
  private SwipeRefreshLayout swipeContainer;
  private Snackbar errorSnackbar;
  private FloatingActionButton btnCompose;
  private UserModel user;
  private HandlerThread dbSaveThread = new HandlerThread("DbSaveThread");
  private Handler dbSaveHandler;


  private TweetListModel.PageType pageType = HOME;
  private String pageTypeRefId = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    dbSaveThread.start();
    dbSaveHandler = new Handler(dbSaveThread.getLooper());

    setContentView(R.layout.activity_twitter);
    toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbarLayout);
    toolbar = (Toolbar) findViewById(R.id.tbMain);
    toolbarBackground = (ImageView) findViewById(R.id.ivBackground);
    btnCompose = (FloatingActionButton) findViewById(R.id.btnCompose);
    rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
    swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    getSupportActionBar().setLogo(R.drawable.twitter_logo_white_sm);
    getSupportActionBar().setDisplayUseLogoEnabled(true);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    client = TwitterApplication.getRestClient();

    if (!getIntent().hasExtra(ARG_YOUR_USER)) {
      finish();
      return;
    }
    if (!getIntent().hasExtra(ARG_PAGE_TYPE)) {
      finish();
      return;
    }

    user = Parcels.unwrap(getIntent().getParcelableExtra(ARG_YOUR_USER));
    pageType = (TweetListModel.PageType) getIntent().getSerializableExtra(ARG_PAGE_TYPE);

    if (pageType != HOME) {
      if (!getIntent().hasExtra(ARG_PAGE_TYPE_REF_ID)) {
        finish();
        return;
      }
      pageTypeRefId = getIntent().getStringExtra(ARG_PAGE_TYPE_REF_ID);
    }
    initUser();
    new TweetDbLoaderTask().execute(new Pair(pageType, pageTypeRefId));
  }

  // This works but it feels too heavyweight, future async are going to try HandlerThread.
  private class TweetDbLoaderTask extends AsyncTask<Pair<TweetListModel.PageType, String>,
      Void, List<TweetModel>> {
    @Override
    protected void onPreExecute() {
      swipeContainer.setRefreshing(true);
    }

    @Override
    protected List<TweetModel> doInBackground(Pair<TweetListModel.PageType, String>... params) {
      List<TweetModel> result = new ArrayList<>();
      for (Pair<TweetListModel.PageType, String> param : params) {
        result.addAll(TweetListModel.recentItems(param.first, param.second));
      }
      return result;
    }

    @Override
    protected void onPostExecute(List<TweetModel> tweetModels) {
      swipeContainer.setRefreshing(false);
      // Populate list with initial DB.
      tweetAdapter = new TweetListAdapter(TwitterActivity.this);
      tweetAdapter.addAll(tweetModels);
      rvTweets.setAdapter(tweetAdapter);
      rvTweets.setLayoutManager(new LinearLayoutManager(TwitterActivity.this));
      rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(
          (LinearLayoutManager) rvTweets.getLayoutManager()) {
        @Override
        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
          loadOlderTweets();
        }
      });
      swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          loadNewerTweets();
        }
      });

      // Don't automatically load tweets while debugging, unless you have to, it wastes API tokens.
      // TODO: Replace this with a time-based refresh window (if refreshed > 1 minute ago).
      if (tweetAdapter.getItemCount() == 0 || !BuildConfig.DEBUG) {
        loadNewerTweets();
      }
    }
  }

  private void initUser() {
    UserModel titleUser = user;
    if (pageType == USER) {
      titleUser = UserModel.byId(pageTypeRefId);
    }
    getSupportActionBar().setDisplayShowTitleEnabled(true);
    getSupportActionBar().setTitle(titleUser.getName());
    getSupportActionBar().setSubtitle("@" + titleUser.getScreenName());
    if (pageType != HOME) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    try {
      String backgroundColor = titleUser.getProfileBackgroundColor();
      int backgroundColorInt = ContextCompat.getColor(this, R.color.twitter_primary);
      if (backgroundColor != null) {
        backgroundColorInt = Color.parseColor("#" + backgroundColor);
      }
      toolbarLayout.setBackgroundColor(backgroundColorInt);
      float[] hsl = new float[3];
      ColorUtils.colorToHSL(backgroundColorInt, hsl);
      hsl[2] = 1.0f - hsl[2]; // inverse value from background.
      int textColorInt = ColorUtils.HSLToColor(hsl);
      toolbarLayout.setExpandedTitleColor(textColorInt);
      toolbarLayout.setCollapsedTitleTextColor(textColorInt);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    String profileBackground = titleUser.getProfileBackgroundImageUrlHttps();
    if (profileBackground != null) {
      Picasso.with(this).load(profileBackground).into(toolbarBackground);
    }

    if (pageType == HOME) {
      btnCompose.setVisibility(View.VISIBLE);
      btnCompose.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          ComposeFragment fragment = ComposeFragment.newInstance(pageType, pageTypeRefId, user);
          fragment.show(getSupportFragmentManager(), "ComposeFragment");
        }
      });
    }
  }

  public void loadNewerTweets() {
    String tweetId = TweetListModel.getMostRecentTweetId(pageType, pageTypeRefId);
    if (tweetId != null) {
      // Get next new tweet, or else you'll load the same tweet twice.
      tweetId = new BigInteger(tweetId).add(BigInteger.ONE).toString();
    }
    loadTweets(tweetId, null);
  }

  public void loadOlderTweets() {
    String tweetId = TweetListModel.getOldestTweetId(pageType, pageTypeRefId);
    if (tweetId != null) {
      // Get next older tweet, or else you'll load the same tweet twice.
      tweetId = new BigInteger(tweetId).subtract(BigInteger.ONE).toString();
    }
    loadTweets(null, tweetId);
  }

  public void loadTweets(String minId, String maxId) {
    JsonHttpResponseHandler tweetResponseHandler = new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        try {
          // This handler will not persist anything until the DB until below, because
          // save() on the UI thread is too CPU expensive.
          final List<TweetModel> newTweets = TweetModel.fromJsonArray(response);
          // Sorted list maintains order.
          tweetAdapter.addAll(newTweets);
          swipeContainer.setRefreshing(false);

          // All DB persistence happens in this handler.
          dbSaveHandler.post(new Runnable() {
            @Override
            public void run() {
              Log.v(TAG,"Tweet load associate all STARTED: " +
                  DateFormat.getInstance().format(System.currentTimeMillis()));
              TweetListModel.associateAllAndSave(pageType, pageTypeRefId, newTweets);
              Log.v(TAG,"Tweet load associate all DONE: " +
                  DateFormat.getInstance().format(System.currentTimeMillis()));
            }
          });
        } catch (JSONException e) {
          showErrorSnackbar(SnackbarErrorType.INTERNAL, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              loadNewerTweets();
            }
          });
        } finally {
          swipeContainer.setRefreshing(false);
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
        swipeContainer.setRefreshing(false);
        boolean isThrottled = (statusCode - (statusCode % 10)) == 420;
        showErrorSnackbar(isThrottled ? SnackbarErrorType.THROTTLED : SnackbarErrorType.NETWORK,
            new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                loadNewerTweets();
              }
            });
      }
    };

    switch (pageType) {
      case HOME:
        client.getHomeTimeline(minId, maxId, tweetResponseHandler);
        break;
      case USER:
        client.getUserTimeline(pageTypeRefId, minId, maxId, tweetResponseHandler);
        break;
    }
  }


  public enum SnackbarErrorType {
    NETWORK,
    INTERNAL,
    THROTTLED
  }

  public void showErrorSnackbar(SnackbarErrorType errorType, View.OnClickListener listener) {
    // don't show again if already showing.
    if (errorSnackbar != null && errorSnackbar.isShownOrQueued()) {
      return;
    }

    int snackbarStrResId = R.string.snackbar_network;
    switch (errorType) {
      case INTERNAL:
        snackbarStrResId = R.string.snackbar_internal;
        break;
      case NETWORK:
        snackbarStrResId = R.string.snackbar_network;
        break;
      case THROTTLED:
        snackbarStrResId = R.string.snackbar_throttled;
    }
    errorSnackbar = Snackbar.make(findViewById(R.id.mainLayout),
        snackbarStrResId,
        Snackbar.LENGTH_INDEFINITE)
        .setAction(R.string.snackbar_retry, listener);
    errorSnackbar.show(); // Don’t forget to show!
  }

  @Override
  public void onHeaderClick(int position) {
    UserModel tweetUser = tweetAdapter.getItem(position).getUser();
    if (tweetUser == null || user.getId().equals(tweetUser.getId())
        || pageTypeRefId.equals(tweetUser.getId())) {
      return;
    }
    Intent intent = new Intent(this, TwitterActivity.class);
    intent.putExtra(ARG_YOUR_USER, Parcels.wrap(user));
    intent.putExtra(ARG_PAGE_TYPE, USER);
    intent.putExtra(ARG_PAGE_TYPE_REF_ID, tweetUser.getId());
    startActivity(intent);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (pageType != HOME) finish();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onComposerCompleted(boolean tweeted, TweetModel tweet) {
    // Temporarily adds tweet but does not store it in DB (to allow refresh to work later).
    tweetAdapter.add(tweet);
    // Scroll to top.
    rvTweets.scrollToPosition(0);
  }
}
