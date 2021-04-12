package sg.halp.user.restAPI;

import android.app.Activity;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class GiveRatingAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public GiveRatingAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void giveRatingForPatient(int rating, int RATE_PROVIDER) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }

        Commonutils.progressDialog_show(activity, "Rating...");
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.ServiceType.RATE_PROVIDER);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put(Const.Params.COMMENT, "");
        map.put(Const.Params.RATING, String.valueOf(rating));

        EbizworldUtils.appLogDebug("HaoLS", "RATE_PROVIDER: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, RATE_PROVIDER, asyncTaskCompleteListener);

    }

    public void giveRatingForNursingHome(int rating, int RATE_PROVIDER) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }

        Commonutils.progressDialog_show(activity, "Rating...");
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.NursingHomeService.RATE_PROVIDER_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put(Const.Params.COMMENT, "");
        map.put(Const.Params.RATING, String.valueOf(rating));

        EbizworldUtils.appLogDebug("HaoLS", "RATE_PROVIDER: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, RATE_PROVIDER, asyncTaskCompleteListener);

    }

    public void giveRatingForHospital(int rating, int RATE_PROVIDER) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }

        Commonutils.progressDialog_show(activity, "Rating...");
        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.HospitalService.RATE_PROVIDER_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put(Const.Params.COMMENT, "");
        map.put(Const.Params.RATING, String.valueOf(rating));

        EbizworldUtils.appLogDebug("HaoLS", "RATE_PROVIDER: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, RATE_PROVIDER, asyncTaskCompleteListener);

    }
}
