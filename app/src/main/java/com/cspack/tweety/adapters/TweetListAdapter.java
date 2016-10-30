package com.cspack.tweety.adapters;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cspack.tweety.R;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.util.TimestampUtil;
import com.cspack.tweety.util.TweetHeaderUtil;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by chris on 10/28/16.
 */

public class  TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.ViewHolder> {
  public interface TweetAdapterListener {
    void onHeaderClick(int position);
  }
  private TweetAdapterListener listener;

  public class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageAuthor;
    ImageView imagePhoto;
    LinearLayout tweetHeader;
    TextView tvAuthor;
    TextView tvText;
    TextView tvTweetAge;

    public ViewHolder (View view) {
      super(view);
      tweetHeader = (LinearLayout) view.findViewById(R.id.tweetHeader);
      imageAuthor = (ImageView) view.findViewById(R.id.imageAuthor);
      imagePhoto = (ImageView) view.findViewById(R.id.tweetPhoto);
      tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
      tvText = (TextView) view.findViewById(R.id.tvTweetText);
      tvTweetAge = (TextView) view.findViewById(R.id.tvTweetAge);

      // Static event listeners.
      tweetHeader.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          listener.onHeaderClick(getAdapterPosition());
        }
      });
    }

  };

  private SortedList<TweetModel> tweetList;
  public TweetListAdapter(@NonNull TweetAdapterListener listener) {
    this.tweetList = new SortedList<TweetModel>(TweetModel.class,
        new SortedListAdapterCallback<TweetModel>(this) {
      @Override
      public int compare(TweetModel o1, TweetModel o2) {
        // Put in descending order (highest ID goes at top).
        return (new BigInteger(o1.getId()).compareTo(new BigInteger(o2.getId()))) * -1;
      }

      @Override
      public boolean areContentsTheSame(TweetModel oldItem, TweetModel newItem) {
        if (oldItem.getId().equals(newItem.getId())) {
          // TODO: Check contents, but tweets should be static.
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
    holder.tvAuthor.setText("");
    holder.imageAuthor.setImageDrawable(null);
    holder.imagePhoto.setImageDrawable(null);

    if (tweet.getUser() != null) {
      TweetHeaderUtil.PopulateAuthorTextView(tweet.getUser(), holder.tvAuthor);
      String profileImageUrl = tweet.getUser().getProfileImageUrl();
      if (profileImageUrl != null) {
        Picasso.with(holder.imageAuthor.getContext()).load(profileImageUrl).into(
            holder.imageAuthor);
      }
      String tweetPhotoUrl = tweet.getFirstPhotoUrl();
      if (tweetPhotoUrl != null) {
        Picasso.with(holder.imagePhoto.getContext()).load(tweetPhotoUrl).transform(
            new RoundedCornersTransformation(10, 10)).into(holder.imagePhoto);
      }
    }
    holder.tvText.setText(tweet.getText());
    holder.tvTweetAge.setText(TimestampUtil.GetRelativeTimeAgo(tweet.getCreatedAt()));
  }

  @Override
  public void onViewDetachedFromWindow(ViewHolder holder) {
    Picasso.with(holder.imageAuthor.getContext()).cancelRequest(holder.imageAuthor);
    Picasso.with(holder.imagePhoto.getContext()).cancelRequest(holder.imagePhoto);
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
  @Override
  public int getItemCount() {
    return tweetList.size();
  }
}
