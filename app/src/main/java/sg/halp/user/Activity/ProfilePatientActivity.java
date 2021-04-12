package sg.halp.user.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import sg.halp.user.Adapter.AmbulanceOperatorSpinnerAdapter;
import sg.halp.user.Adapter.SimpleSpinnerAdapter;
import sg.halp.user.Dialog.ShowPictureDialog;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.Patient;
import sg.halp.user.Models.PatientProfile;
import sg.halp.user.R;
import sg.halp.user.RealmController.RealmController;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.AmbulanceOperatorListAPI;
import sg.halp.user.restAPI.GetInformationAPI;
import sg.halp.user.restAPI.UpdateProfileAPI;

public class ProfilePatientActivity extends AppCompatActivity implements View.OnClickListener, AsyncTaskCompleteListener {

    @BindView(R.id.toolbar_profile)
    Toolbar toolbar_profile;

    @BindView(R.id.profile_back)
    ImageButton profile_back;

    @BindView(R.id.tv_edit_profile)
    TextView tv_edit_profile;

    @BindView(R.id.img_profile_image)
    ImageView img_profile_image;

    @BindView(R.id.edt_fullname)
    EditText edt_fullname;

    @BindView(R.id.edt_family_member_name)
    EditText edt_family_member_name;

    @BindView(R.id.edt_patient_mobile)
    EditText edt_patient_mobile;

    @BindView(R.id.edt_email)
    EditText edt_email;

    @BindView(R.id.edt_home_number)
    EditText edt_home_number;

    @BindView(R.id.edt_block_number)
    EditText edt_block_number;

    @BindView(R.id.edt_unit_number)
    EditText edt_unit_number;

    @BindView(R.id.edt_postal)
    EditText edt_postal;

    @BindView(R.id.edt_street_name)
    EditText edt_street_name;

    @BindView(R.id.edt_preferred_username)
    EditText edt_preferred_username;

    @BindView(R.id.edt_weight)
    EditText edt_weight;

    @BindView(R.id.cb_lift_landing)
    CheckBox cb_lift_landing;

    @BindView(R.id.cb_stairs)
    CheckBox cb_stairs;

    @BindView(R.id.cb_no_stairs)
    CheckBox cb_no_stairs;

    @BindView(R.id.cb_low_stairs)
    CheckBox cb_low_stairs;

    @BindView(R.id.spn_ambulance_operator)
    Spinner spn_ambulance_operator;

    @BindView(R.id.spn_patient_condition)
    Spinner spn_patient_condition;

    @BindView(R.id.cb_stretcher)
    CheckBox cb_stretcher;

    @BindView(R.id.cb_wheel_chair)
    CheckBox cb_wheel_chair;

    @BindView(R.id.cb_oxygen)
    CheckBox cb_oxygen;

    @BindView(R.id.cb_escorts)
    CheckBox cb_escorts;

    @BindView(R.id.edt_additional_pation_information)
    EditText edt_additional_pation_information;

    @BindView(R.id.spn_payment_mode)
    Spinner spn_payment_mode;

    private AQuery aQuery;
    private ParseContent pcontent;
    private Realm realm;
    private ArrayList<AmbulanceOperator> mAmbulanceOperators;
    private AmbulanceOperator mAmbulanceOperator;
    private Patient mPatient;
    private String mFilePath ="";
    private ArrayList<String> mPatientConditions;
    private String mPatientCondition;
    private ArrayList<String> mPaymentModes;
    private String mPaymentMode;
    private AmbulanceOperatorSpinnerAdapter mAmbulanceOperatorSpinnerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        setContentView(R.layout.activity_patient_profile);
        ButterKnife.bind(this);

        aQuery = new AQuery(this);
        pcontent = new ParseContent(this);

        this.realm = RealmController.with(this).getRealm();
        RealmController.with(this).refresh();

        mAmbulanceOperators = new ArrayList<>();

        setSupportActionBar(toolbar_profile);
        getSupportActionBar().setTitle(null);

        img_profile_image.setOnClickListener(this);
        profile_back.setOnClickListener(this);
        tv_edit_profile.setOnClickListener(this);

