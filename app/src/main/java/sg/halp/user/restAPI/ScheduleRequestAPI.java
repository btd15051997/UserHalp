package sg.halp.user.restAPI;

import android.app.Activity;

import org.json.JSONArray;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class ScheduleRequestAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public ScheduleRequestAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void bookSchedulePatient(Schedule schedule, HospitalDischarge hospitalDischarge, int REQUEST_LATER) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        Commonutils.progressDialog_show(mActivity, "Requesting...");

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.REQUEST_LATER);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        map.put(Const.Params.SERVICE_TYPE, schedule.getOperatorID());

        map.put(Const.Params.S_ADDRESS, schedule.getS_address());
        map.put(Const.Params.D_ADDRESS, schedule.getD_address());

        map.put(Const.Params.LATITUDE, schedule.getS_lat());
        map.put(Const.Params.LONGITUDE, schedule.getS_lng());

        map.put(Const.Params.D_LATITUDE, schedule.getD_lat());
        map.put(Const.Params.D_LONGITUDE, schedule.getD_lng());

        map.put(Const.Params.SCHEDULE_REQUEST_TIME, schedule.getRequest_date());

        map.put(Const.Params.REQ_STATUS_TYPE, "1");

        map.put(Const.Params.A_AND_E, String.valueOf(schedule.getA_and_e()));

        map.put(Const.Params.IMH, String.valueOf(schedule.getImh()));

        map.put(Const.Params.FERRY_TERMINALS, String.valueOf(schedule.getFerry_terminals()));

        map.put(Const.Params.STAIRCASE, String.valueOf(schedule.getStaircase()));

        map.put(Const.Params.TARMAC, String.valueOf(schedule.getTarmac()));

        map.put(Const.Params.FAMILY_MEMBER, String.valueOf(schedule.getFamilyMember()));
        map.put(Const.Params.HOUSE_UNIT, schedule.getHouseUnit());
        map.put(Const.Params.OXYGEN_TANK, String.valueOf(schedule.getOxygen_tank()));
        map.put(Const.Params.WEIGHT, String.valueOf(schedule.getWeight()));
        map.put(Const.Params.CASE_TYPE, String.valueOf(schedule.getCase_type()));

        if (schedule.getCase_type() == 5 && hospitalDischarge != null){

            map.put(Const.Params.SCHEDULE_PATIENT_NAME, hospitalDischarge.getPatientName());
            map.put(Const.Params.SCHEDULE_WARD_NUMBER, hospitalDischarge.getWardNumber());
            map.put(Const.Params.SCHEDULE_HOSPITAL, hospitalDischarge.getHospital());
            map.put(Const.Params.SCHEDULE_ROOM_NUMBER, hospitalDischarge.getRoomNumber());
            map.put(Const.Params.SCHEDULE_BED_NUMBER, hospitalDischarge.getBedNumber());
            map.put(Const.Params.TIME_OF_DISCHARGE, hospitalDischarge.getTimeOfDischarge());

        }

        EbizworldUtils.appLogDebug("HaoLS", "Create schedule: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, REQUEST_LATER, asyncTaskCompleteListener);

    }

    public void bookNursingHomeSchedule(int NURSE_HOSPITAL_REGISTER_SCHEDULE, JSONArray jsonArray){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)){

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.NursingHomeService.REGISTER_SCHEDULE_URL);
        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        hashMap.put(Const.Params.OPERATOR_ID, new PreferenceHelper(mActivity).getRequestType());
        hashMap.put(Const.Params.ARR_REQUEST, jsonArray.toString());

        EbizworldUtils.appLogDebug("HaoLS", "Registering schedule " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, NURSE_HOSPITAL_REGISTER_SCHEDULE, asyncTaskCompleteListener);
    }

    public void bookHospitalSchedule(int NURSE_HOSPITAL_REGISTER_SCHEDULE, JSONArray jsonArray){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)){

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.HospitalService.REGISTER_SCHEDULE_URL);
        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        hashMap.put(Const.Params.OPERATOR_ID, new PreferenceHelper(mActivity).getRequestType());
        hashMap.put(Const.Params.ARR_REQUEST, jsonArray.toString());

        EbizworldUtils.appLogDebug("HaoLS", "Registering schedule " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, NURSE_HOSPITAL_REGISTER_SCHEDULE, asyncTaskCompleteListener);
    }
}
