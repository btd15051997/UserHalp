package sg.halp.user.Fragment.SignUpNext;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Fragment.BaseRegisterFragment;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.RegisterManual;
import sg.halp.user.Presenter.SignUpNext.SignUpNextPresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.WelcomeActivity;
import sg.halp.user.restAPI.RegisterManualAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by Mahesh on 7/5/2017.
 */

public class SignUpNextFragment extends Fragment implements ISignUpNextView, View.OnClickListener {

    @BindView(R.id.user_fullname)
    EditText user_fullname;/*user_lname,*/

    @BindView(R.id.user_email)
    EditText user_email;

    @BindView(R.id.user_password)
    EditText user_password;

    @BindView(R.id.user_referral_code)
    EditText user_referral_code;

    @BindView(R.id.close_sign)
    ImageView close_sign;

    @BindView(R.id.btn_next)
    TextView btn_next;

    @BindView(R.id.applyRefCode)
    TextView applyRefCode;

    @BindView(R.id.cb_policy_terms)
    CheckBox cb_policy_terms;

    @BindView(R.id.privacy_policy)
    TextView privacy_policy;

    @BindView(R.id.terms_conditions)
    TextView terms_conditions;

    private Activity activity;
    private String mobile = "", country_code = "";
    private SignUpNextPresenter mSignUpNextPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_next, container, false);

        ButterKnife.bind(this, view);

        mSignUpNextPresenter = new SignUpNextPresenter(activity, this);

        close_sign.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        applyRefCode.setOnClickListener(this);
        privacy_policy.setOnClickListener(this);
        terms_conditions.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {

            mobile = bundle.getString(Const.Params.MOBILE);
            country_code = bundle.getString(Const.Params.COUNTRY_CODE);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.close_sign:{

                onClose();

            }
            break;

            case R.id.privacy_policy:{

                onPrivacyPolicy();

            }
            break;

            case R.id.terms_conditions:{

                onTermConditions();

            }
            break;

            case R.id.btn_next:{

                mSignUpNextPresenter.onRegister(registerManual(), cb_policy_terms);

            }
            break;

            case R.id.applyRefCode:{

                if(TextUtils.isEmpty(user_referral_code.getText().toString())){

                    mSignUpNextPresenter.onApplyReferralCode(user_referral_code.getText().toString().trim());

                }

            }
            break;
        }
    }

    @Override
    public RegisterManual registerManual() {

        return new RegisterManual(
                user_fullname.getText().toString(),
                user_email.getText().toString(),
                user_password.getText().toString(),
                mobile,
                country_code,
                "1",
                new PreferenceHelper(activity).getDeviceToken(),
                Const.DEVICE_TYPE_ANDROID,
                Const.MANUAL,
                TimeZone.getDefault().getID(),
                user_referral_code.getText().toString()
        );

    }

    @Override
    public void onDisconnectNetwork() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);

    }

    @Override
    public void showProgressDialog() {

        Commonutils.progressDialog_show(activity, getResources().getString(R.string.reg_load));

    }

    @Override
    public void hideProgressDialog() {

        Commonutils.progressDialog_hide();

    }

    @Override
    public void onRegisterSuccess() {

        EbizworldUtils.makeActivityAnimation(activity, MainActivity.class);
        activity.finish();

    }

    @Override
    public void onRegisterFail(String error) {

        EbizworldUtils.showShortToast(error, activity);

    }

    @Override
    public void onApplyReferralCodeResponse(String message) {

        EbizworldUtils.showShortToast(message, activity);

    }

    @Override
    public void onClose() {

        EbizworldUtils.makeActivityAnimation(activity, WelcomeActivity.class);

    }

    @Override
    public void onPrivacyPolicy() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Const.ServiceType.PRIVACY_POLICY_URL));
        startActivity(intent);

    }

    @Override
    public void onTermConditions() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndNormalize(Uri.parse(Const.ServiceType.TERMS_CONDITIONS_URL));
        startActivity(intent);

    }

    @Override
    public void onFullNameError() {
        user_fullname.setError(getResources().getString(R.string.txt_fullname_error));
        user_fullname.requestFocus();
    }

    @Override
    public void onEmailError() {
        user_email.setError(getResources().getString(R.string.txt_email_error));
        user_email.requestFocus();
    }

    @Override
    public void onEmailIncorrect() {
        user_email.setError(getResources().getString(R.string.txt_incorrect_error));
        user_email.requestFocus();
    }

    @Override
    public void onPasswordError() {
        user_password.setError(getResources().getString(R.string.txt_pass_error));
        user_password.requestFocus();
    }

    @Override
    public void onPolicyTermsUncheck() {

        EbizworldUtils.showLongToast(getResources().getString(R.string.privacy_terms_warning), activity);

    }
}
