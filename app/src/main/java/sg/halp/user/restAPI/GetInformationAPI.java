package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class GetInformationAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public GetInformationAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getPatientInformation(String userID, String sessionToken, int GET_ACCOUNT_INFORMATION){

        if(!EbizworldUtils.isNetworkAvailable(activity)){

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.ServiceType.GET_USER_PROFILE + Const.Params.ID + "=" + userID +
                "&" + Const.Params.TOKEN + "=" + sessionToken);

        EbizworldUtils.appLogDebug("HaoLS", "getPatientInformation " + hashMap.toString());

        new VolleyRequester(activity, Const.GET, hashMap, GET_ACCOUNT_INFORMATION, asyncTaskCompleteListener);
    }

    public void getNursingHomeInformation(String userID, String sessionToken, int GET_ACCOUNT_INFORMATION){

        if(!EbizworldUtils.isNetworkAvailable(activity)){

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.NursingHomeService.GET_ACCOUNT_INFORMATION_URL + Const.Params.ID + "=" + userID +
                "&" + Const.Params.TOKEN + "=" + sessionToken);

        EbizworldUtils.appLogDebug("HaoLS", "getNursingHomeInformation " + hashMap.toString());

        new VolleyRequester(activity, Const.GET, hashMap, GET_ACCOUNT_INFORMATION, asyncTaskCompleteListener);
    }

    public void getHospitalInformation(String userID, String sessionToken, int GET_ACCOUNT_INFORMATION){

        if(!EbizworldUtils.isNetworkAvailable(activity)){

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;

        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.HospitalService.GET_ACCOUNT_INFORMATION_URL + Const.Params.ID + "=" + userID +
                "&" + Const.Params.TOKEN + "=" + sessionToken);

        EbizworldUtils.appLogDebug("HaoLS", "getHospitalInformation " + hashMap.toString());

        new VolleyRequester(activity, Const.GET, hashMap, GET_ACCOUNT_INFORMATION, asyncTaskCompleteListener);
    }

}
