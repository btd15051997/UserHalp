package sg.halp.user.Presenter.BillingInfo;

import sg.halp.user.Dialog.RequestLoaderDialog;
import sg.halp.user.Models.RequestOptional;

public interface IBillingInfoPresenter {

    void paymentByCash(int requestID, String loginType);
    void getBrainTreeToken(String loginType);
    void postNonceToServer(String nonce, String loginType);
    void getNormalTripBillingInfo(RequestOptional requestOptional, String loginType);
    void checkRequestStatus(String loginType, RequestLoaderDialog requestLoaderDialog);
}
