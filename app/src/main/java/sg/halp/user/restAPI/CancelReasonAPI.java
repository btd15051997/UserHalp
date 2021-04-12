package sg.halp.user.restAPI;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class CancelReasonAPI {

    private Activity activity;
    private AsyncTaskCompleteListener  asyncTaskCompleteListener;

    public CancelReasonAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getCancelRideReasonListForPatient(int CANCEL_REASON) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.CANCEL_REASON);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());

        Log.d("HaoLS", "cancel_req lst" + map.toString());
        new VolleyRequester(activity, Const.POST, map, CANCEL_REASON, asyncTaskCompleteListener);
    }

    public void getCancelRideReasonListForNursingHome(int CANCEL_REASON) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.NursingHomeService.CANCEL_REASONS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());

        Log.d("HaoLS", "cancel_req lst" + map.toString());
        new VolleyRequester(activity, Const.POST, map, CANCEL_REASON, asyncTaskCompleteListener);
    }

    public void getCancelRideReasonListForHospital(int CANCEL_REASON) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.HospitalService.CANCEL_REASONS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());

        Log.d("HaoLS", "cancel_req lst" + map.toString());
        new VolleyRequester(activity, Const.POST, map, CANCEL_REASON, asyncTaskCompleteListener);
    }
}
