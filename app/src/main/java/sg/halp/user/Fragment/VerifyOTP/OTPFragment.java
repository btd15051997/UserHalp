package sg.halp.user.Fragment.VerifyOTP;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Fragment.BaseRegisterFragment;
import sg.halp.user.Fragment.SignUpNext.SignUpNextFragment;
import sg.halp.user.Presenter.VerifyOTP.VerifyOTPPresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.WelcomeActivity;

/**
 * Created by Mahesh on 7/5/2017.
 */

public class OTPFragment extends Fragment implements IVerifyOTPView, View.OnClickListener{

    @BindView(R.id.et_otp_mobile)
    EditText et_otp_mobile;

    @BindView(R.id.user_otp)
    EditText user_otp;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @BindView(R.id.input_layout_otp)
    TextInputLayout input_layout_otp;

    @BindView(R.id.close_sign)
    ImageView close_sign;

    @BindView(R.id.btn_edit_number)
    ImageView btn_edit_number;

    @BindView(R.id.btn_resend)
    TextView btn_resend;

    @BindView(R.id.btn_confirm_otp)
    TextView btn_confirm_otp;

    private String code = "";
    private Activity activity;
    private VerifyOTPPresenter mVerifyOTPPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otp_verify, container, false);

        ButterKnife.bind(this, view);

        mVerifyOTPPresenter = new VerifyOTPPresenter(activity, this);

        close_sign.setOnClickListener(this);
        btn_edit_number.setOnClickListener(this);
        btn_confirm_otp.setOnClickListener(this);
        btn_resend.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {

            String country_code = bundle.getString(Const.Params.COUNTRY_CODE);
            String mobile = bundle.getString(Const.Params.MOBILE);
            code = bundle.getString("code");
            et_otp_mobile.setText(mobile);

            ccp.setDefaultCountryUsingNameCode(country_code);
            ccp.resetToDefaultCountry();
            user_otp.setText(code);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.close_sign:{

                onClose();

            }
            break;

            case R.id.btn_edit_number:{

                onEditNumber();
            }
            break;

            case R.id.btn_confirm_otp:{

                mVerifyOTPPresenter.checkOTP(code, user_otp.getText().toString().trim());

            }
            break;

            case R.id.btn_resend:{

                mVerifyOTPPresenter.getOTP(et_otp_mobile.getText().toString().trim(), ccp.getSelectedCountryCodeWithPlus());

            }
                break;
        }
    }

    @Override
    public void onClose() {

        EbizworldUtils.makeActivityAnimation(activity, WelcomeActivity.class);
        activity.finish();

    }

    @Override
    public void onEditNumber() {

        ccp.setCcpClickable(true);
        et_otp_mobile.setEnabled(true);
        et_otp_mobile.requestFocus();

    }

    @Override
    public void onOTPError() {
        user_otp.setError(getResources().getString(R.string.txt_otp_error));
        user_otp.requestFocus();
    }

    @Override
    public void onVerifyFail() {
        user_otp.setError(getResources().getString(R.string.txt_otp_wrong));
        user_otp.requestFocus();
    }

    @Override
    public void onVerifySuccess() {

        Bundle bundle = new Bundle();
        SignUpNextFragment signUpNextFragment = new SignUpNextFragment();
        bundle.putString(Const.Params.MOBILE, et_otp_mobile.getText().toString());
        bundle.putString(Const.Params.COUNTRY_CODE, ccp.getSelectedCountryCodeWithPlus());
        signUpNextFragment.setArguments(bundle);

    }

    @Override
    public void onDisconnectNetwork() {
        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
    }

    @Override
    public void showProgressDialog() {

        Commonutils.progressDialog_show(activity, "Loading...");
    }

    @Override
    public void hideProgressDialog() {

        Commonutils.progressDialog_hide();

    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void onDisplayOTPCode(String code) {

        user_otp.setText("");
        user_otp.setText(code);

    }

    @Override
    public void onGetOTPFail(String error) {

        EbizworldUtils.showShortToast(error, activity);

    }

    @Override
    public void onMobileError() {
        et_otp_mobile.setError(getResources().getString(R.string.txt_phone_error));
        et_otp_mobile.requestFocus();
        et_otp_mobile.setText("");
    }
}
