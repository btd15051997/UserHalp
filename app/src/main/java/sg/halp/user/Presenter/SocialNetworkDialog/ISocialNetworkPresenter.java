package sg.halp.user.Presenter.SocialNetworkDialog;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

public interface ISocialNetworkPresenter {

    void handleGoogleSignInResult(GoogleSignInResult result);
    void handleFacebookSignIn(CallbackManager callbackManager);
}
