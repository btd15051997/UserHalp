package sg.halp.user.Fragment.Account;

import android.app.Activity;

import java.util.List;

import sg.halp.user.Models.AccountSettings;

public interface IAccountView {

    void showPreSelectPaymentDialog();
    void deleteAllEventsCalendar(Activity activity);
    void patientSetting();
    void nurseSetting();
    void hospitalSetting();
    List<AccountSettings> getAccountSettingsList();
}
