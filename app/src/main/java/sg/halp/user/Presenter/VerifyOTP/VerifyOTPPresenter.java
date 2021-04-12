package sg.halp.user.Presenter.VerifyOTP;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Fragment.VerifyOTP.IVerifyOTPView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.restAPI.GetOTPAPI;

public class VerifyOTPPresenter implements IVerifyOTPPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IVerifyOTPView iVerifyOTPView;

    public VerifyOTPPresenter(Activity activity, IVerifyOTPView iVerifyOTPView) {
        this.activity = activity;
        this.iVerifyOTPView = iVerifyOTPView;
    }


    @Override
    public void getOTP(String mobile, String countryCode) {

        if (TextUtils.isEmpty(mobile)){

            iVerifyOTPView.onMobileError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iVerifyOTPView.onDisconnectNetwork();

        }else {

            iVerifyOTPView.showProgressDialog();

            new GetOTPAPI(activity, this)
                    .getOTP(mobile, countryCode, Const.ServiceCode.GET_OTP);

        }

    }

    @Override
    public void checkOTP(String code, String userInputCode) {

        if (TextUtils.isEmpty(userInputCode)){

            iVerifyOTPView.onOTPError();
        }else if (!userInputCode.equals(code)){

            iVerifyOTPView.onVerifyFail();

        }else {

            iVerifyOTPView.onVerifySuccess();

        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode){

            case  Const.ServiceCode.GET_OTP:{

                EbizworldUtils.appLogInfo("HaoLS", "OTP Response: " + response);
                iVerifyOTPView.hideProgressDialog();

                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {

                        iVerifyOTPView.setCode(job.optString("code"));
                        iVerifyOTPView.onDisplayOTPCode(job.optString("code"));

                    } else if (job.has("message")){

                        String error = job.optString("message");

                        iVerifyOTPView.onGetOTPFail(error);

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    EbizworldUtils.appLogError("HaoLS", "GET OTP failed: " + e.toString());

                    iVerifyOTPView.onGetOTPFail(e.toString());

                }

            }
            break;
        }
    }
}
