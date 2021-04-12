package sg.halp.user.Presenter.SignUpNext;

import android.widget.CheckBox;

import sg.halp.user.Models.RegisterManual;

public interface ISignUpNextPresenter {

    void onRegister(RegisterManual registerManual, CheckBox cb_policy_terms);
    void onApplyReferralCode(String code);

}
