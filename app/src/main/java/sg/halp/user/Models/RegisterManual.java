package sg.halp.user.Models;

public class RegisterManual {

    private String fullname;
    private String email;
    private String password;
    private String mobile;
    private String countryCode;
    private String currencyCode;
    private String deviceToken;
    private String deviceType;
    private String loginBy;
    private String timeZone;
    private String referralCode;

    public RegisterManual() {
    }

    public RegisterManual(String fullname,
                          String email,
                          String password,
                          String mobile,
                          String countryCode,
                          String currencyCode,
                          String deviceToken,
                          String deviceType,
                          String loginBy,
                          String timeZone,
                          String referralCode) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.countryCode = countryCode;
        this.currencyCode = currencyCode;
        this.deviceToken = deviceToken;
        this.deviceType = deviceType;
        this.loginBy = loginBy;
        this.timeZone = timeZone;
        this.referralCode = referralCode;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLoginBy() {
        return loginBy;
    }

    public void setLoginBy(String loginBy) {
        this.loginBy = loginBy;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
