package sg.halp.user.Dialog.PatientSchedule;

import android.widget.CheckBox;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;

public interface IPatientScheduleView {

    void initDialog();
    void initFamilyMember();
    void initWeight();
    void initOxygenTank();
    void initCaseType();
    void initSourceAddress();
    void initDestinationAddress();
    void initButtonSubmitSchedule();
    void initializeWheelView(final List<String> ambulanceOperatorsWheelView, final ArrayList<AmbulanceOperator> ambulanceOperators);

    Schedule getPatientSchedule(LatLng dropLatLng,
                                LatLng pickLatLng,
                                String type,
                                String dateTime,
                                String s_address,
                                String d_address,
                                CheckBox cb_staircase,
                                String houseUnit,
                                int familyMember,
                                int weight,
                                int oxygenTank,
                                int case_type);

    void onDisconnectNetwork();
    void onFailure();
    void onSourceLatLngRespond(LatLng source);
    void onDestinationLatLngRespond(LatLng destination);
    void showBillingScheduleDialog(
            String staircase,
            String weight,
            String tarmac,
            String oxygen,
            String caseType,
            String total,
            String currency,
            String other_expense,
            Schedule patientSchedule,
            HospitalDischarge hospitalDischarge
    );
}
