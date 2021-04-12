package sg.halp.user.Presenter.ProfileHospital;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sg.halp.user.Activity.ProfileHospital.IProfileHospitalView;
import sg.halp.user.Activity.ProfileHospital.ProfileHospitalActivity;
import sg.halp.user.Adapter.AmbulanceOperatorSpinnerAdapter;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.HospitalProfile;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.AmbulanceOperatorListAPI;
import sg.halp.user.restAPI.GetInformationAPI;
import sg.halp.user.restAPI.UpdateProfileAPI;

public class ProfileHospitalPresenter implements IProfileHospitalPresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IProfileHospitalView iProfileHospitalView;

    public ProfileHospitalPresenter(Activity activity, IProfileHospitalView iProfileHospitalView) {
        this.activity = activity;
        this.iProfileHospitalView = iProfileHospitalView;
    }


    @Override
    public void getAmbulanceOperatorList() {

        new AmbulanceOperatorListAPI(activity, this)
                .getAmbulanceOperatorForHospital(Const.ServiceCode.AMBULANCE_OPERATOR);

    }

    @Override
    public void updateProfile(HospitalProfile hospitalProfile) {

        if (TextUtils.isEmpty(hospitalProfile.getFullname())){

            iProfileHospitalView.onFullnameError();

        }else if(TextUtils.isEmpty(hospitalProfile.getPhoneNumber())){

            iProfileHospitalView.onMobileError();

        }else if (TextUtils.isEmpty(hospitalProfile.getContactName())){

            iProfileHospitalView.onContactNameError();

        }else if (TextUtils.isEmpty(hospitalProfile.getContacNumber())){

            iProfileHospitalView.onContactNumberError();

        }else if(TextUtils.isEmpty(hospitalProfile.getPreferredUsername())){

            iProfileHospitalView.onPreferredUsernameError();

        }else if (TextUtils.isEmpty(hospitalProfile.getPostal())){

            iProfileHospitalView.onPostalError();

        }else if (TextUtils.isEmpty(hospitalProfile.getFloorNumber())){

            iProfileHospitalView.onFloorNumberError();

        }else if (TextUtils.isEmpty(hospitalProfile.getWard())){

            iProfileHospitalView.onWardError();

        }else if (TextUtils.isEmpty(hospitalProfile.getAddress())){

            iProfileHospitalView.onAddressError();

        }else if (!EbizworldUtils.isNetworkAvailable(activity)){

            iProfileHospitalView.onDisconnectNetwork();

        }else {

            new UpdateProfileAPI(activity, this)
                    .updateHospitalProfile(
                            new PreferenceHelper(activity).getUserId(),
                            new PreferenceHelper(activity).getSessionToken(),
                            hospitalProfile,
                            Const.ServiceCode.UPDATE_PROFILE
                    );

            iProfileHospitalView.showProgressDialog();

        }
    }

    @Override
    public void getInformation() {

        new GetInformationAPI(activity, this)
                .getHospitalInformation(
                        new PreferenceHelper(activity).getUserId(),
                        new PreferenceHelper(activity).getSessionToken(),
                        Const.ServiceCode.GET_ACCOUNT_INFORMATION
                );

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.AMBULANCE_OPERATOR:{

                EbizworldUtils.appLogInfo("HaoLS", "AMBULANCE_OPERATOR: " + response);

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

                                iProfileHospitalView.setupSpinnerAmbulanceOperator(ambulanceOperators);

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

                Commonutils.progressDialog_hide();
                EbizworldUtils.appLogInfo("HaoLS", "UPDATE_PROFILE: " + response);
                if (response != null) {
                    EbizworldUtils.appLogDebug("HaoLS", "profile response" + response);
                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            iProfileHospitalView.onCleanFileCache();

                            getInformation();

                        } else {

                            if (job1.has("error_messages")){

                                String error = job1.getString("error_messages");

                                iProfileHospitalView.onErrorMessage(error);

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

                EbizworldUtils.appLogInfo("HaoLS", "GET_ACCOUNT_INFORMATION: " + response);

                iProfileHospitalView.hideProgressDialog();

                if (response != null){

                    EbizworldUtils.appLogDebug("HaoLS", "Get account information detail " + response);

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            ParseContent parseContent = new ParseContent(activity);

                            if (parseContent.isSuccessWithStoreId(response) && parseContent.parseHospitalAndStoreToDatabaseByGSON(response)){

                                iProfileHospitalView.onUpdateProfileSuccess();

                                activity.onBackPressed();

                                EbizworldUtils.appLogDebug("HaoLS", "Hospital isSuccessWithStore succeeded ");
                            }

                        }else {

                            if (jsonObject.has("error_messages")){

                                String errorMessage = jsonObject.getString("error_messages");

                                iProfileHospitalView.onErrorMessage(errorMessage);

                                EbizworldUtils.appLogDebug("HaoLS", "Get account information isSuccessWithStore failed ");

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
