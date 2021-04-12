package sg.halp.user.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.Models.Schedule;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.CancelScheduleRequestAPI;
import sg.halp.user.restAPI.ScheduleListAPI;

public class CancellationPolicyDialog extends DialogFragment implements View.OnClickListener, AsyncTaskCompleteListener {

    @BindView(R.id.custom_simple_title)
    TextView policy_title;

    @BindView(R.id.custom_simple_content)
    TextView policy_content;

    @BindView(R.id.btn_yes)
    TextView btn_yes;

    @BindView(R.id.btn_no)
    TextView btn_no;

    private Activity mContext;
    private Schedule mSchedule;
    private DialogFragmentCallback.ScheduleCallback scheduleCallback;
    private List<Schedule> mScheduleList;

    public CancellationPolicyDialog() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mContext = getActivity();

        if (bundle != null){

            mSchedule = bundle.getParcelable(Const.SCHEDULE);

        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnim_leftright_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.fade_drawable));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom_simple);

        ButterKnife.bind(this, dialog);

        policy_title.setText(getResources().getString(R.string.cancellation_policy_title));
        policy_content.setText(getResources().getString(R.string.cancellation_policy_content));

        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);

        return dialog;
    }

    public void setScheduleList(DialogFragmentCallback.ScheduleCallback scheduleCallback){

        this.scheduleCallback = scheduleCallback;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_yes:{

                if (mSchedule != null){

                    switch (new PreferenceHelper(mContext).getLoginType()){

                        case Const.PatientService.PATIENT:{

                            new CancelScheduleRequestAPI(getActivity(), this)
                                    .cancelPatientSchedule(mSchedule.getRequest_id(), Const.ServiceCode.CANCEL_SCHEDULE_RIDE);

                        }
                        break;

                        case Const.NursingHomeService.NURSING_HOME:{

                            new CancelScheduleRequestAPI(getActivity(), this)
                                    .cancelNurseSchedule(mSchedule.getRequest_id(), Const.ServiceCode.CANCEL_SCHEDULE_RIDE);

                        }
                        break;

                        case Const.HospitalService.HOSPITAL:{

                            new CancelScheduleRequestAPI(getActivity(), this)
                                    .cancelHospitalSchedule(mSchedule.getRequest_id(), Const.ServiceCode.CANCEL_SCHEDULE_RIDE);

                        }
                        break;

                    }

                }

            }
            break;

            case R.id.btn_no:{

                dismiss();

            }
            break;
        }
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.CANCEL_SCHEDULE_RIDE:{
                EbizworldUtils.appLogInfo("HaoLS", "Cancel schedule request: " + response);
                Commonutils.progressDialog_hide();
                if (response != null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")) {

                            EbizworldUtils.appLogDebug("HaoLS", "Cancel schedule succeeded: " + response);

                            Commonutils.showtoast(mContext.getResources().getString(R.string.txt_cancel_schedule), mContext);

                            switch (new PreferenceHelper(mContext).getLoginType()){

                                case Const.PatientService.PATIENT:{

                                    new ScheduleListAPI(mContext, this).getPatientSchedule(Const.ServiceCode.GET_SCHEDULE);

                                }
                                break;

                                case Const.NursingHomeService.NURSING_HOME:{

                                    new ScheduleListAPI(mContext, this).getNursingHomeSchedule(Const.ServiceCode.GET_SCHEDULE);

                                }
                                break;

                                case Const.HospitalService.HOSPITAL:{

                                    new ScheduleListAPI(mContext, this).getHospitalSchedule(Const.ServiceCode.GET_SCHEDULE);
                                }
                                break;
                            }

                        } else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error")){

                                String error = jsonObject.getString("error");
                                EbizworldUtils.showShortToast(error, mContext);

                            }else if (jsonObject.has(Const.Params.PRICE_CANCEL_SCHEDULE)){

                                dismiss();

                                Bundle bundle = new Bundle();
                                bundle.putString(Const.Params.PRICE_CANCEL_SCHEDULE, jsonObject.getString(Const.Params.PRICE_CANCEL_SCHEDULE));
                                bundle.putString(Const.Params.CURRENCY, jsonObject.getString(Const.Params.CURRENCY));
                                bundle.putString(Const.Params.REQUEST_ID, String.valueOf(mSchedule.getRequest_id()));

                                FragmentManager fragmentManager = ((AppCompatActivity)mContext).getSupportFragmentManager();
                                CancellationFeeBillingDialog cancellationFeeBillingDialog = new CancellationFeeBillingDialog();
                                cancellationFeeBillingDialog.setCancelable(false);
                                cancellationFeeBillingDialog.setArguments(bundle);
                                cancellationFeeBillingDialog.show(fragmentManager, Const.CANCELLATION_FEE_BILLING_INFO_DIALOGFRAGMENT);

                            }

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Cancel ride failed " + e.toString());

                    }

                }else{

                    EbizworldUtils.appLogDebug("HaoLS", "Cancel ride failed because response is null ");
                }
            }
            break;

            case Const.ServiceCode.GET_SCHEDULE:{

                if (response != null) {

                    Log.d("HaoLS", "Schedule list" + response);

                    JSONObject scheduleObject = null;
                    try {

                        scheduleObject = new JSONObject(response);

                        if (scheduleObject.getString("success").equals("true")) {

                            ParseContent parseContent = new ParseContent(mContext);

                            switch (new PreferenceHelper(mContext).getLoginType()) {

                                case Const.PatientService.PATIENT: {

                                    mScheduleList = parseContent.parsePatientSchedule(response);
                                }
                                break;

                                case Const.NursingHomeService.NURSING_HOME: {

                                    mScheduleList = parseContent.parseNursingHomeSchedule(response);

                                }
                                break;

                                case Const.HospitalService.HOSPITAL: {

                                    mScheduleList = parseContent.parseHospitalSchedule(response);

                                }
                                break;
                            }

                            if (mScheduleList != null && scheduleCallback != null){

                                scheduleCallback.onScheduleCallback(mScheduleList);
                                dismiss();

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogInfo("HaoLS", "GET_SCHEDULE failed: " + e.toString());
                    }

                }
            }
            break;
        }

    }
}
