package sg.halp.user.Presenter.ProfileHospital;

import sg.halp.user.Models.HospitalProfile;

public interface IProfileHospitalPresenter {

    void getAmbulanceOperatorList();
    void updateProfile(HospitalProfile hospitalProfile);
    void getInformation();
}
