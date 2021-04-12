package sg.halp.user.Activity.SignInNursingHome;

public interface ISignInNursingHomeView {

    void onUsernameError();
    void onPasswordError();
    void onCancel();
    void onLoginSuccess();
    void onLoginFail(String error);
    void onNetworkDisconnect();
    void onShowProgressDialog();
    void onHideProgressDialog();

}
