package com.cspack.tweety.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cspack.tweety.BuildConfig;
import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.TwitterClient;
import com.cspack.tweety.adapters.TweetListAdapter;
import com.cspack.tweety.interfaces.TweetControllerInteractionListener;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.util.EndlessRecyclerViewScrollListener;
import com.cspack.tweety.util.SnackbarUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.cspack.tweety.models.TweetListModel.PageType.HOME;

/**
 * A fragment representing a list of Items. <p /> Activities containing this fragment MUST implement
 * the {@link TweetControllerInteractionListener} interface.
 */
public class TweetStreamFragment extends Fragment {
  public static final String ARG_PAGE_TYPE = "pageType";
  public static final String ARG_PAGE_TYPE_REF_ID = "pageTypeRefId";
  public static final String RECYCLER_SAVED_LAYOUT = "rvTweetsSavedInstanceState";
  public static final String SAVED_TWEET_ADAPTER = "tweetAdapterSavedInstanceState";
  private static final String TAG = TweetStreamFragment.class.getSimpleName();

  private TweetListModel.PageType pageType = HOME;
  private String pageTypeRefId = "";
  private TweetControllerInteractionListener listener;
  private TwitterClient client;

  private SwipeRefreshLayout swipeContainer;
  private TweetListAdapter tweetAdapter;
  private RecyclerView rvTweets;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the fragment (e.g. upon
   * screen orientation changes).
   */
  public TweetStreamFragment() {}

  @SuppressWarnings("unused")
  public static TweetStreamFragment newInstance(TweetListModel.PageType pageType,
                                                String pageTypeRefId) {
    TweetStreamFragment fragment = new TweetStreamFragment();
    Bundle args = new Bundle();
    args.putSerializable(ARG_PAGE_TYPE, pageType);
    args.putString(ARG_PAGE_TYPE_REF_ID, pageTypeRefId);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onResume() {
    super.onResume();
  }

  public void loadNewerTweets() {
    TweetListModel.getMostRecentTweetAsync(pageType, pageTypeRefId,
        new QueryTransaction.QueryResultSingleCallback<TweetListModel>() {
          @Override
          public void onSingleQueryResult(QueryTransaction transaction,
                                          @Nullable TweetListModel o) {
            String minId = null;
            if (o != null) {
              minId = new BigInteger(o.getTweet().getId()).subtract(BigInteger.ONE).toString();
            }
            loadTweets(minId, /*maxId=*/null);
          }
        });
  }

  public void loadOlderTweets(int totalItemCount) {
    // First try loading more from DB, if they exist.
    TweetListModel.recentItemsAsync(pageType, pageTypeRefId, totalItemCount,
        new QueryTransaction.QueryResultListCallback<TweetListModel>() {
          @Override
          public void onListQueryResult(QueryTransaction transaction,
                                        @Nullable List<TweetListModel> tResult) {
            if (tResult != null) {
              List<TweetModel> list = new ArrayList<>(tResult.size());
              for (TweetListModel tlm : tResult) {
                list.add(tlm.getTweet());
              }
              tweetAdapter.addAll(list);
            }
            // Download more if you hit the bottom.
            if (tResult == null || tResult.size() < TweetListModel.RESULT_LIMIT) {
              TweetListModel.getOldestTweetAsync(pageType, pageTypeRefId,
                  new QueryTransaction.QueryResultSingleCallback<TweetListModel>() {
                    @Override
                    public void onSingleQueryResult(QueryTransaction transaction,
                                                    @Nullable TweetListModel o) {
                      String maxId = null;
                      if (o != null) {
                        // Get next older tweet, or else you'll load the same tweet twice.
                        maxId = new BigInteger(o.getTweet().getId()).subtract(BigInteger.ONE)
                            .toString();
                      }
                      loadTweets(/*minId=*/null, maxId);
                    }
                  });
            }
          }
        });
  }

  private Boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager
        = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
  }

