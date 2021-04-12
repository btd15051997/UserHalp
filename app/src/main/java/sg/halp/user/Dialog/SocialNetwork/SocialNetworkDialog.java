package sg.halp.user.Dialog.SocialNetwork;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.Models.SocialMediaProfile;
import sg.halp.user.Presenter.SocialNetworkDialog.SocialNetworkPresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.EbizworldUtils;

public class SocialNetworkDialog extends DialogFragment
        implements ISocialNetworkView,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    @BindView(R.id.back_social)
    ImageView back_social;

    @BindView(R.id.lay_google)
    LinearLayout lay_google;

    @BindView(R.id.lay_fb)
    LinearLayout lay_fb;

    private Activity activity;
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private SocialNetworkPresenter mSocialNetworkPresenter;
    private DialogFragmentCallback.SocialNetworkListener socialNetworkListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(activity, R.style.DialogThemeforview);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.social_connect_popup);

        ButterKnife.bind(this, dialog);

        mSocialNetworkPresenter = new SocialNetworkPresenter(activity, this);

        initialGoogle();

        back_social.setOnClickListener(this);
        lay_fb.setOnClickListener(this);
        lay_google.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();

        LoginManager.getInstance().logOut();

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()){

            mGoogleApiClient.disconnect();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mSocialNetworkPresenter.handleGoogleSignInResult(result);
            EbizworldUtils.appLogInfo("HaoLS", "RC_SIGN_IN " + result.toString());

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.back_social:{

                dismiss();

            }
            break;

            case R.id.lay_google:{

                onGoogleSignIn();

            }
            break;

            case R.id.lay_fb:{

                initialFacebook();

            }
            break;

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        EbizworldUtils.appLogError("HaoLS", "GoogleApiClient onConnectionFailed: " + connectionResult);

    }

    @Override
    public void initialGoogle() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void initialFacebook() {

        FacebookSdk.sdkInitialize(activity);
        CallbackManager callbackManager = CallbackManager.Factory.create();

        mSocialNetworkPresenter.handleFacebookSignIn(callbackManager);

    }

    @Override
    public void onGoogleSignIn() {

        EbizworldUtils.showSimpleProgressDialog(activity, getResources().getString(R.string.txt_gmail), false);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onLoginSuccess(SocialMediaProfile socialMediaProfile) {

        dismiss();

        if (socialNetworkListener != null){

            socialNetworkListener.onSuccess(socialMediaProfile);

        }

    }

    @Override
    public void onLoginFail() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.login_failed), activity);

    }

    @Override
    public void onFacebookLoginCancel() {

        EbizworldUtils.showLongToast(getString(R.string.login_cancelled), activity);

    }

    @Override
    public void setSocialNetworkListener(DialogFragmentCallback.SocialNetworkListener socialNetworkListener) {
        this.socialNetworkListener = socialNetworkListener;
    }
}
