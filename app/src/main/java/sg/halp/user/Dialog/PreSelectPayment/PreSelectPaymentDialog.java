package sg.halp.user.Dialog.PreSelectPayment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Presenter.PreSelectPaymentDialog.PreSelectPaymentPresenter;
import sg.halp.user.R;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;

public class PreSelectPaymentDialog extends DialogFragment implements IPreSelectPaymentView {

    @BindView(R.id.tv_pre_select_payment_title)
    TextView tv_pre_select_payment_title;

    @BindView(R.id.lv_pre_select_payment_content)
    ListView lv_pre_select_payment_content;

    private Activity activity;
    private PreSelectPaymentPresenter mPreSelectPaymentPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Dialog dialog = new Dialog(activity, R.style.DialogSlideAnim_leftright_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.fade_drawable));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_pre_select_payment);

        ButterKnife.bind(this, dialog);
        
        mPreSelectPaymentPresenter = new PreSelectPaymentPresenter(activity, this);

        initContent();

        return dialog;
    }

    @Override
    public void initContent() {

        final List<String> paymentModes = new ArrayList<>();
        paymentModes.add(getResources().getString(R.string.pre_select_payment_none));
        paymentModes.add(getResources().getString(R.string.pre_select_payment_cash));
        paymentModes.add(getResources().getString(R.string.pre_select_payment_paypal));
        paymentModes.add(getResources().getString(R.string.pre_select_payment_creditcard));

        final List<String> paymentCodes = new ArrayList<>();
        paymentCodes.add(Const.PreSelectPaymentParams.NONE);
        paymentCodes.add(Const.PreSelectPaymentParams.CASH);
        paymentCodes.add(Const.PreSelectPaymentParams.PAYPAL);
        paymentCodes.add(Const.PreSelectPaymentParams.CREDIT_CARDS);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, paymentModes);
        lv_pre_select_payment_content.setAdapter(arrayAdapter);
        lv_pre_select_payment_content.setSelection(0);
        lv_pre_select_payment_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mPreSelectPaymentPresenter.updatePreSelectPayment(paymentModes.get(position), paymentCodes.get(position));

            }
        });

    }

    @Override
    public void onUpdatePaymentSuccess() {

        dismiss();
    }

    @Override
    public void onUpdatePaymentFail(String error) {

        EbizworldUtils.showShortToast(error, activity);

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
}
