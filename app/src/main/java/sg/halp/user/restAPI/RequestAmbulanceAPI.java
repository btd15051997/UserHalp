package sg.halp.user.restAPI;

import android.app.Activity;
import android.util.Log;

import java.util.HashMap;

import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.RequestOptional;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class RequestAmbulanceAPI {

    private Activity activity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public RequestAmbulanceAPI(Activity activity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.activity = activity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void RequestAmbulanceForPatient(RequestOptional requestOptional, String userID, String sessionToken, int REQUEST_AMBULANCE) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.ServiceType.REQUEST_AMBULANCE);
        map.put(Const.Params.ID, userID);
        map.put(Const.Params.TOKEN, sessionToken);

        map.put(Const.Params.S_LATITUDE, String.valueOf(requestOptional.getPic_lat()));
        map.put(Const.Params.S_LONGITUDE, String.valueOf(requestOptional.getPic_lng()));

        map.put(Const.Params.D_LONGITUDE, String.valueOf(requestOptional.getDrop_lng()));
        map.put(Const.Params.D_LATITUDE, String.valueOf(requestOptional.getDrop_lat()));

        /*map.put(Const.Params.IS_ADSTOP, String.valueOf(requestOptional.getIsAddStop()));
        map.put(Const.Params.ADSTOP_LONGITUDE, String.valueOf(requestOptional.getAddStop_lng()));
        map.put(Const.Params.ADSTOP_LATITUDE, String.valueOf(requestOptional.getAddStop_lat()));
        map.put(Const.Params.ADSTOP_ADDRESS, requestOptional.getAddStop_address());*/

        map.put(Const.Params.SERVICE_TYPE, String.valueOf(requestOptional.getOperator_id()));
        map.put(Const.Params.S_ADDRESS, requestOptional.getPic_address());
        map.put(Const.Params.D_ADDRESS, requestOptional.getDrop_address());
        map.put(Const.Params.REQ_STATUS_TYPE, String.valueOf(requestOptional.getRequest_status_type()));
        map.put(Const.Params.PROMOCODE, requestOptional.getPromoCode());
        map.put(Const.Params.REMARK, requestOptional.getRemark());

        map.put(Const.Params.A_AND_E,String.valueOf(requestOptional.getA_and_e()));
        map.put(Const.Params.IMH,String.valueOf(requestOptional.getImh()));
        map.put(Const.Params.FERRY_TERMINALS,String.valueOf(requestOptional.getFerry_terminals()));
        map.put(Const.Params.STAIRCASE,String.valueOf(requestOptional.getStaircase()));
        map.put(Const.Params.TARMAC,String.valueOf(requestOptional.getTarmac()));
        map.put(Const.Params.FAMILY_MEMBER, String.valueOf(requestOptional.getFamily_member()));
        map.put(Const.Params.HOUSE_UNIT, requestOptional.getHouseUnit());
        map.put(Const.Params.WEIGHT, String.valueOf(requestOptional.getWeight()));
        map.put(Const.Params.OXYGEN_TANK,String.valueOf(requestOptional.getOxygen()));
        map.put(Const.Params.CASE_TYPE, String.valueOf(requestOptional.getCaseType()));

        EbizworldUtils.appLogDebug("HaoLS", "Request ambulance: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, REQUEST_AMBULANCE, asyncTaskCompleteListener);
    }

    public void RequestAmbulanceForNursingHome(RequestOptional requestOptional, String userID, String sessionToken, int REQUEST_AMBULANCE) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.NursingHomeService.SEND_REQUEST_URL);
        map.put(Const.Params.ID, userID);
        map.put(Const.Params.TOKEN, sessionToken);

        map.put(Const.Params.S_LATITUDE, String.valueOf(requestOptional.getPic_lat()));
        map.put(Const.Params.S_LONGITUDE, String.valueOf(requestOptional.getPic_lng()));

        map.put(Const.Params.D_LONGITUDE, String.valueOf(requestOptional.getDrop_lng()));
        map.put(Const.Params.D_LATITUDE, String.valueOf(requestOptional.getDrop_lat()));

        /*map.put(Const.Params.IS_ADSTOP, String.valueOf(requestOptional.getIsAddStop()));
        map.put(Const.Params.ADSTOP_LONGITUDE, String.valueOf(requestOptional.getAddStop_lng()));
        map.put(Const.Params.ADSTOP_LATITUDE, String.valueOf(requestOptional.getAddStop_lat()));
        map.put(Const.Params.ADSTOP_ADDRESS, requestOptional.getAddStop_address());*/

        map.put(Const.Params.SERVICE_TYPE, String.valueOf(requestOptional.getOperator_id()));
        map.put(Const.Params.S_ADDRESS, requestOptional.getPic_address());
        map.put(Const.Params.D_ADDRESS, requestOptional.getDrop_address());
        map.put(Const.Params.REQ_STATUS_TYPE, String.valueOf(requestOptional.getRequest_status_type()));
        map.put(Const.Params.PROMOCODE, requestOptional.getPromoCode());
        map.put(Const.Params.REMARK, requestOptional.getRemark());

        map.put(Const.Params.A_AND_E,String.valueOf(requestOptional.getA_and_e()));
        map.put(Const.Params.IMH,String.valueOf(requestOptional.getImh()));
        map.put(Const.Params.FERRY_TERMINALS,String.valueOf(requestOptional.getFerry_terminals()));
        map.put(Const.Params.STAIRCASE,String.valueOf(requestOptional.getStaircase()));
        map.put(Const.Params.TARMAC,String.valueOf(requestOptional.getTarmac()));
        map.put(Const.Params.FAMILY_MEMBER, String.valueOf(requestOptional.getFamily_member()));
        map.put(Const.Params.HOUSE_UNIT, requestOptional.getHouseUnit());
        map.put(Const.Params.WEIGHT, String.valueOf(requestOptional.getWeight()));
        map.put(Const.Params.OXYGEN_TANK,String.valueOf(requestOptional.getOxygen()));
        map.put(Const.Params.CASE_TYPE, String.valueOf(requestOptional.getCaseType()));

        EbizworldUtils.appLogDebug("HaoLS", "Request ambulance: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, REQUEST_AMBULANCE, asyncTaskCompleteListener);
    }

    public void RequestAmbulanceForHospital(RequestOptional requestOptional, String userID, String sessionToken, int REQUEST_AMBULANCE) {

        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(activity.getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();

        map.put(Const.Params.URL, Const.HospitalService.SEND_REQUEST_URL);
        map.put(Const.Params.ID, userID);
        map.put(Const.Params.TOKEN, sessionToken);

        map.put(Const.Params.S_LATITUDE, String.valueOf(requestOptional.getPic_lat()));
        map.put(Const.Params.S_LONGITUDE, String.valueOf(requestOptional.getPic_lng()));

        map.put(Const.Params.D_LONGITUDE, String.valueOf(requestOptional.getDrop_lng()));
        map.put(Const.Params.D_LATITUDE, String.valueOf(requestOptional.getDrop_lat()));

        /*map.put(Const.Params.IS_ADSTOP, String.valueOf(requestOptional.getIsAddStop()));
        map.put(Const.Params.ADSTOP_LONGITUDE, String.valueOf(requestOptional.getAddStop_lng()));
        map.put(Const.Params.ADSTOP_LATITUDE, String.valueOf(requestOptional.getAddStop_lat()));
        map.put(Const.Params.ADSTOP_ADDRESS, requestOptional.getAddStop_address());*/

        map.put(Const.Params.SERVICE_TYPE, String.valueOf(requestOptional.getOperator_id()));
        map.put(Const.Params.S_ADDRESS, requestOptional.getPic_address());
        map.put(Const.Params.D_ADDRESS, requestOptional.getDrop_address());
        map.put(Const.Params.REQ_STATUS_TYPE, String.valueOf(requestOptional.getRequest_status_type()));
        map.put(Const.Params.PROMOCODE, requestOptional.getPromoCode());
        map.put(Const.Params.REMARK, requestOptional.getRemark());

        map.put(Const.Params.A_AND_E,String.valueOf(requestOptional.getA_and_e()));
        map.put(Const.Params.IMH,String.valueOf(requestOptional.getImh()));
        map.put(Const.Params.FERRY_TERMINALS,String.valueOf(requestOptional.getFerry_terminals()));
        map.put(Const.Params.STAIRCASE,String.valueOf(requestOptional.getStaircase()));
        map.put(Const.Params.TARMAC,String.valueOf(requestOptional.getTarmac()));
        map.put(Const.Params.FAMILY_MEMBER, String.valueOf(requestOptional.getFamily_member()));
        map.put(Const.Params.HOUSE_UNIT, requestOptional.getHouseUnit());
        map.put(Const.Params.WEIGHT, String.valueOf(requestOptional.getWeight()));
        map.put(Const.Params.OXYGEN_TANK,String.valueOf(requestOptional.getOxygen()));
        map.put(Const.Params.CASE_TYPE, String.valueOf(requestOptional.getCaseType()));

        EbizworldUtils.appLogDebug("HaoLS", "Request ambulance: " + map.toString());
        new VolleyRequester(activity, Const.POST, map, REQUEST_AMBULANCE, asyncTaskCompleteListener);
    }
}
