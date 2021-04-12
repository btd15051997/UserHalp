package sg.halp.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class HospitalProfile implements Parcelable {

    private String fullname;
    private String email;
    private String contactName;
    private String contacNumber;
    private String preferredUsername;
    private String postal;
    private String floorNumber;
    private String ward;
    private String picture;
    private String phoneNumber;
    private String address;
    private int operatorID;

    public HospitalProfile() {

    }

    protected HospitalProfile(Parcel in) {
        fullname = in.readString();
        email = in.readString();
        contactName = in.readString();
        contacNumber = in.readString();
        preferredUsername = in.readString();
        postal = in.readString();
        floorNumber = in.readString();
        ward = in.readString();
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
        dest.writeString(contacNumber);
        dest.writeString(preferredUsername);
        dest.writeString(postal);
        dest.writeString(floorNumber);
        dest.writeString(ward);
        dest.writeString(picture);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeInt(operatorID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HospitalProfile> CREATOR = new Creator<HospitalProfile>() {
        @Override
        public HospitalProfile createFromParcel(Parcel in) {
            return new HospitalProfile(in);
        }

        @Override
        public HospitalProfile[] newArray(int size) {
            return new HospitalProfile[size];
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

    public String getContacNumber() {
        return contacNumber;
    }

    public void setContacNumber(String contacNumber) {
        this.contacNumber = contacNumber;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
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
