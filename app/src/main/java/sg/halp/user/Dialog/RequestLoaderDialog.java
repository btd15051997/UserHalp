package sg.halp.user.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.Models.RequestOptional;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.RequestAmbulanceAPI;
import sg.halp.user.restAPI.WaitingCancelRequestAPI;

public class RequestLoaderDialog extends DialogFragment implements AsyncTaskCompleteListener {

    @BindView(R.id.content)
    RippleBackground rippleBackground;

    @BindView(R.id.cancel_req_create)
    TextView cancel_req_create;

    @BindView(R.id.req_status)
    TextView req_status;

    private Activity activity;
    private DialogFragmentCallback.CancelRequestListener cancelRequestListener;
    private RequestOptional mRequestOptional;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();

        if (getArguments() != null){

            mRequestOptional = getArguments().getParcelable(Const.Params.REQUEST_OPTIONAL);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_black)));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.request_loading);

        ButterKnife.bind(this, dialog);

        cancel_req_create.setEnabled(false);
        cancel_req_create.setBackgroundColor(getResources().getColor(R.color.lightblue100));
        rippleBackground.startRippleAnimation();

        switch (new PreferenceHelper(activity).getLoginType()){

            case Const.PatientService.PATIENT:{

                if (mRequestOptional != null){

                    new RequestAmbulanceAPI(activity, this)
                            .RequestAmbulanceForPatient(
                                    mRequestOptional,
                                    new PreferenceHelper(activity).getUserId(),
                                    new PreferenceHelper(activity).getSessionToken(),
                                    Const.ServiceCode.REQUEST_AMBULANCE
                            );

                }

                cancel_req_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        req_status.setText(getResources().getString(R.string.txt_canceling_req));

                        new WaitingCancelRequestAPI(activity, RequestLoaderDialog.this)
                                .waitingCancelRequestForPatient(
                                        new PreferenceHelper(activity).getUserId(),
                                        new PreferenceHelper(activity).getSessionToken(),
                                        new PreferenceHelper(activity).getRequestId(),
                                        Const.ServiceCode.CANCEL_CREATE_REQUEST
                                );

                    }
                });
            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                if (mRequestOptional != null){

                    new RequestAmbulanceAPI(activity, this)
                            .RequestAmbulanceForNursingHome(
                                    mRequestOptional,
                                    new PreferenceHelper(activity).getUserId(),
                                    new PreferenceHelper(activity).getSessionToken(),
                                    Const.ServiceCode.REQUEST_AMBULANCE
                            );

                }

                cancel_req_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        req_status.setText(getResources().getString(R.string.txt_canceling_req));

                        new WaitingCancelRequestAPI(activity, RequestLoaderDialog.this)
                                .waitingCancelRequestForNursingHome(
                                        new PreferenceHelper(activity).getUserId(),
                                        new PreferenceHelper(activity).getSessionToken(),
                                        new PreferenceHelper(activity).getRequestId(),
                                        Const.ServiceCode.CANCEL_CREATE_REQUEST
                                );

                    }
                });
            }
            break;

            case Const.HospitalService.HOSPITAL:{

                if (mRequestOptional != null){

                    new RequestAmbulanceAPI(activity, this)
                            .RequestAmbulanceForHospital(
                                    mRequestOptional,
                                    new PreferenceHelper(activity).getUserId(),
                                    new PreferenceHelper(activity).getSessionToken(),
                                    Const.ServiceCode.REQUEST_AMBULANCE
                            );

                }

                cancel_req_create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        req_status.setText(getResources().getString(R.string.txt_canceling_req));

                        new WaitingCancelRequestAPI(activity, RequestLoaderDialog.this)
                                .waitingCancelRequestForHospital(
                                        new PreferenceHelper(activity).getUserId(),
                                        new PreferenceHelper(activity).getSessionToken(),
                                        new PreferenceHelper(activity).getRequestId(),
                                        Const.ServiceCode.CANCEL_CREATE_REQUEST
                                );

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

            case Const.ServiceCode.CANCEL_CREATE_REQUEST:{

                EbizworldUtils.appLogInfo("HaoLS", "cancel req_response " + response);

                if (response != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            if (cancelRequestListener != null){

                                cancelRequestListener.onSuccess(true);

                            }
                            new PreferenceHelper(activity).putRequestId(-1);
                            new PreferenceHelper(activity).clearRequestData();
                            dismiss();

                        }else {

                            if (cancelRequestListener != null){

                                cancelRequestListener.onFail(true);

                            }
                            dismiss();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogInfo("HaoLS", "cancel req_response failed: " + e.toString());
                    }
                }
            }
            break;

            case Const.ServiceCode.REQUEST_AMBULANCE:
                EbizworldUtils.appLogInfo("HaoLS", "create req_response succeeded: " + response);

                if (response != null){

                    try {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {

                            new PreferenceHelper(activity).putRequestId(Integer.parseInt(job1.getString("request_id")));
//                            startGetRequestStatus();

                            if (cancel_req_create != null){

                                cancel_req_create.setEnabled(true);
                                cancel_req_create.setBackgroundColor(getResources().getColor(R.color.lightblueA700));

                            }

                        }else {

                            dismiss();
                            String error = job1.getString("error");
                            Commonutils.showtoast(error, activity);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "create req_response failed: " + e.toString());
                    }
                }
                break;
        }
    }

    public void setCancelRequestListener(DialogFragmentCallback.CancelRequestListener cancelRequestListener) {
        this.cancelRequestListener = cancelRequestListener;
    }
}
