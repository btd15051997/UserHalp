package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class ScheduleListAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public ScheduleListAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getPatientSchedule(int GET_SCHEDULE) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)){

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.GET_SCHEDULE);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        EbizworldUtils.appLogDebug("HaoLS", "GET_SCHEDULE: " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, GET_SCHEDULE, asyncTaskCompleteListener);
    }

    public void getNursingHomeSchedule(int GET_SCHEDULE){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)){

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put(Const.Params.URL, Const.NursingHomeService.LIST_SCHEDULE_REQUEST_URL);
        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        EbizworldUtils.appLogDebug("HaoLS","GET_SCHEDULE: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, GET_SCHEDULE, asyncTaskCompleteListener);
    }

    public void getHospitalSchedule(int GET_SCHEDULE){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)){

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.HospitalService.LIST_SCHEDULE_REQUEST_URL);
        hashMap.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        hashMap.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        EbizworldUtils.appLogDebug("HaoLS", "GET_SCHEDULE: " + hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, GET_SCHEDULE, asyncTaskCompleteListener);
    }
}
