package sg.halp.user.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Fragment.ScheduleListFragment;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.MainActivity;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.PaymentAPI;

public class CancellationFeeBillingDialog extends DialogFragment implements View.OnClickListener, AsyncTaskCompleteListener {

    @BindView(R.id.custom_simple_title)
    TextView billing_title;

    @BindView(R.id.custom_simple_content)
    TextView billing_content;

    @BindView(R.id.btn_yes)
    TextView btn_yes;

    @BindView(R.id.btn_no)
    TextView btn_no;

    private String price_cancellation = "", currency = "", requestID = "";
    private Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = getActivity();

        Bundle bundle = getArguments();

        if (bundle != null){

            price_cancellation = bundle.getString(Const.Params.PRICE_CANCEL_SCHEDULE);
            currency = bundle.getString(Const.Params.CURRENCY);
            requestID = bundle.getString(Const.Params.REQUEST_ID);

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim_leftright_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.fade_drawable));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom_simple);

        ButterKnife.bind(this, dialog);

        billing_title.setText(getResources().getString(R.string.cancellation_billing_info_title));
        billing_content.setText(currency + " " + price_cancellation + " " + getResources().getString(R.string.cancellation_billing_info_content));

        btn_yes.setText(getResources().getString(R.string.txt_pay_paypal));
        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.ServiceCode.REQUEST_PAYPAL){

            if (resultCode == Activity.RESULT_OK){

                DropInResult dropInResult = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                if (dropInResult.getPaymentMethodNonce() != null){

                    if (new PreferenceHelper(mActivity).getLoginType().equals(Const.PatientService.PATIENT)){

                        Commonutils.progressDialog_show(mActivity, "Loading...");
                        new PaymentAPI(mActivity, this)
                                .postNonceToServerForPatientSchedule(dropInResult.getPaymentMethodNonce().getNonce(), Const.ServiceCode.POST_PAYPAL_NONCE, Integer.parseInt(requestID));

                    }else if (new PreferenceHelper(mActivity).getLoginType().equals(Const.NursingHomeService.NURSING_HOME)){

                        Commonutils.progressDialog_show(mActivity, "Loading...");
                        new PaymentAPI(mActivity, this)
                                .postNonceToServerForNursingHomeSchedule(dropInResult.getPaymentMethodNonce().getNonce(), Const.ServiceCode.POST_PAYPAL_NONCE, Integer.parseInt(requestID));

                    }else if (new PreferenceHelper(mActivity).getLoginType().equals(Const.HospitalService.HOSPITAL)){

                        Commonutils.progressDialog_show(mActivity, "Loading...");
                        new PaymentAPI(mActivity, this)
                                .postNonceToServerForHospitalSchedule(dropInResult.getPaymentMethodNonce().getNonce(), Const.ServiceCode.POST_PAYPAL_NONCE, Integer.parseInt(requestID));

                    }

                    EbizworldUtils.appLogDebug("HaoLS", "Billing Info Nonce: " + dropInResult.getPaymentMethodNonce().getNonce());
                }

            }else if (resultCode == Activity.RESULT_CANCELED){

                EbizworldUtils.showShortToast("Request is canceled", mActivity);

            }else {

                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                EbizworldUtils.showShortToast(exception.toString(), mActivity);
                EbizworldUtils.appLogError("HaoLS", "PayPal result" + exception.toString());
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_yes:{

                if (new PreferenceHelper(mActivity).getLoginType().equals(Const.PatientService.PATIENT)){

                    new PaymentAPI(mActivity, this).getBrainTreeClientTokenForPatient(Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL);

                }else if (new PreferenceHelper(mActivity).getLoginType().equals(Const.NursingHomeService.NURSING_HOME)){

                    new PaymentAPI(mActivity, this).getBrainTreeClientTokenForNursingHome(Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL);

                }else if (new PreferenceHelper(mActivity).getLoginType().equals(Const.HospitalService.HOSPITAL)){

                    new PaymentAPI(mActivity, this).getBrainTreeClientTokenForHospital(Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL);

                }

            }
            break;

            case R.id.btn_no:{

                dismiss();

            }
            break;
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL:{

                EbizworldUtils.appLogInfo("HaoLS", "GET_BRAIN_TREE_TOKEN_URL: " + response);

                if (response != null && mActivity != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (jsonObject.has("client_token")){

                                DropInRequest dropInRequest = new DropInRequest()
                                        .clientToken(jsonObject.getString("client_token"));

                                startActivityForResult(dropInRequest.getIntent(mActivity), Const.ServiceCode.REQUEST_PAYPAL);
                            }

                        }else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                EbizworldUtils.showShortToast(jsonObject.getString("error"), mActivity);

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
                Commonutils.progressDialog_hide();

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            dismiss();

                            ((MainActivity)mActivity).addFragment(new ScheduleListFragment(), false, Const.SCHEDULE_LIST_FRAGMENT, true);

                        }else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                EbizworldUtils.showShortToast(jsonObject.getString("error"), mActivity);

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "POST_PAYPAL_NONCE: " + e.toString());
                        EbizworldUtils.showShortToast("POST_PAYPAL_NONCE: " + e.toString(), mActivity);
                    }
                }

            }
            break;
        }

    }
}
