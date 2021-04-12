package sg.halp.user.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aurelhubert.simpleratingbar.SimpleRatingBar;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sg.halp.user.Adapter.AdsAdapter;
import sg.halp.user.Adapter.AmbulanceOperatorHorizontalAdapter;
import sg.halp.user.Adapter.PlacesAutoCompleteAdapter;
import sg.halp.user.Dialog.PatientSchedule.PatientScheduleDialog;
import sg.halp.user.Fragment.BillingInfo.BillingInfoFragment;
import sg.halp.user.HttpRequester.VolleyRequester;
import sg.halp.user.Interface.AdapterCallback;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Location.LocationHelper;
import sg.halp.user.Models.AdsList;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.NearByDrivers;
import sg.halp.user.Models.RequestDetail;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.Utils.ParseContent;
import sg.halp.user.Utils.PreferenceHelper;
import sg.halp.user.WelcomeActivity;
import sg.halp.user.restAPI.CheckRequestStatusAPI;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 1/5/2017.
 */

public class HomeMapFragment extends BaseFragment
        implements
        LocationHelper.OnLocationReceived,
        AsyncTaskCompleteListener,
        OnMapReadyCallback,
        AdapterCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveListener {

    private String TAG = HomeMapFragment.class.getSimpleName();
    private GoogleMap mGoogleMap;
    private Bundle mBundle;
    SupportMapFragment HomemapFragment;
    private View view;
    private LocationHelper locHelper;
    private Location myLocation;
    private LatLng latlong;
    private static final int DURATION = 1500;
    private TextView tv_current_location, tv_time_date, tv_total_dis, tv_estimate_fare;

    private TextView mTv_ambulance_operator_notice;
    private Button mBtn_another_ambulance_operator;
    private LinearLayout mAny_ambulance_operator_group;
    private View mView_select;
    private boolean isAnyAmbulanceOperatorGroup = false;
    private boolean isExist = false;

    private View view_between_schedule_current_location;
    private static Marker pickup_marker, drop_marker, my_marker;
    MarkerOptions pickup_opt;
    /*private FloatingActionButton btn_floating_hourly, btn_floating_airport, btn_floating_bolt;*/

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private AutoCompleteTextView et_sch_destination_address, et_sch_source_address;
    private PlacesAutoCompleteAdapter mPlacesAutoCompleteAdapter;
    private LatLng sourceLatLng, destinationLatLng;
    private String sourceAddress, destinationAddress;
    private String base_price = "", min_price = "", booking_fee = "", currency = "", distance_unit = "", ambulance_price = "";

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionGranted = true;

    private static LinearLayout layout_search;
    AmbulanceOperatorHorizontalAdapter ambulanceOperatorHorizontalAdapter;
    private boolean s_click = false, d_click = false;
    public static ImageButton btn_mylocation;

    private ArrayList<AmbulanceOperator> mAmbulanceOperatorsMain, mAmbulanceOperatorsTemp;

    private ArrayList<NearByDrivers> driverslatlngs = new ArrayList<>();

    private HashMap<Marker, Integer> markermap = new HashMap<>();
    private ArrayList<Marker> mMarkersMap = new ArrayList<>();
    private ArrayList<Marker> markers = new ArrayList<Marker>();
    private int marker_position;

    Handler providerhandler = new Handler();
    public static String pickup_add = "";
    private ImageButton btn_add_schedule;
    private LatLng sch_pic_latLng, sch_drop_latLng;
    private String ambulance_type;
    private ProgressBar pbfareProgress;
    private ImageView bottomSheetArrowImage;
    private RecyclerView adsRecyclerView;
    private AdsAdapter adsAdapter;
    private List<AdsList> adsLists;

    AsyncTaskCompleteListener asyncTaskCompleteListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBundle = savedInstanceState;
        mAmbulanceOperatorsMain = new ArrayList<AmbulanceOperator>();
        mAmbulanceOperatorsTemp = new ArrayList<>();
        /*providerhandler = new Handler();*/

        asyncTaskCompleteListener = (AsyncTaskCompleteListener) this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.home_map_fragment, container,
                false);

        if (getActivity() != null){

            ((AppCompatActivity) getActivity()).getSupportActionBar();
        }

        tv_current_location = (TextView) view.findViewById(R.id.tv_current_location);
        layout_search = (LinearLayout) view.findViewById(R.id.layout_search);
        btn_mylocation = (ImageButton) view.findViewById(R.id.btn_mylocation);
        btn_add_schedule = (ImageButton) view.findViewById(R.id.btn_add_schedule);
        view_between_schedule_current_location = (View) view.findViewById(R.id.view_between_schedule_current_location);

        HomemapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.home_map);
        btn_mylocation.setOnClickListener(this);
        tv_current_location.setOnClickListener(this);
        tv_current_location.setSelected(true);

        btn_add_schedule.setOnClickListener(this);

        if (HomemapFragment != null) {

            HomemapFragment.getMapAsync(this);

        }

        providerhandler.postDelayed(runnable, 30000);

        switch (new PreferenceHelper(activity).getLoginType()){

            case Const.PatientService.PATIENT:{

                new CheckRequestStatusAPI(activity, HomeMapFragment.this)
                        .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

            }
            break;

            case Const.NursingHomeService.NURSING_HOME:{

                new CheckRequestStatusAPI(activity, HomeMapFragment.this)
                        .checkRequestStatusForNursingHome(Const.ServiceCode.CHECKREQUEST_STATUS);
            }
            break;

            case Const.HospitalService.HOSPITAL:{

                new CheckRequestStatusAPI(activity, HomeMapFragment.this)
                        .checkRequestStatusForHospital(Const.ServiceCode.CHECKREQUEST_STATUS);
            }
            break;
        }

        return view;
    }

    private void getAds() {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.ADVERTISEMENTS);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());

        EbizworldUtils.appLogDebug(TAG, "adsList " + map);

        new VolleyRequester(activity, Const.POST, map, Const.ServiceCode.ADVERTISEMENTS, this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        EbizworldUtils.removeProgressDialog();

        if (mGoogleMap != null) {

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                mLocationPermissionGranted = false;

                return;
            }
            getUISettingsGoogleMap(); //Customize method
            setMapStyle(); //Customize style map
            /*getDeviceLocation();*/

             mGoogleMap.setMyLocationEnabled(true);
            //mGoogleMap.setMaxZoomPreference(17);
            //  EbizworldUtils.removeLoader();

            /*MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(activity, R.raw.night_modemap);
            mGoogleMap.setMapStyle(style);*/

//            Info driver windows
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                                               @Override
                                               public View getInfoWindow(Marker marker) {
                                                   View view = null;

                                                   if (markermap.get(marker) != -1 && markermap.get(marker) != -2) {

                                                       view = activity.getLayoutInflater().inflate(R.layout.driver_info_window, null);

                                                       TextView txt_driver_name = (TextView) view.findViewById(R.id.driver_name);

                                                       if (driverslatlngs.size() > 0) {
                                                           txt_driver_name.setText(driverslatlngs.get(marker_position).getDriver_name());
                                                           SimpleRatingBar driver_rate = (SimpleRatingBar) view.findViewById(R.id.driver_rate);
                                                           driver_rate.setRating(driverslatlngs.get(marker_position).getDriver_rate());

                                                       }

                                                   }

                                                   return view;

                                               }

                                               @Override
                                               public View getInfoContents(Marker marker) {
                                                   // Getting view from the layout file infowindowlayout.xml
                                                   return null;
                                               }
                                           }
            );

        }

        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);

    }

    private void moveCamera(LatLng latLng, float zoom){

        Log.d(TAG, "moveCamera: moving to the latitude " + latLng.latitude + " and longitude " + latLng.longitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }

    private void getLocationPermission(){

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mLocationPermissionGranted = true;

        }else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, Const.PermissionRequestCode.ACCESS_LOCATION);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionGranted = false;

        switch (requestCode){

            case Const.PermissionRequestCode.ACCESS_LOCATION:{

//                If permission cancelled, array will empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    mLocationPermissionGranted = true;

                    if (mGoogleMap != null){

                        mGoogleMap.setMyLocationEnabled(true);

                    }

                }

            }
            break;

        }
    }

    //    Set onMarkerClick
    @Override
    public boolean onMarkerClick(Marker marker) {

        if (markermap.get(marker) != -1 && markermap.get(marker) != -2) {

            marker_position = markermap.get(marker);

        } else if (markermap.get(marker) == -1) {

            SearchPlaceFragment searcfragment = new SearchPlaceFragment();
            Bundle mbundle = new Bundle();
            mbundle.putString("pickup_address", pickup_add);
            searcfragment.setArguments(mbundle);
            activity.addFragment(searcfragment, false, Const.SEARCH_FRAGMENT, true);

        }

        return false;
    }

    //    Settings UI for GoogleMap
    public void getUISettingsGoogleMap(){

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);

    }

