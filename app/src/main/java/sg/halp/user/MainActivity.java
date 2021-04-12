package sg.halp.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import sg.halp.user.Activity.HomeLoginActivity;
import sg.halp.user.Activity.ProfileHospital.ProfileHospitalActivity;
import sg.halp.user.Activity.ProfileNursingHome.ProfileNursingHomeActivity;
import sg.halp.user.Activity.ProfilePatientActivity;
import sg.halp.user.Fragment.Account.AccountFragment;
import sg.halp.user.Fragment.BaseFragment;
import sg.halp.user.Fragment.BillingInfo.BillingInfoFragment;
import sg.halp.user.Fragment.HistoryRideFragment;
import sg.halp.user.Fragment.HomeMapFragment;
import sg.halp.user.Fragment.RatingFragment;
import sg.halp.user.Fragment.ScheduleListFragment;
import sg.halp.user.Fragment.SearchPlaceFragment;
import sg.halp.user.Fragment.TravelMapFragment;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.RequestDetail;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.restAPI.CheckRequestStatusAPI;

public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener, BottomNavigationView.OnNavigationItemSelectedListener {

//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle drawerToggle;
    public String currentFragment = "";
    private Toolbar mainToolbar;
    private Bundle mbundle;
    private ParseContent pcontent;
    private AlertDialog gpsAlertDialog, internetDialog;
    private boolean isGpsDialogShowing = false, isRecieverRegistered = false, isNetDialogShowing = false;
    private boolean gpswindowshowing = false;
    AlertDialog.Builder gpsBuilder;
    private LocationManager manager;
//    private ImageButton bnt_menu;
    private Dialog load_dialog;
    private BottomNavigationView mBottomNavigationView;
    private BroadcastReceiver patientScheduleStart;

    private BroadcastReceiver scheduleReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            EbizworldUtils.appLogDebug("AmbulanceFCMService", "Broadcast received action " + intent.getAction());
            //Receive Intent from AmbulanceFCMService for Action Schedule
            if (intent.getAction().equals(Const.NotificationParams.SCHEDULE)){

                EbizworldUtils.appLogDebug("AmbulanceFCMService", intent.getExtras().getString(Const.NotificationParams.ACTION));

                Intent startActivityIntent = new Intent();
                startActivityIntent.setClassName(context.getPackageName(), MainActivity.class.getName());
                startActivityIntent.putExtra(Const.NotificationParams.ACTION, Const.NotificationParams.SCHEDULE);
                startActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                context.startActivity(startActivityIntent);
                EbizworldUtils.appLogDebug("AmbulanceFCMService", "Starting MainActivity from FCMScheduleReceiver");
            }

        }
    };

    private BroadcastReceiver accountLogoutReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null && intent.getStringExtra(Const.NotificationParams.TYPE_ACCOUNT_LOGOUT).equals(Const.NotificationParams.TYPE_ACCOUNT_LOGOUT)){

                EbizworldUtils.showShortToast("You have logged in other device!", MainActivity.this);
                new PreferenceHelper(MainActivity.this).Logout();
                startActivity(new Intent(MainActivity.this, HomeLoginActivity.class));
                MainActivity.this.finish();

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        if (!TextUtils.isEmpty(new PreferenceHelper(this).getLanguage())) {

            Locale myLocale = null;
            switch (new PreferenceHelper(this).getLanguage()) {
                case "":
                    myLocale = new Locale("en");
                    break;
                case "en":
                    myLocale = new Locale("en");

                    break;
                case "fr":
                    myLocale = new Locale("fr");
                    break;

            }


            Locale.setDefault(myLocale);
            Configuration config = new Configuration();
            config.locale = myLocale;
            this.getResources().updateConfiguration(config,
                    this.getResources().getDisplayMetrics());
        }

        setContentView(R.layout.activity_main);

        pcontent = new ParseContent(this);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        mainToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle(null);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (mBottomNavigationView != null){

            mBottomNavigationView.inflateMenu(R.menu.consumer_bottom_navigation_menu);
            mBottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
            addFragment(new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);

        }


/*        if (new PreferenceHelper(this).getLoginType().equals(Const.PatientService.PATIENT)){
            mbundle = savedInstanceState;

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;

            if (currentapiVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                    return;
                } else {

                    new CheckRequestStatusAPI(MainActivity.this, MainActivity.this)
                            .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

                }

            } else {

                new CheckRequestStatusAPI(MainActivity.this, MainActivity.this)
                        .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

            }

            EbizworldUtils.appLogDebug("asher", "phone main act " + new PreferenceHelper(this).getPhone()+" "+new PreferenceHelper(this).getLoginBy());
            if (!new PreferenceHelper(this).getLoginBy().equalsIgnoreCase(Const.MANUAL)) {
                EbizworldUtils.appLogDebug("asher", "phone main act1 " + new PreferenceHelper(this).getPhone());
                if (new PreferenceHelper(this).getPhone().isEmpty() || new PreferenceHelper(this).getPhone() == null) {
                    EbizworldUtils.appLogDebug("asher", "phone main act2 " + new PreferenceHelper(this).getPhone());
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.txt_update_number))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    openProfileActivity();
                                }
                            });

                    android.support.v7.app.AlertDialog alert = builder.create();
                    alert.show();
                }

            }

            if (mBottomNavigationView != null){

                mBottomNavigationView.inflateMenu(R.menu.consumer_bottom_navigation_menu);
                mBottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                addFragment(new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);

            }

            patientScheduleStart = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                if (intent != null){

                    if (intent.getExtras().getString(Const.NotificationParams.ACTION_SCHEDULE) != null &&
                            intent.getExtras().getString(Const.NotificationParams.ACTION_SCHEDULE).equals(Const.NotificationParams.TYPE_SCHEDULE_STARTED)){

                        new CheckRequestStatusAPI(MainActivity.this, MainActivity.this)
                                .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

                    }

                }

                }
            };

        }else if (new PreferenceHelper(this).getLoginType().equals(Const.NursingHomeService.NURSING_HOME)){

            if (mBottomNavigationView != null){

                mBottomNavigationView.inflateMenu(R.menu.nursing_home_bottom_navigation_menu);
                mBottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                //Check Intent received from FCMScheduleReceiver
                if (getIntent().getExtras() != null &&
                        getIntent().getExtras().getString(Const.NotificationParams.ACTION) != null &&
                        getIntent().getExtras().getString(Const.NotificationParams.ACTION).equals(Const.NotificationParams.SCHEDULE)){

                    EbizworldUtils.appLogDebug("AmbulanceFCMService", "MainActivity " + getIntent().getExtras().getString(Const.NotificationParams.ACTION));

                    addFragment(new ScheduleListFragment(), false, Const.SCHEDULE_LIST_FRAGMENT, true);

                }else {

                    addFragment(new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);
                }




            }
        }else if (new PreferenceHelper(this).getLoginType().equals(Const.HospitalService.HOSPITAL)){

            if (mBottomNavigationView != null){

                mBottomNavigationView.inflateMenu(R.menu.hospital_bottom_navigation_menu);
                mBottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
                //Check Intent received from FCMScheduleReceiver
                if (getIntent().getExtras() != null &&
                        getIntent().getExtras().getString(Const.NotificationParams.ACTION) != null &&
                        getIntent().getExtras().getString(Const.NotificationParams.ACTION).equals(Const.NotificationParams.SCHEDULE)){

                    EbizworldUtils.appLogDebug("AmbulanceFCMService", "MainActivity: " + getIntent().getExtras().getString(Const.NotificationParams.ACTION));

                    addFragment(new ScheduleListFragment(), false, Const.SCHEDULE_LIST_FRAGMENT, true);

                }else {

                    addFragment(new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);
                }

            }
        }*/

    }

    private void openProfileActivity() {

        switch (new PreferenceHelper(this).getLoginType()){

            case Const.PatientService.PATIENT:{

                Intent intent = new Intent(this, ProfilePatientActivity.class);
                startActivity(intent);
                finish();

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                Intent intent = new Intent(this, ProfileNursingHomeActivity.class);
                startActivity(intent);
                finish();

            }
            break;

            case Const.HospitalService.HOSPITAL:{

                Intent intent = new Intent(this, ProfileHospitalActivity.class);
                startActivity(intent);
                finish();

            }
            break;
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        /*drawerToggle.syncState();*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        /*drawerToggle.onConfigurationChanged(newConfig);*/
    }


    @Override
    public void onBackPressed() {
        //EbizworldUtils.appLogDebug("CurrentFragment", currentFragment);

        if (!currentFragment.equals(Const.HOME_MAP_FRAGMENT)) {

            if (currentFragment.equals(Const.REQUEST_FRAGMENT)){

                addFragment(new SearchPlaceFragment(), false, Const.SEARCH_FRAGMENT, true);

            }/*else if (currentFragment.equals(Const.NURSE_REGISTER_SCHEDULE_FRAGMENT)){

                addFragment(new ScheduleListFragment(), false, Const.SCHEDULE_LIST_FRAGMENT, true);

            }*/else {

                addFragment(new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);
                mBottomNavigationView.getMenu().findItem(R.id.action_home).setChecked(true);
            }

        } else {

            if (!isFinishing()) {

                openExitDialog();

            }

        }

    }

    private void openExitDialog() {

        final Dialog exit_dialog = new Dialog(this, R.style.DialogSlideAnim_leftright);
        exit_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        exit_dialog.setCancelable(false);
        exit_dialog.setContentView(R.layout.dialog_logout);

        TextView title = (TextView) exit_dialog.findViewById(R.id.logout_title);
        title.setText(getResources().getString(R.string.dialog_exit_caps));

        TextView content = (TextView) exit_dialog.findViewById(R.id.logout_content);
        content.setText(getResources().getString(R.string.dialog_exit_text));

        TextView btn_yes = (TextView) exit_dialog.findViewById(R.id.btn_logout_yes);
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exit_dialog.dismiss();
                finishAffinity();

            }
        });

        TextView btn_no = (TextView) exit_dialog.findViewById(R.id.btn_logout_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exit_dialog.dismiss();

            }
        });

        exit_dialog.show();
    }

    private void ShowGpsDialog() {

        isGpsDialogShowing = true;

        gpsBuilder = new AlertDialog.Builder(
                this);


        gpsBuilder.setCancelable(false);
        gpsBuilder
                .setTitle(getResources().getString(R.string.txt_gps_off))
                .setMessage(getResources().getString(R.string.txt_gps_msg))
                .setPositiveButton(getResources().getString(R.string.txt_enable),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                Intent intent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                removeGpsDialog();
                            }
                        })

                .setNegativeButton(getResources().getString(R.string.txt_exit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeGpsDialog();
                                finishAffinity();
                            }
                        });
        gpsAlertDialog = gpsBuilder.create();

        gpsAlertDialog.show();

    }


    private void removeGpsDialog() {
        if (gpsAlertDialog != null && gpsAlertDialog.isShowing()) {
            gpsAlertDialog.dismiss();
            isGpsDialogShowing = false;
            gpsAlertDialog = null;


        }
    }

    public void addFragment(Fragment fragment, boolean addToBackStack,
                            String tag, boolean isAnimate) {

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (isAnimate) {
            ft.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_left, R.anim.slide_in_left,
                    R.anim.slide_out_right);

        }

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }

        ft.replace(R.id.content_frame, fragment, tag);
        currentFragment = tag;
        ft.commitAllowingStateLoss();

        if(tag.equals(Const.HOME_MAP_FRAGMENT) ||
                tag.equals(Const.SCHEDULE_LIST_FRAGMENT) ||
                tag.equals(Const.HISTORY_FRAGMENT) ||
                tag.equals(Const.ACCOUNT_FRAGMENT)){

            if (mBottomNavigationView != null)
                mBottomNavigationView.setVisibility(View.VISIBLE);

        }else {

            if (mBottomNavigationView != null)
                mBottomNavigationView.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // do something else
            if (isGpsDialogShowing) {
                return;
            }
            ShowGpsDialog();
        } else {
            removeGpsDialog();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isRecieverRegistered){

            if (new PreferenceHelper(this).getLoginType().equals(Const.NursingHomeService.NURSING_HOME) ||
                    new PreferenceHelper(this).getLoginType().equals(Const.HospitalService.HOSPITAL)){

                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(scheduleReceiver, new IntentFilter(Const.NotificationParams.SCHEDULE)); //Register Broadcast to receive FCM schedule start
                EbizworldUtils.appLogDebug("AmbulanceFCMService", "MainActivity: Register FCMScheduleReceiver");

            }

            registerReceiver(internetConnectionReciever, new IntentFilter(
                    "android.net.conn.CONNECTIVITY_CHANGE"));

            registerReceiver(GpsChangeReceiver, new IntentFilter(
                    LocationManager.PROVIDERS_CHANGED_ACTION));

            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(accountLogoutReceiver, new IntentFilter(Const.NotificationParams.TYPE_ACCOUNT_LOGOUT));

            if (patientScheduleStart != null){

                LocalBroadcastManager.getInstance(getApplicationContext())
                        .registerReceiver(patientScheduleStart, new IntentFilter(Const.NotificationParams.ACTION_SCHEDULE));

            }
            isRecieverRegistered = true;

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isRecieverRegistered) {

            unregisterReceiver(GpsChangeReceiver);
            unregisterReceiver(internetConnectionReciever);
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(accountLogoutReceiver);

            if (new PreferenceHelper(this).getLoginType().equals(Const.NursingHomeService.NURSING_HOME) ||
                    new PreferenceHelper(this).getLoginType().equals(Const.HospitalService.HOSPITAL)){

                LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(scheduleReceiver); //Unregister Broadcast to receive FCM schedule start
                EbizworldUtils.appLogDebug("AmbulanceFCMService", "MainActivity: Unregister FCMScheduleReceiver");

            }

            if (patientScheduleStart != null){

                LocalBroadcastManager.getInstance(getApplicationContext())
                        .unregisterReceiver(patientScheduleStart);

            }

            isRecieverRegistered = false;

        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public BroadcastReceiver internetConnectionReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo activeWIFIInfo = connectivityManager
                    .getNetworkInfo(connectivityManager.TYPE_WIFI);

            if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
                removeInternetDialog();
            } else {
                if (isNetDialogShowing) {
                    return;
                }
                showInternetDialog();
            }
        }
    };

    private void removeInternetDialog(){
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
            isNetDialogShowing = false;
            internetDialog = null;

        }
    }

    private void showInternetDialog() {

        isNetDialogShowing = true;
        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(
                MainActivity.this);
        internetBuilder.setCancelable(false);
        internetBuilder
                .setTitle(getString(R.string.dialog_no_internet))
                .setMessage(getString(R.string.dialog_no_inter_message))
                .setPositiveButton(getString(R.string.dialog_enable_3g),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                Intent intent = new Intent(
                                        android.provider.Settings.ACTION_SETTINGS);
                                startActivity(intent);
                                removeInternetDialog();
                            }
                        })
                .setNeutralButton(getString(R.string.dialog_enable_wifi),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // User pressed Cancel button. Write
                                // Logic Here
                                startActivity(new Intent(
                                        Settings.ACTION_WIFI_SETTINGS));
                                removeInternetDialog();
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_exit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeInternetDialog();
                            }
                        });
        internetDialog = internetBuilder.create();
        internetDialog.show();
    }


    public BroadcastReceiver GpsChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


        if (intent.getAction() != null) {

            final LocationManager manager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // do something

                removeGpsDialog();
            } else {
                // do something else
                if (isGpsDialogShowing) {
                    return;
                }

                ShowGpsDialog();
            }

        }
        }
    };

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.CHECKREQUEST_STATUS:

                EbizworldUtils.appLogInfo("HaoLS", "check req status " + response);

                if (response != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            Bundle bundle = new Bundle();
                            RequestDetail requestDetail = null;

                            switch (new PreferenceHelper(this).getLoginType()){

                                case Const.PatientService.PATIENT:{

                                    requestDetail = new ParseContent(this).parseRequestStatusNormal(response);

                                }
                                break;

                                case Const.NursingHomeService.NURSING_HOME:{

                                    requestDetail = new ParseContent(this).parseRequestStatusSchedule(response);

                                }

                                case Const.HospitalService.HOSPITAL:{

                                    requestDetail = new ParseContent(this).parseRequestStatusSchedule(response);

                                }

                            }
                            /*List<RequestDetail> requestDetails = new ParseContent(activity).*/
                            TravelMapFragment travelfragment = new TravelMapFragment();
                            if (requestDetail == null) {
                                return;
                            }

                            EbizworldUtils.appLogDebug("Status", "onTaskCompleted:" + requestDetail.getTripStatus());

                            switch (requestDetail.getTripStatus()) {
                                case Const.NO_REQUEST:
                                    new PreferenceHelper(this).clearRequestData();

                                    break;

                                case Const.IS_ACCEPTED:{
                                    bundle.putSerializable(Const.REQUEST_DETAIL,
                                            requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS,
                                            Const.IS_ACCEPTED);
                                    if (!this.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {
                                        travelfragment.setArguments(bundle);
                                        this.addFragment(travelfragment, false, Const.TRAVEL_MAP_FRAGMENT,
                                                true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                case Const.IS_DRIVER_DEPARTED:{
                                    bundle.putSerializable(Const.REQUEST_DETAIL,
                                            requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS,
                                            Const.IS_ACCEPTED);
                                    if (!this.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {

                                        travelfragment.setArguments(bundle);
                                        this.addFragment(travelfragment, false, Const.TRAVEL_MAP_FRAGMENT,
                                                true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                case Const.IS_DRIVER_ARRIVED:{
                                    bundle.putSerializable(Const.REQUEST_DETAIL,
                                            requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS,
                                            Const.IS_ACCEPTED);
                                    if (!this.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {

                                        travelfragment.setArguments(bundle);
                                        this.addFragment(travelfragment, false, Const.TRAVEL_MAP_FRAGMENT,
                                                true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                case Const.IS_DRIVER_TRIP_STARTED:{

                                    bundle.putSerializable(Const.REQUEST_DETAIL,
                                            requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS,
                                            Const.IS_ACCEPTED);
                                    if (!this.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {

                                        travelfragment.setArguments(bundle);
                                        this.addFragment(travelfragment, false, Const.TRAVEL_MAP_FRAGMENT,
                                                true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";

                                }
                                break;

                                case Const.IS_DRIVER_TRIP_ENDED:{

                                    if (!this.currentFragment.equals(Const.BILLING_INFO_FRAGMENT) && !this.isFinishing()){

                                        BillingInfoFragment billingInfoFragment = new BillingInfoFragment();
                                        billingInfoFragment.setArguments(bundle);

                                        this.addFragment(billingInfoFragment, false, Const.BILLING_INFO_FRAGMENT, true);

                                    }
                                }
                                break;

                                case Const.IS_DRIVER_RATED:{

                                    bundle.putSerializable(Const.REQUEST_DETAIL, requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS, Const.IS_ACCEPTED);

                                    if (!currentFragment.equals(Const.RATING_FRAGMENT)) {

                                        RatingFragment feedbackFragment = new RatingFragment();
                                        feedbackFragment.setArguments(bundle);
                                        addFragment(feedbackFragment, false, Const.RATING_FRAGMENT, true);

                                    }

                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                default:
                                    break;

                            }

                        }else {

                            if (jsonObject.has("error_code")){

                                if (jsonObject.getInt("error_code") == 104){

                                    EbizworldUtils.showShortToast("You have logged in other device!", MainActivity.this);

                                    if (jsonObject.has("error")){
                                        EbizworldUtils.appLogDebug("HaoLS", jsonObject.getString("error"));
                                    }

                                    new PreferenceHelper(MainActivity.this).Logout();
                                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                                    MainActivity.this.finish();

                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
                break;

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //Log.d("mahesh","coming to permission");
                    new CheckRequestStatusAPI(MainActivity.this, MainActivity.this)
                            .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if (currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT) ||
                currentFragment.equals(Const.REQUEST_FRAGMENT) ||
                currentFragment.equals(Const.NURSE_REGISTER_SCHEDULE_FRAGMENT) ||
                currentFragment.equals(Const.BILLING_INFO_FRAGMENT)){

            Toast.makeText(this, getResources().getString(R.string.navigationbottom_warning), Toast.LENGTH_SHORT).show();

        }else {

            switch (menuItem.getItemId()){

                case R.id.action_home:{

                    addFragment(new HomeMapFragment(), false, Const.HOME_MAP_FRAGMENT, true);

                }
                return true;

                case R.id.action_history:{

                    addFragment(new HistoryRideFragment(), false, Const.HISTORY_FRAGMENT, true);

                }

                return true;

                /*case R.id.action_payment:{

                    addFragment(new AddPaymentFragment(), false, Const.PAYMENT_FRAGMENT, true);
                }
                return true;*/

                case R.id.action_account:{

                    addFragment(new AccountFragment(), false, Const.ACCOUNT_FRAGMENT, true);

                }
                return true;

                case R.id.action_schedule:{

                    addFragment(new ScheduleListFragment(), false, Const.SCHEDULE_LIST_FRAGMENT, true);

                }
                return true;
            }

        }
        return false;
    }

}

