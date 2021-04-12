package sg.halp.user.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PatientProfile implements Parcelable {

    private String picture;
    private String fullname;
    private String familyMember;
    private String phoneNumeber;
    private String email;
    private String homeNumber;
    private String blockNumber;
    private String unitNumber;
    private String postal;
    private String streetName;
    private String preferredUsername;
    private String weight;
    private int liftLanding;
    private int stairs;
    private int noStairs;
    private int lowStairs;
    private int operatorID;
    private String patientCondition;
    private int stretcher;
    private int oxygen;
    private int wheelchair;
    private int escort;
    private String additionInformation;
    private String paymentMode;

    public PatientProfile() {

    }

    protected PatientProfile(Parcel in) {
        picture = in.readString();
        fullname = in.readString();
        familyMember = in.readString();
        phoneNumeber = in.readString();
        email = in.readString();
        homeNumber = in.readString();
        blockNumber = in.readString();
        unitNumber = in.readString();
        postal = in.readString();
        streetName = in.readString();
        preferredUsername = in.readString();
        weight = in.readString();
        liftLanding = in.readInt();
        stairs = in.readInt();
        noStairs = in.readInt();
        lowStairs = in.readInt();
        operatorID = in.readInt();
        patientCondition = in.readString();
        stretcher = in.readInt();
        oxygen = in.readInt();
        wheelchair = in.readInt();
        escort = in.readInt();
        additionInformation = in.readString();
        paymentMode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(picture);
        dest.writeString(fullname);
        dest.writeString(familyMember);
        dest.writeString(phoneNumeber);
        dest.writeString(email);
        dest.writeString(homeNumber);
        dest.writeString(blockNumber);
        dest.writeString(unitNumber);
        dest.writeString(postal);
        dest.writeString(streetName);
        dest.writeString(preferredUsername);
        dest.writeString(weight);
        dest.writeInt(liftLanding);
        dest.writeInt(stairs);
        dest.writeInt(noStairs);
        dest.writeInt(lowStairs);
        dest.writeInt(operatorID);
        dest.writeString(patientCondition);
        dest.writeInt(stretcher);
        dest.writeInt(oxygen);
        dest.writeInt(wheelchair);
        dest.writeInt(escort);
        dest.writeString(additionInformation);
        dest.writeString(paymentMode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PatientProfile> CREATOR = new Creator<PatientProfile>() {
        @Override
        public PatientProfile createFromParcel(Parcel in) {
            return new PatientProfile(in);
        }

        @Override
        public PatientProfile[] newArray(int size) {
            return new PatientProfile[size];
        }
    };

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFamilyMember() {
        return familyMember;
    }

    public void setFamilyMember(String familyMember) {
        this.familyMember = familyMember;
    }

    public String getPhoneNumeber() {
        return phoneNumeber;
    }

    public void setPhoneNumeber(String phoneNumeber) {
        this.phoneNumeber = phoneNumeber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getLiftLanding() {
        return liftLanding;
    }

    public void setLiftLanding(int liftLanding) {
        this.liftLanding = liftLanding;
    }

    public int getStairs() {
        return stairs;
    }

    public void setStairs(int stairs) {
        this.stairs = stairs;
    }

    public int getNoStairs() {
        return noStairs;
    }

    public void setNoStairs(int noStairs) {
        this.noStairs = noStairs;
    }

    public int getLowStairs() {
        return lowStairs;
    }

    public void setLowStairs(int lowStairs) {
        this.lowStairs = lowStairs;
    }

    public int getOperatorID() {
        return operatorID;
    }

    public void setOperatorID(int operatorID) {
        this.operatorID = operatorID;
    }

    public String getPatientCondition() {
        return patientCondition;
    }

    public void setPatientCondition(String patientCondition) {
        this.patientCondition = patientCondition;
    }

    public int getStretcher() {
        return stretcher;
    }

    public void setStretcher(int stretcher) {
        this.stretcher = stretcher;
    }

    public int getOxygen() {
        return oxygen;
    }

    public void setOxygen(int oxygen) {
        this.oxygen = oxygen;
    }

    public int getWheelchair() {
        return wheelchair;
    }

    public void setWheelchair(int wheelchair) {
        this.wheelchair = wheelchair;
    }

    public int getEscort() {
        return escort;
    }

    public void setEscort(int escort) {
        this.escort = escort;
    }

    public String getAdditionInformation() {
        return additionInformation;
    }

    public void setAdditionInformation(String additionInformation) {
        this.additionInformation = additionInformation;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
