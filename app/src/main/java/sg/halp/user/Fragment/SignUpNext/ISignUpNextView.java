package sg.halp.user.Fragment.SignUpNext;

import sg.halp.user.Models.RegisterManual;

public interface ISignUpNextView {

    void onClose();
    void onPrivacyPolicy();
    void onTermConditions();
    void onFullNameError();
    void onEmailError();
    void onEmailIncorrect();
    void onPasswordError();
    void onPolicyTermsUncheck();
    RegisterManual registerManual();
    void onDisconnectNetwork();
    void showProgressDialog();
    void hideProgressDialog();
    void onRegisterSuccess();
    void onRegisterFail(String error);
    void onApplyReferralCodeResponse(String message);
}
