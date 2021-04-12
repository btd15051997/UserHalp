package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class PreSelectPaymentAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public PreSelectPaymentAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getPatientPaymentMode(String userID, String token, int GET_PRE_SELECT_PAYMENT){

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.ServiceType.GET_PRE_SELECT_PAYMENT_URL + Const.Params.ID + "=" + userID + "&" + Const.Params.TOKEN + "=" + token);

        EbizworldUtils.appLogDebug("HaoLS", "GET_PRE_SELECT_PAYMENT: " + hashMap.toString());

        new VolleyRequester(activity, Const.GET, hashMap, GET_PRE_SELECT_PAYMENT, asyncTaskCompleteListener);

    }

    public void updatePatientPaymentMode(String userID, String token, String paymentMode, int UPDATE_PRE_SELECT_PAYMENT){

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(Const.Params.URL, Const.ServiceType.UPDATE_PRE_SELECT_PAYMENT_URL);
        hashMap.put(Const.Params.ID, userID);
        hashMap.put(Const.Params.TOKEN, token);
        hashMap.put(Const.Params.PAYMENT_MODE, paymentMode);

        EbizworldUtils.appLogDebug("HaoLS", "GET_PRE_SELECT_PAYMENT: " + hashMap.toString());

        new VolleyRequester(activity, Const.POST, hashMap, UPDATE_PRE_SELECT_PAYMENT, asyncTaskCompleteListener);

    }
}
