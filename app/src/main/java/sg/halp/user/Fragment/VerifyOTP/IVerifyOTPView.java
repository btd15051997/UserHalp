package sg.halp.user.Fragment.VerifyOTP;

public interface IVerifyOTPView {

    void onClose();
    void onEditNumber();
    void onOTPError();
    void onVerifyFail();
    void onVerifySuccess();
    void onDisconnectNetwork();
    void showProgressDialog();
    void hideProgressDialog();
    void setCode(String code);
    void onDisplayOTPCode(String code);
    void onGetOTPFail(String error);
    void onMobileError();
}
