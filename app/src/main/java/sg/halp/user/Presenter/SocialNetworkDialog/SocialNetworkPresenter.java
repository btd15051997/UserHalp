package sg.halp.user.Presenter.SocialNetworkDialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import sg.halp.user.Models.SocialMediaProfile;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Dialog.SocialNetwork.ISocialNetworkView;

public class SocialNetworkPresenter implements ISocialNetworkPresenter, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Activity activity;
    private ISocialNetworkView iSocialNetworkView;

    public SocialNetworkPresenter(Activity activity, ISocialNetworkView iSocialNetworkView) {
        this.activity = activity;
        this.iSocialNetworkView = iSocialNetworkView;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            final SocialMediaProfile socialMediaProfile = new SocialMediaProfile();

            if (googleSignInAccount != null) {

                socialMediaProfile.setFullname(googleSignInAccount.getDisplayName());
                EbizworldUtils.appLogDebug("HaoLS", "display name: " + googleSignInAccount.getDisplayName());

                socialMediaProfile.setPictureUrl(googleSignInAccount.getPhotoUrl().toString());

                EbizworldUtils.appLogDebug("HaoLS", "photo url: " + socialMediaProfile.getPictureUrl());

                socialMediaProfile.setEmailId(googleSignInAccount.getEmail());

                EbizworldUtils.appLogDebug("HaoLS", "Name: " + socialMediaProfile.getFullname() + ", email: " + socialMediaProfile.getEmailId() + ", Image: " + socialMediaProfile.getPictureUrl());

                /*if (personName != null){

                mediaProfile.setFullname(personName);

                }*/

                if (socialMediaProfile.getFullname().contains(" ")) {

                    String[] split = socialMediaProfile.getFullname().split(" ");
                    socialMediaProfile.setFirstName(split[0]);
                    socialMediaProfile.setLastName(split[1]);

                } else {

                    socialMediaProfile.setFirstName(socialMediaProfile.getFullname());

                }

                if (!TextUtils.isEmpty(socialMediaProfile.getPictureUrl())
                        || !socialMediaProfile.getPictureUrl().equalsIgnoreCase("null")) {

                    final String[] sPictureUrl = {socialMediaProfile.getPictureUrl()};

                    new AQuery(activity).id(R.id.social_img).image(sPictureUrl[0],
                            true,
                            true,
                            200,
                            0,
                            new BitmapAjaxCallback() {

                                @Override
                                public void callback(String url, ImageView iv, Bitmap bm,
                                                     AjaxStatus status) {

                                    if (url != null && !url.equals("")) {
                                        sPictureUrl[0] = new AQuery(activity).getCachedFile(url).getPath();
                                        socialMediaProfile.setPictureUrl(sPictureUrl[0]);
                                    }

                                }

                            });

                } else {

                    socialMediaProfile.setPictureUrl("");

                }

                socialMediaProfile.setSocialUniqueId(googleSignInAccount.getId());

                socialMediaProfile.setLoginType("google");

                iSocialNetworkView.onLoginSuccess(socialMediaProfile);

            }else {

                iSocialNetworkView.onLoginFail();

            }

        } else {

            iSocialNetworkView.onLoginFail();

        }

    }

    @Override
    public void handleFacebookSignIn(CallbackManager callbackManager) {

        final SocialMediaProfile socialMediaProfile = new SocialMediaProfile();

        LoginManager.getInstance()
                .logInWithReadPermissions(activity, Arrays.asList("public_profile", "email", "user_birthday", "user_photos", "user_location"));

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                                if (jsonObject != null && graphResponse != null) {

                                    EbizworldUtils.appLogDebug("HaoLS", "Facebook callback Json Object: " + jsonObject.toString());
                                    EbizworldUtils.appLogDebug("HaoLS", "Facebook callback Graph response: " + graphResponse.toString());

                                    try {

                                        socialMediaProfile.setFullname(jsonObject.getString("name"));
                                        socialMediaProfile.setEmailId(jsonObject.getString("email"));
                                        socialMediaProfile.setSocialUniqueId(jsonObject.getString("id"));

                                        socialMediaProfile.setPictureUrl("https://graph.facebook.com/" + socialMediaProfile.getSocialUniqueId() + "/picture?type=large");

                                        final String[] sPictureUrl = {socialMediaProfile.getPictureUrl()};

                                        new AQuery(activity).id(R.id.social_img).image(sPictureUrl[0],
                                                true,
                                                true,
                                                200,
                                                0,
                                                new BitmapAjaxCallback() {

                                                    @Override
                                                    public void callback(String url, ImageView iv, Bitmap bm,
                                                                         AjaxStatus status) {

                                                        if (url != null && !url.equals("")) {
                                                            sPictureUrl[0] = new AQuery(activity).getCachedFile(url).getPath();
                                                            EbizworldUtils.appLogDebug("HaoLS", "Avatar: " + sPictureUrl[0]);
                                                            socialMediaProfile.setPictureUrl(sPictureUrl[0]);
                                                        }

                                                    }

                                                });

                                        if (socialMediaProfile.getFullname() != null) {

                                            String[] name = socialMediaProfile.getFullname().split(" ");

                                            if (name[0] != null) {

                                                socialMediaProfile.setFirstName(name[0]);

                                            }

                                            if (name[1] != null) {

                                                socialMediaProfile.setLastName(name[1]);

                                            }
                                        }

                                        socialMediaProfile.setLoginType(Const.SOCIAL_FACEBOOK);

                                        EbizworldUtils.appLogDebug("all details: ", socialMediaProfile.getFullname() + " email: " + socialMediaProfile.getEmailId() + " picture: " + socialMediaProfile.getPictureUrl());

                                        if (socialMediaProfile.getSocialUniqueId() != null) {

                                            /*userLogin(Const.SOCIAL_FACEBOOK);*/

                                            iSocialNetworkView.onLoginSuccess(socialMediaProfile);

                                        } else {

                                            iSocialNetworkView.onLoginFail();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        EbizworldUtils.appLogError("HaoLS", "Facebook callback failed: " + e.toString());
                                    }

                                }
                            }
                        }

                );

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,locale,hometown,email,gender,birthday,location");
                request.setParameters(parameters);
                request.executeAsync();
            }


            @Override
            public void onCancel() {
                iSocialNetworkView.onFacebookLoginCancel();
            }

            @Override
            public void onError(FacebookException error) {

                iSocialNetworkView.onLoginFail();
                EbizworldUtils.appLogDebug("HaoLS", "Facebook loginManual failed " + error.toString());
            }
        });

    }
}
