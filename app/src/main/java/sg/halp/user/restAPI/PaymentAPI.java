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

public class PaymentAPI {

    private Activity mActivity;
    private AsyncTaskCompleteListener asyncTaskCompleteListener;

    public PaymentAPI(Activity mActivity, AsyncTaskCompleteListener asyncTaskCompleteListener) {
        this.mActivity = mActivity;
        this.asyncTaskCompleteListener = asyncTaskCompleteListener;
    }

    public void getBrainTreeClientTokenForPatient(int GET_BRAIN_TREE_TOKEN_URL){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GET_BRAIN_TREE_TOKEN_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        EbizworldUtils.appLogDebug("HaoLS", "BrainTreeClientTokenMap for patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, GET_BRAIN_TREE_TOKEN_URL, asyncTaskCompleteListener);
    }

    public void getBrainTreeClientTokenForNursingHome(int GET_BRAIN_TREE_TOKEN_URL){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.NursingHomeService.GET_BRAINTREE_TOKEN_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        EbizworldUtils.appLogDebug("HaoLS", "BrainTreeClientTokenMap for nursing home: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, GET_BRAIN_TREE_TOKEN_URL, asyncTaskCompleteListener);
    }

    public void getBrainTreeClientTokenForHospital(int GET_BRAIN_TREE_TOKEN_URL){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.HospitalService.GET_BRAINTREE_TOKEN_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());

        EbizworldUtils.appLogDebug("HaoLS", "BrainTreeClientTokenMap for hospital: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, GET_BRAIN_TREE_TOKEN_URL, asyncTaskCompleteListener);
    }

    public void postNonceToServerForPatientNormal(String nonce, int POST_PAYPAL_NONCE) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.POST_PAYPAL_NONCE_NORMAL_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(mActivity).getRequestId()));
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        EbizworldUtils.appLogDebug("HaoLS", "postNonceToServer for Patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, POST_PAYPAL_NONCE, asyncTaskCompleteListener);

    }

    public void postNonceToServerForPatientSchedule(String nonce, int POST_PAYPAL_NONCE, int requestID) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.POST_PAYPAL_NONCE_SCHEDULE_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(requestID));
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        EbizworldUtils.appLogDebug("HaoLS", "postNonceToServer for Patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, POST_PAYPAL_NONCE, asyncTaskCompleteListener);

    }

    public void postNonceToServerForNursingHomeNormal(String nonce, int POST_PAYPAL_NONCE) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.NursingHomeService.POST_PAYPAL_NONCE_NORMAL_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(mActivity).getRequestId()));
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        EbizworldUtils.appLogDebug("HaoLS", "postNonceToServer for Patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, POST_PAYPAL_NONCE, asyncTaskCompleteListener);

    }

    public void postNonceToServerForNursingHomeSchedule(String nonce, int POST_PAYPAL_NONCE, int requestID) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.NursingHomeService.POST_PAYPAL_NONCE_SCHEDULE_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(requestID));
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        EbizworldUtils.appLogDebug("HaoLS", "postNonceToServer for Patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, POST_PAYPAL_NONCE, asyncTaskCompleteListener);

    }

    public void postNonceToServerForHospitalNormal(String nonce, int POST_PAYPAL_NONCE) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.HospitalService.POST_PAYPAL_NONCE_NORMAL_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(mActivity).getRequestId()));
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        EbizworldUtils.appLogDebug("HaoLS", "postNonceToServer for Patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, POST_PAYPAL_NONCE, asyncTaskCompleteListener);

    }

    public void postNonceToServerForHospitalSchedule(String nonce, int POST_PAYPAL_NONCE, int requestID) {

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;

        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.HospitalService.POST_PAYPAL_NONCE_SCHEDULE_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(requestID));
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        EbizworldUtils.appLogDebug("HaoLS", "postNonceToServer for Patient: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, POST_PAYPAL_NONCE, asyncTaskCompleteListener);

    }

    public void paymentByCashForPatient(int PAYMENT_CASH, int requestID){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.PAYMENT_CASH);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(requestID));

        EbizworldUtils.appLogDebug("HaoLS", "getPaymentByCash: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, PAYMENT_CASH, asyncTaskCompleteListener);
    }

    public void paymentByCashForNursingHome(int PAYMENT_CASH, int requestID){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.NursingHomeService.PAYMENT_BY_CASH_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(requestID));

        EbizworldUtils.appLogDebug("HaoLS", "getPaymentByCash: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, PAYMENT_CASH, asyncTaskCompleteListener);
    }

    public void paymentByCashForHospital(int PAYMENT_CASH, int requestID){

        if (!EbizworldUtils.isNetworkAvailable(mActivity)) {

            EbizworldUtils.showShortToast(mActivity.getResources().getString(R.string.network_error), mActivity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.HospitalService.PAYMENT_BY_CASH_URL);
        map.put(Const.Params.ID, new PreferenceHelper(mActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mActivity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(requestID));

        EbizworldUtils.appLogDebug("HaoLS", "getPaymentByCash: " + map.toString());

        new VolleyRequester(mActivity, Const.POST, map, PAYMENT_CASH, asyncTaskCompleteListener);
    }
}
