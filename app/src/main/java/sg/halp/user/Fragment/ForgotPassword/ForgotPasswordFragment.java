package sg.halp.user.Fragment.ForgotPassword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Activity.SignInPatient.SignInPatientActivity;
import sg.halp.user.Fragment.BaseRegisterFragment;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Presenter.ForgetPassword.ForgetPasswordPresenter;
import sg.halp.user.R;
import sg.halp.user.SignInActivity;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 1/5/2017.
 */

public class ForgotPasswordFragment extends Fragment implements IForgotPasswordView, View.OnClickListener{

    @BindView(R.id.btn_forgot_cancel)
    ImageButton btn_forgot_cancel;

    @BindView(R.id.et_email_forgot)
    EditText et_email_forgot;

    @BindView(R.id.forgot_pass_btn)
    TextView forgot_pass_btn;

    @BindView(R.id.input_layout_email_forgot)
    TextInputLayout input_layout_email_forgot;

    private Activity activity;
    private ForgetPasswordPresenter mForgetPasswordPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        ButterKnife.bind(this, view);

        mForgetPasswordPresenter = new ForgetPasswordPresenter(activity, ForgotPasswordFragment.this);

        btn_forgot_cancel.setOnClickListener(this);
        forgot_pass_btn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forgot_cancel:{

                onCancel();

            }
            break;

            case R.id.forgot_pass_btn:{

                mForgetPasswordPresenter.requestPassword(et_email_forgot.getText().toString().trim());

            }
            break;


        }
    }

    @Override
    public void onDestroyView() {

        Fragment fragment = (getFragmentManager()
                .findFragmentById(R.id.frame_login));
        if (fragment.isResumed()) {
            getFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        super.onDestroyView();
    }

    @Override
    public void onCancel() {

        EbizworldUtils.makeActivityAnimation(activity, SignInPatientActivity.class);

    }

    @Override
    public void onUsernameError() {

        et_email_forgot.setText("");
        et_email_forgot.requestFocus();
        et_email_forgot.setError(activity.getString(R.string.txt_email_error));

    }

    @Override
    public void onDisconnectNetwork() {

        EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);

    }

    @Override
    public void showProgressDialog() {

        Commonutils.progressDialog_show(activity, getResources().getString(R.string.req_pass_load));

    }

    @Override
    public void hideProgressDialog() {

        Commonutils.progressDialog_hide();

    }

    @Override
    public void onRequestSuccess() {

        EbizworldUtils.showShortToast(activity.getResources().getString(R.string.txt_success_forgot_password), activity);
        EbizworldUtils.makeActivityAnimation(activity, SignInPatientActivity.class);

    }

    @Override
    public void onRequestFail(String error) {

        EbizworldUtils.showShortToast(error, activity);

    }
}
