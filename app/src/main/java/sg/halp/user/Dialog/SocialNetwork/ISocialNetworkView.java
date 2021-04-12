package sg.halp.user.Dialog.SocialNetwork;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.Models.SocialMediaProfile;

public interface ISocialNetworkView {

    void initialGoogle();
    void initialFacebook();
    void onGoogleSignIn();
    void onLoginSuccess(SocialMediaProfile socialMediaProfile);
    void onLoginFail();
    void onFacebookLoginCancel();
    void setSocialNetworkListener(DialogFragmentCallback.SocialNetworkListener socialNetworkListener);
}
