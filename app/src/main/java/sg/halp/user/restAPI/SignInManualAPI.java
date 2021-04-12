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
import sg.halp.user.R;
import sg.halp.user.SignInActivity;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class SignInManualAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public SignInManualAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void hospitalLogin(String username, String password, String deviceToken, String loginType, int HOSPITAL_LOGIN){

        HashMap<String,String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.HospitalService.LOGIN_URL);

        if (deviceToken == null){

            if (!AmbulanceFCMService.getDeviceTokenManual(activity).isEmpty()){

                map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(activity).getDeviceToken());

            }else {

                EbizworldUtils.showLongToast(activity.getResources().getString(R.string.something_was_wrong), activity);
                return;
            }

        }else {

            map.put(Const.Params.DEVICE_TOKEN, deviceToken);
        }

        map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE);
        map.put(Const.Params.LOGIN_BY, loginType);
        map.put(Const.Params.EMAIL, username);
        map.put(Const.Params.PASSWORD, password);
        Log.d("HaoLS", map.toString());

        new VolleyRequester(activity,
                Const.POST,
                map,
                HOSPITAL_LOGIN,
                asyncTaskCompleteListener);

    }

    public void nursingHomeLogin(String username, String password, String deviceToken, String loginType, int NURSE_LOGIN){

        HashMap<String,String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.NursingHomeService.LOGIN_URL);

        if (deviceToken == null){

            if (!AmbulanceFCMService.getDeviceTokenManual(activity).isEmpty()){

                map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(activity).getDeviceToken());

            }else {

                EbizworldUtils.showLongToast(activity.getResources().getString(R.string.something_was_wrong), activity);
                return;
            }

        }else {

            map.put(Const.Params.DEVICE_TOKEN, deviceToken);
        }

        map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE);
        map.put(Const.Params.LOGIN_BY, loginType);
        map.put(Const.Params.EMAIL, username);
        map.put(Const.Params.PASSWORD, password);
        Log.d("HaoLS", map.toString());

        new VolleyRequester(activity,
                Const.POST,
                map,
                NURSE_LOGIN,
                asyncTaskCompleteListener);

    }

    public void patientLogin(String username, String password, String deviceToken, String loginType, int LOGIN){

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.LOGIN);
        map.put(Const.Params.EMAIL, username);
        map.put(Const.Params.PASSWORD, password);

        if (deviceToken == null){

            if (!AmbulanceFCMService.getDeviceTokenManual(activity).isEmpty()){

                map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(activity).getDeviceToken());

            }else {

                EbizworldUtils.showLongToast(activity.getResources().getString(R.string.something_was_wrong), activity);
                return;
            }

        }else {

            map.put(Const.Params.DEVICE_TOKEN, deviceToken);
        }

        map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
        map.put(Const.Params.LOGIN_BY, loginType);
        EbizworldUtils.appLogDebug("HaoLS", "Login: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, LOGIN, asyncTaskCompleteListener);
    }
}
