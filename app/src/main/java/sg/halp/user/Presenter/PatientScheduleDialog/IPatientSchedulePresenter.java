package sg.halp.user.Presenter.PatientScheduleDialog;

import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;

public interface IPatientSchedulePresenter {

    void getAmbulanceOperator();
    void getLatLngFromSourceAddress(String source);
    void getLatLngFromDestinationAddress(String destination);
    void getBillingInfo(Schedule patientSchedule, HospitalDischarge hospitalDischarge);
}
