package sg.halp.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class NursingHomeProfile implements Parcelable {

    private String fullname;
    private String email;
    private String contactName;
    private String contactNumber;
    private String preferredUsername;
    private String picture;
    private String phoneNumber;
    private String address;
    private int operatorID;

    public NursingHomeProfile() {

    }

    protected NursingHomeProfile(Parcel in) {
        fullname = in.readString();
        email = in.readString();
        contactName = in.readString();
        contactNumber = in.readString();
        preferredUsername = in.readString();
        picture = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        operatorID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullname);
        dest.writeString(email);
        dest.writeString(contactName);
        dest.writeString(contactNumber);
        dest.writeString(preferredUsername);
        dest.writeString(picture);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeInt(operatorID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NursingHomeProfile> CREATOR = new Creator<NursingHomeProfile>() {
        @Override
        public NursingHomeProfile createFromParcel(Parcel in) {
            return new NursingHomeProfile(in);
        }

        @Override
        public NursingHomeProfile[] newArray(int size) {
            return new NursingHomeProfile[size];
        }
    };

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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(int operatorID) {
        this.operatorID = operatorID;
    }
}
