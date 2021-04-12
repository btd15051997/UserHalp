package sg.halp.user.Activity.ProfileHospital;

import java.util.List;

import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.HospitalProfile;

public interface IProfileHospitalView {

    void disableHospitalViews();
    void enableHospitalViews();
    HospitalProfile getHospitalProfile();
    void setHospitalValues();
    void showCropPictureDialog();
    void showProgressDialog();
    void hideProgressDialog();
    void onDisconnectNetwork();
    void onErrorMessage(String error);
    void setupSpinnerAmbulanceOperator(List<AmbulanceOperator> ambulanceOperators);
    void onUpdateProfileSuccess();
    void onFullnameError();
    void onMobileError();
    void onContactNameError();
    void onContactNumberError();
    void onPreferredUsernameError();
    void onPostalError();
    void onFloorNumberError();
    void onWardError();
    void onAddressError();
    void onCleanFileCache();
}
