package sg.halp.user.Fragment.ForgotPassword;

public interface IForgotPasswordView {

    void onCancel();
    void onUsernameError();
    void onDisconnectNetwork();
    void showProgressDialog();
    void hideProgressDialog();
    void onRequestSuccess();
    void onRequestFail(String error);
}
