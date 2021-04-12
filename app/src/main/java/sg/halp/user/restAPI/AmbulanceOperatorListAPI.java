package sg.halp.user.restAPI;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class AmbulanceOperatorListAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public AmbulanceOperatorListAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getAmbulanceOperatorForPatient(int AMBULANCE_OPERATOR){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }


        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.OPERATORS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        Log.d("HaoLS", "Home ambulance type " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, AMBULANCE_OPERATOR, asyncTaskCompleteListener);
    }

    public void getAmbulanceOperatorForNursingHome(int AMBULANCE_OPERATOR){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }


        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.NursingHomeService.OPERATORS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        Log.d("HaoLS", "Home ambulance type " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, AMBULANCE_OPERATOR, asyncTaskCompleteListener);
    }

    public void getAmbulanceOperatorForHospital(int AMBULANCE_OPERATOR){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }


        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.HospitalService.OPERATORS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        Log.d("HaoLS", "Home ambulance type " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, AMBULANCE_OPERATOR, asyncTaskCompleteListener);
    }
}
