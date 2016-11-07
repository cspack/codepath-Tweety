package com.cspack.tweety.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.cspack.tweety.R;
import com.cspack.tweety.TwitterApplication;
import com.cspack.tweety.TwitterClient;
import com.cspack.tweety.models.UserModel;
import com.cspack.tweety.util.SnackbarUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterClient> {

  private Button btnLogin;
  private TextView tvLoginFailure;
  private ProgressBar progressBar;
  private SnackbarUtil snackbarUtil;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    tvLoginFailure = (TextView) findViewById(R.id.textView);
    btnLogin = (Button) findViewById(R.id.btnLogin);
    progressBar = (ProgressBar) findViewById(R.id.progressBar);
    if (TwitterApplication.getRestClient().checkAccessToken() != null) {
      progressBar.setVisibility(View.VISIBLE);
      btnLogin.setVisibility(View.GONE);
      tvLoginFailure.setVisibility(View.GONE);
    }
    snackbarUtil = new SnackbarUtil(findViewById(android.R.id.content));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.login, menu);
    return true;
  }

  @Override
  public void onLoginSuccess() {
    progressBar.setVisibility(View.VISIBLE);
    btnLogin.setVisibility(View.GONE);
    tvLoginFailure.setVisibility(View.GONE);
    // Lookup your user before continuing.
    TwitterApplication.getRestClient().getYourUser(new JsonHttpResponseHandler() {
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        UserModel user = new UserModel(response);
        user.save();
        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        i.putExtra(HomeActivity.ARG_YOUR_USER, Parcels.wrap(user));
        LoginActivity.this.startActivity(i);
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
        onLoginFailure(null);
      }
    });
  }

  // OAuth authentication flow failed, handle the error
  // i.e Display an error dialog or toast
  @Override
  public void onLoginFailure(Exception e) {
    snackbarUtil.show(SnackbarUtil.SnackbarErrorType.LOGIN_FAILED, null);
    progressBar.setVisibility(View.GONE);
    btnLogin.setVisibility(View.VISIBLE);
    tvLoginFailure.setVisibility(View.VISIBLE);
    if (e != null) e.printStackTrace();
  }

  // Click handler method for the button used to start OAuth flow
  // Uses the client to initiate OAuth authorization
  // This should be tied to a button used to login
  public void loginToRest(View view) {
    getClient().connect();
    btnLogin.setVisibility(View.GONE);
    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void onStop() {
    super.onStop();
  }
}
