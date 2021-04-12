package sg.halp.user.Models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import sg.halp.user.Utils.Const;

public class NursingHome extends RealmObject {

    @PrimaryKey
    @SerializedName(Const.Params.ID)
    private int id;

    @SerializedName(Const.Params.FULLNAME)
    private String mFullName;

    @SerializedName(Const.Params.CONTACT_NAME)
    private String mContact_Name;

    @SerializedName(Const.Params.CONTACT_NO)
    private String mContact_Number;

    @SerializedName(Const.Params.PREFERRED_USERNAME)
    private String mPreferred_Username;

    @SerializedName(Const.Params.MOBILE)
    private String mMobile;

    @SerializedName(Const.Params.EMAIL)
    private String mEmail;

    @SerializedName(Const.Params.ADDRESS)
    private String mAddress;

    @SerializedName(Const.Params.PICTURE)
    private String mPictureUrl;

    @SerializedName(Const.Params.TIMEZONE)
    private String mTimezone;

    @SerializedName(Const.Params.GENDER)
    private String mGender;

    @SerializedName(Const.Params.COUNTRY)
    private String mCountry;

    @SerializedName(Const.Params.CURRENCY_CODE)
    private String mCurrencyCode;

    @SerializedName(Const.Params.OPERATOR_ID)
    private int mOperatorID;

    public int getmOperatorID() {
        return mOperatorID;
    }

    public void setmOperatorID(int mOperatorID) {
        this.mOperatorID = mOperatorID;
    }

    public NursingHome() {

    }

    public NursingHome(int id,
                       String mFullName,
                       String mContact_Name,
                       String mContact_Number,
                       String mPreferred_Username,
                       String mMobile,
                       String mEmail,
                       String mAddress,
                       String mPictureUrl,
                       String mTimezone,
                       String mGender,
                       String mCountry,
                       String mCurrencyCode) {

        this.id = id;
        this.mFullName = mFullName;
        this.mContact_Name = mContact_Name;
        this.mContact_Number = mContact_Number;
        this.mPreferred_Username = mPreferred_Username;
        this.mMobile = mMobile;
        this.mEmail = mEmail;
        this.mAddress = mAddress;
        this.mPictureUrl = mPictureUrl;
        this.mTimezone = mTimezone;
        this.mGender = mGender;
        this.mCountry = mCountry;
        this.mCurrencyCode = mCurrencyCode;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmFullName() {
        return mFullName;
    }

    public void setmFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getmContact_Name() {
        return mContact_Name;
    }

    public void setmContact_Name(String mContact_Name) {
        this.mContact_Name = mContact_Name;
    }

    public String getmContact_Number() {
        return mContact_Number;
    }

    public void setmContact_Number(String mContact_Number) {
        this.mContact_Number = mContact_Number;
    }

    public String getmPreferred_Username() {
        return mPreferred_Username;
    }

    public void setmPreferred_Username(String mPreferred_Username) {
        this.mPreferred_Username = mPreferred_Username;
    }

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmPictureUrl() {
        return mPictureUrl;
    }

    public void setmPictureUrl(String mPictureUrl) {
        this.mPictureUrl = mPictureUrl;
    }

    public String getmTimezone() {
        return mTimezone;
    }

    public void setmTimezone(String mTimezone) {
        this.mTimezone = mTimezone;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmCurrencyCode() {
        return mCurrencyCode;
    }

    public void setmCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }
}
