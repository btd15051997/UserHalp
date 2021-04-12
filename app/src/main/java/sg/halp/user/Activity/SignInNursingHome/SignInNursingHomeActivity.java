package sg.halp.user.Activity.SignInNursingHome;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Activity.SignInPatient.SignInPatientActivity;
import sg.halp.user.MainActivity;
import sg.halp.user.Presenter.SignInNursingHome.SignInNursingHomePresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.WelcomeActivity;

public class SignInNursingHomeActivity extends AppCompatActivity implements ISignInNursingHomeView, View.OnClickListener {

    @BindView(R.id.btn_cancel)
    ImageButton btn_cancel;

    @BindView(R.id.edt_login_userid)
    EditText et_login_userid;

    @BindView(R.id.edt_login_password)
    EditText et_login_password;

    @BindView(R.id.btn_login)
    TextView login_btn;

    @BindView(R.id.btn_register_social)
    TextView btn_register_social;

    @BindView(R.id.btn_new_user)
    TextView btn_new_user;

    @BindView(R.id.btn_forgot_pass)
    TextView btn_forgot_pass;

    private SignInNursingHomePresenter mSignInNursingHomePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mSignInNursingHomePresenter = new SignInNursingHomePresenter(SignInNursingHomeActivity.this, SignInNursingHomeActivity.this);

        btn_new_user.setVisibility(View.GONE);
        btn_forgot_pass.setVisibility(View.GONE);
        btn_register_social.setVisibility(View.GONE);

        btn_cancel.setOnClickListener(this);
        login_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_cancel:{

                onCancel();

            }
            break;

            case R.id.btn_login:{

                if (mSignInNursingHomePresenter != null){

                    mSignInNursingHomePresenter.loginManual(
                            et_login_userid.getText().toString().trim(),
                            et_login_password.getText().toString().trim(),
                            Const.MANUAL
                    );
                }

            }
            break;

        }

    }

    @Override
    public void onBackPressed() {
        EbizworldUtils.makeActivityAnimation(SignInNursingHomeActivity.this, WelcomeActivity.class);
        finish();
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

        EbizworldUtils.makeActivityAnimation(SignInNursingHomeActivity.this, WelcomeActivity.class);

        new PreferenceHelper(this).putLoginType(null);

    }

    @Override
    public void onLoginSuccess() {

        EbizworldUtils.makeActivityAnimation(SignInNursingHomeActivity.this, MainActivity.class);

    }

    @Override
    public void onLoginFail(String error) {

        EbizworldUtils.showShortToast(error, SignInNursingHomeActivity.this);

    }

    @Override
    public void onNetworkDisconnect() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), SignInNursingHomeActivity.this);

    }

    @Override
    public void onShowProgressDialog() {

        Commonutils.progressDialog_show(this, "Processing");

    }

    @Override
    public void onHideProgressDialog() {

        Commonutils.progressDialog_hide();

    }
}
