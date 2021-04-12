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

public class CancelNormalRequestAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public CancelNormalRequestAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void cancelNormalRequestForPatient(String reason_id, String reasontext, int CANCEL_RIDE) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.CANCEL_RIDE);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put(Const.Params.REASON_ID, reason_id);
        map.put(Const.Params.CANCELLATION_REASON, reasontext);

        Log.d("HaoLS", "cancel_reg" + map.toString());

        new VolleyRequester(activity, Const.POST, map, CANCEL_RIDE,asyncTaskCompleteListener);
    }

    public void cancelNormalRequestForNursingHome(String reason_id, String reasontext, int CANCEL_RIDE) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.NursingHomeService.CANCEL_TRIP_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put("reason_id", reason_id);
        map.put("cancellation_reason", reasontext);

        Log.d("HaoLS", "cancel_reg" + map.toString());

        new VolleyRequester(activity, Const.POST, map, CANCEL_RIDE,asyncTaskCompleteListener);
    }

    public void cancelNormalRequestForHospital(String reason_id, String reasontext, int CANCEL_RIDE) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.HospitalService.CANCEL_REQUEST_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put("reason_id", reason_id);
        map.put("cancellation_reason", reasontext);

        Log.d("HaoLS", "cancel_reg" + map.toString());

        new VolleyRequester(activity, Const.POST, map, CANCEL_RIDE,asyncTaskCompleteListener);
    }
}
