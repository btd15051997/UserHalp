package sg.halp.user.Presenter.SignInPatient;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.SocialMediaProfile;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.Activity.SignInPatient.ISignInPatientView;
import sg.halp.user.restAPI.RegisterSocialNetworkAPI;
import sg.halp.user.restAPI.SignInManualAPI;
import sg.halp.user.restAPI.SignInSocialNetworkAPI;

public class SignInPatientPresenter implements ISignInPatientPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private ISignInPatientView iSignInPatientView;
    private String mPassword;
    private ParseContent pcontent;
    private SocialMediaProfile mSocialMediaProfile;

    public SignInPatientPresenter(Activity activity, ISignInPatientView iSignInPatientView) {

        this.activity = activity;
        this.iSignInPatientView = iSignInPatientView;

    }

    @Override
    public void loginManual(String username, String password, String loginType) {

        if (TextUtils.isEmpty(username)){

            iSignInPatientView.onUsernameError();

        }else if (TextUtils.isEmpty(password)){

            iSignInPatientView.onPasswordError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignInPatientView.onNetworkDisconnect();

        }else if (TextUtils.equals(loginType, Const.MANUAL)){

            this.mPassword = password;

            pcontent = new ParseContent(activity);

            iSignInPatientView.onShowProgressDialog();

            new SignInManualAPI(activity, this).patientLogin(
                    username,
                    password,
                    new PreferenceHelper(activity).getDeviceToken(),
                    loginType,
                    Const.ServiceCode.LOGIN
            );


        }

    }

    @Override
    public void loginSocialNetwork(SocialMediaProfile socialMediaProfile, String loginType) {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iSignInPatientView.onNetworkDisconnect();

        }else if (socialMediaProfile == null){

            iSignInPatientView.onLoginFail(activity.getResources().getString(R.string.something_was_wrong));

        }else {

            this.mSocialMediaProfile = socialMediaProfile;

            pcontent = new ParseContent(activity);

            iSignInPatientView.onShowProgressDialog();

            new SignInSocialNetworkAPI(activity, this).patientSignInSocialNetwork(
                    new PreferenceHelper(activity).getDeviceToken(),
                    socialMediaProfile,
                    Const.ServiceCode.SOCIAL_LOGIN
            );

        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.LOGIN:{

                EbizworldUtils.appLogInfo("HaoLS", "Login response: " + response);
                iSignInPatientView.onHideProgressDialog();

                if (response != null) {

                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            if (pcontent.isSuccessWithStoreId(response)) {

                                pcontent.parsePatientAndStoreToDb(response);

                                new PreferenceHelper(activity).putPassword(mPassword);

                                iSignInPatientView.onLoginSuccess();

                            }

                        } else {

                            if (job1.getString("error_code").equals("167")){

                                String error = job1.getString("error");

                                iSignInPatientView.onLoginFail(error);

                            }else {

                                String error = job1.getString("error");
                                iSignInPatientView.onLoginFail(error);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("Manh", "Login failed: " + e.toString());
                    }
                }

            }
            break;

            case Const.ServiceCode.SOCIAL_LOGIN:{

                EbizworldUtils.appLogInfo("HaoLS", "Login response: " + response);
                iSignInPatientView.onHideProgressDialog();

                if (response != null){

                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            if (pcontent.isSuccessWithStoreId(response)) {

                                pcontent.parsePatientAndStoreToDb(response);

                                new PreferenceHelper(activity).putPassword(mPassword);

                                iSignInPatientView.onLoginSuccess();

                            }

                        } else {

                            if (job1.getString("error_code").equals("125")) {

                                if (mSocialMediaProfile != null){

                                    if (!EbizworldUtils.isNetworkAvailable(activity)){

                                        iSignInPatientView.onNetworkDisconnect();

                                    }else {

                                        iSignInPatientView.onShowProgressDialog();

                                        new RegisterSocialNetworkAPI(activity, this)
                                                .registerPatient(
                                                        mSocialMediaProfile,
                                                        new PreferenceHelper(activity).getDeviceToken(),
                                                        Const.ServiceCode.SOCIAL_REGISTER
                                                );

                                    }

                                }

                            }else if (job1.getString("error_code").equals("167")){

                                String error = job1.getString("error");

                                iSignInPatientView.onLoginFail(error);

                            }else {

                                String error = job1.getString("error");
                                iSignInPatientView.onLoginFail(error);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("Manh", "Login failed: " + e.toString());
                    }

                }

            }
            break;

            case Const.ServiceCode.SOCIAL_REGISTER:{

                EbizworldUtils.appLogInfo("HaoLS", "register response: " + response);
                iSignInPatientView.onHideProgressDialog();

                if (response != null){

                    try {

                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {


                            if (pcontent.isSuccessWithStoreId(response)) {

                                pcontent.parsePatientAndStoreToDb(response);

                                iSignInPatientView.onLoginSuccess();

                            }

                        } else {

                            String error = job1.getString("error_messages");
                            iSignInPatientView.onLoginFail(error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "Register failed: " + e.toString());
                    }

                }
            }
            break;
        }
    }
}
