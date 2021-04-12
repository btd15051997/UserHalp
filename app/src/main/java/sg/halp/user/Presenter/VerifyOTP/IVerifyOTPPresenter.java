package sg.halp.user.Presenter.VerifyOTP;

public interface IVerifyOTPPresenter {

    void getOTP(String mobile, String countryCode);
    void checkOTP(String code, String userInputCode);
}
