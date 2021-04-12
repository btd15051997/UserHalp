package sg.halp.user.Dialog.BillingInfoSchedule;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.ScheduleRequestAPI;

public class BillingInfoScheduleDialog extends DialogFragment implements IBillingInfoView, AsyncTaskCompleteListener {

    @BindView(R.id.tv_billing_info_notice)
    TextView tv_billing_info_notice;

    @BindView(R.id.tv_billing_info_total_price)
    TextView tv_billing_info_total_price;

    @BindView(R.id.tv_billing_info_staircase_value)
    TextView tv_billing_info_staircase_value;

    @BindView(R.id.tv_billing_info_tarmac_value)
    TextView tv_billing_info_tarmac_value;

    @BindView(R.id.tv_billing_info_weight_value)
    TextView tv_billing_info_weight_value;

    @BindView(R.id.tv_billing_info_oxygen_tank_value)
    TextView tv_billing_info_oxygen_tank_value;

    @BindView(R.id.tv_billing_info_pickup_type_value)
    TextView tv_billing_info_pickup_type_value;

    @BindView(R.id.tv_billing_info_confirm)
    TextView tv_billing_info_confirm;

    @BindView(R.id.tv_billing_info_deny)
    TextView tv_billing_info_deny;

    @BindView(R.id.billing_info_payment_group)
    LinearLayout billing_info_payment_group;

    @BindView(R.id.edt_extra_items)
    EditText edt_extra_items;

    @BindView(R.id.edt_extra_amount)
    EditText edt_extra_amount;

    @BindView(R.id.extra_item_group)
    LinearLayout extra_item_group;

    private Activity mActivity;
    private String staircase = "", weight = "", tarmac = "", oxygen = "", caseType = "", total = "", currency = "", other_expense = "";
    private Schedule mSchedule;
    private HospitalDischarge mHospitalDischarge;
    private DialogFragmentCallback.DialogDismissCallback dialogDismissCallback;
    private DialogFragmentCallback.BillingInfoCallback billingInfoCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();

