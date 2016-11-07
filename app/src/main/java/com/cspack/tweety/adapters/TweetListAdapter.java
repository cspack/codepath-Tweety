package com.cspack.tweety.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.TwitterClient;
import com.cspack.tweety.databinding.ListTwitterBinding;
import com.cspack.tweety.databinding.TweetHeaderBinding;
import com.cspack.tweety.interfaces.TweetControllerInteractionListener;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.util.NetworkUtil;
import com.cspack.tweety.util.SnackbarUtil;
import com.cspack.tweety.util.TimestampUtil;
import com.cspack.tweety.util.TweetHeaderUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import org.json.JSONObject;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chris on 10/28/16.
 */

public class  TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.ViewHolder> {
  private TweetControllerInteractionListener listener;

  public class ViewHolder extends RecyclerView.ViewHolder {
    Context context;
    ListTwitterBinding binding;
    TweetHeaderBinding tweetHeaderBinding;

    public ViewHolder (View view) {
      super(view);
      context = view.getContext();
      binding = ListTwitterBinding.bind(view);
      tweetHeaderBinding = binding.tweetHeaderInclude;

      // Static event listeners.
      tweetHeaderBinding.tweetHeader.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          TweetModel tweet = getItem(getAdapterPosition());
          if (tweet == null) {
            return;
          }
          UserModel user = tweet.getUser();
          if (user == null) {
            return;
          }
          listener.startAction(TweetListModel.PageType.USER, user.getId());
        }
      });

      binding.tvFavorites.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (NetworkUtil.isNetworkAvailable(v.getContext())) {
            TweetModel tweet = getItem(getAdapterPosition());
            if (tweet == null) return;
            TwitterApplication.getRestClient().markTweetAsFavorite(
                tweet.getId(), !tweet.isFavorited(), new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TweetModel newTweet = new TweetModel(response);
                    newTweet.update();
                    int id = findTweetPositionById(newTweet.getId());
                    if (id != -1) {
                      tweetList.updateItemAt(id, newTweet);
                    } else {
                      if (listener != null) listener.showError(
                          SnackbarUtil.SnackbarErrorType.INTERNAL, null);
                    }
                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                        JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (listener != null)
                      listener.showError(SnackbarUtil.SnackbarErrorType.NETWORK, null);
                  }
                }

            );
          } else {
            if (listener != null)
              listener.showError(SnackbarUtil.SnackbarErrorType.OFFLINE, null);
          }
        }
      });


      binding.tvRetweets.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (NetworkUtil.isNetworkAvailable(v.getContext())) {
            TweetModel tweet = getItem(getAdapterPosition());
            if (tweet == null) return;
            TwitterApplication.getRestClient().postRetweet(
                tweet.getId(), !tweet.isRetweeted(), new JsonHttpResponseHandler() {
                  @Override
                  public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TweetModel newTweet = new TweetModel(response);
                    newTweet.update();
                    int id = findTweetPositionById(newTweet.getId());
                    if (id != -1) {
                      tweetList.updateItemAt(id, newTweet);
                    } else {
                      if (listener != null) listener.showError(
                          SnackbarUtil.SnackbarErrorType.INTERNAL, null);
                    }
                  }

                  @Override
                  public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                        JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    if (listener != null)
                      listener.showError(SnackbarUtil.SnackbarErrorType.NETWORK, null);
                  }
                }

            );
          } else {
            if (listener != null)
              listener.showError(SnackbarUtil.SnackbarErrorType.OFFLINE, null);
          }
        }
      });

      binding.tvReplies.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener == null) return;
          if (NetworkUtil.isNetworkAvailable(v.getContext())) {
            TweetModel tweet = getItem(getAdapterPosition());
            listener.startAction(TweetListModel.PageType.TWEET_REPLY, tweet.getId());
          } else {
            listener.showError(SnackbarUtil.SnackbarErrorType.NETWORK, null);
          }
        }
      });
    }
  };

  private SortedList<TweetModel> tweetList;
  public TweetListAdapter(@NonNull TweetControllerInteractionListener listener) {
    this.tweetList = new SortedList<TweetModel>(TweetModel.class,
        new SortedListAdapterCallback<TweetModel>(this) {
      @Override
      public int compare(TweetModel o1, TweetModel o2) {
        // Put in descending order (highest ID goes at top).
        return (new BigInteger(o1.getId()).compareTo(new BigInteger(o2.getId()))) * -1;
      }

      @Override
      public boolean areContentsTheSame(TweetModel oldItem, TweetModel newItem) {
        if (oldItem.getId().equals(newItem.getId()) &&
            oldItem.isFavorited() == newItem.isFavorited() &&
            oldItem.isRetweeted() == newItem.isRetweeted() &&
            oldItem.getFavoritesCount() == newItem.getFavoritesCount() &&
            oldItem.getRetweetCount() == newItem.getRetweetCount()) {
          return true;
        }
        return false;
      }

      @Override
      public boolean areItemsTheSame(TweetModel item1, TweetModel item2) {
        if (item1.getId().equals(item2.getId())) {
          // TODO: Check contents, but tweets should be static.
          return true;
        }
        return false;
      }
    });
    this.listener = listener;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.list_twitter, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    TweetModel tweet = tweetList.get(position);
    if (tweet == null) {
      // Ignore, this can happen at load time.
      return;
    }
    TweetHeaderUtil.PopulateTweetBinding(tweet, true, true, holder.binding);
  }

  @Override
  public void onViewDetachedFromWindow(ViewHolder holder) {
    // It's proper to cancel requests in adapters.
    Picasso.with(holder.context).cancelRequest(holder.tweetHeaderBinding.imageAuthor);
    Picasso.with(holder.context).cancelRequest(holder.binding.tweetPhoto);
  }

  public TweetModel getItem(int position) {
    return tweetList.get(position);
  }
  public void add(TweetModel tweet) {
    tweetList.add(tweet);
  }
  public void addAll(List<TweetModel> tweets) {
    tweetList.addAll(tweets);
  }
  public List<TweetModel> getItems() {
    List<TweetModel> list = new ArrayList<>(tweetList.size());
    for (int i = 0; i < tweetList.size(); i++) {
      list.add(tweetList.get(i));
    }
    return list;
  }

  public int findTweetPositionById(String tweetId) {
    for (int i = 0; i < tweetList.size(); i++) {
      if (tweetList.get(i).getId().equals(tweetId)) return i;
    }
    return -1;
  }

  @Override
  public int getItemCount() {
    return tweetList.size();
  }
}
