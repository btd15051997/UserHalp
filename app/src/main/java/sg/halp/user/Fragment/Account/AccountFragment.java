package sg.halp.user.Fragment.Account;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.halp.user.Activity.ProfileHospital.ProfileHospitalActivity;
import sg.halp.user.Activity.ProfileNursingHome.ProfileNursingHomeActivity;
import sg.halp.user.Activity.ProfilePatientActivity;
import sg.halp.user.Adapter.AccountSettingsAdapter;
import sg.halp.user.BuildConfig;
import sg.halp.user.Dialog.PreSelectPayment.PreSelectPaymentDialog;
import sg.halp.user.Fragment.BaseFragment;
import sg.halp.user.HelpwebActivity;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.MainActivity;
import sg.halp.user.Models.AccountSettings;
import sg.halp.user.Models.CalendarReminder;
import sg.halp.user.Models.Hospital;
import sg.halp.user.Models.NursingHome;
import sg.halp.user.Models.Patient;
import sg.halp.user.R;
import sg.halp.user.RealmController.RealmController;
import sg.halp.user.Utils.CalendarReminderHelper;
import sg.halp.user.Utils.Commonutils;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.WelcomeActivity;


/**
 * Created by user on 12/28/2016.
 */

public class AccountFragment extends BaseFragment implements IAccountView, AdapterView.OnItemClickListener {

    @BindView(R.id.lv_drawer_user_settings)
    ListView accountSettingsListView;


    @BindView(R.id.iv_account_icon)
    ImageView accountIcon;

    @BindView(R.id.tv_account_name) TextView accountName;

    @BindView(R.id.tv_build_version)
    TextView tv_build_version;