  public void loadTweets(final String minId, final String maxId) {
    if (!isNetworkAvailable()) {
      if (listener != null)
        listener.showError(SnackbarUtil.SnackbarErrorType.NETWORK, new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            loadTweets(minId, maxId);
          }
        });
      return;
    }
    JsonHttpResponseHandler tweetResponseHandler = new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        try {
          // This handler will not persist anything until the DB until below, because
          // save() on the UI thread is too CPU expensive.
          final List<TweetModel> newTweets = TweetModel.fromJsonArray(response);
          // Sorted list maintains order.
          tweetAdapter.addAll(newTweets);
          if (minId != null && maxId == null) {
            // If loading new tweets, scroll list to top.
            // TODO: refactor this into something more readable, it's a hack as-is.
            rvTweets.scrollToPosition(0);
          }
          swipeContainer.setRefreshing(false);

          Log.v(TAG, "Tweet load associate all STARTED: " + getTimestamp());
          TweetListModel.associateAllAndSave(pageType, pageTypeRefId, newTweets);
          Log.v(TAG, "Tweet load associate all DONE: " +
              getTimestamp());
        } catch (JSONException e) {
          listener.showError(SnackbarUtil.SnackbarErrorType.INTERNAL,
              new View.OnClickListener() {
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
        listener.showError(isThrottled ? SnackbarUtil.SnackbarErrorType.THROTTLED
                : SnackbarUtil.SnackbarErrorType.NETWORK,
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
      case MENTIONS:
        client.getMentionsTimeline(minId, maxId, tweetResponseHandler);
        break;
      case USER:
        client.getUserTimeline(pageTypeRefId, null, minId, maxId, tweetResponseHandler);
        break;
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      pageType = (TweetListModel.PageType) getArguments().getSerializable(ARG_PAGE_TYPE);
      pageTypeRefId = getArguments().getString(ARG_PAGE_TYPE_REF_ID);
    }
    client = TwitterApplication.getRestClient();
    tweetAdapter = new TweetListAdapter(listener);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tweet_stream, container, false);
    rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
    swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    // Initialize UI.
    rvTweets.setAdapter(tweetAdapter);
    rvTweets.setLayoutManager(new LinearLayoutManager(getContext()));
    rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(
        (LinearLayoutManager) rvTweets.getLayoutManager()) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadOlderTweets(totalItemsCount);
      }
    });
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        loadNewerTweets();
      }
    });
    if (savedInstanceState == null) {
      if (!swipeContainer.isRefreshing()) swipeContainer.setRefreshing(true);
      TweetListModel.recentItemsAsync(pageType, pageTypeRefId, tweetAdapter.getItemCount(),
          new QueryTransaction.QueryResultListCallback<TweetListModel>() {
            @Override
            public void onListQueryResult(QueryTransaction transaction,
                                          @Nullable List<TweetListModel> tResult) {
              swipeContainer.setRefreshing(false);
              // Populate list with initial DB.
              List<TweetModel> list = new ArrayList<>(tResult.size());
              for (TweetListModel tlm : tResult) {
                list.add(tlm.getTweet());
              }
              // Merged as a micro-optimization to reduce sorting.
              tweetAdapter.addAll(list);
              // TODO: Replace this with a time-based refresh window (if refreshed > 1 minute ago).
              if (tweetAdapter.getItemCount() == 0 || !BuildConfig.DEBUG) {
                loadNewerTweets();
              }
            }
          });
    } else {
      // Restore recycler view from saved instance state.
      tweetAdapter.addAll((List<TweetModel>) Parcels.unwrap(
          savedInstanceState.getParcelable(SAVED_TWEET_ADAPTER)));
      rvTweets.getLayoutManager().onRestoreInstanceState(
          savedInstanceState.getParcelable(RECYCLER_SAVED_LAYOUT));
    }
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (rvTweets != null)
      outState.putParcelable(RECYCLER_SAVED_LAYOUT,
          rvTweets.getLayoutManager().onSaveInstanceState());
    if (tweetAdapter != null)
      outState.putParcelable(SAVED_TWEET_ADAPTER, Parcels.wrap(tweetAdapter.getItems()));
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof TweetControllerInteractionListener) {
      listener = (TweetControllerInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement TweetControllerInteractionListener");
    }
  }

  private static String getTimestamp() {
    return new SimpleDateFormat("HH:mm:ss.SSS").getInstance().format(System.currentTimeMillis());
  }

  @Override
  public void onPause() {
    super.onPause();
    // Bug fix: http://stackoverflow.com/questions/29773876/swiperefreshlayout-refresh-conflict-with-fragmenttransactions
    if (swipeContainer!=null) {
      swipeContainer.setRefreshing(false);
      swipeContainer.destroyDrawingCache();
      swipeContainer.clearAnimation();
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    listener = null;
  }
}
