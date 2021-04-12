package sg.halp.user.Presenter.PreSelectPaymentDialog;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Dialog.PreSelectPayment.IPreSelectPaymentView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.PreSelectPaymentAPI;

public class PreSelectPaymentPresenter implements IPreSelectPaymentPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IPreSelectPaymentView iPreSelectPaymentView;
    private String mPaymentMode;

    public PreSelectPaymentPresenter(Activity activity, IPreSelectPaymentView iPreSelectPaymentView) {
        this.activity = activity;
        this.iPreSelectPaymentView = iPreSelectPaymentView;
    }

    @Override
    public void updatePreSelectPayment(String paymentMode, String code) {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iPreSelectPaymentView.onDisconnectNetwork();

        }else {

            iPreSelectPaymentView.showProgressDialog();

            mPaymentMode = code;

            new PreSelectPaymentAPI(activity, this)
                    .updatePatientPaymentMode(
                            new PreferenceHelper(activity).getUserId(),
                            new PreferenceHelper(activity).getPassword(),
                            code,
                            Const.ServiceCode.UPDATE_PRE_SELECT_PAYMENT
                    );

        }

    }

    @Override
    public void getPreSelectPayment() {

        new PreSelectPaymentAPI(activity, this)
                .getPatientPaymentMode(
                        new PreferenceHelper(activity).getUserId(),
                        new PreferenceHelper(activity).getSessionToken(),
                        Const.ServiceCode.GET_PRE_SELECT_PAYMENT);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case  Const.ServiceCode.UPDATE_PRE_SELECT_PAYMENT:{

                EbizworldUtils.appLogInfo("HaoLS", "UPDATE_PRE_SELECT_PAYMENT: " + response);
                iPreSelectPaymentView.hideProgressDialog();

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            new PreferenceHelper(activity).putPaymentMode(mPaymentMode);

                            iPreSelectPaymentView.onUpdatePaymentSuccess();

                        }else {

                            if (jsonObject.has("error")){

                                iPreSelectPaymentView.onUpdatePaymentFail(jsonObject.getString("error"));

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        EbizworldUtils.appLogInfo("HaoLS", "UPDATE_PRE_SELECT_PAYMENT fail: " + e.toString());

                        iPreSelectPaymentView.onUpdatePaymentFail(e.toString());
                    }

                }

            }
            break;

            case Const.ServiceCode.GET_PRE_SELECT_PAYMENT:{

                EbizworldUtils.appLogInfo("HaoLS", "GET_PRE_SELECT_PAYMENT: " + response);

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("true")){

                        }else {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "GET_PRE_SELECT_PAYMENT failed: " + e.toString());
                    }
                }
            }
            break;

        }

    }
}
