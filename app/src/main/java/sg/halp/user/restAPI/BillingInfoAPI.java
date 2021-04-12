package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.RequestOptional;
import sg.halp.user.Models.Schedule;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class BillingInfoAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public BillingInfoAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getPatientScheduleBillingInfo(Schedule schedule, int STATICCODE){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(Const.Params.URL, Const.ServiceType.BILLING_INFO);

        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        hashMap.put(Const.Params.TIME, schedule.getRequest_date());
        hashMap.put(Const.Params.A_AND_E, String.valueOf(schedule.getA_and_e()));
        hashMap.put(Const.Params.IMH, String.valueOf(schedule.getImh()));
        hashMap.put(Const.Params.FERRY_TERMINALS, String.valueOf(schedule.getFerry_terminals()));
        hashMap.put(Const.Params.STAIRCASE, String.valueOf(schedule.getStaircase()));
        hashMap.put(Const.Params.TARMAC, String.valueOf(schedule.getTarmac()));
        hashMap.put(Const.Params.WEIGHT, String.valueOf(schedule.getWeight()));
        hashMap.put(Const.Params.OXYGEN_TANK, String.valueOf(schedule.getOxygen_tank()));
        hashMap.put(Const.Params.CASE_TYPE, String.valueOf(schedule.getCase_type()));

        EbizworldUtils.appLogDebug("HaoLS", "Get Billing info for Patient's schedule: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, STATICCODE, asyncTaskCompleteListener);
    }

    public void getNursingHomeScheduleBillingInfo(Schedule schedule, int BILLING_INFO){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(Const.Params.URL, Const.NursingHomeService.BILLING_INFO);

        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        hashMap.put(Const.Params.TIME, schedule.getRequest_date());
        hashMap.put(Const.Params.A_AND_E, String.valueOf(schedule.getA_and_e()));
        hashMap.put(Const.Params.IMH, String.valueOf(schedule.getImh()));
        hashMap.put(Const.Params.FERRY_TERMINALS, String.valueOf(schedule.getFerry_terminals()));
        hashMap.put(Const.Params.STAIRCASE, String.valueOf(schedule.getStaircase()));
        hashMap.put(Const.Params.TARMAC, String.valueOf(schedule.getTarmac()));
        hashMap.put(Const.Params.WEIGHT, String.valueOf(schedule.getWeight()));
        hashMap.put(Const.Params.OXYGEN_TANK, String.valueOf(schedule.getOxygen_tank()));
        hashMap.put(Const.Params.CASE_TYPE, String.valueOf(schedule.getCase_type()));

        EbizworldUtils.appLogDebug("HaoLS", "Get Billing info for Hospital/Nursing home's schedule: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, BILLING_INFO, asyncTaskCompleteListener);
    }

    public void getHospitalScheduleBillingInfo(Schedule schedule, int BILLING_INFO){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(Const.Params.URL, Const.HospitalService.BILLING_INFO);

        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        hashMap.put(Const.Params.TIME, schedule.getRequest_date());
        hashMap.put(Const.Params.A_AND_E, String.valueOf(schedule.getA_and_e()));
        hashMap.put(Const.Params.IMH, String.valueOf(schedule.getImh()));
        hashMap.put(Const.Params.FERRY_TERMINALS, String.valueOf(schedule.getFerry_terminals()));
        hashMap.put(Const.Params.STAIRCASE, String.valueOf(schedule.getStaircase()));
        hashMap.put(Const.Params.TARMAC, String.valueOf(schedule.getTarmac()));
        hashMap.put(Const.Params.WEIGHT, String.valueOf(schedule.getWeight()));
        hashMap.put(Const.Params.OXYGEN_TANK, String.valueOf(schedule.getOxygen_tank()));
        hashMap.put(Const.Params.CASE_TYPE, String.valueOf(schedule.getCase_type()));

        EbizworldUtils.appLogDebug("HaoLS", "Get Billing info for Hospital/Nursing home's schedule: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, BILLING_INFO, asyncTaskCompleteListener);
    }

    public void getPatientNormalBillingInfo(RequestOptional requestOptional, String userID, String sessionToken, int BILLING_INFO){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.ServiceType.BILLING_INFO);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.A_AND_E, String.valueOf(requestOptional.getA_and_e()));
        hashMap.put(Const.Params.IMH, String.valueOf(requestOptional.getImh()));
        hashMap.put(Const.Params.FERRY_TERMINALS, String.valueOf(requestOptional.getFerry_terminals()));
        hashMap.put(Const.Params.STAIRCASE, String.valueOf(requestOptional.getStaircase()));
        hashMap.put(Const.Params.TARMAC, String.valueOf(requestOptional.getTarmac()));
        hashMap.put(Const.Params.WEIGHT, String.valueOf(requestOptional.getWeight()));
        hashMap.put(Const.Params.OXYGEN_TANK, String.valueOf(requestOptional.getOxygen()));
        hashMap.put(Const.Params.CASE_TYPE, String.valueOf(requestOptional.getCaseType()));

        EbizworldUtils.appLogDebug("HaoLS", "Get Billing info: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, BILLING_INFO, asyncTaskCompleteListener);
    }

    public void getNursingHomeNormalBillingInfo(RequestOptional requestOptional, String userID, String sessionToken, int BILLING_INFO){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.NursingHomeService.BILLING_INFO);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.A_AND_E, String.valueOf(requestOptional.getA_and_e()));
        hashMap.put(Const.Params.IMH, String.valueOf(requestOptional.getImh()));
        hashMap.put(Const.Params.FERRY_TERMINALS, String.valueOf(requestOptional.getFerry_terminals()));
        hashMap.put(Const.Params.STAIRCASE, String.valueOf(requestOptional.getStaircase()));
        hashMap.put(Const.Params.TARMAC, String.valueOf(requestOptional.getTarmac()));
        hashMap.put(Const.Params.WEIGHT, String.valueOf(requestOptional.getWeight()));
        hashMap.put(Const.Params.OXYGEN_TANK, String.valueOf(requestOptional.getOxygen()));
        hashMap.put(Const.Params.CASE_TYPE, String.valueOf(requestOptional.getCaseType()));

        EbizworldUtils.appLogDebug("HaoLS", "Get Billing info: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, BILLING_INFO, asyncTaskCompleteListener);
    }

    public void getHospitalNormalBillingInfo(RequestOptional requestOptional, String userID, String sessionToken, int BILLING_INFO){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.HospitalService.BILLING_INFO);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.A_AND_E, String.valueOf(requestOptional.getA_and_e()));
        hashMap.put(Const.Params.IMH, String.valueOf(requestOptional.getImh()));
        hashMap.put(Const.Params.FERRY_TERMINALS, String.valueOf(requestOptional.getFerry_terminals()));
        hashMap.put(Const.Params.STAIRCASE, String.valueOf(requestOptional.getStaircase()));
        hashMap.put(Const.Params.TARMAC, String.valueOf(requestOptional.getTarmac()));
        hashMap.put(Const.Params.WEIGHT, String.valueOf(requestOptional.getWeight()));
        hashMap.put(Const.Params.OXYGEN_TANK, String.valueOf(requestOptional.getOxygen()));
        hashMap.put(Const.Params.CASE_TYPE, String.valueOf(requestOptional.getCaseType()));

        EbizworldUtils.appLogDebug("HaoLS", "Get Billing info: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, BILLING_INFO, asyncTaskCompleteListener);
    }
}
