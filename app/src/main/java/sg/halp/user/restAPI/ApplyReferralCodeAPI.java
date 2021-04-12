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

public class ApplyReferralCodeAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public ApplyReferralCodeAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void applyPatientReferral(String code, int APPLY_REFERRAL) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.APPLY_REFERRAL);
        map.put(Const.Params.REFERRAL_CODE, code);
        EbizworldUtils.appLogDebug("HaoLS", "referral map: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, APPLY_REFERRAL, asyncTaskCompleteListener);
    }
}
