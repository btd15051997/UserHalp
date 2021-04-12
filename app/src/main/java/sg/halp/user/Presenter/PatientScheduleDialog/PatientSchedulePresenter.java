package sg.halp.user.Presenter.PatientScheduleDialog;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import sg.halp.user.Dialog.PatientSchedule.IPatientScheduleView;
import sg.halp.user.Interface.AsyncTaskCompleteListener;
import sg.halp.user.Models.AmbulanceOperator;
import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;
import sg.halp.user.R;
import sg.halp.user.Utils.Const;
import sg.halp.user.Utils.EbizworldUtils;
import sg.halp.user.restAPI.AmbulanceOperatorListAPI;
import sg.halp.user.restAPI.BillingInfoAPI;
import sg.halp.user.restAPI.LatLngGoogleAPI;

public class PatientSchedulePresenter implements IPatientSchedulePresenter, AsyncTaskCompleteListener {

    private Activity activity;
    private IPatientScheduleView iPatientScheduleView;
    private Schedule mPatientSchedule;
    private HospitalDischarge mHospitalDischarge;

    public PatientSchedulePresenter(Activity activity, IPatientScheduleView iPatientScheduleView) {
        this.activity = activity;
        this.iPatientScheduleView = iPatientScheduleView;
    }

    @Override
    public void getAmbulanceOperator() {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iPatientScheduleView.onDisconnectNetwork();

        }else {

            new AmbulanceOperatorListAPI(activity, this)
                    .getAmbulanceOperatorForPatient(Const.ServiceCode.AMBULANCE_OPERATOR);

        }

    }

    @Override
    public void getLatLngFromSourceAddress(String source) {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iPatientScheduleView.onDisconnectNetwork();

        }else {

            try {

                new LatLngGoogleAPI(activity, this)
                        .getLatLngFromSourceAddress(URLEncoder.encode(source, "utf-8"), Const.ServiceCode.LOCATION_API_BASE_SOURCE);

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            }
        }

    }

    @Override
    public void getLatLngFromDestinationAddress(String destination) {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iPatientScheduleView.onDisconnectNetwork();

        }else {

            try {

                new LatLngGoogleAPI(activity, this)
                        .getLatLngFromDestinationAddress(URLEncoder.encode(destination, "utf-8"), Const.ServiceCode.LOCATION_API_BASE_DESTINATION);

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            }

        }

    }

    @Override
    public void getBillingInfo(Schedule patientSchedule, HospitalDischarge hospitalDischarge) {

        if (!EbizworldUtils.isNetworkAvailable(activity)){

            iPatientScheduleView.onDisconnectNetwork();

        }else {

            mPatientSchedule = patientSchedule;
            mHospitalDischarge = hospitalDischarge;

            new BillingInfoAPI(activity, this)
                    .getPatientScheduleBillingInfo(patientSchedule, Const.ServiceCode.BILLING_INFO);
        }

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode){

            case Const.ServiceCode.AMBULANCE_OPERATOR:{

                EbizworldUtils.appLogInfo("HaoLS", "Ambulance operator: " + response);

                if (response != null){

                    try {
                        JSONObject job = new JSONObject(response);

                        if (job.getString("success").equals("true")) {

                            JSONArray jsonArray = job.getJSONArray("operator");

                            if (jsonArray.length() > 0) {

                                ArrayList<AmbulanceOperator> ambulanceOperatorsMain = new ArrayList<>();

                                ambulanceOperatorsMain.add(new AmbulanceOperator("0",
                                        activity.getResources().getString(R.string.any_ambulance),
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""));

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    EbizworldUtils.appLogDebug("HaoLS", "Get homeambulance type index " + i);

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    AmbulanceOperator ambulanceOperator = new AmbulanceOperator();
                                    ambulanceOperator.setCurrencey_unit(job.optString("currency"));
                                    ambulanceOperator.setId(jsonObject.getString("id"));
                                    ambulanceOperator.setAmbulanceCost(jsonObject.getString("min_fare"));
                                    ambulanceOperator.setAmbulanceImage(jsonObject.getString("picture"));
                                    ambulanceOperator.setAmbulanceOperator(jsonObject.getString("name"));
                                    ambulanceOperator.setAmbulance_price_min(jsonObject.getString("price_per_min"));
                                    ambulanceOperator.setAmbulance_price_distance(jsonObject.getString("price_per_unit_distance"));
                                    ambulanceOperator.setAmbulanceSeats(jsonObject.getString("number_seat"));
                                    ambulanceOperatorsMain.add(ambulanceOperator);

                                }

                                if (ambulanceOperatorsMain.size() > 0){

                                    List<String> ambulanceOperatorsWheelView = new ArrayList<>();

                                    for (int i = 0; i < ambulanceOperatorsMain.size(); i++){

                                        ambulanceOperatorsWheelView.add(ambulanceOperatorsMain.get(i).getAmbulanceOperator());
                                    }

                                    iPatientScheduleView.initializeWheelView(ambulanceOperatorsWheelView, ambulanceOperatorsMain);
                                }

                            }

                        }else {

                            iPatientScheduleView.onFailure();

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();

                        iPatientScheduleView.onFailure();

                    }

                }

            }
            break;

            case Const.ServiceCode.LOCATION_API_BASE_SOURCE:{

                if (null != response) {
                    EbizworldUtils.appLogDebug("HaoLS", "Location API base source " + response);
                    try {

                        JSONObject job = new JSONObject(response);
                        JSONArray jarray = job.optJSONArray("results");
                        JSONObject locObj = jarray.getJSONObject(0);
                        JSONObject geometryOBJ = locObj.optJSONObject("geometry");
                        JSONObject locationOBJ = geometryOBJ.optJSONObject("location");
                        double lat = locationOBJ.getDouble("lat");
                        double lan = locationOBJ.getDouble("lng");

                        iPatientScheduleView.onSourceLatLngRespond(new LatLng(lat, lan));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "Location API base source: " + e.toString());
                    }
                }

            }
            break;

            case Const.ServiceCode.LOCATION_API_BASE_DESTINATION:{

                if (null != response) {
                    try {
                        JSONObject job = new JSONObject(response);
                        JSONArray jarray = job.optJSONArray("results");
                        JSONObject locObj = jarray.getJSONObject(0);
                        JSONObject geometryOBJ = locObj.optJSONObject("geometry");
                        JSONObject locationOBJ = geometryOBJ.optJSONObject("location");
                        double lat = locationOBJ.getDouble("lat");
                        double lan = locationOBJ.getDouble("lng");

                        iPatientScheduleView.onDestinationLatLngRespond(new LatLng(lat, lan));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            break;

            case  Const.ServiceCode.BILLING_INFO:{
                EbizworldUtils.appLogInfo("HaoLS", "Patient's schedule billing info: " + response);

                if (response != null){

                    try {

                        JSONObject jsonObject = new JSONObject(response);

                        if (jsonObject.getString("success").equals("true")){

                            JSONObject object = jsonObject.getJSONObject(Const.Params.BILLING_INFO);

                            String staircase = object.getString(Const.Params.STAIRCASE);
                            String weight = object.getString(Const.Params.WEIGHT);
                            String tarmac = object.getString(Const.Params.TARMAC);
                            String oxygen = object.getString(Const.Params.OXYGEN_TANK);
                            String caseType = object.getString(Const.Params.CASE_TYPE);
                            String total = object.getString(Const.Params.TOTAL);
                            String currency = object.getString(Const.Params.CURRENCY);

                            String other_expense = "";

                            if (object.has(Const.Params.OTHER_EXPENSES)){

                                other_expense = object.getString(Const.Params.OTHER_EXPENSES);

                            }

                            iPatientScheduleView.showBillingScheduleDialog(
                                    staircase,
                                    weight,
                                    tarmac,
                                    oxygen,
                                    caseType,
                                    total,
                                    currency,
                                    other_expense,
                                    mPatientSchedule,
                                    mHospitalDischarge
                            );

                        }

                    } catch (JSONException e) {

                        e.printStackTrace();
                        EbizworldUtils.appLogError("HaoLS", "Patient's schedule billing info failed: " + e.toString());

                    }

                }
            }
            break;

        }
    }
}
