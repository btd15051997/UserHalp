package sg.halp.user.Fragment.BillingInfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.DatabaseHandler;
import sg.halp.user.Dialog.RequestLoaderDialog;
import sg.halp.user.Fragment.BaseFragment;
import sg.halp.user.Fragment.HomeMapFragment;
import sg.halp.user.Fragment.RatingFragment;
import sg.halp.user.Fragment.TravelMapFragment;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.RequestDetail;
import sg.halp.user.Models.RequestOptional;
import sg.halp.user.Presenter.BillingInfo.BillingInfoPresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.BillingInfoAPI;
import sg.halp.user.restAPI.CheckRequestStatusAPI;
import sg.halp.user.restAPI.PaymentAPI;

public class BillingInfoFragment extends Fragment implements IBillingInfoView, View.OnClickListener {

    @BindView(R.id.tv_billing_info_notice)
    TextView mTv_billing_info_notice;

    @BindView(R.id.tv_billing_info_total_price)
    TextView mTv_billing_info_total_price;

    @BindView(R.id.btn_pay_bycash)
    Button mBtn_pay_bycash;

    @BindView(R.id.btn_pay_bypaypal)
    Button mBtn_pay_bypaypal;

    @BindView(R.id.tv_billing_info_a_and_e_value)
    TextView mTv_billing_info_a_and_e_value;

    @BindView(R.id.tv_billing_info_imh_value)
    TextView mTv_billing_info_imh_value;

    @BindView(R.id.tv_billing_info_ferry_terminals_value)
    TextView mTv_billing_info_ferry_terminals_value;

    @BindView(R.id.tv_billing_info_staircase_value)
    TextView mTv_billing_info_staircase_value;

    @BindView(R.id.tv_billing_info_tarmac_value)
    TextView mTv_billing_info_tarmac_value;

    @BindView(R.id.tv_billing_info_weight_value)
    TextView mTv_billing_info_weight_value;

    @BindView(R.id.tv_billing_info_oxygen_tank_value)
    TextView mTv_billing_info_oxygen_tank_value;

    @BindView(R.id.tv_billing_info_pickup_type_value)
    TextView mTv_billing_info_pickup_type_value;

    @BindView(R.id.billing_info_payment_group)
    LinearLayout billing_info_payment_group;

    @BindView(R.id.billing_info_table_price_notice_group)
    LinearLayout billing_info_table_price_notice_group;

    @BindView(R.id.billing_info_request_group)
    LinearLayout billing_info_request_group;

    @BindView(R.id.tv_billing_info_confirm)
    TextView tv_billing_info_confirm;

    @BindView(R.id.tv_billing_info_deny)
    TextView tv_billing_info_deny;

    @BindView(R.id.edt_extra_items)
    EditText edt_extra_items;

    @BindView(R.id.edt_extra_amount)
    EditText edt_extra_amount;

    @BindView(R.id.extra_item_group)
    LinearLayout extra_item_group;

    private View mView;
    private MainActivity activity;
    private BillingInfoPresenter mBillingInfoPresenter;
    private DatabaseHandler mDatabaseHandler;
    private RequestOptional mRequestOptional;
    private Boolean isBroadcastReceiverRegister = false;
    private RequestLoaderDialog requestLoaderDialog;

