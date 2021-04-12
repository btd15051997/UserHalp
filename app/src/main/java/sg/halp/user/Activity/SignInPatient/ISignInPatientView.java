package sg.halp.user.Activity.SignInPatient;

import android.support.v4.app.Fragment;

public interface ISignInPatientView {

    void onUsernameError();
    void onPasswordError();
    void onCancel();
    void onLoginSuccess();
    void onLoginFail(String error);
    void onNetworkDisconnect();
    void onShowProgressDialog();
    void onHideProgressDialog();
    void showSocialNetworkDialog();
    void addFragment(Fragment fragment, boolean addToBackStack, String tag, boolean isAnimate);
    void onNewUser();
    void onForgetPassword();

}
