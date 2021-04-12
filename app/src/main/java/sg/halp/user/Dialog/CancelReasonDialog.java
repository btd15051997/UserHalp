package sg.halp.user.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Adapter.CancelReasonAdapter;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Interface.DialogFragmentCallback;
import sg.halp.user.Models.CancelReason;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.Utils.RecyclerLongPressClickListener;
import sg.halp.user.restAPI.CancelNormalRequestAPI;
import sg.halp.user.restAPI.CancelReasonAPI;

public class CancelReasonDialog extends DialogFragment implements AsyncTaskCompleteListener, View.OnClickListener {

    @BindView(R.id.cancel_reason_lst)
    RecyclerView cancel_reason_lst;

    @BindView(R.id.edt_another_reason)
    EditText edt_another_reason;

    @BindView(R.id.img_close)
    ImageView img_close;

    @BindView(R.id.img_confirm)
    ImageView img_confirm;

    private Activity activity;
    private ArrayList<CancelReason> cancelReasonLst;
    private DialogFragmentCallback.CancelRequestListener cancelRequestListener;
    private CancelReason cancelReason;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(activity, R.style.DialogThemeforview);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.fade_drawable));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_cancel_request);

        ButterKnife.bind(this, dialog);

        switch (new PreferenceHelper(activity).getLoginType()){

            case Const.PatientService.PATIENT:{

                new CancelReasonAPI(activity, this)
                        .getCancelRideReasonListForPatient(Const.ServiceCode.CANCEL_REASON);

                cancel_reason_lst.addOnItemTouchListener(new RecyclerLongPressClickListener(activity, cancel_reason_lst, new RecyclerLongPressClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        cancelReason = cancelReasonLst.get(position);

                        EbizworldUtils.showShortToast(cancelReason.getReasontext(), activity);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));


            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new CancelReasonAPI(activity, this)
                        .getCancelRideReasonListForNursingHome(Const.ServiceCode.CANCEL_REASON);

                cancel_reason_lst.addOnItemTouchListener(new RecyclerLongPressClickListener(activity, cancel_reason_lst, new RecyclerLongPressClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        cancelReason = cancelReasonLst.get(position);
                        EbizworldUtils.showShortToast(cancelReason.getReasontext(), activity);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));


            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new CancelReasonAPI(activity, this)
                        .getCancelRideReasonListForHospital(Const.ServiceCode.CANCEL_REASON);

                cancel_reason_lst.addOnItemTouchListener(new RecyclerLongPressClickListener(activity, cancel_reason_lst, new RecyclerLongPressClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        cancelReason = cancelReasonLst.get(position);
                        EbizworldUtils.showShortToast(cancelReason.getReasontext(), activity);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));


            }
            break;
        }

        img_close.setOnClickListener(this);
        img_confirm.setOnClickListener(this);

        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.CANCEL_REASON:{

                EbizworldUtils.appLogInfo("HaoLS", "cancel reason: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("success").equals("true")) {

                        cancelReasonLst = new ArrayList<>();

                        JSONArray jsonArray = jsonObject.optJSONArray("data");

                        if (null != jsonArray && jsonArray.length() > 0)

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject dataObj = jsonArray.optJSONObject(i);
                                CancelReason cancel = new CancelReason();
                                cancel.setReasonId(dataObj.optString("id"));
                                cancel.setReasontext(dataObj.optString("cancel_reason"));
                                cancelReasonLst.add(cancel);

                            }

                        if (null != cancelReasonLst && cancelReasonLst.size() > 0) {

                            CancelReasonAdapter CancelAdapter = new CancelReasonAdapter(activity, cancelReasonLst);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
                            cancel_reason_lst.setLayoutManager(mLayoutManager);
                            cancel_reason_lst.setItemAnimator(new DefaultItemAnimator());
                            cancel_reason_lst.setAdapter(CancelAdapter);

                        } else {

                            EbizworldUtils.showShortToast(getResources().getString(R.string.txt_no_cancel_reason), activity);

                        }
                    } else {

                        EbizworldUtils.showShortToast(jsonObject.optString("error_message"), activity);
                        EbizworldUtils.appLogError("HaoLS", "Cancel reason: " + jsonObject.getString("error_message"));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    EbizworldUtils.appLogError("HaoLS", "Cancel reason: " + e.toString());
                }

            }
            break;

            case Const.ServiceCode.CANCEL_RIDE:{

                EbizworldUtils.appLogInfo("HaoLS", "Cancel request: " + response);

                if (response != null){

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("true")) {

                            if (cancelRequestListener != null){

                                cancelRequestListener.onSuccess(true);
                            }

                            dismiss();

                        } else {

                            if (jsonObject.has("error")){

                                EbizworldUtils.showShortToast(jsonObject.getString("error"), activity);

                            }

                            if (cancelRequestListener != null){

                                cancelRequestListener.onFail(true);
                            }

                            dismiss();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogDebug("HaoLS", "Cancel request failed " + e.toString());
                    }

                }

            }
            break;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_close:{

                if (cancelRequestListener != null){

                    cancelRequestListener.onFail(true);

                }

                dismiss();

            }
            break;

            case R.id.img_confirm:{

                switch (new PreferenceHelper(activity).getLoginType()){

                    case Const.PatientService.PATIENT:{

                        if (cancelReason != null){

                            new CancelNormalRequestAPI(activity, CancelReasonDialog.this)
                                    .cancelNormalRequestForPatient(cancelReason.getReasonId(), edt_another_reason.getText().toString().trim(), Const.ServiceCode.CANCEL_RIDE);

                        }else {

                            EbizworldUtils.showShortToast(getResources().getString(R.string.cancellation_reason_warning), activity);
                        }

                    }
                    break;

                    case Const.NursingHomeService.NURSING_HOME:{

                        if (cancelReason != null){

                            new CancelNormalRequestAPI(activity, CancelReasonDialog.this)
                                    .cancelNormalRequestForNursingHome(cancelReason.getReasonId(), edt_another_reason.getText().toString().trim(), Const.ServiceCode.CANCEL_RIDE);

                        }else {

                            EbizworldUtils.showShortToast(getResources().getString(R.string.cancellation_reason_warning), activity);
                        }

                    }
                    break;

                    case Const.HospitalService.HOSPITAL:{

                        if (cancelReason != null){

                            new CancelNormalRequestAPI(activity, CancelReasonDialog.this)
                                    .cancelNormalRequestForHospital(cancelReason.getReasonId(), edt_another_reason.getText().toString().trim(), Const.ServiceCode.CANCEL_RIDE);

                        }else {

                            EbizworldUtils.showShortToast(getResources().getString(R.string.cancellation_reason_warning), activity);
                        }

                    }
                    break;
                }
            }
            break;
        }

    }

    public void setCancelRequestListener(DialogFragmentCallback.CancelRequestListener cancelRequestListener) {
        this.cancelRequestListener = cancelRequestListener;
    }
}
