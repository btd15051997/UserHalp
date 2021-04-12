package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.RegisterManual;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class RegisterManualAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public RegisterManualAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void registerManualForPatient(RegisterManual registerManual, int REGISTER) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Const.Params.URL, Const.ServiceType.REGISTER);
        hashMap.put(Const.Params.FULLNAME, registerManual.getFullname());
        hashMap.put(Const.Params.EMAIL, registerManual.getEmail());
        hashMap.put(Const.Params.PASSWORD, registerManual.getPassword());
        hashMap.put(Const.Params.MOBILE, registerManual.getMobile());
        hashMap.put(Const.Params.COUNTRY_CODE, registerManual.getCountryCode());
        hashMap.put(Const.Params.CURRENCY_CODE, registerManual.getCurrencyCode());
        hashMap.put(Const.Params.DEVICE_TOKEN, registerManual.getDeviceToken());
        hashMap.put(Const.Params.DEVICE_TYPE, registerManual.getDeviceType());
        hashMap.put(Const.Params.LOGIN_BY, registerManual.getLoginBy());
        hashMap.put(Const.Params.TIMEZONE, registerManual.getTimeZone());
        hashMap.put(Const.Params.REFERRAL_CODE, registerManual.getReferralCode());

        EbizworldUtils.appLogDebug("HaoLS", hashMap.toString());

        new VolleyRequester(mActivity, Const.POST, hashMap, REGISTER, asyncTaskCompleteListener);

    }
}
