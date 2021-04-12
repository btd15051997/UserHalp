package sg.halp.user.Activity.SignInPatient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Dialog.SocialNetwork.SocialNetworkDialog;
import sg.halp.user.Fragment.ForgotPassword.ForgotPasswordFragment;
import sg.halp.user.Fragment.SignUpMobile.SignUpMobileFragment;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.SocialMediaProfile;
import sg.halp.user.Presenter.SignInPatient.SignInPatientPresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.WelcomeActivity;

public class SignInPatientActivity extends AppCompatActivity implements ISignInPatientView, View.OnClickListener {

    @BindView(R.id.login_layout)
    RelativeLayout login_layout;

    @BindView(R.id.btn_cancel)
    ImageButton btn_cancel;

    @BindView(R.id.edt_login_userid)
    EditText et_login_userid;

    @BindView(R.id.edt_login_password)
    EditText et_login_password;

    @BindView(R.id.btn_login)
    TextView btn_login;

    @BindView(R.id.btn_register_social)
    TextView btn_register_social;

    @BindView(R.id.btn_new_user)
    TextView btn_new_user;

    @BindView(R.id.btn_forgot_pass)
    TextView btn_forgot_pass;

    private SignInPatientPresenter mSignInPatientPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mSignInPatientPresenter = new SignInPatientPresenter(SignInPatientActivity.this, SignInPatientActivity.this);

        btn_register_social.setVisibility(View.GONE);
        btn_register_social.setOnClickListener(this);
        btn_new_user.setOnClickListener(this);
        btn_forgot_pass.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_register_social:{

                showSocialNetworkDialog();

            }
            break;

            case R.id.btn_new_user:{

                onNewUser();

            }
            break;

            case R.id.btn_forgot_pass:{

                onForgetPassword();

            }
            break;

            case R.id.btn_cancel:{

                onCancel();

            }
            break;

            case R.id.btn_login:{

/*                mSignInPatientPresenter.loginManual(
                        et_login_userid.getText().toString().trim(),
                        et_login_password.getText().toString().trim(),
                        Const.MANUAL
                );*/

                onLoginSuccess();

            }
            break;

        }

    }

    @Override
    public void onBackPressed() {
        if (login_layout.getVisibility() == View.GONE) {

            EbizworldUtils.makeActivityAnimation(SignInPatientActivity.this, SignInPatientActivity.class);

        } else {
            EbizworldUtils.makeActivityAnimation(SignInPatientActivity.this, WelcomeActivity.class);
            finish();
        }
    }

    @Override
    public void onUsernameError() {

        et_login_userid.setError(getResources().getString(R.string.txt_email_error));
        et_login_userid.requestFocus();
        et_login_userid.setText("");

    }

    @Override
    public void onPasswordError() {

        et_login_password.setError(getResources().getString(R.string.txt_pass_error));
        et_login_password.requestFocus();
        et_login_password.setText("");

    }

    @Override
    public void onCancel() {

        EbizworldUtils.makeActivityAnimation(SignInPatientActivity.this, WelcomeActivity.class);

        new PreferenceHelper(this).putLoginType(null);

    }

    @Override
    public void onLoginSuccess() {

        EbizworldUtils.makeActivityAnimation(SignInPatientActivity.this, MainActivity.class);

    }

    @Override
    public void onLoginFail(String error) {

        EbizworldUtils.showShortToast(error, SignInPatientActivity.this);

    }

    @Override
    public void onNetworkDisconnect() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), SignInPatientActivity.this);

    }

    @Override
    public void onShowProgressDialog() {

        Commonutils.progressDialog_show(this, "Processing");

    }

    @Override
    public void onHideProgressDialog() {

        Commonutils.progressDialog_hide();

    }

    @Override
    public void showSocialNetworkDialog() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        SocialNetworkDialog socialNetworkDialog = new SocialNetworkDialog();
        socialNetworkDialog.setCancelable(false);
        socialNetworkDialog.show(fragmentManager, Const.CANCELLATION_FEE_BILLING_INFO_DIALOGFRAGMENT);
        socialNetworkDialog.setSocialNetworkListener(new DialogFragmentCallback.SocialNetworkListener() {
            @Override
            public void onSuccess(SocialMediaProfile socialMediaProfile) {

                if (socialMediaProfile != null){

                    mSignInPatientPresenter.loginSocialNetwork(socialMediaProfile, socialMediaProfile.getLoginType());

                }

            }

            @Override
            public void onFail(String error) {

            }
        });

    }

    @Override
    public void addFragment(Fragment fragment, boolean isAddToBackStack, String tag, boolean isAnimate) {

        FragmentManager manager = getSupportFragmentManager();
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
    public void onNewUser() {

        addFragment(new SignUpMobileFragment(), false, Const.REGISTER_FRAGMENT, true);
        login_layout.setVisibility(View.GONE);

    }

    @Override
    public void onForgetPassword() {

        addFragment(new ForgotPasswordFragment(), false, Const.FORGOT_PASSWORD_FRAGMENT, true);
        login_layout.setVisibility(View.GONE);

    }
}
