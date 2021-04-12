package sg.halp.user.Presenter.BillingInfo;

import android.app.Activity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Dialog.RequestLoaderDialog;
import sg.halp.user.Fragment.BaseFragment;
import sg.halp.user.Fragment.BillingInfo.IBillingInfoView;
import sg.halp.user.Fragment.TravelMapFragment;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.RequestDetail;
import sg.halp.user.Models.RequestOptional;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.BillingInfoAPI;
import sg.halp.user.restAPI.CheckRequestStatusAPI;
import sg.halp.user.restAPI.PaymentAPI;

public class BillingInfoPresenter implements IBillingInfoPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IBillingInfoView iBillingInfoView;
    private RequestDetail mRequestDetail;
    private RequestOptional mRequestOptional;
    private RequestLoaderDialog requestLoaderDialog;

    public BillingInfoPresenter(Activity activity, IBillingInfoView iBillingInfoView) {
        this.activity = activity;
        this.iBillingInfoView = iBillingInfoView;
    }


    @Override
    public void paymentByCash(int requestID, String loginType) {

        iBillingInfoView.showProgressDialog();

        switch (loginType){

            case Const.PatientService.PATIENT:{

                new PaymentAPI(activity, this)
                        .paymentByCashForPatient(Const.ServiceCode.PAYMENT_CASH, requestID);

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new PaymentAPI(activity, this)
                        .paymentByCashForNursingHome(Const.ServiceCode.PAYMENT_CASH, requestID);

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new PaymentAPI(activity, this)
                        .paymentByCashForHospital(Const.ServiceCode.PAYMENT_CASH, requestID);

            }
            break;
        }

    }

    @Override
    public void getBrainTreeToken(String loginType) {

        switch (loginType){

            case Const.PatientService.PATIENT:{

                new PaymentAPI(activity, this)
                        .getBrainTreeClientTokenForPatient(Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL);

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new PaymentAPI(activity, this)
                        .getBrainTreeClientTokenForNursingHome(Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL);
            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new PaymentAPI(activity, this)
                        .getBrainTreeClientTokenForHospital(Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL);

            }
            break;
        }

    }

    @Override
    public void postNonceToServer(String nonce, String loginType) {

        EbizworldUtils.appLogDebug("HaoLS", "Billing Info Nonce: " + nonce);

        iBillingInfoView.showProgressDialog();

        switch (loginType){

            case Const.PatientService.PATIENT:{

                new PaymentAPI(activity, this)
                        .postNonceToServerForPatientNormal(nonce, Const.ServiceCode.POST_PAYPAL_NONCE);

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new PaymentAPI(activity, this)
                        .postNonceToServerForNursingHomeNormal(nonce, Const.ServiceCode.POST_PAYPAL_NONCE);

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new PaymentAPI(activity, this)
                        .postNonceToServerForHospitalNormal(nonce, Const.ServiceCode.POST_PAYPAL_NONCE);

            }
            break;
        }
    }

    @Override
    public void getNormalTripBillingInfo(RequestOptional requestOptional, String loginType) {

        iBillingInfoView.showProgressDialog();

        mRequestOptional = requestOptional;

        switch (loginType){

            case Const.PatientService.PATIENT:{

                new BillingInfoAPI(activity, this)
                        .getPatientNormalBillingInfo(
                                requestOptional,
                                new PreferenceHelper(activity).getUserId(),
                                new PreferenceHelper(activity).getSessionToken(),
                                Const.ServiceCode.BILLING_INFO);

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new BillingInfoAPI(activity, this)
                        .getNursingHomeNormalBillingInfo(
                                requestOptional,
                                new PreferenceHelper(activity).getUserId(),
                                new PreferenceHelper(activity).getSessionToken(),
                                Const.ServiceCode.BILLING_INFO);

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new BillingInfoAPI(activity, this)
                        .getHospitalNormalBillingInfo(
                                requestOptional,
                                new PreferenceHelper(activity).getUserId(),
                                new PreferenceHelper(activity).getSessionToken(),
                                Const.ServiceCode.BILLING_INFO);

            }
            break;
        }

    }

    @Override
    public void checkRequestStatus(String loginType, RequestLoaderDialog requestLoaderDialog) {

        this.requestLoaderDialog = requestLoaderDialog;

        switch (loginType){

            case Const.PatientService.PATIENT:{

                new CheckRequestStatusAPI(activity, this)
                        .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new CheckRequestStatusAPI(activity, this)
                        .checkRequestStatusForNursingHome(Const.ServiceCode.CHECKREQUEST_STATUS);

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new CheckRequestStatusAPI(activity, this)
                        .checkRequestStatusForHospital(Const.ServiceCode.CHECKREQUEST_STATUS);

            }
            break;
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){

            case Const.ServiceCode.PAYMENT_CASH:{

                EbizworldUtils.appLogInfo("HaoLS", "Payment Cash " + response);
                iBillingInfoView.hideProgressDialog();

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            iBillingInfoView.onPayCashSuccess(mRequestDetail);

                        }else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                iBillingInfoView.onHandleFail(jsonObject.getString("error"));

                            }else {

                                iBillingInfoView.onHandleFail(activity.getResources().getString(R.string.payment_by_cash_failed));

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", activity.getResources().getString(R.string.payment_by_cash_failed) + " " + e.toString());

                        iBillingInfoView.onHandleFail(activity.getResources().getString(R.string.payment_by_cash_failed));
                    }
                }

            }
            break;

            case Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL:{
                EbizworldUtils.appLogInfo("HaoLS", "GET_BRAIN_TREE_TOKEN_URL: " + response);

                if (response != null && activity != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (jsonObject.has("client_token")){

                                iBillingInfoView.onBrainTreeTokenResponse(jsonObject.getString("client_token"));

                            }

                        }else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                iBillingInfoView.onHandleFail(jsonObject.getString("error"));

                            }

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "GET_BRAIN_TREE_TOKEN_URL: " + e.toString());

                    }


                }

            }
            break;

            case Const.ServiceCode.POST_PAYPAL_NONCE:{
                EbizworldUtils.appLogInfo("HaoLS", "Post paypal nonce: " + response);
                iBillingInfoView.hideProgressDialog();

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            iBillingInfoView.onPostPayPalNonceSuccess(mRequestDetail);

                        }else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                iBillingInfoView.onHandleFail(jsonObject.getString("error"));

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "POST_PAYPAL_NONCE: " + e.toString());

                        iBillingInfoView.onHandleFail(e.toString());
                    }
                }

            }
            break;

            case Const.ServiceCode.BILLING_INFO:{
                EbizworldUtils.appLogInfo("HaoLS", "Billing info: " + response);
                iBillingInfoView.hideProgressDialog();

                try {
                    JSONObject object = new JSONObject(response);

                    if (object.getString("success").equals("true")){

                        JSONObject jsonObject = object.getJSONObject(Const.Params.BILLING_INFO);

                        String currency = "";
                        String a_and_e = "";
                        String imh = "";
                        String ferry_terminals = "";
                        String staircase = "";
                        String tarmac = "";
                        String weight = "";
                        String oxygenTank = "";
                        String caseType = "";
                        int other_expenses = 0, total = 0;

                        if (jsonObject.has("currency")){

                            currency = jsonObject.getString("currency");

                        }

                        if (jsonObject.has(Const.Params.A_AND_E)){

                            a_and_e = jsonObject.getString(Const.Params.A_AND_E);
                        }

                        if (jsonObject.has(Const.Params.IMH)){

                            imh = jsonObject.getString(Const.Params.IMH);
                        }

                        if (jsonObject.has(Const.Params.FERRY_TERMINALS)){

                            ferry_terminals = jsonObject.getString(Const.Params.FERRY_TERMINALS);
                        }

                        if (jsonObject.has(Const.Params.STAIRCASE)){

                            staircase = jsonObject.getString(Const.Params.STAIRCASE);
                        }

                        if (jsonObject.has(Const.Params.TARMAC)){

                            tarmac = jsonObject.getString(Const.Params.TARMAC);
                        }

                        if (jsonObject.has(Const.Params.WEIGHT)){

                            weight = jsonObject.getString(Const.Params.WEIGHT);
                        }

                        if (jsonObject.has(Const.Params.OXYGEN_TANK)){

                            oxygenTank = jsonObject.getString(Const.Params.OXYGEN_TANK);
                        }

                        if (jsonObject.has(Const.Params.CASE_TYPE)){

                            caseType = jsonObject.getString(Const.Params.CASE_TYPE);
                        }



                        if (jsonObject.has(Const.Params.OTHER_EXPENSES) && jsonObject.getInt(Const.Params.OTHER_EXPENSES) > 0){

                            if (jsonObject.has(Const.Params.TOTAL)){

                                total = jsonObject.getInt(Const.Params.TOTAL);
                                other_expenses = jsonObject.getInt(Const.Params.OTHER_EXPENSES);

                                total += other_expenses;

                            }

                        }else if (jsonObject.has(Const.Params.TOTAL)){

                            total = jsonObject.getInt(Const.Params.TOTAL);

                        }

                        iBillingInfoView.onRequestBillingInfoSuccess(
                                currency,
                                a_and_e,
                                imh,
                                ferry_terminals,
                                staircase,
                                tarmac,
                                weight,
                                oxygenTank,
                                caseType,
                                other_expenses,
                                total
                        );

                    }else {

                        iBillingInfoView.onRequestBillingInfoFail();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    EbizworldUtils.appLogError("HaoLS", "Get billing info failed: " + e.toString());

                    iBillingInfoView.onRequestBillingInfoFail();
                }

            }
            break;

            case Const.ServiceCode.CHECKREQUEST_STATUS:{

                EbizworldUtils.appLogInfo("HaoLS", "check req status: " + response);

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            mRequestDetail = new ParseContent(activity).parseRequestStatusNormal(response);

                            if (mRequestDetail == null){

                                iBillingInfoView.onHandleFail(activity.getResources().getString(R.string.something_was_wrong));
                                return;

                            }

                            if (mRequestOptional != null){

                                Bundle bundle = new Bundle();

                                switch (mRequestDetail.getTripStatus()) {

                                    case Const.NO_REQUEST:
                                        new PreferenceHelper(activity).clearRequestData();

                                        if (requestLoaderDialog != null && requestLoaderDialog.getDialog().isShowing()) {

                                            requestLoaderDialog.dismiss();

                                            iBillingInfoView.onHandleFail(activity.getResources().getString(R.string.txt_no_provider_error));

                                        }

                                        break;

                                    case Const.IS_ACCEPTED:{

                                        bundle.putSerializable(Const.REQUEST_DETAIL,
                                                mRequestDetail);

                                        bundle.putInt(Const.DRIVER_STATUS,
                                                Const.IS_ACCEPTED);

                                        iBillingInfoView.onRequestAccept(bundle);


                                        BaseFragment.drop_latlan = null;
                                        BaseFragment.pic_latlan = null;
                                        BaseFragment.s_address = "";
                                        BaseFragment.d_address = "";

                                    }
                                    break;
                                    case Const.IS_DRIVER_DEPARTED:{

                                    }
                                    break;

                                    case Const.IS_DRIVER_ARRIVED:{

                                    }
                                    break;

                                    case Const.IS_DRIVER_TRIP_STARTED:{

                                    }
                                    break;

                                    case Const.IS_DRIVER_TRIP_ENDED:{

                                    }
                                    break;

                                    case Const.IS_DRIVER_RATED:{

                                    }
                                    break;

                                    default:
                                        break;

                                }

                            }else {

                                iBillingInfoView.onFinalBillingInfoSuccess(mRequestDetail, new PreferenceHelper(activity).getPaymentMode());

                            }

                        }else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                iBillingInfoView.onHandleFail(jsonObject.getString("error"));

                            }

                            if (requestLoaderDialog != null && requestLoaderDialog.getDialog().isShowing()) {

                                requestLoaderDialog.dismiss();

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "CHECKREQUEST_STATUS failed " + e.toString());

                        iBillingInfoView.onHandleFail(e.toString());

                    }


                }

            }
            break;

        }
    }
}
