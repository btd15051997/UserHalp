package sg.halp.user.restAPI;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;

import sg.halp.user.FirebaseHandler.AmbulanceFCMService;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.SocialMediaProfile;
import sg.halp.user.R;
import sg.halp.user.SignInActivity;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class SignInSocialNetworkAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public SignInSocialNetworkAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {

        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;

    }

    public void patientSignInSocialNetwork(String deviceToken, SocialMediaProfile socialMediaProfile, int SOCIAL_LOGIN){

        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.ServiceType.LOGIN);

        map.put(Const.Params.SOCIAL_ID, socialMediaProfile.getSocialUniqueId());

        if (deviceToken == null){

            map.put(Const.Params.DEVICE_TOKEN, AmbulanceFCMService.getDeviceTokenManual(activity));

        }else {

            map.put(Const.Params.DEVICE_TOKEN, deviceToken);

        }

        map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
        map.put(Const.Params.LOGIN_BY, socialMediaProfile.getLoginType());

        EbizworldUtils.appLogDebug("HaoLS", "social " + map.toString());

        new VolleyRequester(activity, Const.POST, map, SOCIAL_LOGIN, asyncTaskCompleteListener);
    }
}
