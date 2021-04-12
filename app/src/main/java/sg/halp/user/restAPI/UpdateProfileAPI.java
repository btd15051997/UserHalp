package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.MultiPartRequester;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.HospitalProfile;
import sg.halp.user.Models.NursingHomeProfile;
import sg.halp.user.Models.PatientProfile;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class UpdateProfileAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public UpdateProfileAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void updatePatientProfile(String userID, String sessionToken, PatientProfile patientProfile, int UPDATE_PROFILE) {

        if(!EbizworldUtils.isNetworkAvailable(activity)){

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.ServiceType.UPDATE_PROFILE);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.PICTURE, patientProfile.getPicture());
        hashMap.put(Const.Params.FULLNAME, patientProfile.getFullname());
        hashMap.put(Const.Params.FAMILY_MEMBER_NAME, patientProfile.getFamilyMember());
        hashMap.put(Const.Params.MOBILE, patientProfile.getPhoneNumeber());
        hashMap.put(Const.Params.EMAIL, patientProfile.getEmail());
        hashMap.put(Const.Params.HOME_NUMBER, patientProfile.getHomeNumber());
        hashMap.put(Const.Params.BLOCK_NUMBER, patientProfile.getBlockNumber());
        hashMap.put(Const.Params.UNIT_NUMBER, patientProfile.getUnitNumber());
        hashMap.put(Const.Params.POSTAL, patientProfile.getPostal());
        hashMap.put(Const.Params.STREET_NAME, patientProfile.getStreetName());
        hashMap.put(Const.Params.PREFERRED_USERNAME, patientProfile.getPreferredUsername());
        hashMap.put(Const.Params.WEIGHT, patientProfile.getWeight());
        hashMap.put(Const.Params.LIFT_LANDING, String.valueOf(patientProfile.getLiftLanding()));
        hashMap.put(Const.Params.STAIRS, String.valueOf(patientProfile.getStairs()));
        hashMap.put(Const.Params.NO_STAIRS, String.valueOf(patientProfile.getNoStairs()));
        hashMap.put(Const.Params.LOW_STAIRS, String.valueOf(patientProfile.getLowStairs()));
        hashMap.put(Const.Params.OPERATOR_ID, String.valueOf(patientProfile.getOperatorID()));
        hashMap.put(Const.Params.PATIENT_CONDITION, patientProfile.getPatientCondition());
        hashMap.put(Const.Params.STRETCHER, String.valueOf(patientProfile.getStretcher()));
        hashMap.put(Const.Params.WHEEL_CHAIR, String.valueOf(patientProfile.getWheelchair()));
        hashMap.put(Const.Params.OXYGEN, String.valueOf(patientProfile.getOxygen()));
        hashMap.put(Const.Params.ESCORTS, String.valueOf(patientProfile.getEscort()));
        hashMap.put(Const.Params.ADD_INFORMATION, patientProfile.getAdditionInformation());
        hashMap.put(Const.Params.PAYMENT_MODE, patientProfile.getPaymentMode());

        EbizworldUtils.appLogDebug("HaoLS", hashMap.toString());

        if (patientProfile.getPicture().equals("") || null == patientProfile.getPicture()) {

            new VolleyRequester(activity, Const.POST, hashMap, UPDATE_PROFILE, asyncTaskCompleteListener);

        } else {

            new MultiPartRequester(activity, hashMap, UPDATE_PROFILE, asyncTaskCompleteListener);

        }

    }

    public void updateNursingHomeProfile(String userID, String sessionToken, NursingHomeProfile nursingHomeProfile, int UPDATE_PROFILE){

        if(!EbizworldUtils.isNetworkAvailable(activity)){

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.NursingHomeService.UPDATE_PROFILE_URL);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.FULLNAME, nursingHomeProfile.getFullname());
        hashMap.put(Const.Params.EMAIL, nursingHomeProfile.getEmail());
        hashMap.put(Const.Params.CONTACT_NAME, nursingHomeProfile.getContactName());
        hashMap.put(Const.Params.CONTACT_NO, nursingHomeProfile.getContactNumber());
        hashMap.put(Const.Params.PREFERRED_USERNAME, nursingHomeProfile.getPreferredUsername());
        hashMap.put(Const.Params.PICTURE, nursingHomeProfile.getPicture());
        hashMap.put(Const.Params.MOBILE, nursingHomeProfile.getPhoneNumber());
        hashMap.put(Const.Params.ADDRESS, nursingHomeProfile.getAddress());
        /*map.put(Const.Params.OPERATOR_ID, edt_operator_id.getText().toString());*/

        hashMap.put(Const.Params.OPERATOR_ID, String.valueOf(nursingHomeProfile.getOperatorID()));

        EbizworldUtils.appLogDebug("HaoLS", Const.Params.OPERATOR_ID + " " + String.valueOf(nursingHomeProfile.getOperatorID()));

        /*map.put(Const.Params.GENDER, rd_btn.getText().toString());*/

        EbizworldUtils.appLogDebug("HaoLS", hashMap.toString());

        if (nursingHomeProfile.getPicture().equals("") || null == nursingHomeProfile.getPicture()) {

            new VolleyRequester(activity, Const.POST, hashMap, UPDATE_PROFILE, asyncTaskCompleteListener);

        } else {

            new MultiPartRequester(activity, hashMap, UPDATE_PROFILE, asyncTaskCompleteListener);

        }

    }

    public void updateHospitalProfile(String userID, String sessionToken, HospitalProfile hospitalProfile, int UPDATE_PROFILE){

        if(!EbizworldUtils.isNetworkAvailable(activity)){

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.HospitalService.UPDATE_PROFILE_URL);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.FULLNAME, hospitalProfile.getFullname());
        hashMap.put(Const.Params.EMAIL, hospitalProfile.getEmail());
        hashMap.put(Const.Params.CONTACT_NAME, hospitalProfile.getContactName());
        hashMap.put(Const.Params.CONTACT_NO, hospitalProfile.getContacNumber());
        hashMap.put(Const.Params.PREFERRED_USERNAME, hospitalProfile.getPreferredUsername());
        hashMap.put(Const.Params.POSTAL, hospitalProfile.getPostal());
        hashMap.put(Const.Params.FLOOR_NUMBER, hospitalProfile.getFloorNumber());
        hashMap.put(Const.Params.WARD, hospitalProfile.getWard());
        hashMap.put(Const.Params.PICTURE, hospitalProfile.getPicture());
        hashMap.put(Const.Params.MOBILE, hospitalProfile.getPhoneNumber());
        hashMap.put(Const.Params.ADDRESS, hospitalProfile.getAddress());
        /*hashMap.put(Const.Params.OPERATOR_ID, edt_operator_id.getText().toString());*/

        hashMap.put(Const.Params.OPERATOR_ID, String.valueOf(hospitalProfile.getOperatorID()));

        EbizworldUtils.appLogDebug("HaoLS", Const.Params.OPERATOR_ID + " " + String.valueOf(hospitalProfile.getOperatorID()));

        /*hashMap.put(Const.Params.GENDER, rd_btn.getText().toString());*/

        EbizworldUtils.appLogDebug("HaoLS", hashMap.toString());

        if (hospitalProfile.getPicture().equals("") || null == hospitalProfile.getPicture()) {

            new VolleyRequester(activity, Const.POST, hashMap, UPDATE_PROFILE, asyncTaskCompleteListener);

        } else {

            new MultiPartRequester(activity, hashMap, UPDATE_PROFILE, asyncTaskCompleteListener);

        }

    }
}
