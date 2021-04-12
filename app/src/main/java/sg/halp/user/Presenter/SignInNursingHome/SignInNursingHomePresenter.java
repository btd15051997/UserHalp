package sg.halp.user.Presenter.SignInNursingHome;

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
import sg.halp.user.Activity.SignInNursingHome.ISignInNursingHomeView;
import sg.halp.user.restAPI.SignInManualAPI;

public class SignInNursingHomePresenter implements ISignInNursingHomePresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private ISignInNursingHomeView iSignInNursingHomeView;
    private ParseContent pcontent;
    private String mPassword;

    public SignInNursingHomePresenter(Activity activity, ISignInNursingHomeView iSignInNursingHomeView) {
        this.activity = activity;
        this.iSignInNursingHomeView = iSignInNursingHomeView;
    }

    @Override
    public void loginManual(String username, String password, String loginType) {

        if (TextUtils.isEmpty(username)){

            iSignInNursingHomeView.onUsernameError();

        }else if (TextUtils.isEmpty(password)){

            iSignInNursingHomeView.onPasswordError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignInNursingHomeView.onNetworkDisconnect();

        }else if (TextUtils.equals(loginType, Const.MANUAL)){

            this.mPassword = password;

            pcontent = new ParseContent(activity);

            iSignInNursingHomeView.onShowProgressDialog();

            new SignInManualAPI(activity, SignInNursingHomePresenter.this)
                    .nursingHomeLogin(
                            username,
                            password,
                            new PreferenceHelper(activity).getDeviceToken(),
                            loginType,
                            Const.ServiceCode.NURSE_LOGIN
                    );

        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.NURSE_LOGIN:{

                EbizworldUtils.appLogInfo("HaoLS", "Hospital loginManual Response " + response);

                iSignInNursingHomeView.onHideProgressDialog();

                if (response != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (pcontent.isSuccessWithStoreId(response) && pcontent.parseNurseAndStoreToDbByGSON(response)){

                                new PreferenceHelper(activity).putPassword(this.mPassword);

                                iSignInNursingHomeView.onLoginSuccess();

                                EbizworldUtils.appLogDebug("HaoLS", "Login succeeded");

                            }else {

                                EbizworldUtils.appLogDebug("HaoLS", "Login failed");
                                iSignInNursingHomeView.onLoginFail(activity.getResources().getString(R.string.something_was_wrong));
                            }

                        }else {

                            String error = jsonObject.getString("error");

                            iSignInNursingHomeView.onLoginFail(error);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Login failed " + e.toString());

                        iSignInNursingHomeView.onLoginFail(e.toString());

                    }
                }


            }
            break;

        }

    }
}
