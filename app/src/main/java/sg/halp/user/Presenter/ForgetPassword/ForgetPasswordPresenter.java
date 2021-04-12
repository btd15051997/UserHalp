package sg.halp.user.Presenter.ForgetPassword;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Fragment.ForgotPassword.IForgotPasswordView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.restAPI.ForgetPasswordAPI;

public class ForgetPasswordPresenter implements IForgetPasswordPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IForgotPasswordView iForgotPasswordView;

    public ForgetPasswordPresenter(Activity activity, IForgotPasswordView iForgotPasswordView) {
        this.activity = activity;
        this.iForgotPasswordView = iForgotPasswordView;
    }

    @Override
    public void requestPassword(String username) {

        if (TextUtils.isEmpty(username)){

            iForgotPasswordView.onUsernameError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iForgotPasswordView.onDisconnectNetwork();

        }else {

            iForgotPasswordView.showProgressDialog();

            new ForgetPasswordAPI(activity, this)
                    .requestPatientPassword(username, Const.ServiceCode.FORGOT_PASSWORD);
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.FORGOT_PASSWORD:{
                EbizworldUtils.appLogInfo("HaoLS", "FORGOT_PASSWORD: " + response);
                iForgotPasswordView.hideProgressDialog();

                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {

                        iForgotPasswordView.onRequestSuccess();

                    } else {

                        String error = job.getString("error_messages");
                        iForgotPasswordView.onRequestFail(error);

                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    iForgotPasswordView.onRequestFail(e.toString());

                }

            }
            break;
        }

    }
}
