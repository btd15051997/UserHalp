package sg.halp.user.Presenter.SignInPatient;

import sg.halp.user.Models.SocialMediaProfile;

public interface ISignInPatientPresenter {

    void loginManual(String username, String password, String loginType);
    void loginSocialNetwork(SocialMediaProfile socialMediaProfile, String loginType);
}