        if (getArguments() != null){

            staircase = getArguments().getString(Const.Params.STAIRCASE);
            weight = getArguments().getString(Const.Params.WEIGHT);
            tarmac = getArguments().getString(Const.Params.TARMAC);
            oxygen = getArguments().getString(Const.Params.OXYGEN_TANK);
            caseType = getArguments().getString(Const.Params.CASE_TYPE);
            total = getArguments().getString(Const.Params.TOTAL);
            currency = getArguments().getString(Const.Params.CURRENCY);
            other_expense = getArguments().getString(Const.Params.OTHER_EXPENSES);
            mSchedule = getArguments().getParcelable(Const.SCHEDULE);
            mHospitalDischarge = getArguments().getParcelable(Const.HOSPITAL_DISCHARGE_OPTION);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(mActivity, R.style.DialogSlideAnim_leftright_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.fragment_billing_info);

        ButterKnife.bind(this, dialog);

        extra_item_group.setVisibility(View.GONE);

        switch (new PreferenceHelper(mActivity).getLoginType()){

            case Const.PatientService.PATIENT:{

                billing_info_payment_group.setVisibility(View.GONE);
                tv_billing_info_notice.setVisibility(View.GONE);

                if (!other_expense.equals("")){

                    int expense = Integer.parseInt(total) + Integer.parseInt(other_expense);

                    tv_billing_info_total_price.setText(currency + " " + total + " - " + String.valueOf(expense));

                }else {

                    tv_billing_info_total_price.setText(currency + " " + total);

                }
                tv_billing_info_staircase_value.setText(currency + " " + staircase);
                tv_billing_info_tarmac_value.setText(currency + " " + tarmac);
                tv_billing_info_weight_value.setText(currency + " " + weight);
                tv_billing_info_oxygen_tank_value.setText(currency + " " + oxygen);
                tv_billing_info_pickup_type_value.setText(currency + " " + caseType);

                tv_billing_info_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mSchedule != null){

                            new ScheduleRequestAPI(mActivity, BillingInfoScheduleDialog.this)
                                    .bookSchedulePatient(mSchedule, mHospitalDischarge, Const.ServiceCode.REQUEST_LATER);

                        }

                    }
                });

                tv_billing_info_deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dismiss();
                    }
                });

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                billing_info_payment_group.setVisibility(View.GONE);
                tv_billing_info_notice.setVisibility(View.GONE);

                if (!other_expense.equals("")){

                    int expense = Integer.parseInt(total) + Integer.parseInt(other_expense);

                    tv_billing_info_total_price.setText(currency + " " + total + " - " + String.valueOf(expense));

                }else {

                    tv_billing_info_total_price.setText(currency + " " + total);

                }

                tv_billing_info_staircase_value.setText(currency + " " + staircase);
                tv_billing_info_tarmac_value.setText(currency + " " + tarmac);
                tv_billing_info_weight_value.setText(currency + " " + weight);
                tv_billing_info_oxygen_tank_value.setText(currency + " " + oxygen);
                tv_billing_info_pickup_type_value.setText(currency + " " + caseType);

                tv_billing_info_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (billingInfoCallback != null){

                            billingInfoCallback.onConfirmListener(true);
                        }

                        dismiss();
                    }
                });

                tv_billing_info_deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (billingInfoCallback != null){

                            billingInfoCallback.onCancelListener(true);
                        }

                        dismiss();

                    }
                });

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                billing_info_payment_group.setVisibility(View.GONE);
                tv_billing_info_notice.setVisibility(View.GONE);

                if (!other_expense.equals("")){

                    int expense = Integer.parseInt(total) + Integer.parseInt(other_expense);

                    tv_billing_info_total_price.setText(currency + " " + total + " - " + String.valueOf(expense));

                }else {

                    tv_billing_info_total_price.setText(currency + " " + total);

                }

                tv_billing_info_staircase_value.setText(currency + " " + staircase);
                tv_billing_info_tarmac_value.setText(currency + " " + tarmac);
                tv_billing_info_weight_value.setText(currency + " " + weight);
                tv_billing_info_oxygen_tank_value.setText(currency + " " + oxygen);
                tv_billing_info_pickup_type_value.setText(currency + " " + caseType);

                tv_billing_info_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (billingInfoCallback != null){

                            billingInfoCallback.onConfirmListener(true);

                        }

                        dismiss();
                    }
                });

                tv_billing_info_deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (billingInfoCallback != null){

                            billingInfoCallback.onCancelListener(true);

                        }

                        dismiss();

                    }
                });
            }
            break;
        }

        return dialog;
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.REQUEST_LATER:{

                if (response != null) {

                    try {

                        JSONObject job = new JSONObject(response);

                        if (job.getString("success").equals("true")) {

                            Log.d("HaoLS", "Create schedule succeeded" + response);

                            Commonutils.progressDialog_hide();
                            Commonutils.showtoast(getResources().getString(R.string.txt_trip_schedule_success), mActivity);
                            new PreferenceHelper(getActivity()).putRequestType("0");
                            new PreferenceHelper(getActivity()).putAmbulance_name(getResources().getString(R.string.any_ambulance));

                            dismiss();

                            if (dialogDismissCallback != null){

                                dialogDismissCallback.onDialogDismissListener(true);

                            }


                        } else {

                            Commonutils.progressDialog_hide();
                            String error = job.getString("error");
                            Commonutils.showtoast(error, mActivity);

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Request schedule failed " + e.toString());

                    }
                }

            }
            break;
        }

    }

    public void setDialogDismissCallback(DialogFragmentCallback.DialogDismissCallback dialogDismissCallback) {
        this.dialogDismissCallback = dialogDismissCallback;
    }

    public void setBillingInfoCallback(DialogFragmentCallback.BillingInfoCallback billingInfoCallback) {
        this.billingInfoCallback = billingInfoCallback;
    }

    @Override
    public void initializeContent() {

        billing_info_payment_group.setVisibility(View.GONE);
        tv_billing_info_notice.setVisibility(View.GONE);

        if (!other_expense.equals("")){

            int expense = Integer.parseInt(total) + Integer.parseInt(other_expense);

            tv_billing_info_total_price.setText(currency + " " + total + " - " + String.valueOf(expense));

        }else {

            tv_billing_info_total_price.setText(currency + " " + total);

        }
        tv_billing_info_staircase_value.setText(currency + " " + staircase);
        tv_billing_info_tarmac_value.setText(currency + " " + tarmac);
        tv_billing_info_weight_value.setText(currency + " " + weight);
        tv_billing_info_oxygen_tank_value.setText(currency + " " + oxygen);
        tv_billing_info_pickup_type_value.setText(currency + " " + caseType);

    }
}