    private View view;
    private MainActivity mMainActivity;
    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_account, container, false);

        ButterKnife.bind(this, view);

        switch (new PreferenceHelper(getActivity()).getLoginType()){

            case Const.PatientService.PATIENT:{

                patientSetting();

                accountIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EbizworldUtils.makeActivityAnimation(mMainActivity, ProfilePatientActivity.class);
                    }
                });

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                nurseSetting();

                accountIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EbizworldUtils.makeActivityAnimation(mMainActivity, ProfileNursingHomeActivity.class);
                    }
                });

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                hospitalSetting();

                accountIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EbizworldUtils.makeActivityAnimation(mMainActivity, ProfileHospitalActivity.class);
                    }
                });

            }
            break;
        }

        return view;
    }

    @Override
    public void patientSetting(){

        String pictureUrl = new PreferenceHelper(mMainActivity).getPicture();

        /*User userprofile = RealmController.with(this).getUser(Integer.valueOf(new PreferenceHelper(getActivity()).getUserId()));*/
        Patient patientProfile = RealmController.with(this).getPatient(Integer.valueOf(new PreferenceHelper(getActivity()).getUserId()));

        /*String name = new PreferenceHelper(mMainActivity).getUser_name();*/

        tv_build_version.setText("Version: " + BuildConfig.VERSION_NAME);

        if (!pictureUrl.equals("")) {

            Log.e("asher","nav pic "+ pictureUrl);


            /*Picasso.get().load(pictureUrl).error(R.drawable.defult_user).into(accountIcon);*/

            Glide.with(mMainActivity)
                    .load(pictureUrl)
                    .apply(new RequestOptions().error(R.drawable.defult_user))
                    .into(accountIcon);

        }

        if (patientProfile.getmFullname() != null) {

            accountName.setText(patientProfile.getmFullname());

        }

        AccountSettingsAdapter settingsAdapter = new AccountSettingsAdapter(mMainActivity, getAccountSettingsList());

        accountSettingsListView.setAdapter(settingsAdapter);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mMainActivity, getResources().getIdentifier("layout_animation_from_left", "anim", mMainActivity.getPackageName()));

        accountSettingsListView.setLayoutAnimation(animation);

        settingsAdapter.notifyDataSetChanged();

        accountSettingsListView.scheduleLayoutAnimation();

        accountSettingsListView.setOnItemClickListener(this);

    }

    @Override
    public void nurseSetting(){

        NursingHome nursingHome = RealmController.with(this).getNurse(Integer.valueOf(new PreferenceHelper(getActivity()).getUserId()));
        /*String name = new PreferenceHelper(mMainActivity).getUser_name();
        String pictureUrl = new PreferenceHelper(mMainActivity).getPicture();

        String pictureUrl = nursingHome.getmPictureUrl();*/

        tv_build_version.setText("Version: " + BuildConfig.VERSION_NAME);

        if (nursingHome.getmPictureUrl() != null) {

            EbizworldUtils.appLogDebug("HaoLS","nav pic "+ nursingHome.getmPictureUrl());


            /*Picasso.get().load(pictureUrl).error(R.drawable.defult_user).into(accountIcon);*/

            Glide.with(mMainActivity)
                    .load(nursingHome.getmPictureUrl())
                    .apply(new RequestOptions().error(R.drawable.defult_user))
                    .into(accountIcon);

        }

        if (nursingHome.getmFullName() != null) {

            accountName.setText(nursingHome.getmFullName());

        }

        AccountSettingsAdapter settingsAdapter = new AccountSettingsAdapter(mMainActivity, getAccountSettingsList());

        accountSettingsListView.setAdapter(settingsAdapter);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mMainActivity, getResources().getIdentifier("layout_animation_from_left", "anim", mMainActivity.getPackageName()));

        accountSettingsListView.setLayoutAnimation(animation);

        settingsAdapter.notifyDataSetChanged();

        accountSettingsListView.scheduleLayoutAnimation();

        accountSettingsListView.setOnItemClickListener(this);
    }

    @Override
    public void hospitalSetting(){

        Hospital hospital = RealmController.with(this).getHospital(Integer.valueOf(new PreferenceHelper(getActivity()).getUserId()));
        /*String name = new PreferenceHelper(mMainActivity).getUser_name();
        String pictureUrl = new PreferenceHelper(mMainActivity).getPicture();

        String pictureUrl = nurse.getmPictureUrl();*/

        tv_build_version.setText("Version: " + BuildConfig.VERSION_NAME);

        if (hospital.getmCompanyPicture() != null) {

            EbizworldUtils.appLogDebug("HaoLS","nav pic "+ hospital.getmCompanyPicture());


            /*Picasso.get().load(pictureUrl).error(R.drawable.defult_user).into(accountIcon);*/

            Glide.with(mMainActivity)
                    .load(hospital.getmCompanyPicture())
                    .apply(new RequestOptions().error(R.drawable.defult_user))
                    .into(accountIcon);

        }else {

            EbizworldUtils.appLogDebug("HaoLS","nav pic isEmpty");
        }

        if (hospital.getmHospitalName() != null) {

            accountName.setText(hospital.getmHospitalName());

        }

        AccountSettingsAdapter settingsAdapter = new AccountSettingsAdapter(mMainActivity, getAccountSettingsList());

        accountSettingsListView.setAdapter(settingsAdapter);

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(mMainActivity, getResources().getIdentifier("layout_animation_from_left", "anim", mMainActivity.getPackageName()));

        accountSettingsListView.setLayoutAnimation(animation);

        settingsAdapter.notifyDataSetChanged();

        accountSettingsListView.scheduleLayoutAnimation();

        accountSettingsListView.setOnItemClickListener(this);
    }

    @Override
    public List<AccountSettings> getAccountSettingsList() {

        List<AccountSettings> accountSettingsList = new ArrayList<>();

        /*accountSettingsList.add(new AccountSettings(R.drawable.home_map_marker, getString(R.string.my_home)));
        accountSettingsList.add(new AccountSettings(R.drawable.flash, getString(R.string.ask_bot)));
        accountSettingsList.add(new AccountSettings(R.drawable.credit_card, getString(R.string.my_payment)));
        accountSettingsList.add(new AccountSettings(R.drawable.wallet, getString(R.string.ambulance2u_wallet)));
        accountSettingsList.add(new AccountSettings(R.drawable.ic_favorite_heart_button, getString(R.string.saved_places)));
        accountSettingsList.add(new AccountSettings(R.drawable.clock_alert, getString(R.string.ride_history)));*/

        if (new PreferenceHelper(getActivity()).getLoginType().equals(Const.PatientService.PATIENT)){

            /*accountSettingsList.add(new AccountSettings(R.drawable.ic_clock, getString(R.string.txt_hourly_booking)));*/
            /*accountSettingsList.add(new AccountSettings(R.drawable.sale, getString(R.string.referral_title)));*/
            /*accountSettingsList.add(new AccountSettings(R.drawable.ic_list_schedule, getString(R.string.later_title)));*/

            accountSettingsList.add(new AccountSettings(R.drawable.credit_card, getString(R.string.pre_select_payment)));

        }

        /*accountSettingsList.add(new AccountSettings(R.drawable.wallet, getString(R.string.history_payment)));*/
        accountSettingsList.add(new AccountSettings(R.drawable.help_circle, getString(R.string.my_help)));
        accountSettingsList.add(new AccountSettings(R.drawable.ic_power_off, getString(R.string.txt_logout)));

        return accountSettingsList;

    }

    @Override
    public void onResume() {
        super.onResume();
//        mMainActivity.currentFragment = Const.UserSettingsFragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (new PreferenceHelper(getActivity()).getLoginType().equals(Const.PatientService.PATIENT)){

            switch (position) {

                case 0:{

                    showPreSelectPaymentDialog();

                }
                break;

                case 1:
                    startActivity(new Intent(mMainActivity, HelpwebActivity.class));
                    break;

                case 2:
                    showLogoutDialog();
                    break;

            }

        }else if (new PreferenceHelper(getActivity()).getLoginType().equals(Const.NursingHomeService.NURSING_HOME) ||
                new PreferenceHelper(getActivity()).getLoginType().equals(Const.HospitalService.HOSPITAL)){

            switch (position){

                /*case 0:{
                    mMainActivity.addFragment(new HistoryPaymentFragment(), false, Const.HISTORY_PAYMENT_FRAGMENT, true);
                }
                break;*/

                case 0:
                    startActivity(new Intent(mMainActivity, HelpwebActivity.class));
                    break;

                case 1:
                    showLogoutDialog();
                    break;
            }

        }


    }

    private void showhelp() {
        final Dialog help_dialog = new Dialog(mMainActivity, R.style.DialogSlideAnim_leftright);
        help_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        help_dialog.setCancelable(true);
        help_dialog.setContentView(R.layout.help_layout);

        help_dialog.show();
    }


    private void showrefferal() {
        final Dialog refrel_dialog = new Dialog(mMainActivity, R.style.DialogThemeforview);
        refrel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        refrel_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_black)));
        refrel_dialog.setCancelable(true);
        refrel_dialog.setContentView(R.layout.refferalcode_layout);
        //  user = RealmController.with(this).getUser(Integer.valueOf(new PreferenceHelper(mMainActivity).getUserId()));
        final TextView refCode=refrel_dialog.findViewById(R.id.refCode);
        refCode.setText(new PreferenceHelper(mMainActivity).getReferralCode());
        ((ImageButton)refrel_dialog.findViewById(R.id.referral_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refrel_dialog.dismiss();
            }
        });

        ((ImageView)refrel_dialog.findViewById(R.id.twitter_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refrel_dialog.dismiss();
            }
        });

        if(new PreferenceHelper(mMainActivity).getReferralBONUS().isEmpty()){
            ((TextView) refrel_dialog.findViewById(R.id.txt_referl_earn)).setText("00");
        }else {

            ((TextView) refrel_dialog.findViewById(R.id.txt_referl_earn)).setText(new PreferenceHelper(mMainActivity).getReferralBONUS());

        }

        ((TextView)refrel_dialog.findViewById(R.id.gm_share)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app and earn by entering my referral code:" + refCode.getText().toString()+" while registering" + "https://play.google.com/store/apps/details?id=com.nikola.user");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);*/
                refrel_dialog.dismiss();

            }

        });

        ((ImageView)refrel_dialog.findViewById(R.id.fb_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent
                        .putExtra(Intent.EXTRA_TEXT, "Hey check out my app and earn by entering my referral code:" + refCode.getText().toString()+" while registering.  " + "https://play.google.com/store/apps/details?id=com.nikola.user");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mMainActivity,"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }
                refrel_dialog.dismiss();
            }
        });
        refrel_dialog.show();
    }

    private void showLogoutDialog() {

        dialog = new Dialog(mMainActivity, R.style.DialogThemeforview);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_logout);
        TextView btn_logout_yes = (TextView) dialog.findViewById(R.id.btn_logout_yes);

        btn_logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         /*       dialog.dismiss();
                new PreferenceHelper(mMainActivity).Logout();

                BaseFragment.drop_latlan = null;
                BaseFragment.pic_latlan = null;
                BaseFragment.s_address = "";
                BaseFragment.d_address = "";

                new PreferenceHelper(mMainActivity).Logout();
                Intent i = new Intent(mMainActivity, WelcomeActivity.class);
                startActivity(i);
                mMainActivity.finish();*/
                logoutApi();
                dialog.dismiss();
            }
        });

        TextView btn_logout_no = (TextView) dialog.findViewById(R.id.btn_logout_no);
        btn_logout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void logoutApi() {
        if (!EbizworldUtils.isNetworkAvailable(mMainActivity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), mMainActivity);
            return;
        }
        Commonutils.progressDialog_show(mMainActivity, getString(R.string.logout_txt));

        HashMap<String, String> map = new HashMap<String, String>();

        if (new PreferenceHelper(mMainActivity).getLoginType().equals(Const.PatientService.PATIENT)){

            map.put(Const.Params.URL, Const.ServiceType.LOGOUT_URL);

        }else if (new PreferenceHelper(mMainActivity).getLoginType().equals(Const.NursingHomeService.NURSING_HOME)){

            map.put(Const.Params.URL, Const.NursingHomeService.LOGOUT_URL);

        }else if (new PreferenceHelper(mMainActivity).getLoginType().equals(Const.HospitalService.HOSPITAL)){

            map.put(Const.Params.URL, Const.HospitalService.LOGOUT_URL);
        }

        map.put(Const.Params.ID, new PreferenceHelper(mMainActivity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(mMainActivity).getSessionToken());
        Log.d("asher", "logout map " + map.toString());
        new VolleyRequester(mMainActivity, Const.POST, map, Const.ServiceCode.LOGOUT, this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {

            case Const.ServiceCode.LOGOUT:
                EbizworldUtils.appLogInfo("HaoLS", "logout Response: " + response);
                EbizworldUtils.removeProgressDialog();

                try {

                    JSONObject job = new JSONObject(response);

                    if (job.getString("success").equals("true")) {

                        dialog.dismiss();

                        BaseFragment.drop_latlan = null;
                        BaseFragment.pic_latlan = null;
                        BaseFragment.s_address = "";
                        BaseFragment.d_address = "";

                        new PreferenceHelper(mMainActivity).Logout();

                        switch (new PreferenceHelper(mMainActivity).getLoginType()){

                            case Const.PatientService.PATIENT:{

                                RealmController.with(this).clearAllPatient();

                                /*Delete all CalendarReminder before logout*/
                                deleteAllEventsCalendar(mMainActivity);

                            }
                            break;

                            case Const.NursingHomeService.NURSING_HOME:{

                                RealmController.with(this).clearAllNurse();

                                /*Delete all CalendarReminder before logout*/
                                deleteAllEventsCalendar(mMainActivity);

                            }
                            break;

                            case Const.HospitalService.HOSPITAL:{

                                RealmController.with(this).clearAllHospital();

                                /*Delete all CalendarReminder before logout*/
                                deleteAllEventsCalendar(mMainActivity);

                            }
                            break;
                        }

                        Intent i = new Intent(mMainActivity, WelcomeActivity.class);
                        startActivity(i);
                        mMainActivity.finish();

                        EbizworldUtils.appLogDebug("HaoLS", "Logout succeeded");
                    } else {
                        /*String error_code = job.optString("error_code");
                        if (error_code.equals("104")) {
                            EbizworldUtils.showShortToast("You have logged in other device!", mMainActivity);
                            new PreferenceHelper(mMainActivity).Logout();
                            startActivity(new Intent(mMainActivity, WelcomeActivity.class));
                            mMainActivity.finish();

                        }*/

                        EbizworldUtils.appLogDebug("HaoLS", "Logout failed");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    EbizworldUtils.appLogDebug("HaoLS", "Logout failed " + e.toString());
                }
                break;
        }

    }

    @Override
    public void deleteAllEventsCalendar(Activity activity){

        List<CalendarReminder> calendarReminders = RealmController.with(this).getCalendarReminders();

        if (calendarReminders.size() > 0){

            for (CalendarReminder calendarReminder : calendarReminders){

                new CalendarReminderHelper(activity).deleteEvents(calendarReminder.getEventID());

            }

            RealmController.with(this).clearAllCalendarReminder();
        }
    }

    @Override
    public void showPreSelectPaymentDialog() {

        FragmentManager fragmentManager = ((AppCompatActivity) mMainActivity).getSupportFragmentManager();
        PreSelectPaymentDialog preSelectPaymentDialog = new PreSelectPaymentDialog();
        preSelectPaymentDialog.setCancelable(true);
        preSelectPaymentDialog.show(fragmentManager, Const.PRE_SELECT_PAYMENT_DIALOGFRAGMENT);

    }
}