    private BroadcastReceiver requestAcceptReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null &&
                    intent.getStringExtra(Const.NotificationParams.ACTION_REQUEST).equals(Const.NotificationParams.TYPE_SERVICE_ACCEPT) ||
                    intent.getStringExtra(Const.NotificationParams.ACTION_REQUEST).equals(Const.NotificationParams.TYPE_SERVICE_REJECT)){

                EbizworldUtils.appLogInfo("HaoLS", "requestAcceptReceiver: " + intent.getStringExtra(Const.NotificationParams.ACTION_REQUEST));

                if (mBillingInfoPresenter != null){

                    mBillingInfoPresenter.checkRequestStatus(new PreferenceHelper(activity).getLoginType(), requestLoaderDialog);

                }

            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (MainActivity) getActivity();

        mDatabaseHandler = new DatabaseHandler(activity);

        if (mDatabaseHandler != null){

            mDatabaseHandler.DeleteChat(String.valueOf(new PreferenceHelper(activity).getRequestId())); //Delete chat from Request ID

        }

        if (getArguments() != null){

            mRequestOptional = getArguments().getParcelable(Const.Params.REQUEST_OPTIONAL);

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_billing_info, container, false);
        ButterKnife.bind(this, mView);

        mBillingInfoPresenter = new BillingInfoPresenter(activity, this);

        if (mRequestOptional != null){

            settingForRequestBillingInfo();

            mBillingInfoPresenter.getNormalTripBillingInfo(mRequestOptional, new PreferenceHelper(activity).getLoginType());

        }else {

            settingForFinalBillingInfoWhenEndTrip();

            mBillingInfoPresenter.checkRequestStatus(new PreferenceHelper(activity).getLoginType(), requestLoaderDialog);
        }

        mBtn_pay_bycash.setOnClickListener(this);
        mBtn_pay_bypaypal.setOnClickListener(this);

        tv_billing_info_confirm.setOnClickListener(this);
        tv_billing_info_deny.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

        if (!isBroadcastReceiverRegister){

            LocalBroadcastManager.getInstance(activity).registerReceiver(requestAcceptReceiver, new IntentFilter(Const.NotificationParams.ACTION_REQUEST));
            isBroadcastReceiverRegister = true;

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (isBroadcastReceiverRegister){

            LocalBroadcastManager.getInstance(activity).unregisterReceiver(requestAcceptReceiver);
            isBroadcastReceiverRegister = false;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_pay_bycash:{

                mBillingInfoPresenter.paymentByCash(new PreferenceHelper(activity).getRequestId(), new PreferenceHelper(activity).getLoginType());

            }
            break;

            case R.id.btn_pay_bypaypal:{

                mBillingInfoPresenter.getBrainTreeToken(new PreferenceHelper(activity).getLoginType());

            }
            break;

            case R.id.tv_billing_info_confirm:{

                onConfirmNormalRequest(mRequestOptional);

            }
            break;

            case R.id.tv_billing_info_deny:{

                EbizworldUtils.addFragment(activity, R.id.content_frame, new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);

            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Const.ServiceCode.REQUEST_PAYPAL){

            if (resultCode == Activity.RESULT_OK){

                DropInResult dropInResult = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                if (dropInResult.getPaymentMethodNonce() != null){

                    mBillingInfoPresenter.postNonceToServer(dropInResult.getPaymentMethodNonce().toString(), new PreferenceHelper(activity).getLoginType());

                }

            }else if (resultCode == Activity.RESULT_CANCELED){

                onHandleFail("Request is canceled");

            }else {

                Exception exception = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                onHandleFail(exception.toString());
                EbizworldUtils.appLogError("HaoLS", "PayPal result" + exception.toString());
            }
        }
    }

    @Override
    public void settingForRequestBillingInfo() {

        mTv_billing_info_notice.setVisibility(View.GONE);
        billing_info_payment_group.setVisibility(View.GONE);
        extra_item_group.setVisibility(View.GONE);
        billing_info_table_price_notice_group.setVisibility(View.VISIBLE);
        billing_info_request_group.setVisibility(View.VISIBLE);

    }

    @Override
    public void settingForFinalBillingInfoWhenEndTrip() {

        mTv_billing_info_notice.setVisibility(View.VISIBLE);
        billing_info_payment_group.setVisibility(View.VISIBLE);
        extra_item_group.setVisibility(View.VISIBLE);
        billing_info_table_price_notice_group.setVisibility(View.GONE);
        billing_info_request_group.setVisibility(View.GONE);

    }

    @Override
    public void onFinalBillingInfoSuccess(RequestDetail requestDetail, String paymentMode) {

        mTv_billing_info_total_price.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getTrip_total_price());
        mTv_billing_info_a_and_e_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getA_and_e());
        mTv_billing_info_imh_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getImh());
        mTv_billing_info_ferry_terminals_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getFerry_terminals());
        mTv_billing_info_staircase_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getStaircase());
        mTv_billing_info_tarmac_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getTarmac());
        mTv_billing_info_weight_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getWeight());
        mTv_billing_info_oxygen_tank_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getOxygen_tank());
        mTv_billing_info_pickup_type_value.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getPickup_type());

        if (requestDetail.getIs_expense().equals("true") || requestDetail.getIs_expense().equals("1")){

            extra_item_group.setVisibility(View.VISIBLE);

            edt_extra_items.setText(requestDetail.getExraItems());
            edt_extra_amount.setText(requestDetail.getCurrnecy_unit() + " " + String.valueOf(requestDetail.getExtraAmount()));

        }else {

            extra_item_group.setVisibility(View.GONE);
        }

        switch (paymentMode){

            case Const.PreSelectPaymentParams.CASH:{

                onClick(mBtn_pay_bycash);

            }
            break;

            case Const.PreSelectPaymentParams.CREDIT_CARDS:{

            }
            break;

            case Const.PreSelectPaymentParams.PAYPAL:{

            }
            break;

            default:{

            }
            break;
        }

    }

    @Override
    public void onHandleFail(String error) {

        EbizworldUtils.showShortToast(error, activity);

    }

    @Override
    public void onRequestAccept(Bundle bundle) {

        if (requestLoaderDialog != null && requestLoaderDialog.getDialog().isShowing()) {

            requestLoaderDialog.dismiss();

        }

        TravelMapFragment travelMapFragment = new TravelMapFragment();
        travelMapFragment.setArguments(bundle);
        EbizworldUtils.addFragment(activity, R.id.content_frame, travelMapFragment, false, Const.TRAVEL_MAP_FRAGMENT, true);

    }

    @Override
    public void onRequestBillingInfoSuccess(
            String currency,
            String a_and_e,
            String imh,
            String ferry_terminals,
            String staircase,
            String tarmac,
            String weight,
            String oxygen_tank,
            String case_type,
            int other_expenses,
            int total) {

        mTv_billing_info_a_and_e_value.setText(currency + " " + a_and_e);

        mTv_billing_info_imh_value.setText(currency + " " + imh);

        mTv_billing_info_ferry_terminals_value.setText(currency + " " + ferry_terminals);

        mTv_billing_info_staircase_value.setText(currency + " " + staircase);

        mTv_billing_info_tarmac_value.setText(currency + " " + tarmac);

        mTv_billing_info_weight_value.setText(currency + " " + weight);

        mTv_billing_info_oxygen_tank_value.setText(currency + " " + oxygen_tank);

        mTv_billing_info_pickup_type_value.setText(currency + " " + case_type);



        if (other_expenses > 0){

            mTv_billing_info_total_price.setText(currency + " " + String.valueOf(total) + " - " + String.valueOf(total + other_expenses));

        }else {

            mTv_billing_info_total_price.setText(currency + " " + String.valueOf(total));

        }

    }

    @Override
    public void onRequestBillingInfoFail() {

        EbizworldUtils.addFragment(activity, R.id.content_frame, new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);

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
    public void onBrainTreeTokenResponse(String client_token) {

        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(client_token);

        startActivityForResult(dropInRequest.getIntent(activity), Const.ServiceCode.REQUEST_PAYPAL);

    }

    @Override
    public void onPostPayPalNonceSuccess(RequestDetail requestDetail) {

        if (!activity.currentFragment.equals(Const.RATING_FRAGMENT) && !activity.isFinishing()){

            Bundle bundle = new Bundle();
            RatingFragment ratingFragment = new RatingFragment();
            bundle.putSerializable(Const.REQUEST_DETAIL, requestDetail);
            ratingFragment.setArguments(bundle);

            EbizworldUtils.addFragment(activity, R.id.content_frame, ratingFragment, false, Const.RATING_FRAGMENT, true);

        }

    }

    @Override
    public void onPayCashSuccess(RequestDetail requestDetail) {

        if (!activity.currentFragment.equals(Const.RATING_FRAGMENT) && !activity.isFinishing()){

            Bundle bundle = new Bundle();
            RatingFragment ratingFragment = new RatingFragment();
            bundle.putSerializable(Const.REQUEST_DETAIL, requestDetail);
            ratingFragment.setArguments(bundle);
            EbizworldUtils.addFragment(activity, R.id.content_frame, ratingFragment, false, Const.RATING_FRAGMENT, true);

        }

    }

    @Override
    public void onConfirmNormalRequest(RequestOptional requestOptional) {

        if (requestOptional != null){

            Bundle bundle = new Bundle();
            bundle.putParcelable(Const.Params.REQUEST_OPTIONAL, requestOptional);

            FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
            requestLoaderDialog = new RequestLoaderDialog();
            requestLoaderDialog.setArguments(bundle);
            requestLoaderDialog.setCancelable(false);
            requestLoaderDialog.show(fragmentManager, Const.REQUEST_LOADER_DIALOGFRAGMENT);

        }

    }
}
