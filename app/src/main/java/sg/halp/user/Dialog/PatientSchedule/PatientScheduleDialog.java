package sg.halp.user.Dialog.PatientSchedule;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.zyyoona7.wheel.WheelView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Adapter.PlacesAutoCompleteAdapter;
import sg.halp.user.Adapter.SimpleSpinnerAdapter;
import sg.halp.user.Dialog.BillingInfoSchedule.BillingInfoScheduleDialog;
import sg.halp.user.Dialog.HospitalDischargeOptionDialog;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;
import sg.halp.user.Presenter.PatientScheduleDialog.PatientSchedulePresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class PatientScheduleDialog extends DialogFragment implements IPatientScheduleView{

    @BindView(R.id.tv_pickup_time_date)
    TextView tv_pickup_time_date;

    @BindView(R.id.tv_ambulance_operator_notice)
    TextView tv_ambulance_operator_notice;

    @BindView(R.id.wheel_view_ambulance_operator)
    WheelView wheel_view_ambulance_operator;

    @BindView(R.id.actv_sch_source_address)
    AutoCompleteTextView actv_sch_source_address;

    @BindView(R.id.actv_sch_destination_address)
    AutoCompleteTextView actv_sch_destination_address;

    @BindView(R.id.cb_staircase_value)
    CheckBox cb_staircase_value;

    @BindView(R.id.spn_family_member_value)
    Spinner spn_family_member_value;

    @BindView(R.id.edt_house_unit_value)
    EditText edt_house_unit_value;

    @BindView(R.id.spn_weight_value)
    Spinner spn_weight_value;

    @BindView(R.id.spn_oxygen_tank_value)
    Spinner spn_oxygen_tank_value;

    @BindView(R.id.spn_pickup_type_value)
    Spinner spn_pickup_type_value;

    @BindView(R.id.btn_schedule_submit)
    TextView btn_schedule_submit;

    private PatientSchedulePresenter mPatientSchedulePresenter;
    private Activity activity;
    private AmbulanceOperator mAmbulanceOperator;
    private HospitalDischarge mHospitalDischarge;
    private int familyMember = 0, weight = 0, oxygenTank = 0, caseType = 1;
    private String datetime = "", timeSet = "", date = "", time = "", pickupDateTime = "";
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private LatLng sch_pic_latLng, sch_drop_latLng;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim_leftright_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_black)));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_patient_schedule);

        ButterKnife.bind(this, dialog);

        mPatientSchedulePresenter = new PatientSchedulePresenter(activity, this);
        mPatientSchedulePresenter.getAmbulanceOperator();

        return dialog;
    }

    @Override
    public void initDialog(){

        new PreferenceHelper(activity).putRequestType("0");
        new PreferenceHelper(activity).putAmbulance_name(getResources().getString(R.string.any_ambulance));
        tv_ambulance_operator_notice.setSelected(true);
        tv_ambulance_operator_notice.setText(new PreferenceHelper(activity).getAmbulance_name() + " " + getResources().getString(R.string.ambulance_operator_notice));

        initFamilyMember();

        initWeight();

        initOxygenTank();

        initCaseType();

        initSourceAddress();

        initDestinationAddress();

        initButtonSubmitSchedule();

        if (mAmbulanceOperator != null){

            tv_ambulance_operator_notice.setSelected(true);
            tv_ambulance_operator_notice.setText(mAmbulanceOperator.getAmbulanceOperator().toUpperCase() + " " + getResources().getString(R.string.ambulance_operator_notice));

        }

        tv_pickup_time_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePicker();
            }
        });


    }

    @Override
    public void initFamilyMember() {

        List<String> familyMembers = new ArrayList<>();
        familyMembers.add("0");
        familyMembers.add("1");
        familyMembers.add("2");
        SimpleSpinnerAdapter familyMemberSimpleSpinnerAdapter = new SimpleSpinnerAdapter(activity, familyMembers);
        spn_family_member_value.setAdapter(familyMemberSimpleSpinnerAdapter);
        spn_family_member_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 1:{
                        familyMember = 1;
                    }
                    break;

                    case 2:{
                        familyMember = 2;
                    }
                    break;

                    default:{
                        familyMember = 0;
                    }
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void initWeight() {

        List<String> weightList = new ArrayList<>();
        weightList.add(getResources().getString(R.string.weight_less_than_eighty));
        weightList.add(getResources().getString(R.string.weight_over_eighty));
        weightList.add(getResources().getString(R.string.weight_over_one_hundred));
        weightList.add(getResources().getString(R.string.weight_over_one_hundred_and_twenty));
        SimpleSpinnerAdapter simpleSpinnerAdapter = new SimpleSpinnerAdapter(activity, weightList);
        spn_weight_value.setAdapter(simpleSpinnerAdapter);
        spn_weight_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0:{

                        weight = 0;

                    }
                    break;

                    case 1:{

                        weight = 1;
                    }
                    break;

                    case 2:{

                        weight = 2;
                    }
                    break;

                    case 3:{

                        weight = 3;
                    }
                    break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void initOxygenTank() {

        final List<String> oxygenTanks = new ArrayList<>();
        oxygenTanks.add("0");
        oxygenTanks.add("2");
        oxygenTanks.add("3");
        oxygenTanks.add("4");
        oxygenTanks.add("5");
        SimpleSpinnerAdapter oxygenTankAdapter = new SimpleSpinnerAdapter(activity, oxygenTanks);
        spn_oxygen_tank_value.setAdapter(oxygenTankAdapter);
        spn_oxygen_tank_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                oxygenTank = Integer.parseInt(oxygenTanks.get(position));
                EbizworldUtils.appLogDebug("HaoLS", "Oxygen Tank " + oxygenTank);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void initCaseType() {

        final List<String> caseTypes = new ArrayList<>();
        caseTypes.add(getResources().getString(R.string.medical_appointment));
        caseTypes.add(getResources().getString(R.string.ad_hoc));
        caseTypes.add(getResources().getString(R.string.airport_selectar));
        caseTypes.add(getResources().getString(R.string.airport_changi));
        caseTypes.add(getResources().getString(R.string.hospital_discharge));
        caseTypes.add(getResources().getString(R.string.a_and_e));
        caseTypes.add(getResources().getString(R.string.imh));
        caseTypes.add(getResources().getString(R.string.ferry_terminals));
        caseTypes.add(getResources().getString(R.string.airport_tarmac));

        SimpleSpinnerAdapter caseTypeAdapter = new SimpleSpinnerAdapter(activity, caseTypes);
        spn_pickup_type_value.setAdapter(caseTypeAdapter);
        spn_pickup_type_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0:{

                        caseType = 1;
                    }
                    break;

                    case 1:{

                        caseType = 2;
                    }
                    break;

                    case 2:{

                        caseType = 3;
                    }
                    break;

                    case 3:{
                        caseType = 4;
                    }
                    break;

                    case 4:{

                        FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
                        HospitalDischargeOptionDialog hospitalDischargeOptionDialog = new HospitalDischargeOptionDialog();

                        if (mHospitalDischarge != null){

                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Const.HOSPITAL_DISCHARGE_OPTION, mHospitalDischarge);
                            hospitalDischargeOptionDialog.setArguments(bundle);
                        }

                        hospitalDischargeOptionDialog.setCancelable(false);
                        hospitalDischargeOptionDialog.show(fragmentManager, Const.HOSPITAL_DISCHARGE_OPTION_DIALOGFRAGMENT);
                        hospitalDischargeOptionDialog.setHospitalDischargeCallback(new DialogFragmentCallback.HospitalDischargeCallback() {
                            @Override
                            public void onHospitalDischargeCallback(HospitalDischarge hospitalDischarge) {
                                if (hospitalDischarge != null){

                                    mHospitalDischarge = hospitalDischarge;
                                    caseType = 5;
                                    EbizworldUtils.appLogDebug("HaoLS", "Case type: " + caseType);

                                }
                            }

                            @Override
                            public void onHospitalDischargeCancel(Boolean isDismiss) {

                                if (isDismiss == true){

                                    caseType = 1;
                                    spn_pickup_type_value.setSelection(0);
                                    EbizworldUtils.appLogDebug("HaoLS", "Case type: " + caseType);

                                }

                            }
                        });

                    }
                    break;

                    case 5:{
                        caseType = 6;
                    }
                    break;

                    case 6:{
                        caseType = 7;
                    }
                    break;

                    case 7:{
                        caseType = 8;
                    }
                    break;

                    case 8:{
                        caseType = 9;
                    }
                    break;
                }

                EbizworldUtils.appLogDebug("HaoLS", "Case type " + caseType);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void initSourceAddress() {

        final PlacesAutoCompleteAdapter placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(activity, R.layout.autocomplete_list_text);
        actv_sch_source_address.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));
        actv_sch_source_address.setAdapter(placesAutoCompleteAdapter);
        actv_sch_source_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                actv_sch_source_address.setSelection(0);

                EbizworldUtils.hideKeyBoard(activity);

                final String selectedSourcePlace = placesAutoCompleteAdapter.getItem(i);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mPatientSchedulePresenter.getLatLngFromSourceAddress(selectedSourcePlace);

                    }
                }).start();

            }
        });

    }

    @Override
    public void initDestinationAddress() {

        actv_sch_destination_address.setDropDownBackgroundDrawable(new ColorDrawable(Color.WHITE));

        final PlacesAutoCompleteAdapter placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(activity, R.layout.autocomplete_list_text);

        actv_sch_destination_address.setAdapter(placesAutoCompleteAdapter);
        actv_sch_destination_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                actv_sch_destination_address.setSelection(0);

                // sch_drop_latLng[0] = getLocationFromAddress(activity, et_sch_destination_address.getText().toString());
                EbizworldUtils.hideKeyBoard(activity);

                final String selectedDestPlace = placesAutoCompleteAdapter.getItem(i);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mPatientSchedulePresenter.getLatLngFromDestinationAddress(selectedDestPlace);

                    }
                }).start();
            }
        });


    }

    @Override
    public void initButtonSubmitSchedule() {

        btn_schedule_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (tv_pickup_time_date.getText().toString().length() == 0) {

                    Commonutils.showtoast(getResources().getString(R.string.txt_error_date_time), activity);

                } else if (actv_sch_destination_address.getText().toString().length() == 0) {

                    Commonutils.showtoast(getResources().getString(R.string.txt_destination_error), activity);

                } else {

                    Schedule mPatientSchedule = getPatientSchedule(sch_drop_latLng,
                            sch_pic_latLng,
                            new PreferenceHelper(activity).getRequestType(),
                            datetime,
                            actv_sch_source_address.getText().toString(),
                            actv_sch_destination_address.getText().toString(),
                            cb_staircase_value,
                            edt_house_unit_value.getText().toString().trim(),
                            familyMember,
                            weight,
                            oxygenTank,
                            caseType);

                    if (mPatientSchedule != null){

                        mPatientSchedulePresenter.getBillingInfo(mPatientSchedule, mHospitalDischarge);

                    }


                }

            }
        });

    }

    @Override
    public void initializeWheelView(final List<String> ambulanceOperatorsWheelView, final ArrayList<AmbulanceOperator> ambulanceOperators) {

        wheel_view_ambulance_operator.setTypeface(Typeface.SERIF);
        wheel_view_ambulance_operator.setData(ambulanceOperatorsWheelView);
        wheel_view_ambulance_operator.setOnWheelChangedListener(new WheelView.OnWheelChangedListener() {
            @Override
            public void onWheelScroll(int scrollOffsetY) {

            }

            @Override
            public void onWheelItemChanged(int oldPosition, int newPosition) {

            }

            @Override
            public void onWheelSelected(int position) {

                for (int i = 0; i < ambulanceOperators.size(); i++){

                    if(ambulanceOperators.get(i).getAmbulanceOperator().toLowerCase().equals(ambulanceOperatorsWheelView.get(position).toLowerCase())){

                        mAmbulanceOperator = ambulanceOperators.get(i);

                        break;

                    }else {

                        mAmbulanceOperator = null;

                    }
                }

                if(mAmbulanceOperator != null){

                    EbizworldUtils.appLogDebug("HaoLS", "mAmbulanceOperator is " + mAmbulanceOperator.getAmbulanceOperator());

                    tv_ambulance_operator_notice.setText(mAmbulanceOperator.getAmbulanceOperator().toUpperCase() + " " + getResources().getString(R.string.ambulance_operator_notice));
                    new PreferenceHelper(activity).putRequestType(mAmbulanceOperator.getId());
                    new PreferenceHelper(activity).putAmbulance_name(mAmbulanceOperator.getAmbulanceOperator());

                }else {

                    EbizworldUtils.appLogDebug("HaoLS", "mAmbulanceOperator is null");
                    new PreferenceHelper(activity).putRequestType("0");
                    new PreferenceHelper(activity).putAmbulance_name(getResources().getString(R.string.any_ambulance));
                    tv_ambulance_operator_notice.setText(new PreferenceHelper(activity).getAmbulance_name() + " " + getResources().getString(R.string.ambulance_operator_notice));
                }

            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

        initDialog();

    }

    private void DatePicker() {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(activity, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(android.widget.DatePicker view,
                                          int year, int monthOfYear, int dayOfMonth) {

                        if (view.isShown()) {

                            date = Integer.toString(year) + "-"
                                    + Integer.toString(monthOfYear + 1) + "-"
                                    + Integer.toString(dayOfMonth);

                            datetime = date;

                            TimePicker();

                            datePickerDialog.dismiss();
                        }
                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.txt_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datePickerDialog.dismiss();
                    }
                });
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 3);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());

        datePickerDialog.show();
    }

    public void TimePicker() {

        final Calendar calendar = Calendar.getInstance();


        calendar.add(Calendar.MINUTE, 30);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);


        timePickerDialog = new TimePickerDialog(activity, R.style.datepicker,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(android.widget.TimePicker view,
                                          int hourOfDay, int minute) {
                        if (view.isShown()) {
                            timePickerDialog.dismiss();
                            int hour = hourOfDay;
                            int min = minute;

                            if (hourOfDay > 12){

                                hour -= 12;
                                timeSet = "PM";
                            }else if (hourOfDay == 0){

                                timeSet = "AM";

                            }else if (hourOfDay == 12){

                                timeSet = "PM";
                            }else {

                                timeSet = "AM";

                            }

                            if (minute < 10){

                                time = String.valueOf(hourOfDay)+ ":0" +
                                        String.valueOf(min) + ":" + "00 " + timeSet;

                            }else {

                                time = String.valueOf(hourOfDay)+ ":" +
                                        String.valueOf(min) + ":" + "00 " + timeSet;

                            }

                            pickupDateTime = date + " " + time;

                            datetime = datetime.concat(" "
                                    + Integer.toString(hourOfDay) + ":"
                                    + Integer.toString(minute) + ":" + "00");

                            tv_pickup_time_date.setText(pickupDateTime);

                            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            calendar.set(Calendar.MINUTE, minute);

                        }
                    }
                }, mHour, mMinute, false);

        timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.txt_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timePickerDialog.dismiss();
                    }
                });



        timePickerDialog.show();

    }

    @Override
    public Schedule getPatientSchedule(LatLng dropLatLng,
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
                                        int case_type) {

        Schedule patientSchedule = new Schedule();

        patientSchedule.setOperatorID(type);
        patientSchedule.setS_address(s_address);
        patientSchedule.setD_address(d_address);

        if (pickLatLng != null){

            patientSchedule.setS_lat(String.valueOf(pickLatLng.latitude));
            patientSchedule.setS_lng(String.valueOf(pickLatLng.longitude));

        }

        if (dropLatLng != null){

            patientSchedule.setD_lat(String.valueOf(dropLatLng.latitude));
            patientSchedule.setD_lng(String.valueOf(dropLatLng.longitude));

        }

        patientSchedule.setRequest_date(dateTime);
        patientSchedule.setStatus_request("1");

        patientSchedule.setA_and_e(0);

        patientSchedule.setImh(0);

        patientSchedule.setFerry_terminals(0);

        patientSchedule.setTarmac(0);

        if (cb_staircase.isChecked()){

            patientSchedule.setStaircase(1);
        }else {

            patientSchedule.setStaircase(0);
        }

        patientSchedule.setFamilyMember(familyMember);
        patientSchedule.setHouseUnit(houseUnit);
        patientSchedule.setOxygen_tank(oxygenTank);
        patientSchedule.setWeight(weight);
        patientSchedule.setCase_type(case_type);

        return patientSchedule;
    }

    @Override
    public void onDisconnectNetwork() {
        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
    }

    @Override
    public void onFailure() {

        dismiss();

    }

    @Override
    public void onSourceLatLngRespond(LatLng source) {

        sch_pic_latLng = source;

    }

    @Override
    public void onDestinationLatLngRespond(LatLng destination) {

        sch_drop_latLng = destination;
    }

    @Override
    public void showBillingScheduleDialog(String staircase,
                                          String weight,
                                          String tarmac,
                                          String oxygen,
                                          String caseType,
                                          String total,
                                          String currency,
                                          String other_expense,
                                          Schedule patientSchedule,
                                          HospitalDischarge hospitalDischarge) {

        Bundle bundle = new Bundle();
        bundle.putString(Const.Params.STAIRCASE, staircase);
        bundle.putString(Const.Params.WEIGHT, weight);
        bundle.putString(Const.Params.TARMAC, tarmac);
        bundle.putString(Const.Params.OXYGEN_TANK, oxygen);
        bundle.putString(Const.Params.CASE_TYPE, caseType);
        bundle.putString(Const.Params.TOTAL, total);
        bundle.putString(Const.Params.CURRENCY, currency);
        bundle.putString(Const.Params.OTHER_EXPENSES, other_expense);

        bundle.putParcelable(Const.SCHEDULE, patientSchedule);

        if (hospitalDischarge != null){

            bundle.putParcelable(Const.HOSPITAL_DISCHARGE_OPTION, hospitalDischarge);

        }

        FragmentManager fragmentManager = ((AppCompatActivity) activity).getSupportFragmentManager();
        BillingInfoScheduleDialog billingInfoScheduleDialog = new BillingInfoScheduleDialog();
        billingInfoScheduleDialog.setArguments(bundle);
        billingInfoScheduleDialog.show(fragmentManager, Const.BILLING_INFO_SCHEDULE_DIALOGFRAGMENT);
        billingInfoScheduleDialog.setDialogDismissCallback(new DialogFragmentCallback.DialogDismissCallback() {
            @Override
            public void onDialogDismissListener(Boolean isDismiss) {

                if (isDismiss){

                    dismiss();

                }
            }
        });

    }
}
