package sg.halp.user.Activity.ProfileNursingHome;

import java.util.List;

import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.NursingHomeProfile;

public interface IProfileNursingHomeView {

    void disableNursingHomeViews();
    void enableNursingHomeViews();
    NursingHomeProfile getNurseProfile();
    void setNurseValues();
    void showCropPictureDialog();
    void showProgressDialog();
    void hideProgressDialog();
    void onDisconnectNetwork();
    void onErrorMessage(String error);
    void setupSpinnerAmbulanceOperator(final List<AmbulanceOperator> ambulanceOperators);
    void onUpdateProfileSuccess();
    void onFullnameError();
    void onMobileError();
    void onContactNameError();
    void onContactNumberError();
    void onPreferredUsernameError();
    void onAddressError();
    void onCleanFileCache();
}
