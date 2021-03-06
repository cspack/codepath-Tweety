package com.cspack.tweety;

import android.content.Context;
import android.support.annotation.Nullable;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.raizlabs.android.dbflow.annotation.NotNull;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
  public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
  public static final String REST_URL = "https://api.twitter.com/1.1";
  public static final String REST_CONSUMER_KEY = "usMbXqclbRC9MesIp13BwhhJR";
  public static final String REST_CONSUMER_SECRET =
      "2hpOswSiz4LjQtninYolLHeICNWgiQlNKGc8bKwRz9V8dxYcKs";
  public static final String REST_CALLBACK_URL = "x-oauthflow-twitter://cspacktweety";

  public TwitterClient(Context context) {
    super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
          REST_CALLBACK_URL);
  }

  private void getTimeline(String url, String minId, String maxId, String user, String screenName,
                           JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl(url);
    RequestParams params = new RequestParams();
    params.put("count", 200);
    params.put("contributor_details", "true");
    // TODO: configure this, it allows retweets.
    params.put("include_rts", "true");
    // TODO: configure this, this enables replies
    params.put("exclude_replies", "false");
    if (minId != null) {
      params.put("since_id", minId);
    }
    if (maxId != null) {
      params.put("max_id", maxId);
    }
    if (user != null) {
      params.put("user_id", user);
    }
    if (screenName != null) {
      params.put("screen_name", screenName);
    }
    client.get(apiUrl, params, handler);
  }
  public void getHomeTimeline(String minId, String maxId, JsonHttpResponseHandler handler) {
    getTimeline("statuses/home_timeline.json", minId, maxId, null, null, handler);
  }

  public void getMentionsTimeline(String minId, String maxId, JsonHttpResponseHandler handler) {
    getTimeline("statuses/mentions_timeline.json", minId, maxId, null, null, handler);
  }

  public void getUserTimeline(@NotNull String user, @Nullable String screenName, String minId,
                              String maxId, JsonHttpResponseHandler handler) {
    getTimeline("statuses/user_timeline.json", minId, maxId, user, screenName, handler);
  }

  public void getYourUser(AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("account/verify_credentials.json");
    client.get(apiUrl, handler);
  }

  public void lookupScreenName(String screenName, JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("users/lookup.json");
    RequestParams params = new RequestParams();
    params.add("screen_name", screenName);
    client.get(apiUrl, params, handler);
  }

  public void postTweet(@NotNull String tweetText, @Nullable String replyToPost,
                        @Nullable String replyToUser, JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("/statuses/update.json");
    RequestParams params = new RequestParams();
    if (replyToPost != null && replyToUser != null) {
      params.put("in_reply_to_status_id", replyToPost);
      if (!tweetText.contains("@" + replyToUser)) {
        tweetText = String.format("@%s %s", replyToUser, tweetText);
      }
    }
    params.put("status", tweetText);
    client.post(apiUrl, params, handler);
  }

  public void markTweetAsFavorite(String tweetId, boolean isFavorite,
                                  JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl(isFavorite ? "/favorites/create.json" : "/favorites/destroy.json");
    RequestParams params = new RequestParams();
    params.put("id", tweetId);
    client.post(apiUrl, params, handler);
  }

  public void postRetweet(String tweetId, boolean doRetweet, JsonHttpResponseHandler handler) {
    StringBuilder urlBuilder = new StringBuilder("/statuses/");
    urlBuilder.append(doRetweet ? "retweet/" : "unretweet/");
    urlBuilder.append(tweetId);
    urlBuilder.append(".json");
    String apiUrl = getApiUrl(urlBuilder.toString());
    client.post(apiUrl, handler);
  }
}