//    Set style for map
    public void setMapStyle(){

        if (getActivity() != null){

            try {
                boolean success = mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.style_map));

                if (!success){

                    EbizworldUtils.appLogError(TAG, "MapStyle: parse map style failed");
                }
            }catch (Resources.NotFoundException e){

                EbizworldUtils.appLogError(TAG, e.getMessage());
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null){

            try {
                MapsInitializer.initialize(getActivity());
            } catch (Exception e) {

            }

            locHelper = new LocationHelper(activity);
            locHelper.setLocationReceivedLister(this);
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

            if (getView().getWindowToken() != null){

                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

            }

            if (new PreferenceHelper(activity).getRequestId() == Const.NO_REQUEST) {
                startgetProvider();
            }

        }

    }


    @Override
    public void onLocationReceived(final LatLng latlong) {
        if (latlong != null) {
           /* mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,
                    16));*/

        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


    @Override
    public void onLocationReceived(Location location) {
        if (location != null) {
            // drawTrip(latlong);
            myLocation = location;
            LatLng latLang = new LatLng(location.getLatitude(),
                    location.getLongitude());
            latlong = latLang;

        }

    }

    @Override
    public void onConntected(Bundle bundle) {

    }

    @Override
    public void onConntected(Location location) {

        if (location != null && null != mGoogleMap) {

            mGoogleMap.clear();

            final LatLng currentlatLang = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentlatLang,
                    16));

            BaseFragment.pic_latlan = currentlatLang;
            getCompleteAddressString(currentlatLang.latitude, currentlatLang.longitude);

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_current_location:
                SearchPlaceFragment searcfragment = new SearchPlaceFragment();
                Bundle mbundle = new Bundle();
                mbundle.putString("pickup_address", pickup_add);
                searcfragment.setArguments(mbundle);
                activity.addFragment(searcfragment, false, Const.SEARCH_FRAGMENT, true);
                break;
            case R.id.btn_mylocation:
                if (mGoogleMap != null && latlong != null) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong,
                            16));
                }
                break;

            case R.id.btn_add_schedule:{

                if (new PreferenceHelper(activity).getLoginType().equals(Const.PatientService.PATIENT)){

                    if (null != pickup_add && null != mAmbulanceOperatorsTemp) {

                        FragmentManager fragmentManager = ((AppCompatActivity)activity).getSupportFragmentManager();
                        PatientScheduleDialog patientScheduleDialog = new PatientScheduleDialog();
                        patientScheduleDialog.show(fragmentManager, Const.PATIENT_SCHEDULE_DIALOGFRAGMENT);


                    } else {

                        EbizworldUtils.showShortToast(getResources().getString(R.string.txt_error), activity);

                    }

                }else if (new PreferenceHelper(activity).getLoginType().equals(Const.NursingHomeService.NURSING_HOME) ||
                        new PreferenceHelper(activity).getLoginType().equals(Const.HospitalService.HOSPITAL)){

                    activity.addFragment(new HospitalNursingScheduleRequestFragment(), false, Const.NURSE_REGISTER_SCHEDULE_FRAGMENT, true);

                }

            }
                break;
        }

    }

    @Override
    public void onMethodCallback(String id, String taxitype, String taxi_price_distance, String taxi_price_min, String taxiimage, String taxi_seats, String basefare) {
        ambulance_type = id;
        if (null != sch_pic_latLng && null != sch_drop_latLng) {
            findDistanceAndTime(sch_pic_latLng, sch_drop_latLng);
            /*showfareestimate(taxitype, taxi_price_distance, taxi_price_min, taxiimage, taxi_seats, basefare);*/
        } else {
            EbizworldUtils.showShortToast(getResources().getString(R.string.txt_drop_pick_error), activity);
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {

        if (null != my_marker) {
            my_marker.remove();
        }

        MarkerOptions pickup_opt = new MarkerOptions();
        pickup_opt.position(latLng);
        pickup_opt.icon(BitmapDescriptorFactory
                .fromBitmap(getMarkerBitmapFromView("---")));

        if (null != mGoogleMap) {
            my_marker = mGoogleMap.addMarker(pickup_opt);
            markermap.put(my_marker, -1);
            mMarkersMap.add(my_marker); //Testing
            BaseFragment.pic_latlan = latLng;
            getCompleteAddress(latLng.latitude, latLng.longitude);
        }


    }

    @Override
    public void onCameraMove() {

    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        activity.currentFragment = Const.HOME_MAP_FRAGMENT;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopCheckingforproviders();
    }

    private void startgetProvider() {
        startCheckProviderTimer();
        //Log.e("mahi", "req_id" + new PreferenceHelper(activity).getRequestId());
    }

    private void startCheckProviderTimer() {
        providerhandler.postDelayed(runnable, 30000);
    }

    private void stopCheckingforproviders() {
        if (providerhandler != null) {
            providerhandler.removeCallbacks(runnable);

            // Log.d("mahi", "stop provider handler");
        }
    }

    Runnable runnable = new Runnable() {
        public void run() {

            /*switch (new PreferenceHelper(activity).getLoginType()){

                case Const.PatientService.PATIENT:{

                    new ProviderAPI(activity, HomeMapFragment.this)
                            .getAllProvidersForPatient(latlong,Const.ServiceCode.GET_PROVIDERS);

                    new CheckRequestStatusAPI(activity, HomeMapFragment.this)
                            .checkRequestStatusForPatient(Const.ServiceCode.CHECKREQUEST_STATUS);

                }
                break;

                case Const.NursingHomeService.NURSING_HOME:{

                    new ProviderAPI(activity, HomeMapFragment.this)
                            .getAllProvidersForNursingHome(latlong,Const.ServiceCode.GET_PROVIDERS);

                    new CheckRequestStatusAPI(activity, HomeMapFragment.this)
                            .checkRequestStatusForNursingHome(Const.ServiceCode.CHECKREQUEST_STATUS);
                }
                break;

                case Const.HospitalService.HOSPITAL:{

                    new ProviderAPI(activity, HomeMapFragment.this)
                            .getAllProvidersForHospital(latlong,Const.ServiceCode.GET_PROVIDERS);

                    new CheckRequestStatusAPI(activity, HomeMapFragment.this)
                            .checkRequestStatusForHospital(Const.ServiceCode.CHECKREQUEST_STATUS);
                }
                break;
            }*/
            providerhandler.postDelayed(this, 30000);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("mahi", "on destory view is calling");
        SupportMapFragment f = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.home_map);
        if (f != null) {
            try {
                getFragmentManager().beginTransaction().remove(f).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

          /*TO clear all views */
        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.content_frame);
        mContainer.removeAllViews();

        mGoogleMap = null;

    }

    @Override
    public void onDestroy() {
        stopCheckingforproviders();
        super.onDestroy();

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {

            case Const.ServiceCode.ADDRESS_API_BASE:
                if (null != response) {
                    try {
                        JSONObject job = new JSONObject(response);
                        JSONArray jarray = job.optJSONArray("results");
                        JSONObject locObj = jarray.getJSONObject(0);
                        pickup_add = locObj.optString("formatted_address");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case Const.ServiceCode.GOOGLE_ADDRESS_API:
                if (null != response) {
                    try {
                        JSONObject job = new JSONObject(response);
                        JSONArray jarray = job.optJSONArray("results");
                        JSONObject locObj = jarray.getJSONObject(0);
                        pickup_add = locObj.optString("formatted_address");
                        tv_current_location.setText(pickup_add);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (null != my_marker && null != mGoogleMap)
                                    my_marker.setIcon((BitmapDescriptorFactory
                                            .fromBitmap(getMarkerBitmapFromView(pickup_add))));
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case Const.ServiceCode.CHECKREQUEST_STATUS:

                EbizworldUtils.appLogInfo("HaoLS", "check req status " + response);

                if (response != null) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            Bundle bundle = new Bundle();
                            RequestDetail requestDetail = null;

                            if (new PreferenceHelper(activity).getLoginType().equals(Const.PatientService.PATIENT)){

                                requestDetail = new ParseContent(activity).parseRequestStatusNormal(response);

                            }else if (new PreferenceHelper(activity).getLoginType().equals(Const.NursingHomeService.NURSING_HOME) ||
                                    new PreferenceHelper(activity).getLoginType().equals(Const.HospitalService.HOSPITAL)){

                                requestDetail = new ParseContent(activity).parseRequestStatusSchedule(response);
                            }
                            /*List<RequestDetail> requestDetails = new ParseContent(activity).*/
                            TravelMapFragment travelMapFragment = new TravelMapFragment();

                            if (requestDetail == null) {
                                return;
                            }

                            EbizworldUtils.appLogDebug("Status", "onTaskCompleted:" + requestDetail.getTripStatus());

                            switch (requestDetail.getTripStatus()) {

                                case Const.NO_REQUEST:
                                    new PreferenceHelper(activity).clearRequestData();
                                    // startgetProvider();


                                    break;

                                case Const.IS_ACCEPTED:{
                                    bundle.putSerializable(Const.REQUEST_DETAIL,
                                            requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS,
                                            Const.IS_ACCEPTED);
                                    if (!activity.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {
                                        stopCheckingforproviders();

                                        travelMapFragment.setArguments(bundle);
                                        activity.addFragment(travelMapFragment, false, Const.TRAVEL_MAP_FRAGMENT,
                                                true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                case Const.IS_DRIVER_DEPARTED:{

                                    bundle.putSerializable(Const.REQUEST_DETAIL, requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS, Const.IS_ACCEPTED);
                                    if (!activity.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {

                                        stopCheckingforproviders();

                                        travelMapFragment.setArguments(bundle);
                                        activity.addFragment(travelMapFragment, false, Const.TRAVEL_MAP_FRAGMENT, true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                case Const.IS_DRIVER_ARRIVED:{
                                    bundle.putSerializable(Const.REQUEST_DETAIL, requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS, Const.IS_ACCEPTED);

                                    if (!activity.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {

                                        stopCheckingforproviders();

                                        travelMapFragment.setArguments(bundle);
                                        activity.addFragment(travelMapFragment, false, Const.TRAVEL_MAP_FRAGMENT, true);

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
                                    if (!activity.currentFragment.equals(Const.TRAVEL_MAP_FRAGMENT)) {
                                        stopCheckingforproviders();

                                        travelMapFragment.setArguments(bundle);
                                        activity.addFragment(travelMapFragment, false, Const.TRAVEL_MAP_FRAGMENT,
                                                true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";

                                }
                                break;

                                case Const.IS_DRIVER_TRIP_ENDED:{

                                    if (!activity.currentFragment.equals(Const.BILLING_INFO_FRAGMENT) && !activity.isFinishing()){

                                        stopCheckingforproviders();

                                        BillingInfoFragment billingInfoFragment = new BillingInfoFragment();
                                        billingInfoFragment.setArguments(bundle);

                                        activity.addFragment(billingInfoFragment, false, Const.BILLING_INFO_FRAGMENT, true);

                                    }
                                }
                                break;

                                case Const.IS_DRIVER_RATED:{

                                    bundle.putSerializable(Const.REQUEST_DETAIL,
                                            requestDetail);
                                    bundle.putInt(Const.DRIVER_STATUS,
                                            Const.IS_ACCEPTED);

                                    if (!activity.currentFragment.equals(Const.RATING_FRAGMENT)) {

                                        RatingFragment feedbackFragment = new RatingFragment();
                                        feedbackFragment.setArguments(bundle);
                                        activity.addFragment(feedbackFragment, false, Const.RATING_FRAGMENT, true);

                                    }


                                    BaseFragment.drop_latlan = null;
                                    BaseFragment.pic_latlan = null;
                                    BaseFragment.s_address = "";
                                    BaseFragment.d_address = "";
                                }
                                break;

                                case Const.IS_PAID:{

                                    if (!activity.currentFragment.equals(Const.RATING_FRAGMENT) && !activity.isFinishing()){

                                        bundle.putSerializable(Const.REQUEST_DETAIL,
                                                requestDetail);
                                        RatingFragment ratingFragment = new RatingFragment();
                                        ratingFragment.setArguments(bundle);

                                        activity.addFragment(ratingFragment, false, Const.BILLING_INFO_FRAGMENT, true);

                                    }

                                }
                                break;

                                default:
                                    break;

                            }

                        }else {

                            if (jsonObject.has("error_code")){

                                if (jsonObject.getInt("error_code") == 104){

                                    EbizworldUtils.showShortToast("You have logged in other device!", activity);

                                    if (jsonObject.has("error")){
                                        EbizworldUtils.appLogDebug("HaoLS", jsonObject.getString("error"));
                                    }

                                    new PreferenceHelper(activity).Logout();
                                    startActivity(new Intent(activity, WelcomeActivity.class));
                                    activity.finish();

                                }
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;

            case Const.ServiceCode.GET_PROVIDERS:
                EbizworldUtils.appLogInfo("HaoLS", "providers: " + response);

                if (response != null) {
                    try {
                        if (mGoogleMap != null) {

                            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);

                        }
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("success").equals("true")) {
                            driverslatlngs.clear();
                            JSONArray jarray = jsonObject.getJSONArray("providers");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject driversObj = jarray.getJSONObject(i);
                                NearByDrivers drivers = new NearByDrivers();
                                drivers.setLatlan(new LatLng(Double.valueOf(driversObj.getString("latitude")), Double.valueOf(driversObj.getString("longitude"))));
                                drivers.setId(driversObj.getString("id"));
                                drivers.setDriver_name(driversObj.getString("first_name"));
                                if (driversObj.getString("rating").equals("0")) {
                                    drivers.setDriver_rate(0);
                                } else {
                                    drivers.setDriver_rate(Integer.valueOf(driversObj.getString("rating").charAt(0)));
                                }
                                drivers.setDriver_distance(driversObj.getString("distance"));
                                driverslatlngs.add(drivers);
                            }

                        } else if (jsonObject.getString("success").equals("false")){

                            if (jsonObject.has("error_code")){

                                if (jsonObject.getInt("error_code") == 104){

                                    stopCheckingforproviders();
                                    EbizworldUtils.showShortToast("You have logged in other device!", activity);

                                    if (jsonObject.has("error")){
                                        EbizworldUtils.appLogDebug("HaoLS", jsonObject.getString("error"));
                                    }

                                    new PreferenceHelper(activity).Logout();
                                    startActivity(new Intent(activity, WelcomeActivity.class));
                                    activity.finish();

                                }
                            }

                            EbizworldUtils.appLogDebug("HaoLS", "Get providers failed");

                        }


                        if (driverslatlngs.size() > 0) {

                            for (Marker marker : markers) {
                                marker.remove();
                            }
                            markers.clear();

                            for (int i = 0; i < driverslatlngs.size(); i++) {

                                final MarkerOptions currentOption = new MarkerOptions();
                                currentOption.position(driverslatlngs.get(i).getLatlan());
                                currentOption.title(driverslatlngs.get(i).getDriver_name());
                                currentOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance_car));
                                if (mGoogleMap != null) {
                                    final Marker[] driver_marker = new Marker[1];
                                    final int finalI = i;

                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (null == driver_marker[0]) {

                                                //driver_marker[0].remove();
                                                driver_marker[0] = mGoogleMap.addMarker(currentOption);

                                            } else {
                                                driver_marker[0].setPosition(driverslatlngs.get(finalI).getLatlan());
                                            }

                                        }
                                    });

                                    markers.add(driver_marker[0]);
                                    //mGoogleMap.animateCamera(location);
                                    markermap.put(driver_marker[0], i);

                                }
                            }

                        } else {

                            for (Marker marker : markers) {
                                marker.remove();
                            }
                            markers.clear();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case 101:

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        JSONArray sourceArray = jsonObject.getJSONArray("origin_addresses");
                        String sourceObject = (String) sourceArray.get(0);

                        JSONArray destinationArray = jsonObject.getJSONArray("destination_addresses");
                        String destinationObject = (String) destinationArray.get(0);

                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        JSONObject elementsObject = jsonArray.getJSONObject(0);
                        JSONArray elementsArray = elementsObject.getJSONArray("elements");
                        JSONObject distanceObject = elementsArray.getJSONObject(0);
                        JSONObject dObject = distanceObject.getJSONObject("distance");
                        String distance = dObject.getString("text");
                        JSONObject durationObject = distanceObject.getJSONObject("duration");
                        String duration = durationObject.getString("text");
                        String dis = dObject.getString("value");
                        String dur = durationObject.getString("value");
                        //Log.d("mahi", "time and dis" + dur + " " + dis);
                        double trip_dis = Integer.valueOf(dis) * 0.001;
                        getTypes(String.valueOf(trip_dis), dur);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            case Const.ServiceCode.GOOGLE_MATRIX:
                // Log.d("mahi", "google distance api" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("OK")) {
                        JSONArray sourceArray = jsonObject.getJSONArray("origin_addresses");
                        String sourceObject = (String) sourceArray.get(0);

                        JSONArray destinationArray = jsonObject.getJSONArray("destination_addresses");
                        String destinationObject = (String) destinationArray.get(0);

                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        JSONObject elementsObject = jsonArray.getJSONObject(0);
                        JSONArray elementsArray = elementsObject.getJSONArray("elements");
                        JSONObject distanceObject = elementsArray.getJSONObject(0);
                        JSONObject dObject = distanceObject.getJSONObject("distance");
                        String distance = dObject.getString("text");
                        JSONObject durationObject = distanceObject.getJSONObject("duration");
                        String duration = durationObject.getString("text");
                        String dis = dObject.getString("value");
                        String dur = durationObject.getString("value");
                        // Log.d("mahi", "time and dis" + dur + " " + dis);
                        double trip_dis = Integer.valueOf(dis) * 0.001;
                        getFare(String.valueOf(trip_dis), dur, ambulance_type);
                        tv_total_dis.setText(distance);

                     /*   et_clientlocation.setText(destinationObject);
                        et_doctorlocation.setText(sourceObject);
                        et_distance.setText("Distance:" + " " + distance);
                        et_eta.setText("ETA:" + " " + duration);*/


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.FARE_CALCULATION:
                Log.d("mahi", "estimate fare" + response);

                if (response != null) {
                    try {
                        JSONObject job1 = new JSONObject(response);
                        if (job1.getString("success").equals("true")) {
                            String fare = job1.getString("estimated_fare");
                            ambulance_price = job1.getString("ambulance_price");
                            base_price = job1.optString("base_price");
                            min_price = job1.optString("min_fare");
                            booking_fee = job1.optString("booking_fee");
                            currency = job1.optString("currency");
                            distance_unit = job1.optString("distance_unit");
                            tv_estimate_fare.setVisibility(View.VISIBLE);
                            tv_estimate_fare.setText(currency + fare);
                            if (pbfareProgress != null) {
                                pbfareProgress.setVisibility(View.GONE);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                break;

            case Const.ServiceCode.AMBULANCE_OPERATOR:
                Log.d("HaoLS", "Ambulance operator" + response);

                if (response != null) {
                    try {
                        JSONObject job = new JSONObject(response);

                        if (job.getString("success").equals("true")) {

                            mAmbulanceOperatorsMain.clear();

                            JSONArray jarray = job.getJSONArray("operator");

                            if (jarray.length() > 0) {
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject taxiobj = jarray.getJSONObject(i);
                                    AmbulanceOperator type = new AmbulanceOperator();
                                    type.setCurrencey_unit(job.optString("currency"));
                                    type.setId(taxiobj.getString("id"));
                                    type.setAmbulanceCost(taxiobj.getString("estimated_fare"));
                                    type.setAmbulanceImage(taxiobj.getString("picture"));
                                    type.setAmbulanceOperator(taxiobj.getString("name"));
                                    type.setAmbulance_price_min(taxiobj.getString("price_per_min"));
                                    type.setAmbulance_price_distance(taxiobj.getString("price_per_unit_distance"));
                                    type.setAmbulanceSeats(taxiobj.getString("number_seat"));
                                    type.setBasefare(taxiobj.optString("min_fare"));
                                    mAmbulanceOperatorsMain.add(type);
                                }

                                if (mAmbulanceOperatorsMain != null) {
                                    // ambulanceOperatorHorizontalAdapter = new AmbulanceOperatorHorizontalAdapter(activity, mAmbulanceOperatorsMain, this);
                                    ambulanceOperatorHorizontalAdapter.notifyDataSetChanged();
                                }


                            }

                        } else {

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                break;

            case Const.ServiceCode.ADVERTISEMENTS:
                EbizworldUtils.appLogDebug(TAG, "addsListResponse " + response);
                try {
                    JSONObject job1 = new JSONObject(response);
                    if (job1.getString("success").equals("true")) {
                        JSONArray jsonArray = job1.optJSONArray("data");
                        if (null != adsLists) {
                            adsLists.clear();
                        }
                        if (null != jsonArray && jsonArray.length() > 0) {
                            adsLists = new ParseContent(activity).parseAdsList(jsonArray);
                            if (adsLists != null) {
                                adsAdapter = new AdsAdapter(adsLists, activity);
                                //Adding adapter to Listview
                                adsRecyclerView.setAdapter(adsAdapter);
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;

        }

    }


    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ADDRESS_API_BASE + LATITUDE + "," + LONGITUDE + "&key=" + Const.GOOGLE_API_KEY);

        Log.d("mahi", "map for address" + map);
        new VolleyRequester(activity, Const.GET, map, Const.ServiceCode.ADDRESS_API_BASE, this);
    }

    private void getCompleteAddress(double LATITUDE, double LONGITUDE) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ADDRESS_API_BASE + LATITUDE + "," + LONGITUDE + "&key=" + Const.GOOGLE_API_KEY);

        Log.d("mahi", "map for address" + map);
        new VolleyRequester(activity, Const.GET, map, Const.ServiceCode.GOOGLE_ADDRESS_API, this);
    }


    private Bitmap getMarkerBitmapFromView(String place) {
        View customMarkerView = ((LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.info_window_pickup, null);
        TextView markertext = (TextView) customMarkerView.findViewById(R.id.txt_pickup_location);

        markertext.setText(place);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                final Place place = PlaceAutocomplete.getPlace(getActivity(), data);


                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!d_click) {
                            et_sch_source_address.setText(place.getAddress());
                            sch_pic_latLng = place.getLatLng();
                            if (null != sch_drop_latLng && null != sch_pic_latLng) {
                                findDistanceAndTimeforTypes(sch_pic_latLng, sch_drop_latLng);
                            }

                        } else {
                            et_sch_destination_address.setText(place.getAddress());
                            sch_drop_latLng = place.getLatLng();
                            findDistanceAndTimeforTypes(sch_pic_latLng, sch_drop_latLng);
                        }

                    }
                });

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.i("mahi", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void findDistanceAndTimeforTypes(LatLng pic_latlan, LatLng drop_latlan) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.GOOGLE_MATRIX_URL + Const.Params.ORIGINS + "="
                + String.valueOf(pic_latlan.latitude) + "," + String.valueOf(pic_latlan.longitude) + "&" + Const.Params.DESTINATION + "="
                + String.valueOf(drop_latlan.latitude) + "," + String.valueOf(drop_latlan.longitude) + "&" + Const.Params.MODE + "="
                + "driving" + "&" + Const.Params.LANGUAGE + "="
                + "en-EN" + "&" + "key=" + Const.GOOGLE_API_KEY + "&" + Const.Params.SENSOR + "="
                + String.valueOf(false));
        Log.e("mahi", "distance api" + map);
        new VolleyRequester(activity, Const.GET, map, 101, this);
    }

    private void getTypes(String dis, String dur) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.OPERATORS_URL);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.DISTANCE, dis);
        map.put(Const.Params.TIME, dur);

        Log.d("mahi", map.toString());
        new VolleyRequester(activity, Const.POST, map, Const.ServiceCode.AMBULANCE_OPERATOR,
                this);
    }

    private void findDistanceAndTime(LatLng s_latlan, LatLng d_latlan) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.GOOGLE_MATRIX_URL + Const.Params.ORIGINS + "="
                + String.valueOf(s_latlan.latitude) + "," + String.valueOf(s_latlan.longitude) + "&" + Const.Params.DESTINATION + "="
                + String.valueOf(d_latlan.latitude) + "," + String.valueOf(d_latlan.longitude) + "&" + Const.Params.MODE + "="
                + "driving" + "&" + Const.Params.LANGUAGE + "="
                + "en-EN" + "&" + "key=" + Const.GOOGLE_API_KEY + "&" + Const.Params.SENSOR + "="
                + String.valueOf(false));
        Log.e("mahi", "distance api" + map);
        new VolleyRequester(activity, Const.GET, map, Const.ServiceCode.GOOGLE_MATRIX, this);
    }

    private void showfarebreakdown(String base_price, String taxi_price_distance, String taxi_price_min, String min_price, String booking_fee, String currency, String distance_unit) {
        final Dialog farebreak = new Dialog(activity, R.style.DialogSlideAnim_leftright_Fullscreen);
        farebreak.requestWindowFeature(Window.FEATURE_NO_TITLE);
        farebreak.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_black)));
        farebreak.setCancelable(true);
        farebreak.setContentView(R.layout.fare_breakdown);
        TextView tv_dis_title = (TextView) farebreak.findViewById(R.id.tv_dis_title);
        TextView tv_base_fare = (TextView) farebreak.findViewById(R.id.tv__history_detail_base_fare);
        TextView tv_min_fare = (TextView) farebreak.findViewById(R.id.tv_history_detail_min_fare);
        TextView tv_per_min_cost = (TextView) farebreak.findViewById(R.id.tv_per_min_cost);
        TextView tv_per_km_price = (TextView) farebreak.findViewById(R.id.tv_per_km_price);
        TextView tv_service_tax_price = (TextView) farebreak.findViewById(R.id.tv_history_detail_service_tax_price);
        TextView tv_booking_price = (TextView) farebreak.findViewById(R.id.tv_history_detail_booking_price);

        tv_base_fare.setText(currency + base_price);
        tv_booking_price.setText(currency + booking_fee);
        tv_min_fare.setText(currency + min_price);
        tv_per_min_cost.setText(currency + taxi_price_min);
        tv_per_km_price.setText(currency + taxi_price_distance);
        tv_service_tax_price.setText(currency + ambulance_price);
        ImageView close_popup = (ImageView) farebreak.findViewById(R.id.close_popup);
        tv_dis_title.setText(getResources().getString(R.string.txt_per) + " " + distance_unit);
        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                farebreak.cancel();
            }
        });


        farebreak.show();

    }

    private void getFare(String distance, String duration, String service_id) {
        if (!EbizworldUtils.isNetworkAvailable(activity)) {

            EbizworldUtils.showShortToast(getResources().getString(R.string.network_error), activity);
            return;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.FARE_CALCULATION);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.DISTANCE, distance);
        map.put(Const.Params.TIME, duration);
        map.put(Const.Params.AMBULANCE_TYPE, service_id);

        Log.d("mahi", map.toString());
        new VolleyRequester(activity, Const.POST, map, Const.ServiceCode.FARE_CALCULATION,
                this);
    }


}
