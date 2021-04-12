package sg.halp.user.Activity.ProfileNursingHome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import sg.halp.user.Adapter.AmbulanceOperatorSpinnerAdapter;
import sg.halp.user.Dialog.ShowPictureDialog;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.NursingHome;
import sg.halp.user.Models.NursingHomeProfile;
import sg.halp.user.Presenter.ProfileNursingHome.ProfileNursingHomePresenter;
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

public class ProfileNursingHomeActivity extends AppCompatActivity implements IProfileNursingHomeView, View.OnClickListener {

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

    @BindView(R.id.edt_profile_email)
    EditText edt_profile_email;

    @BindView(R.id.edt_profile_mobile)
    EditText edt_profile_mobile;

    @BindView(R.id.edt_contact_name)
    EditText edt_contact_name;

    @BindView(R.id.edt_contact_number)
    EditText edt_contact_number;

    @BindView(R.id.edt_preferred_username)
    EditText edt_preferred_username;

    @BindView(R.id.edt_address)
    EditText edt_address;

    @BindView(R.id.spn_ambulance_operator)
    Spinner spn_ambulance_operator;

    private ProfileNursingHomePresenter mProfileNursingHomePresenter;
    private AmbulanceOperator mAmbulanceOperator;
    private NursingHome mNursingHome;
    private String mFilePath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        setContentView(R.layout.activity_nurse_profile);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar_profile);
        getSupportActionBar().setTitle(null);

        mProfileNursingHomePresenter = new ProfileNursingHomePresenter(this, this);

        RealmController.with(this).refresh();

        img_profile_image.setOnClickListener(this);
        profile_back.setOnClickListener(this);
        tv_edit_profile.setOnClickListener(this);

        disableNursingHomeViews();

        setNurseValues();
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

                showCropPictureDialog();

            }
            break;

            case R.id.profile_back:{

                ProfileNursingHomeActivity.this.onBackPressed();

            }
            break;

            case R.id.tv_edit_profile:{

                if (tv_edit_profile.getText().toString().equals(getString(R.string.btn_edit))) {

                    enableNursingHomeViews();

                    tv_edit_profile.setText(getString(R.string.btn_save));

                } else {

                    NursingHomeProfile nursingHomeProfile = getNurseProfile();

                    mProfileNursingHomePresenter.updateProfile(nursingHomeProfile);

                }

            }
            break;
        }

    }

    @Override
    public void enableNursingHomeViews() {

        img_profile_image.setEnabled(true);
        edt_fullname.setEnabled(true);
        edt_profile_email.setEnabled(true);
        edt_profile_mobile.setEnabled(true);
        edt_contact_name.setEnabled(true);
        edt_contact_number.setEnabled(true);
        edt_preferred_username.setEnabled(true);
        edt_address.setEnabled(true);
        spn_ambulance_operator.setEnabled(true);

    }

    @Override
    public void disableNursingHomeViews() {

        img_profile_image.setEnabled(false);
        edt_fullname.setEnabled(false);
        edt_profile_email.setEnabled(false);
        edt_profile_mobile.setEnabled(false);
        edt_contact_name.setEnabled(false);
        edt_contact_number.setEnabled(false);
        edt_preferred_username.setEnabled(false);
        edt_address.setEnabled(false);
        spn_ambulance_operator.setEnabled(false);

    }

    @Override
    public void setNurseValues(){

        mNursingHome = RealmController.with(this).getNurse(Integer.valueOf(new PreferenceHelper(this).getUserId()));

        if (mNursingHome != null) {

            //Getting Ambulance Operator list for set up to Ambulance Operator Spinner
            mProfileNursingHomePresenter.getAmbulanceOperatorList();

            Glide.with(this).load(mNursingHome.getmPictureUrl())
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(new RequestOptions().placeholder(R.drawable.defult_user).error(R.drawable.defult_user).centerCrop())
                    .into(img_profile_image);

            new AQuery(this).id(R.id.img_profile_image).image(mNursingHome.getmPictureUrl(), true, true,
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

            edt_fullname.setText(mNursingHome.getmFullName());
            edt_profile_email.setText(mNursingHome.getmEmail());
            edt_profile_mobile.setText(mNursingHome.getmMobile());
            edt_contact_name.setText(mNursingHome.getmContact_Name());
            edt_contact_number.setText(mNursingHome.getmContact_Number());
            edt_preferred_username.setText(mNursingHome.getmPreferred_Username());
            edt_address.setText(mNursingHome.getmAddress());

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

                    Glide.with(ProfileNursingHomeActivity.this).load(mFilePath)
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

            AmbulanceOperatorSpinnerAdapter mAmbulanceOperatorSpinnerAdapter = new AmbulanceOperatorSpinnerAdapter(this, ambulanceOperators);
            spn_ambulance_operator.setAdapter(mAmbulanceOperatorSpinnerAdapter);

            if (mNursingHome != null){

                EbizworldUtils.appLogDebug("HaoLS", "mNursingHome != null");
                EbizworldUtils.appLogDebug("HaoLS", "Operator id of NursingHome " + String.valueOf(mNursingHome.getmOperatorID()));

                for (int i = 0; i < ambulanceOperators.size(); i++){

                    EbizworldUtils.appLogDebug("HaoLS", "Start for loop " + i );
                    EbizworldUtils.appLogDebug("HaoLS", "Ambulance Operator id " + ambulanceOperators.get(i).getId() );

                    if (mNursingHome.getmOperatorID() == Integer.parseInt(ambulanceOperators.get(i).getId())){

                        spn_ambulance_operator.setSelection(i);

                        EbizworldUtils.appLogDebug("HaoLS", "Spinner set default text with Operator ID " + String.valueOf(mNursingHome.getmOperatorID()));
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

        tv_edit_profile.setText(getString(R.string.btn_edit));
        EbizworldUtils.showShortToast(getString(R.string.update_success_text), this);
        disableNursingHomeViews();

        ProfileNursingHomeActivity.this.onBackPressed();

    }

    @Override
    public void onFullnameError() {

        edt_fullname.setError(getResources().getString(R.string.edittext_empty_notice));

    }

    @Override
    public void onMobileError() {

        edt_profile_mobile.setError(getResources().getString(R.string.edittext_empty_notice));

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

    @Override
    public NursingHomeProfile getNurseProfile(){

        NursingHomeProfile nursingHomeProfile = new NursingHomeProfile();
        nursingHomeProfile.setFullname(edt_fullname.getText().toString());
        nursingHomeProfile.setEmail(edt_profile_email.getText().toString());
        nursingHomeProfile.setContactName(edt_contact_name.getText().toString());
        nursingHomeProfile.setContactNumber(edt_contact_number.getText().toString());
        nursingHomeProfile.setPreferredUsername(edt_preferred_username.getText().toString());
        nursingHomeProfile.setPicture(mFilePath);
        nursingHomeProfile.setPhoneNumber(edt_profile_mobile.getText().toString());
        nursingHomeProfile.setAddress(edt_address.getText().toString());
        /*map.put(Const.Params.OPERATOR_ID, edt_operator_id.getText().toString());*/

        if (mAmbulanceOperator != null){

            nursingHomeProfile.setOperatorID(Integer.parseInt(mAmbulanceOperator.getId()));

        }else {

            nursingHomeProfile.setOperatorID(0);

        }

        return nursingHomeProfile;
    }

}
