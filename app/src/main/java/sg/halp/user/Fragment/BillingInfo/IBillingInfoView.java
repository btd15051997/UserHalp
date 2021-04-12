package sg.halp.user.Fragment.BillingInfo;

import android.os.Bundle;

import sg.halp.user.Models.RequestDetail;
import sg.halp.user.Models.RequestOptional;

public interface IBillingInfoView {

    void settingForRequestBillingInfo();
    void settingForFinalBillingInfoWhenEndTrip();
    void onFinalBillingInfoSuccess(RequestDetail requestDetail, String paymentMode);
    void onHandleFail(String error);
    void onRequestAccept(Bundle bundle);
    void onRequestBillingInfoSuccess(
            String currency,
            String a_and_e,
            String imh,
            String ferry_terminals,
            String staircase,
            String tarmac,
            String weight,
            String oxygen_tank,
            String case_type,
            int other_expenses,
            int total
            );
    void onRequestBillingInfoFail();
    void showProgressDialog();
    void hideProgressDialog();
    void onBrainTreeTokenResponse(String client_token);
    void onPostPayPalNonceSuccess(RequestDetail requestDetail);
    void onPayCashSuccess(RequestDetail requestDetail);
    void onConfirmNormalRequest(RequestOptional requestOptional);
}