        spn_ambulance_operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAmbulanceOperator = mAmbulanceOperators.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        disablePatientViews();

        setPatientValues();
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_profile_image:{

                FragmentManager fragmentManager = getSupportFragmentManager();
                final ShowPictureDialog showPictureDialog = new ShowPictureDialog();
                showPictureDialog.setCancelable(true);
                showPictureDialog.show(fragmentManager, Const.SHOW_PICTURE_DIALOGFRAGMENT);
                showPictureDialog.setCropPictureListener(new DialogFragmentCallback.CropPictureListener() {
                    @Override
                    public void onHandleCrop(String filePath) {

                        if (!filePath.equals("")){

                            mFilePath = filePath;

                            Glide.with(ProfilePatientActivity.this).load(mFilePath)
                                    .transition(new DrawableTransitionOptions().crossFade())
                                    .apply(new RequestOptions().error(R.drawable.defult_user).placeholder(R.drawable.defult_user).centerCrop())
                                    .into(img_profile_image);

                        }
                    }
                });

            }
            break;

            case R.id.profile_back:{

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                this.finish();

            }
            break;

            case R.id.tv_edit_profile:{

                if (tv_edit_profile.getText().toString().equals(getString(R.string.btn_edit))) {

                    enablePatientViews();

                    tv_edit_profile.setText(getString(R.string.btn_save));

                } else {

                    if (edt_fullname.getText().length() == 0 || edt_fullname.getText().length() > 255){

                        edt_fullname.setError(getResources().getString(R.string.edittext_inapproriate_notice));
                    }else if (edt_family_member_name.getText().length() == 0 || edt_family_member_name.getText().length() > 255){

                        edt_family_member_name.setError(getResources().getString(R.string.edittext_inapproriate_notice));

                    }else if (edt_patient_mobile.getText().length() < 6 || edt_patient_mobile.getText().length() > 13){

                        edt_patient_mobile.setError(getResources().getString(R.string.edittext_inapproriate_notice));

                    }else if (edt_block_number.getText().toString().equals("")){

                        edt_block_number.setError(getResources().getString(R.string.edittext_inapproriate_notice));

                    }else if (edt_preferred_username.getText().length() == 0 || edt_preferred_username.getText().length() > 255){

                        edt_preferred_username.setError(getResources().getString(R.string.edittext_inapproriate_notice));

                    }else if (edt_additional_pation_information.getText().length() == 0 || edt_additional_pation_information.getText().length() > 255){

                        edt_additional_pation_information.setError(getResources().getString(R.string.edittext_inapproriate_notice));
                    }else {

                        PatientProfile patientProfile = getPatientProfile();

                        if (patientProfile != null){

                            new UpdateProfileAPI(ProfilePatientActivity.this, ProfilePatientActivity.this)
                                    .updatePatientProfile(
                                            new PreferenceHelper(ProfilePatientActivity.this).getUserId(),
                                            new PreferenceHelper(ProfilePatientActivity.this).getSessionToken(),
                                            patientProfile,
                                            Const.ServiceCode.UPDATE_PROFILE
                                    );

                            Commonutils.progressDialog_show(this, getResources().getString(R.string.updating_pro_load));
                        }
//                                disablePatientViews();

                    }
                }

            }
            break;

        }
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.AMBULANCE_OPERATOR:{

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            JSONArray jsonArray = jsonObject.getJSONArray("operator");


                            if (jsonArray.length() > 0){

                                EbizworldUtils.appLogDebug("HaoLS", "Ambulance Operator list " + jsonArray.toString());

                                mAmbulanceOperators.clear();

                                for (int i = 0; i < jsonArray.length(); i++){

                                    JSONObject object = jsonArray.getJSONObject(i);

                                    AmbulanceOperator ambulanceOperator = new AmbulanceOperator();

                                    ambulanceOperator.setId(String.valueOf(object.getInt("id")));
                                    ambulanceOperator.setAmbulanceOperator(object.getString("name"));
                                    ambulanceOperator.setAmbulanceImage(object.getString("picture"));
                                    mAmbulanceOperators.add(ambulanceOperator);

                                }

                                if (mAmbulanceOperators.size() > 0){

                                    EbizworldUtils.appLogDebug("HaoLS", "Setting up Spinner for Ambulance Operator");

                                    mAmbulanceOperatorSpinnerAdapter = new AmbulanceOperatorSpinnerAdapter(this, mAmbulanceOperators);
                                    spn_ambulance_operator.setAdapter(mAmbulanceOperatorSpinnerAdapter);

                                    if (mPatient != null){

                                        EbizworldUtils.appLogDebug("HaoLS", "mPatient != null");
                                        EbizworldUtils.appLogDebug("HaoLS", "Operator id of Patient " + String.valueOf(mPatient.getmOperatorID()));

                                        for (int i = 0; i < mAmbulanceOperators.size(); i++){

                                            EbizworldUtils.appLogDebug("HaoLS", "Start for loop " + i );
                                            EbizworldUtils.appLogDebug("HaoLS", "Ambulance Operator id " + mAmbulanceOperators.get(i).getId());

                                            if (mPatient.getmOperatorID() == Integer.parseInt(mAmbulanceOperators.get(i).getId())){

                                                spn_ambulance_operator.setSelection(i);

                                                EbizworldUtils.appLogDebug("HaoLS", "Spinner set default text with Operator ID " + String.valueOf(mPatient.getmOperatorID()));
                                                break;
                                            }
                                        }

                                    }

                                }

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

                    try {

                        JSONObject job1 = new JSONObject(response);

                        if (job1.getString("success").equals("true")) {

                            try {

                                if (!mFilePath.equals("")) {

                                    File file = new File(mFilePath);
                                    file.getAbsoluteFile().delete();
                                    mFilePath = "";

                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            new GetInformationAPI(ProfilePatientActivity.this, ProfilePatientActivity.this)
                                    .getPatientInformation(
                                            new PreferenceHelper(this).getUserId(),
                                            new PreferenceHelper(this).getSessionToken(),
                                            Const.ServiceCode.GET_ACCOUNT_INFORMATION
                                    );

                        } else {

                            if (job1.has("error_messages")){

                                String error = job1.getString("error_messages");
                                Commonutils.showtoast(error, this);

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

                if (response != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (pcontent.isSuccessWithStoreId(response)) {

                                pcontent.parsePatientAndStoreToDb(response);

                                Commonutils.showtoast(getString(R.string.update_success_text), this);
                                tv_edit_profile.setText(getString(R.string.btn_edit));

                                disablePatientViews();
                                EbizworldUtils.appLogDebug("HaoLS", "Patient isSuccessWithStore succeeded ");

                                ProfilePatientActivity.this.onBackPressed();

                            }

                        }else {

                            if (jsonObject.has("error_messages")){

                                String errorMessage = jsonObject.getString("error_messages");

                                EbizworldUtils.showShortToast(errorMessage, this);
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

    private void enablePatientViews() {

        img_profile_image.setEnabled(true);
        edt_fullname.setEnabled(true);
        edt_family_member_name.setEnabled(true);
        edt_patient_mobile.setEnabled(true);
        edt_email.setEnabled(true);
        edt_home_number.setEnabled(true);
        edt_block_number.setEnabled(true);
        edt_unit_number.setEnabled(true);
        edt_postal.setEnabled(true);
        edt_street_name.setEnabled(true);
        edt_weight.setEnabled(true);
        cb_lift_landing.setEnabled(true);
        cb_stairs.setEnabled(true);
        cb_no_stairs.setEnabled(true);
        cb_low_stairs.setEnabled(true);
        spn_ambulance_operator.setEnabled(true);
        spn_patient_condition.setEnabled(true);
        cb_stretcher.setEnabled(true);
        cb_wheel_chair.setEnabled(true);
        cb_oxygen.setEnabled(true);
        cb_escorts.setEnabled(true);
        edt_additional_pation_information.setEnabled(true);
        spn_payment_mode.setEnabled(true);
        edt_preferred_username.setEnabled(true);

    }

    private void disablePatientViews() {
        img_profile_image.setEnabled(false);
        edt_fullname.setEnabled(false);
        edt_family_member_name.setEnabled(false);
        edt_patient_mobile.setEnabled(false);
        edt_email.setEnabled(false);
        edt_home_number.setEnabled(false);
        edt_block_number.setEnabled(false);
        edt_unit_number.setEnabled(false);
        edt_postal.setEnabled(false);
        edt_street_name.setEnabled(false);
        edt_weight.setEnabled(false);
        cb_lift_landing.setEnabled(false);
        cb_stairs.setEnabled(false);
        cb_no_stairs.setEnabled(false);
        cb_low_stairs.setEnabled(false);
        spn_ambulance_operator.setEnabled(false);
        spn_patient_condition.setEnabled(false);
        cb_stretcher.setEnabled(false);
        cb_wheel_chair.setEnabled(false);
        cb_oxygen.setEnabled(false);
        cb_escorts.setEnabled(false);
        edt_additional_pation_information.setEnabled(false);
        spn_payment_mode.setEnabled(false);
        edt_preferred_username.setEnabled(false);

    }

    private void setPatientValues() {

        mPatient = RealmController.with(this).getPatient(Integer.valueOf(new PreferenceHelper(this).getUserId()));

        if (mPatient != null) {

            //Getting Ambulance Operator list for set up to Ambulance Operator Spinner
            new AmbulanceOperatorListAPI(ProfilePatientActivity.this, ProfilePatientActivity.this)
                    .getAmbulanceOperatorForPatient(Const.ServiceCode.AMBULANCE_OPERATOR);

            edt_fullname.setText(mPatient.getmFullname());
            Glide.with(this).load(mPatient.getmPicture())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.defult_user).error(R.drawable.defult_user).centerCrop())
                    .into(img_profile_image);

            new AQuery(this).id(R.id.img_profile_image).image(mPatient.getmPicture(), true, true,
                    200, 0, new BitmapAjaxCallback() {

                        @Override
                        public void callback(String url, ImageView iv, Bitmap bm,
                                             AjaxStatus status) {

                            if (url != null && !url.equals("")) {

                                /*mFilePath = aQuery.getCachedFile(url).getPath();

                                EbizworldUtils.appLogDebug("HaoLS", "AQuery file path: " + mFilePath);*/

                            }

                        }

                    });

            edt_family_member_name.setText(mPatient.getmFamilyMemberName());
            edt_patient_mobile.setText(mPatient.getmMobile());
            edt_email.setText(mPatient.getmEmail());
            edt_home_number.setText(mPatient.getmHomeNumber());
            edt_block_number.setText(mPatient.getmBlockNumber());
            edt_unit_number.setText(mPatient.getmUnitNumber());
            edt_postal.setText(mPatient.getmPostal());
            edt_street_name.setText(mPatient.getmStreetName());

            edt_weight.setText(String.valueOf(mPatient.getmWeight()));

            if (mPatient.getmLiftLanding() == 1){

                cb_lift_landing.setChecked(true);

            }

            if (mPatient.getmStairs() == 1){

                cb_stairs.setChecked(true);

            }

            if (mPatient.getmNoStairs() == 1){

                cb_no_stairs.setChecked(true);

            }

            if (mPatient.getmLowStairs() == 1){

                cb_low_stairs.setChecked(true);

            }


//            Set up Patient spinner condition
            EbizworldUtils.appLogDebug("HaoLS", "Setting up Patient spinner condition");
            mPatientConditions = new ArrayList<>();
            mPatientConditions.add("Good");
            mPatientConditions.add("Critical");
            SimpleSpinnerAdapter patientConditionsAdapter = new SimpleSpinnerAdapter(this, mPatientConditions);
            spn_patient_condition.setAdapter(patientConditionsAdapter);
            if (mPatient.getmPatientCondition().equals("Critical")){
                spn_patient_condition.setSelection(1);
            }else {
                spn_patient_condition.setSelection(0);
            }
            spn_patient_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mPatientCondition = mPatientConditions.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

//            Set up Payment mode
            EbizworldUtils.appLogDebug("HaoLS", "Setting up Payment mode");
            mPaymentModes = new ArrayList<>();
            mPaymentModes.add("Cash");
            mPaymentModes.add("Monthly Bill");
            mPaymentModes.add("Digital Wallet");
            mPaymentModes.add("Credit Cards");
            SimpleSpinnerAdapter paymentModesAdapter = new SimpleSpinnerAdapter(this, mPaymentModes);
            spn_payment_mode.setAdapter(paymentModesAdapter);
            spn_payment_mode.setSelection(0);
            for (int i = 0; i < mPaymentModes.size(); i++){

                if(mPatient.getmPaymentMode().equals(mPaymentModes.get(i))){

                    spn_payment_mode.setSelection(i);
                    break;
                }
            }
            spn_payment_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mPaymentMode = mPaymentModes.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (mPatient.getmStretcher() == 1){

                cb_stretcher.setChecked(true);

            }

            if (mPatient.getmWheelChair() == 1){

                cb_wheel_chair.setChecked(true);

            }

            if (mPatient.getmOxygen() == 1){

                cb_oxygen.setChecked(true);

            }

            if (mPatient.getmEscorts() == 1){

                cb_escorts.setChecked(true);

            }

            edt_additional_pation_information.setText(mPatient.getmAddInformation());
            edt_preferred_username.setText(mPatient.getmPreferredUsername());

        }
    }

    private PatientProfile getPatientProfile() {

        PatientProfile patientProfile = new PatientProfile();

        patientProfile.setPicture(mFilePath);
        patientProfile.setFullname(edt_fullname.getText().toString());
        patientProfile.setFamilyMember(edt_family_member_name.getText().toString());
        patientProfile.setPhoneNumeber(edt_patient_mobile.getText().toString());
        patientProfile.setEmail(edt_email.getText().toString());
        patientProfile.setHomeNumber(edt_home_number.getText().toString());
        patientProfile.setBlockNumber(edt_block_number.getText().toString());
        patientProfile.setUnitNumber(edt_unit_number.getText().toString());
        patientProfile.setPostal(edt_postal.getText().toString());
        patientProfile.setStreetName(edt_street_name.getText().toString());
        patientProfile.setPreferredUsername(edt_preferred_username.getText().toString());
        patientProfile.setWeight(edt_weight.getText().toString());

        if (cb_lift_landing.isChecked()){

            patientProfile.setLiftLanding(1);

        }else {

            patientProfile.setLiftLanding(0);
        }

        if (cb_stairs.isChecked()){

            patientProfile.setStairs(1);

        }else {

            patientProfile.setStairs(0);
        }

        if (cb_no_stairs.isChecked()){

            patientProfile.setNoStairs(1);

        }else {

            patientProfile.setNoStairs(0);
        }

        if (cb_low_stairs.isChecked()){

            patientProfile.setLowStairs(1);

        }else {

            patientProfile.setLowStairs(0);
        }

        if (mAmbulanceOperator != null){

            patientProfile.setOperatorID(Integer.parseInt(mAmbulanceOperator.getId()));

        }else {

            patientProfile.setOperatorID(0);

        }

        if(mPatientCondition != null){

            patientProfile.setPatientCondition(mPatientCondition.toLowerCase());

        }else {

            patientProfile.setPatientCondition(spn_patient_condition.getItemAtPosition(0).toString()); //Need change
        }

        if (cb_stretcher.isChecked()){

            patientProfile.setStretcher(1);

        }else {

            patientProfile.setStretcher(0);
        }

        if (cb_wheel_chair.isChecked()){

            patientProfile.setWheelchair(1);

        }else {

            patientProfile.setWheelchair(0);
        }

        if (cb_oxygen.isChecked()){

            patientProfile.setOxygen(1);

        }else {

            patientProfile.setOxygen(0);
        }

        if (cb_escorts.isChecked()){

            patientProfile.setEscort(1);

        }else {

            patientProfile.setEscort(0);
        }

        patientProfile.setAdditionInformation(edt_additional_pation_information.getText().toString());

        if(mPaymentMode != null){

            patientProfile.setPaymentMode(mPaymentMode/*.toLowerCase()*/);

        }else {

            patientProfile.setPaymentMode(spn_payment_mode.getItemAtPosition(0).toString());
        }

        return patientProfile;

    }
}
