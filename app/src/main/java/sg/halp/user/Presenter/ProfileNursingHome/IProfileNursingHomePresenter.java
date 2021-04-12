package sg.halp.user.Presenter.ProfileNursingHome;

import sg.halp.user.Models.HospitalProfile;
import sg.halp.user.Models.NursingHomeProfile;

public interface IProfileNursingHomePresenter {

    void getAmbulanceOperatorList();
    void updateProfile(NursingHomeProfile nursingHomeProfile);
    void getInformation();
}
