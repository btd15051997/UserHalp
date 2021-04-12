package sg.halp.user.Fragment.SignUpMobile;

import android.support.v4.app.Fragment;

public interface ISignUpMobileView {

    void onClose();
    void onMobileError();
    void onDisconnectNetwork();
    void showProgressDialog();
    void hideProgressDialog();
    void addFragment(Fragment fragment, boolean isAddToBackStack, String tag, boolean isAnimate);
    void onGetOTPSuccess(String code);
    void onGetOTPFail(String error);
}
