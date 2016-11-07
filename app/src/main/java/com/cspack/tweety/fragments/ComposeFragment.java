package com.cspack.tweety.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.databinding.ListTwitterBinding;
import com.cspack.tweety.databinding.TweetHeaderBinding;
import com.cspack.tweety.models.TweetListModel;
import com.cspack.tweety.models.TweetModel;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.util.TweetHeaderUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

/**
 * Created by chris on 10/29/16.
 */

public class ComposeFragment extends DialogFragment {
  private static final String ARG_PAGE_TYPE = "pageType";
  private static final String ARG_PAGE_TYPE_REF_ID = "pageTypeRefId";
  private static final String ARG_USER = "user";
  private OnComposeListener listener;
  private TweetListModel.PageType pageType;
  private String pageTypeRefId;
  private UserModel user;
  private UserModel theirUser;
  @Nullable
  private TweetModel referenceTweet;
  private TextInputEditText etCompose;
  private TextView tvError;
  private Button btnSubmit;
  private ProgressBar loadProgress;
  private FrameLayout replyTweetFrame;

  public interface OnComposeListener {
    void onComposerCompleted(boolean tweeted, TweetModel tweet);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      pageType = TweetListModel.PageType.values()[getArguments().getInt(ARG_PAGE_TYPE)];
      pageTypeRefId = getArguments().getString(ARG_PAGE_TYPE_REF_ID);
      user = Parcels.unwrap(getArguments().getParcelable(ARG_USER));
    }

    if (pageType == TweetListModel.PageType.USER) {
      theirUser = UserModel.byId(pageTypeRefId);
    }
    if (pageType == TweetListModel.PageType.TWEET_REPLY) {
      referenceTweet = TweetModel.byId(pageTypeRefId);
      theirUser = referenceTweet.getUser();
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnComposeListener) {
      listener = (OnComposeListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_compose, null);
    tvError = (TextView) view.findViewById(R.id.tvError);
    etCompose = (TextInputEditText) view.findViewById(R.id.etCompose);
    loadProgress = (ProgressBar) view.findViewById(R.id.progressBar);
    replyTweetFrame = (FrameLayout) view.findViewById(R.id.replyTweetFrame);
    etCompose.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        boolean shouldBeEnabled = s.length() > 0;
        if (s.length() > 120) {
          tvError.setVisibility(View.VISIBLE);
        } else {
          tvError.setVisibility(View.GONE);
        }
        if (s.length() >= 140) {
          shouldBeEnabled = false;
          // TODO(chris): localize this.
          tvError.setText("You have reached the tweet text limit.");
        } else {
          int remain = 140 - s.length();
          tvError.setText(String.format("You have %d character%s remaining.", remain,
              remain > 1 ? "s" : ""));
        }
        btnSubmit.setEnabled(shouldBeEnabled);
      }
    });

    etCompose = (TextInputEditText) view.findViewById(R.id.etCompose);
    TextView tweetAuthor = (TextView) view.findViewById(R.id.tvAuthor);
    ImageView authorLogo = (ImageView) view.findViewById(R.id.imageAuthor);
    TweetHeaderUtil.PopulateAuthorTextView(user, tweetAuthor);
    String imageUrl = user.getProfileImageUrl();
    if (imageUrl != null) {
      Picasso.with(authorLogo.getContext()).load(imageUrl).into(authorLogo);
    }

    btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
    btnSubmit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        postTweet();
      }
    });
    Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (listener != null) listener.onComposerCompleted(false, null);
        dismiss();
      }
    });

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.compose_dialog_title);
    builder.setView(view);

    if (theirUser != null && !theirUser.getId().equals(user.getId())) {
      etCompose.setText("@" + theirUser.getScreenName() + " ");
      etCompose.setSelection(etCompose.getText().length());
    }
    if (pageType == TweetListModel.PageType.TWEET_REPLY && referenceTweet != null) {
      View tweetCard = getActivity().getLayoutInflater()
          .inflate(R.layout.list_twitter, replyTweetFrame, false);
      ListTwitterBinding binding = ListTwitterBinding.bind(tweetCard);
      replyTweetFrame.removeAllViews();  // just to be careful.
      replyTweetFrame.addView(tweetCard);
      TweetHeaderUtil.PopulateTweetBinding(referenceTweet, false, false, binding, null);
    }
    return builder.create();
  }

  public static ComposeFragment newInstance(TweetListModel.PageType pageType, String pageTypeRefId, UserModel user) {
    ComposeFragment fragment = new ComposeFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_PAGE_TYPE, pageType.ordinal());
    args.putString(ARG_PAGE_TYPE_REF_ID, pageTypeRefId);
    args.putParcelable(ARG_USER, Parcels.wrap(user));
    fragment.setArguments(args);
    return fragment;
  }

  private JsonHttpResponseHandler postResult = new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
      TweetModel tweet = new TweetModel(response);
      tweet.save();
      if (listener != null) listener.onComposerCompleted(true, tweet);
      dismiss();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                          JSONObject errorResponse) {
      loadProgress.setVisibility(View.GONE);
      btnSubmit.setEnabled(true);
      etCompose.setEnabled(true);
      throwable.printStackTrace();
    }
  };

  private void postTweet() {
    loadProgress.setVisibility(View.VISIBLE);
    etCompose.setEnabled(false);
    btnSubmit.setEnabled(false);

    String replyToPost = null;
    String replyToUser = null;
    if (theirUser != null) {
      replyToUser = theirUser.getScreenName();
    }
    if (pageType == TweetListModel.PageType.TWEET_REPLY)
      replyToPost = pageTypeRefId;
    TwitterApplication.getRestClient().postTweet(etCompose.getText().toString(),
        replyToPost, replyToUser, postResult);
  }
}
