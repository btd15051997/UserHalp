package sg.halp.user.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import sg.halp.user.Utils.Const;

public class Hospital extends RealmObject{

    @PrimaryKey
    @SerializedName(Const.Params.ID)
    private int id;

    @SerializedName(Const.Params.FULLNAME)
    private String mHospitalName;

    @SerializedName(Const.Params.CONTACT_NAME)
    private String mContactName;

    @SerializedName(Const.Params.CONTACT_NO)
    private String mContactNumber;

    @SerializedName(Const.Params.MOBILE)
    private String mMobileNumber;


    @SerializedName(Const.Params.WARD)
    private String mWard;

    @SerializedName(Const.Params.FLOOR_NUMBER)
    private int mFloorNumber;

    @SerializedName(Const.Params.EMAIL)
    private String mEmailAddress;

    @SerializedName(Const.Params.ADDRESS)
    private String mMainAddress;

    @SerializedName(Const.Params.POSTAL)
    private String mPostal;

    @SerializedName(Const.Params.PREFERRED_USERNAME)
    private String mPreferredUsername;

    @SerializedName(Const.Params.OPERATOR_ID)
    private int mAmbulanceOperator;

    @SerializedName(Const.Params.PICTURE)
    private String mCompanyPicture;

    public Hospital() {
    }

    public Hospital(int id,
                    String mHospitalName,
                    String mContactName,
                    String mContactNumber,
                    String mMobileNumber,
                    String mWard,
                    int mFloorNumber,
                    String mEmailAddress,
                    String mMainAddress,
                    String mPostal,
                    String mPreferredUsername,
                    int mAmbulanceOperator,
                    String mCompanyPicture) {

        this.id = id;
        this.mHospitalName = mHospitalName;
        this.mContactName = mContactName;
        this.mContactNumber = mContactNumber;
        this.mMobileNumber = mMobileNumber;
        this.mWard = mWard;
        this.mFloorNumber = mFloorNumber;
        this.mEmailAddress = mEmailAddress;
        this.mMainAddress = mMainAddress;
        this.mPostal = mPostal;
        this.mPreferredUsername = mPreferredUsername;
        this.mAmbulanceOperator = mAmbulanceOperator;
        this.mCompanyPicture = mCompanyPicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getmHospitalName() {
        return mHospitalName;
    }

    public void setmHospitalName(String mHospitalName) {
        this.mHospitalName = mHospitalName;
    }

    public String getmContactName() {
        return mContactName;
    }

    public void setmContactName(String mContactName) {
        this.mContactName = mContactName;
    }

    public String getmContactNumber() {
        return mContactNumber;
    }

    public void setmContactNumber(String mContactNumber) {
        this.mContactNumber = mContactNumber;
    }

    public String getmMobileNumber() {
        return mMobileNumber;
    }

    public void setmMobileNumber(String mMobileNumber) {
        this.mMobileNumber = mMobileNumber;
    }

    public String getmWard() {
        return mWard;
    }

    public void setmWard(String mWard) {
        this.mWard = mWard;
    }

    public int getmFloorNumber() {
        return mFloorNumber;
    }

    public void setmFloorNumber(int mFloorNumber) {
        this.mFloorNumber = mFloorNumber;
    }

    public String getmEmailAddress() {
        return mEmailAddress;
    }

    public void setmEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }

    public String getmMainAddress() {
        return mMainAddress;
    }

    public void setmMainAddress(String mMainAddress) {
        this.mMainAddress = mMainAddress;
    }

    public String getmPostal() {
        return mPostal;
    }

    public void setmPostal(String mPostal) {
        this.mPostal = mPostal;
    }

    public String getmPreferredUsername() {
        return mPreferredUsername;
    }

    public void setmPreferredUsername(String mPreferredUsername) {
        this.mPreferredUsername = mPreferredUsername;
    }

    public int getmAmbulanceOperator() {
        return mAmbulanceOperator;
    }

    public void setmAmbulanceOperator(int mAmbulanceOperator) {
        this.mAmbulanceOperator = mAmbulanceOperator;
    }

    public String getmCompanyPicture() {
        return mCompanyPicture;
    }

    public void setmCompanyPicture(String mCompanyPicture) {
        this.mCompanyPicture = mCompanyPicture;
    }
}
