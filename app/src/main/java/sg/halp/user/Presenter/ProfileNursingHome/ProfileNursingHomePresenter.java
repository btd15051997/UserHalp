package sg.halp.user.Presenter.ProfileNursingHome;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import sg.halp.user.Activity.ProfileNursingHome.IProfileNursingHomeView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.NursingHomeProfile;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.AmbulanceOperatorListAPI;
import sg.halp.user.restAPI.GetInformationAPI;
import sg.halp.user.restAPI.UpdateProfileAPI;

public class ProfileNursingHomePresenter implements IProfileNursingHomePresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IProfileNursingHomeView iProfileNursingHomeView;

    public ProfileNursingHomePresenter(Activity activity, IProfileNursingHomeView iProfileNursingHomeView) {
        this.activity = activity;
        this.iProfileNursingHomeView = iProfileNursingHomeView;
    }

    @Override
    public void getAmbulanceOperatorList() {

        new AmbulanceOperatorListAPI(activity, this)
                .getAmbulanceOperatorForNursingHome(Const.ServiceCode.AMBULANCE_OPERATOR);

    }

    @Override
    public void updateProfile(NursingHomeProfile nursingHomeProfile) {

        if (TextUtils.isEmpty(nursingHomeProfile.getFullname())){

            iProfileNursingHomeView.onFullnameError();

        }else if(TextUtils.isEmpty(nursingHomeProfile.getPhoneNumber())){

            iProfileNursingHomeView.onMobileError();

        }else if (TextUtils.isEmpty(nursingHomeProfile.getContactName())){

            iProfileNursingHomeView.onContactNameError();

        }else if (TextUtils.isEmpty(nursingHomeProfile.getContactNumber())){

            iProfileNursingHomeView.onContactNumberError();

        }else if(TextUtils.isEmpty(nursingHomeProfile.getPreferredUsername())){

            iProfileNursingHomeView.onPreferredUsernameError();

        }else if (TextUtils.isEmpty(nursingHomeProfile.getAddress())){

            iProfileNursingHomeView.onAddressError();

        }else {

            new UpdateProfileAPI(activity, this)
                    .updateNursingHomeProfile(
                            new PreferenceHelper(activity).getUserId(),
                            new PreferenceHelper(activity).getSessionToken(),
                            nursingHomeProfile,
                            Const.ServiceCode.UPDATE_PROFILE
                    );

            iProfileNursingHomeView.showProgressDialog();

        }

    }

    @Override
    public void getInformation() {

        new GetInformationAPI(activity, this)
                .getNursingHomeInformation(
                        new PreferenceHelper(activity).getUserId(),
                        new PreferenceHelper(activity).getSessionToken(),
                        Const.ServiceCode.GET_ACCOUNT_INFORMATION
                );

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.AMBULANCE_OPERATOR:{

                EbizworldUtils.appLogDebug("HaoLS", "AMBULANCE_OPERATOR: " + response);

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            JSONArray jsonArray = jsonObject.getJSONArray("operator");


                            if (jsonArray.length() > 0){

                                EbizworldUtils.appLogDebug("HaoLS", "Ambulance Operator list " + jsonArray.toString());

                                List<AmbulanceOperator> ambulanceOperators = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    AmbulanceOperator ambulanceOperator = new AmbulanceOperator();

                                    ambulanceOperator.setId(String.valueOf(object.getInt("id")));
                                    ambulanceOperator.setAmbulanceOperator(object.getString("name"));
                                    ambulanceOperator.setAmbulanceImage(object.getString("picture"));
                                    ambulanceOperators.add(ambulanceOperator);

                                }

                                iProfileNursingHomeView.setupSpinnerAmbulanceOperator(ambulanceOperators);

                            }

                        }else {

                            EbizworldUtils.appLogDebug("HaoLS", "ProfileActivity get Ambulance Operator failed" + response);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "ProfileActivity get Ambulance Operator failed  " + e.toString());
                    }

                }
            }
            break;

            case Const.ServiceCode.UPDATE_PROFILE:{

                EbizworldUtils.appLogDebug("HaoLS", "Const.ServiceCode.UPDATE_PROFILE: " + response);
                iProfileNursingHomeView.hideProgressDialog();

                if (response != null){

                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            iProfileNursingHomeView.onCleanFileCache();

                            getInformation();

                        } else {

                            if (job1.has("error_messages")){

                                iProfileNursingHomeView.onErrorMessage(job1.getString("error_messages"));

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Update profile failed " + e.toString());

                    }

                }
            }
            break;

            case Const.ServiceCode.GET_ACCOUNT_INFORMATION:{

                EbizworldUtils.appLogDebug("HaoLS", "GET_ACCOUNT_INFORMATION: " + response);

                if (response != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (new ParseContent(activity).isSuccessWithStoreId(response) && new ParseContent(activity).parseNurseAndStoreToDbByGSON(response)){

                                iProfileNursingHomeView.onUpdateProfileSuccess();

                                EbizworldUtils.appLogDebug("HaoLS", "NursingHome isSuccessWithStore succeeded ");
                            }

                        }else {

                            if (jsonObject.has("error_messages")){

                                iProfileNursingHomeView.onErrorMessage(jsonObject.getString("error_messages"));

                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Get account information failed " + e.toString());
                    }
                }
            }
            break;

        }

    }
}
