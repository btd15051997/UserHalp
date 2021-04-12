package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class GetOTPAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public GetOTPAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getOTP(String mobile, String countryCode, int GET_OTP) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.GET_OTP + Const.Params.MOBILE + "=" + mobile
                + "&" + Const.Params.COUNTRY_CODE + "=" + countryCode);

        EbizworldUtils.appLogDebug("HaoLS", "GET_OTP: " + map.toString());
        new VolleyRequester(activity, Const.GET, map, GET_OTP, asyncTaskCompleteListener);
    }
}
