package sg.halp.user.Presenter.SignInHospital;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.Activity.SignInHospital.ISignInHospitalView;
import sg.halp.user.restAPI.SignInManualAPI;

public class SignInHospitalPresenter implements ISignInHospitalPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private ISignInHospitalView iSignInHospitalView;
    private ParseContent pcontent;
    private String mPassword;

    public SignInHospitalPresenter(Activity activity, ISignInHospitalView iSignInHospitalView) {
        this.activity = activity;
        this.iSignInHospitalView = iSignInHospitalView;
    }

    @Override
    public void loginManual(String username, String password, String loginType) {

        if (TextUtils.isEmpty(username)){

            iSignInHospitalView.onUsernameError();

        }else if (TextUtils.isEmpty(password)){

            iSignInHospitalView.onPasswordError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignInHospitalView.onNetworkDisconnect();

        }else if (TextUtils.equals(loginType, Const.MANUAL)){

            this.mPassword = password;

            pcontent = new ParseContent(activity);

            iSignInHospitalView.onShowProgressDialog();

            new SignInManualAPI(activity, SignInHospitalPresenter.this)
                    .hospitalLogin(
                            username,
                            password,
                            new PreferenceHelper(activity).getDeviceToken(),
                            loginType,
                            Const.ServiceCode.HOSPITAL_LOGIN
                    );

        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.HOSPITAL_LOGIN:{

                EbizworldUtils.appLogInfo("HaoLS", "Hospital loginManual Response " + response);

                iSignInHospitalView.onHideProgressDialog();

                if (response != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (pcontent.isSuccessWithStoreId(response) && pcontent.parseHospitalAndStoreToDatabaseByGSON(response)){

                                new PreferenceHelper(activity).putPassword(this.mPassword);

                                iSignInHospitalView.onLoginSuccess();

                                EbizworldUtils.appLogDebug("HaoLS", "Login succeeded");

                            }else {

                                EbizworldUtils.appLogDebug("HaoLS", "Login failed");
                                iSignInHospitalView.onLoginFail(activity.getResources().getString(R.string.something_was_wrong));
                            }

                        }else {

                            String error = jsonObject.getString("error");

                            iSignInHospitalView.onLoginFail(error);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Login failed " + e.toString());

                        iSignInHospitalView.onLoginFail(e.toString());

                    }
                }


            }
            break;

        }

    }

}
