package sg.halp.user.Activity.SignInHospital;

public interface ISignInHospitalView {

    void onUsernameError();
    void onPasswordError();
    void onCancel();
    void onLoginSuccess();
    void onLoginFail(String error);
    void onNetworkDisconnect();
    void onShowProgressDialog();
    void onHideProgressDialog();

}
