package sg.halp.user.Activity.ProfileHospital;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Adapter.AmbulanceOperatorSpinnerAdapter;
import sg.halp.user.Dialog.ShowPictureDialog;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.Hospital;
import sg.halp.user.Models.HospitalProfile;
import sg.halp.user.Presenter.ProfileHospital.ProfileHospitalPresenter;
import sg.halp.user.R;
import sg.halp.user.RealmController.RealmController;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;

public class ProfileHospitalActivity extends AppCompatActivity implements IProfileHospitalView, View.OnClickListener {

    @BindView(R.id.toolbar_profile)
    Toolbar toolbar_profile;

    @BindView(R.id.profile_back)
    ImageButton profile_back;

    @BindView(R.id.tv_edit_profile)
    TextView tv_edit_profile;

    @BindView(R.id.img_profile_image)
    ImageView img_profile_image;

    @BindView(R.id.edt_fullname)
    EditText et_fullname;

    @BindView(R.id.edt_profile_email)
    EditText et_profile_email;

    @BindView(R.id.edt_profile_mobile)
    EditText et_profile_mobile;

    @BindView(R.id.edt_contact_name)
    EditText edt_contact_name;

    @BindView(R.id.edt_contact_number)
    EditText edt_contact_number;

    @BindView(R.id.edt_preferred_username)
    EditText edt_preferred_username;

    @BindView(R.id.tv_floor_number_value)
    EditText tv_floor_number_value;

    @BindView(R.id.tv_ward_value)
    EditText tv_ward_value;

    @BindView(R.id.edt_postal)
    EditText edt_postal;

    @BindView(R.id.edt_address)
    EditText edt_address;

    @BindView(R.id.spn_ambulance_operator)
    Spinner spn_ambulance_operator;

