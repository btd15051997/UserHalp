package sg.halp.user.Presenter.SignUpNext;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.CheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Fragment.SignUpNext.ISignUpNextView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.RegisterManual;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.ApplyReferralCodeAPI;
import sg.halp.user.restAPI.RegisterManualAPI;

public class SignUpNextPresenter implements ISignUpNextPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private ISignUpNextView iSignUpNextView;
    private RegisterManual mRegisterManual;

    public SignUpNextPresenter(Activity activity, ISignUpNextView iSignUpNextView) {
        this.activity = activity;
        this.iSignUpNextView = iSignUpNextView;
    }

    @Override
    public void onRegister(RegisterManual registerManual, CheckBox cb_policy_terms) {

        if (TextUtils.isEmpty(registerManual.getFullname())){

            iSignUpNextView.onFullNameError();

        }else if (TextUtils.isEmpty(registerManual.getPassword())){

            iSignUpNextView.onPasswordError();

        }else if (!cb_policy_terms.isChecked()){

            iSignUpNextView.onPolicyTermsUncheck();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignUpNextView.onDisconnectNetwork();

        }else {

            iSignUpNextView.showProgressDialog();

            mRegisterManual = registerManual;

            new RegisterManualAPI(activity, this)
                    .registerManualForPatient(registerManual, Const.ServiceCode.REGISTER);
        }

    }

    @Override
    public void onApplyReferralCode(String code) {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignUpNextView.onDisconnectNetwork();

        }else {

            iSignUpNextView.showProgressDialog();

            new ApplyReferralCodeAPI(activity, this)
                    .applyPatientReferral(code, Const.ServiceCode.APPLY_REFERRAL);

        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.REGISTER:{

                EbizworldUtils.appLogInfo("HaoLS", "REGISTER response: " + response);
                iSignUpNextView.hideProgressDialog();

                if (response != null)
                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            if (new ParseContent(activity).isSuccessWithStoreId(response)) {

                                new ParseContent(activity).parsePatientAndStoreToDb(response);

                                new PreferenceHelper(activity).putPassword(mRegisterManual.getPassword());

                                iSignUpNextView.onRegisterSuccess();

                            }

                        } else {

                            if (job1.has("error_code")) {

                                if (job1.optString("error_code").equals("168")) {


                                }
                            }

                            if (job1.has("error_messages")) {
                                String error = job1.getString("error_messages");

                                iSignUpNextView.onRegisterFail(error);

                            } else {

                                String error = job1.getString("error");
                                iSignUpNextView.onRegisterFail(error);

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
            break;

            case Const.ServiceCode.APPLY_REFERRAL:{

                EbizworldUtils.appLogInfo("HaoLS", "Referral response: " + response);
                iSignUpNextView.hideProgressDialog();

                if(response!=null) {

                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            iSignUpNextView.onApplyReferralCodeResponse(job1.getString("message"));

                        }else{

                            iSignUpNextView.onApplyReferralCodeResponse(job1.getString("error"));

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        iSignUpNextView.onApplyReferralCodeResponse(e.toString());
                    }

                }

            }
            break;
        }

    }
}
