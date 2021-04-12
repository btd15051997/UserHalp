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

public class LatLngGoogleAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public LatLngGoogleAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getLatLngFromSourceAddress(String selectedSourcePlace, int LOCATION_API_BASE_SOURCE) {
        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.LOCATION_API_BASE + selectedSourcePlace + "&key=" + Const.GOOGLE_API_KEY);

        EbizworldUtils.appLogDebug("HaoLS", "map for s_loc" + map);
        new VolleyRequester(mActivity, Const.GET, map, LOCATION_API_BASE_SOURCE, asyncTaskCompleteListener);
    }

    public void getLatLngFromDestinationAddress(String selectedDestPlace, int LOCATION_API_BASE_DESTINATION) {
        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.LOCATION_API_BASE + selectedDestPlace + "&key=" + Const.GOOGLE_API_KEY);

        Log.d("Manh", "map for d_loc" + map);
        new VolleyRequester(mActivity, Const.GET, map, LOCATION_API_BASE_DESTINATION, asyncTaskCompleteListener);
    }

    public void getCompleteAddress(LatLng target,int GEO_DEST) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.GEO_DEST + target.latitude + "," + target.longitude + "&key=" + Const.GOOGLE_API_KEY);

        Log.d("Manh", "map for address" + map);

        new VolleyRequester(mActivity, Const.GET, map, GEO_DEST, asyncTaskCompleteListener);
    }
}
