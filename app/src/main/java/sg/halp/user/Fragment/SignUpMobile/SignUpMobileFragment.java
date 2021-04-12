package sg.halp.user.Fragment.SignUpMobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Fragment.VerifyOTP.OTPFragment;
import sg.halp.user.Presenter.SignUpMobile.SignUpMobilePresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.WelcomeActivity;

/**
 * Created by Mahesh on 7/5/2017.
 */

public class SignUpMobileFragment extends Fragment implements ISignUpMobileView, View.OnClickListener{

    @BindView(R.id.user_mobile_nuber)
    EditText user_mobile_number;

    @BindView(R.id.close_sign)
    ImageView close_sign;

    @BindView(R.id.ccp)
    CountryCodePicker ccp;

    @BindView(R.id.btn_confirm_phone)
    TextView btn_confirm_phone;

    @BindView(R.id.input_layout_phone)
    TextInputLayout input_layout_phone;

    private Activity activity;
    private SignUpMobilePresenter mSignUpMobilePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup_mobile, container, false);

        ButterKnife.bind(this, view);

        mSignUpMobilePresenter = new SignUpMobilePresenter(this, activity);

        user_mobile_number.requestFocus();
        close_sign.setOnClickListener(this);
        btn_confirm_phone.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_sign:{

                onClose();

            }
            break;

            case R.id.btn_confirm_phone:{

                mSignUpMobilePresenter.getOTP(user_mobile_number.getText().toString().trim(), ccp.getSelectedCountryCodeWithPlus());

            }
            break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {

        super.onDestroy();


    }

    @Override
    public void onClose() {

        EbizworldUtils.makeActivityAnimation(activity, WelcomeActivity.class);

    }

    @Override
    public void onMobileError() {

        user_mobile_number.setError(getResources().getString(R.string.txt_phone_error));
        user_mobile_number.requestFocus();
        user_mobile_number.setText("");

    }

    @Override
    public void onDisconnectNetwork() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);

    }

    @Override
    public void showProgressDialog() {

        Commonutils.progressDialog_show(activity, "Processing");

    }

    @Override
    public void hideProgressDialog() {

        Commonutils.progressDialog_hide();

    }

    @Override
    public void addFragment(Fragment fragment, boolean isAddToBackStack, String tag, boolean isAnimate) {

        FragmentManager manager = ((AppCompatActivity)activity).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();

        if (isAnimate) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_left, R.anim.slide_in_left,
                    R.anim.slide_out_right);

        }

        if (isAddToBackStack) {

            fragmentTransaction.addToBackStack(tag);

        }

        fragmentTransaction.replace(R.id.frame_login, fragment, tag);
        fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
        );
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    public void onGetOTPSuccess(String code) {

        Bundle bundle = new Bundle();

        OTPFragment otpFragment = new OTPFragment();
        bundle.putString(Const.Params.COUNTRY_CODE, ccp.getSelectedCountryNameCode());
        bundle.putString(Const.Params.MOBILE, user_mobile_number.getText().toString());
        bundle.putString("code", code);
        otpFragment.setArguments(bundle);

        addFragment(otpFragment, false, "", true);

    }

    @Override
    public void onGetOTPFail(String error) {

        EbizworldUtils.showShortToast(error, activity);

    }

}
