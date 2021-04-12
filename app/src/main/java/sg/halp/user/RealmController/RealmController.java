package sg.halp.user.RealmController;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import sg.halp.user.Models.CalendarReminder;
import sg.halp.user.Models.Hospital;
import sg.halp.user.Models.NursingHome;
import sg.halp.user.Models.Patient;
import sg.halp.user.Models.User;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by user on 8/22/2016.
 */
public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {

        realm = Realm.getDefaultInstance();

    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {

            instance = new RealmController(fragment.getActivity().getApplication());

        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {


        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    public void clearAllUser() {

        realm.beginTransaction();
        realm.clear(User.class);
        realm.commitTransaction();

    }

    public void clearAllHospital() {

        realm.beginTransaction();
        realm.clear(Hospital.class);
        realm.commitTransaction();

    }

    public void clearAllPatient() {

        realm.beginTransaction();
        realm.clear(Patient.class);
        realm.commitTransaction();

    }

    public void clearAllNurse() {

        realm.beginTransaction();
        realm.clear(NursingHome.class);
        realm.commitTransaction();

    }

    public void clearAllCalendarReminder(){

        realm.beginTransaction();
        realm.clear(CalendarReminder.class);
        realm.commitTransaction();

    }

    public void clearCalendarReminder(CalendarReminder calendarReminder){

        realm.beginTransaction();
        calendarReminder.removeFromRealm();
        realm.commitTransaction();

    }

    /*public RealmResults<User> getusers() {

        return realm.where(User.class).findAll();
    }


    public User getUser(int id) {

        return realm.where(User.class).equalTo("id", id).findFirst();
    }*/

    public RealmResults<Patient> getPatients() {

        return realm.where(Patient.class).findAll();
    }


    public Patient getPatient(int id) {
        return realm.where(Patient.class).equalTo("id", id).findFirst();
    }

    public RealmResults<NursingHome> getNurses() {

        return realm.where(NursingHome.class).findAll();
    }

    public NursingHome getNurse(int id) {

        return realm.where(NursingHome.class).equalTo("id", id).findFirst();
    }

    public RealmResults<Hospital> getHospitals() {

        return realm.where(Hospital.class).findAll();
    }

    public Hospital getHospital(int id) {

        return realm.where(Hospital.class).equalTo("id", id).findFirst();
    }

    public RealmResults<CalendarReminder> getCalendarReminders(){

        return realm.where(CalendarReminder.class).findAll();
    }

    public CalendarReminder getCalenderReminder(int id){

        return realm.where(CalendarReminder.class).equalTo("requestID", id).findFirst();
    }
}