package sg.halp.user.Dialog.PreSelectPayment;

public interface IPreSelectPaymentView {

    void initContent();
    void onUpdatePaymentSuccess();
    void onUpdatePaymentFail(String error);
    void onDisconnectNetwork();
    void showProgressDialog();
    void hideProgressDialog();
}
