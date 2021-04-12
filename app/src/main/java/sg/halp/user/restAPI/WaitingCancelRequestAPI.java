package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class WaitingCancelRequestAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public WaitingCancelRequestAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void waitingCancelRequestForPatient(String userID, String sessionToken, int requestID,int CANCEL_CREATE_REQUEST) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.ServiceType.CANCEL_CREATE_REQUEST);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.REQUEST_ID, String.valueOf(requestID));

        EbizworldUtils.appLogDebug("HaoLS", "Cancel request: " + hashMap.toString());
        new VolleyRequester(activity, Const.POST, hashMap, CANCEL_CREATE_REQUEST, asyncTaskCompleteListener);
    }

    public void waitingCancelRequestForNursingHome(String userID, String sessionToken, int requestID,int CANCEL_CREATE_REQUEST) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.NursingHomeService.WAITING_CANCEL_REQUEST_URL);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.REQUEST_ID, String.valueOf(requestID));

        EbizworldUtils.appLogDebug("HaoLS", "Cancel request: " + hashMap.toString());
        new VolleyRequester(activity, Const.POST, hashMap, CANCEL_CREATE_REQUEST, asyncTaskCompleteListener);
    }

    public void waitingCancelRequestForHospital(String userID, String sessionToken, int requestID,int CANCEL_CREATE_REQUEST) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.HospitalService.WAITING_CANCEL_REQUEST_URL);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, sessionToken);
        hashMap.put(Const.Params.REQUEST_ID, String.valueOf(requestID));

        EbizworldUtils.appLogDebug("HaoLS", "Cancel request: " + hashMap.toString());
        new VolleyRequester(activity, Const.POST, hashMap, CANCEL_CREATE_REQUEST, asyncTaskCompleteListener);
    }
}
