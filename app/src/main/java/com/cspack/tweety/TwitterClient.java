package com.cspack.tweety;

import android.content.Context;
import android.support.annotation.Nullable;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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

  public void getHomeTimeline(String minId, String maxId, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    RequestParams params = new RequestParams();
    // Load 25 tweets if it's a first load, otherwise load ALL new tweets (up to 200).
    params.put("count", minId == null ? 25 : 200);
    params.put("contributor_details", "true");
    if (minId != null) {
      params.put("since_id", minId);
    }
    if (maxId != null) {
      params.put("max_id", maxId);
    }
    client.get(apiUrl, params, handler);
  }

  public void getUserTimeline(@NotNull String user, String minId, String maxId,
                              AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/user_timeline.json");
    RequestParams params = new RequestParams();
    // Load 25 tweets if it's a first load, otherwise load ALL new tweets (up to 200).
    params.put("user_id", user);
    params.put("count", minId == null ? 25 : 200);
    params.put("contributor_details", "true");
    if (minId != null) {
      params.put("since_id", minId);
    }
    if (maxId != null) {
      params.put("max_id", maxId);
    }
    client.get(apiUrl, params, handler);
  }

  public void getUser(AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("account/verify_credentials.json");
    client.get(apiUrl, handler);
  }

  public void postTweet(@NotNull String tweetText, @Nullable String replyToPost,
                        @Nullable String replyToUser, AsyncHttpResponseHandler handler) {
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
}