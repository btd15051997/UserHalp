package sg.halp.user.Presenter.SignUpMobile;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Fragment.SignUpMobile.ISignUpMobileView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.restAPI.GetOTPAPI;

public class SignUpMobilePresenter implements ISignUpMobilePresenter, AsyncTaskCompleteListener {

    private ISignUpMobileView iSignUpMobileView;
    private Activity activity;

    public SignUpMobilePresenter(ISignUpMobileView iSignUpMobileView, Activity activity) {
        this.iSignUpMobileView = iSignUpMobileView;
        this.activity = activity;
    }

    @Override
    public void getOTP(String mobile, String countryCode) {

        if (TextUtils.isEmpty(mobile)){

            iSignUpMobileView.onMobileError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignUpMobileView.onDisconnectNetwork();

        }else {

            iSignUpMobileView.showProgressDialog();

            new GetOTPAPI(activity, this)
                    .getOTP(mobile, countryCode, Const.ServiceCode.GET_OTP);
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.GET_OTP:{
                EbizworldUtils.appLogInfo("HaoLS", "GET_OTP: " + response);
                iSignUpMobileView.hideProgressDialog();

                if (response != null){

                    try {
                        JSONObject job = new JSONObject(response);

                        if (job.getString("success").equals("true")) {

                            String code = job.optString("code");

                            iSignUpMobileView.onGetOTPSuccess(code);

                        } else if (job.has("message")){

                            String error = job.optString("message");
                            EbizworldUtils.appLogError("HaoLS", "OTP Response fail " + error);

                            iSignUpMobileView.onGetOTPFail(error);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "OTP Response failed " + e.toString());

                        iSignUpMobileView.onGetOTPFail(e.toString());
                    }

                }

            }
            break;
        }

    }
}
