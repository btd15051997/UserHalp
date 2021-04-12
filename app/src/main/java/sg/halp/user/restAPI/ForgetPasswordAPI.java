package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class ForgetPasswordAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public ForgetPasswordAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void requestPatientPassword(String username, int FORGOT_PASSWORD) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.FORGOT_PASSWORD);
        map.put(Const.Params.EMAIL, username);
        EbizworldUtils.appLogDebug("HaoLS", "Request password: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, FORGOT_PASSWORD, asyncTaskCompleteListener);

    }

    public void changePatientPassword(String mobile, String password, int CHANGE_PASSWORD){

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.CHANGE_PASSWORD_URL);
        map.put(Const.Params.MOBILE, mobile);
        map.put(Const.Params.PASSWORD, password);
        EbizworldUtils.appLogDebug("HaoLS", "Reset password: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, CHANGE_PASSWORD, asyncTaskCompleteListener);

    }

    public void sendOTPResetPatientPassword(String mobile, int OTP_RESET_PASSWORD){

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.OTP_RESET_PASSWORD_URL + Const.Params.MOBILE + "=" + mobile);
        EbizworldUtils.appLogDebug("HaoLS", "Request password: " + map.toString());
        new VolleyRequester(activity, Const.GET, map, OTP_RESET_PASSWORD, asyncTaskCompleteListener);

    }
}
