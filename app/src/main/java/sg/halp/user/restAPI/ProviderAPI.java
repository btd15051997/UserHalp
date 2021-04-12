package sg.halp.user.restAPI;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class ProviderAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public ProviderAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getAllProvidersForPatient(LatLng latlong, int GET_PROVIDERS) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.GET_PROVIDERS);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        if (latlong != null) {
            map.put(Const.Params.LATITUDE, String.valueOf(latlong.latitude));
            map.put(Const.Params.LONGITUDE, String.valueOf(latlong.longitude));
        }

        map.put(Const.Params.AMBULANCE_TYPE, new PreferenceHelper(mActivity).getRequestType());

        Log.d("HaoLS", "nearby drivers " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, GET_PROVIDERS, asyncTaskCompleteListener);

    }

    public void getAllProvidersForNursingHome(LatLng latlong, int GET_PROVIDERS) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.NursingHomeService.GUEST_PROVIDER_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        if (latlong != null) {
            map.put(Const.Params.LATITUDE, String.valueOf(latlong.latitude));
            map.put(Const.Params.LONGITUDE, String.valueOf(latlong.longitude));
        }

        map.put(Const.Params.AMBULANCE_TYPE, new PreferenceHelper(mActivity).getRequestType());

        Log.d("HaoLS", "nearby drivers " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, GET_PROVIDERS, asyncTaskCompleteListener);

    }

    public void getAllProvidersForHospital(LatLng latlong, int GET_PROVIDERS) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.HospitalService.GUEST_PROVIDER_LIST_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        if (latlong != null) {
            map.put(Const.Params.LATITUDE, String.valueOf(latlong.latitude));
            map.put(Const.Params.LONGITUDE, String.valueOf(latlong.longitude));
        }

        map.put(Const.Params.AMBULANCE_TYPE, new PreferenceHelper(mActivity).getRequestType());

        Log.d("HaoLS", "nearby drivers " + map.toString());
        new VolleyRequester(mActivity, Const.POST, map, GET_PROVIDERS, asyncTaskCompleteListener);

    }
}