    private ProfileHospitalPresenter mProfileHospitalPresenter;
    private String mFilePath = "";
    private AmbulanceOperator mAmbulanceOperator;
    private Hospital mHospital;
    private AQuery aQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        setContentView(R.layout.activity_hospital_profile);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar_profile);
        getSupportActionBar().setTitle(null);

        mProfileHospitalPresenter = new ProfileHospitalPresenter(this, this);

        aQuery = new AQuery(this);
        RealmController.with(this).refresh();

        disableHospitalViews();
        setHospitalValues();

        profile_back.setOnClickListener(this);
        tv_edit_profile.setOnClickListener(this);
        img_profile_image.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.profile_back:{

                onBackPressed();

            }
            break;

            case R.id.img_profile_image:{

                showCropPictureDialog();

            }
            break;

            case R.id.tv_edit_profile:{

                if (tv_edit_profile.getText().toString().equals(getString(R.string.btn_edit))) {

                    enableHospitalViews();

                    tv_edit_profile.setText(getString(R.string.btn_save));

                } else {

                    HospitalProfile hospitalProfile = getHospitalProfile();

                    if (hospitalProfile != null){

                        mProfileHospitalPresenter.updateProfile(hospitalProfile);

                    }

                }

            }
            break;
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void disableHospitalViews() {
        img_profile_image.setEnabled(false);
        et_fullname.setEnabled(false);
        et_profile_email.setEnabled(false);
        et_profile_mobile.setEnabled(false);
        edt_contact_name.setEnabled(false);
        edt_contact_number.setEnabled(false);
        edt_preferred_username.setEnabled(false);
        edt_postal.setEnabled(false);
        tv_floor_number_value.setEnabled(false);
        tv_ward_value.setEnabled(false);
        edt_address.setEnabled(false);
        spn_ambulance_operator.setEnabled(false);
    }

    @Override
    public void enableHospitalViews() {

        img_profile_image.setEnabled(true);
        et_fullname.setEnabled(true);
        et_profile_email.setEnabled(true);
        et_profile_mobile.setEnabled(true);
        edt_contact_name.setEnabled(true);
        edt_contact_number.setEnabled(true);
        edt_preferred_username.setEnabled(true);
        edt_postal.setEnabled(true);
        tv_floor_number_value.setEnabled(true);
        tv_ward_value.setEnabled(true);
        edt_address.setEnabled(true);
        spn_ambulance_operator.setEnabled(true);

    }

    @Override
    public HospitalProfile getHospitalProfile(){

        HospitalProfile hospitalProfile = new HospitalProfile();
        hospitalProfile.setFullname(et_fullname.getText().toString());
        hospitalProfile.setEmail(et_profile_email.getText().toString());
        hospitalProfile.setContactName(edt_contact_name.getText().toString());
        hospitalProfile.setContacNumber(edt_contact_number.getText().toString());
        hospitalProfile.setPreferredUsername(edt_preferred_username.getText().toString());
        hospitalProfile.setPostal(edt_postal.getText().toString());
        hospitalProfile.setFloorNumber(tv_floor_number_value.getText().toString());
        hospitalProfile.setWard(tv_ward_value.getText().toString());
        hospitalProfile.setPicture(mFilePath);
        hospitalProfile.setPhoneNumber(et_profile_mobile.getText().toString());
        hospitalProfile.setAddress(edt_address.getText().toString());

        if (mAmbulanceOperator != null){

            hospitalProfile.setOperatorID(Integer.parseInt(mAmbulanceOperator.getId()));

        }else {

            hospitalProfile.setOperatorID(0);

        }

        return hospitalProfile;

    }

    @Override
    public void setHospitalValues(){

        mHospital = RealmController.with(this).getHospital(Integer.valueOf(new PreferenceHelper(this).getUserId()));

        if (mHospital != null) {

            //Getting Ambulance Operator list for set up to Ambulance Operator Spinner
            mProfileHospitalPresenter.getAmbulanceOperatorList();

            Glide.with(this).load(mHospital.getmCompanyPicture())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.defult_user).error(R.drawable.defult_user).centerCrop())
                    .into(img_profile_image);

            aQuery.id(R.id.img_profile_image).image(mHospital.getmCompanyPicture(), true, true,
                    200, 0, new BitmapAjaxCallback() {

                        @Override
                        public void callback(String url, ImageView iv, Bitmap bm,
                                             AjaxStatus status) {

                            EbizworldUtils.appLogDebug("HaoLS", url);
                            if (!url.isEmpty()) {

                                EbizworldUtils.appLogDebug("HaoLS", url);

                                //    mFilePath = aQuery.getCachedFile(url).getPath();
                            }

                        }

                    });

            et_fullname.setText(mHospital.getmHospitalName());
            et_profile_email.setText(mHospital.getmEmailAddress());
            et_profile_mobile.setText(mHospital.getmMobileNumber());
            edt_contact_name.setText(mHospital.getmContactName());
            edt_contact_number.setText(mHospital.getmContactNumber());
            edt_preferred_username.setText(mHospital.getmPreferredUsername());
            edt_postal.setText(mHospital.getmPostal());
            tv_floor_number_value.setText(String.valueOf(mHospital.getmFloorNumber()));
            tv_ward_value.setText(mHospital.getmWard());
            edt_address.setText(mHospital.getmMainAddress());

        }
    }

    @Override
    public void showCropPictureDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        final ShowPictureDialog showPictureDialog = new ShowPictureDialog();
        showPictureDialog.setCancelable(true);
        showPictureDialog.show(fragmentManager, Const.SHOW_PICTURE_DIALOGFRAGMENT);
        showPictureDialog.setCropPictureListener(new DialogFragmentCallback.CropPictureListener() {
            @Override
            public void onHandleCrop(String filePath) {

                if (!filePath.equals("")){

                    mFilePath = filePath;

                    Glide.with(ProfileHospitalActivity.this).load(mFilePath)
                            .transition(new DrawableTransitionOptions().crossFade())
                            .apply(new RequestOptions().error(R.drawable.defult_user).placeholder(R.drawable.defult_user).centerCrop())
                            .into(img_profile_image);

                }
            }
        });

    }

    @Override
    public void showProgressDialog() {

        Commonutils.progressDialog_show(this, getResources().getString(R.string.txt_load));
    }

    @Override
    public void hideProgressDialog() {

        Commonutils.progressDialog_hide();

    }

    @Override
    public void onDisconnectNetwork() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), this);

    }

    @Override
    public void onErrorMessage(String error) {

        EbizworldUtils.showShortToast(error, this);
    }

    @Override
    public void setupSpinnerAmbulanceOperator(final List<AmbulanceOperator> ambulanceOperators) {

        if (ambulanceOperators.size() > 0){

            EbizworldUtils.appLogDebug("HaoLS", "Setting up Spinner for Ambulance Operator");

            AmbulanceOperatorSpinnerAdapter ambulanceOperatorSpinnerAdapter = new AmbulanceOperatorSpinnerAdapter(this, ambulanceOperators);
            spn_ambulance_operator.setAdapter(ambulanceOperatorSpinnerAdapter);

            if (mHospital != null){

                EbizworldUtils.appLogDebug("HaoLS", "mHospital != null");
                EbizworldUtils.appLogDebug("HaoLS", "Operator id of Hopital " + String.valueOf(mHospital.getmAmbulanceOperator()));

                for (int i = 0; i < ambulanceOperators.size(); i++){

                    EbizworldUtils.appLogDebug("HaoLS", "Start for loop " + i );
                    EbizworldUtils.appLogDebug("HaoLS", "Ambulance Operator id " + ambulanceOperators.get(i).getId() );

                    if (mHospital.getmAmbulanceOperator() == Integer.parseInt(ambulanceOperators.get(i).getId())){

                        spn_ambulance_operator.setSelection(i);

                        EbizworldUtils.appLogDebug("HaoLS", "Spinner set default text with Operator ID " + String.valueOf(mHospital.getmAmbulanceOperator()));
                        break;
                    }
                }

            }

            spn_ambulance_operator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    mAmbulanceOperator = ambulanceOperators.get(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    @Override
    public void onUpdateProfileSuccess() {

        EbizworldUtils.showShortToast(getString(R.string.update_success_text), this);
        tv_edit_profile.setText(getString(R.string.btn_edit));
        disableHospitalViews();

    }

    @Override
    public void onFullnameError() {

        et_fullname.setError(getResources().getString(R.string.edittext_empty_notice));
    }

    @Override
    public void onMobileError() {

        et_profile_mobile.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onContactNameError() {

        edt_contact_name.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onContactNumberError() {

        edt_contact_number.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onPreferredUsernameError() {

        edt_preferred_username.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onPostalError() {

        edt_postal.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onFloorNumberError() {

        tv_floor_number_value.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onWardError() {

        tv_ward_value.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onAddressError() {

        edt_address.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onCleanFileCache() {

        try {

            if (!mFilePath.equals("")) {

                File file = new File(mFilePath);
                file.getAbsoluteFile().delete();
                mFilePath = "";

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}