package sg.halp.user.restAPI;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;
import java.util.TimeZone;

import sg.halp.user.HttpRequester.MultiPartRequester;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.SocialMediaProfile;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class RegisterSocialNetworkAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public RegisterSocialNetworkAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void registerPatient(SocialMediaProfile mediaProfile, String deviceToken, int SOCIAL_REGISTER){

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.REGISTER);
        map.put(Const.Params.FIRSTNAME, mediaProfile.getFirstName());
        map.put(Const.Params.LAST_NAME, mediaProfile.getLastName());
        /*map.put(Const.Params.FULLNAME, mediaProfile.getFullname());*/
        map.put(Const.Params.EMAIL, mediaProfile.getEmailId());

        if (null != mediaProfile.getPictureUrl()) {
            map.put(Const.Params.PICTURE, mediaProfile.getPictureUrl());
        } else {
            map.put(Const.Params.PICTURE, "");
        }

        // map.put(Const.Params.SPECIALITY, String.valueOf(speclty.getId()));
        map.put(Const.Params.DEVICE_TOKEN, deviceToken);
        map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);

        map.put(Const.Params.LOGIN_BY, mediaProfile.getLoginType());
        map.put(Const.Params.SOCIAL_ID, mediaProfile.getSocialUniqueId());

        map.put(Const.Params.MOBILE, "");

        map.put(Const.Params.CURRENCY_CODE, "");
        map.put(Const.Params.TIMEZONE, TimeZone.getDefault().getID());
        map.put(Const.Params.COUNTRY, "");

        map.put(Const.Params.GENDER, "");


        EbizworldUtils.appLogDebug("HaoLS", "Register social network: " + map.toString());

        if (null != mediaProfile.getPictureUrl()) {

            new MultiPartRequester(activity, map, SOCIAL_REGISTER, asyncTaskCompleteListener);

        } else {

            new VolleyRequester(activity, Const.POST, map, SOCIAL_REGISTER, asyncTaskCompleteListener);
        }
    }
}
