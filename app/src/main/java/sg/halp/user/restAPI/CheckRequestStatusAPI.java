package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class CheckRequestStatusAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public CheckRequestStatusAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void checkRequestStatusForPatient(int CHECKREQUEST_STATUS) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.ServiceType.CHECKREQUEST_STATUS);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        EbizworldUtils.appLogDebug("HaoLS","CHECKREQUEST_STATUS: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, CHECKREQUEST_STATUS,asyncTaskCompleteListener);
    }

    public void checkRequestStatusForNursingHome(int CHECKREQUEST_STATUS) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.NursingHomeService.REQUEST_STATUS_CHECK_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        EbizworldUtils.appLogDebug("HaoLS","CHECKREQUEST_STATUS: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, CHECKREQUEST_STATUS,asyncTaskCompleteListener);
    }

    public void checkRequestStatusForHospital(int CHECKREQUEST_STATUS) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.HospitalService.CHECK_REQUEST_STATUS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        EbizworldUtils.appLogDebug("HaoLS","CHECKREQUEST_STATUS: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, CHECKREQUEST_STATUS,asyncTaskCompleteListener);
    }
}
