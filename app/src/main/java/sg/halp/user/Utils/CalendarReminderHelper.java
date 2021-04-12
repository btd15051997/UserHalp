package sg.halp.user.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import io.realm.Realm;
import sg.halp.user.Interface.CalendarReminderListener;
import sg.halp.user.Models.CalendarReminder;
import sg.halp.user.Models.Schedule;

public class CalendarReminderHelper {

    private Activity activity;
    private CalendarReminderListener.insertListener insertListener;
    private long eventID = 0;

    public CalendarReminderHelper() {
    }

    public CalendarReminderHelper(Activity activity) {
        this.activity = activity;
    }

    public void insertCalendar(Schedule schedule){

        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        Date date = parseDate(schedule.getRequest_date());
        int year = Integer.parseInt(schedule.getRequest_date().substring(0,4));
        EbizworldUtils.appLogDebug("HaoLS", "Request date: " + schedule.getRequest_date());

        if (date != null){

            EbizworldUtils.appLogDebug("HaoLS",
                    "Year: " + year +
                            " - Month: " + date.getMonth() +
                            " - Date: " + date.getDate() +
                            " - Hour: " + date.getHours() +
                            " - Minute: " + date.getMinutes());

            beginTime.set(date.getYear() + 1900, date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
            endTime.set(date.getYear() + 1900, date.getMonth(), date.getDate(), date.getHours() + 2, date.getMinutes());

            EbizworldUtils.appLogDebug("HaoLS",
                    "Begin time: " + beginTime.getTimeInMillis() +
                    " - End time: " + endTime.getTimeInMillis());

            ContentResolver contentResolver = activity.getContentResolver();
            ContentValues cvEvents = new ContentValues();
            cvEvents.put(CalendarContract.Events.DTSTART, beginTime.getTimeInMillis());
            cvEvents.put(CalendarContract.Events.DTEND, endTime.getTimeInMillis());
            cvEvents.put(CalendarContract.Events.TITLE, schedule.getOperatorName());
            cvEvents.put(CalendarContract.Events.DESCRIPTION, schedule.getOperatorName());
            cvEvents.put(CalendarContract.Events.CALENDAR_ID, 1);
            cvEvents.put(CalendarContract.Events.EVENT_LOCATION, schedule.getS_address());
            cvEvents.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
            cvEvents.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            Uri uriEvents = contentResolver.insert(CalendarContract.Events.CONTENT_URI, cvEvents);

            if (uriEvents != null) {

                eventID = Long.parseLong(uriEvents.getLastPathSegment());

                EbizworldUtils.appLogDebug("HaoLS", "Event ID: " + eventID);

                ContentValues cvReminders = new ContentValues();
                cvReminders.put(CalendarContract.Reminders.EVENT_ID, eventID);
                cvReminders.put(CalendarContract.Reminders.MINUTES, 30);
                cvReminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                Uri uriReminders = contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, cvReminders);

                if (insertListener != null){

                    insertListener.onInsert(eventID);
                }
            }
        }

    }

    public void insertCalendarByIntent(Schedule schedule){

        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();

        Date date = parseDate(schedule.getRequest_date());
        int year = Integer.parseInt(schedule.getRequest_date().substring(0,4));
        EbizworldUtils.appLogDebug("HaoLS", "Request date: " + schedule.getRequest_date());

        if (date != null){

            EbizworldUtils.appLogDebug("HaoLS",
                    "Year: " + year +
                    " - Month: " + date.getMonth() +
                    " - Date: " + date.getDate() +
                    " - Hour: " + date.getHours() +
                    " - Minute: " + date.getMinutes());

            beginTime.set(date.getYear() + 1900, date.getMonth(), date.getDate(), date.getHours(), date.getMinutes());
            endTime.set(date.getYear() + 1900, date.getMonth(), date.getDate(), date.getHours() + 2, date.getMinutes());

            EbizworldUtils.appLogDebug("HaoLS",
                    "Begin time: " + beginTime.getTimeInMillis() +
                            " - End time: " + endTime.getTimeInMillis());

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, schedule.getOperatorName())
                    .putExtra(CalendarContract.Events.DESCRIPTION, schedule.getOperatorName())
                    .putExtra(CalendarContract.Events.CALENDAR_ID, 1)
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, schedule.getS_address())
                    .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                    .putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

            activity.startActivity(intent);
        }

    }

    public void deleteEvents(long eventID){

        ContentResolver contentResolver = activity.getContentResolver();
        Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        int rows = contentResolver.delete(deleteUri, null, null);

    }

    private Date parseDate(String requestDate){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date date = null;

        try {

            date = dateFormat.parse(requestDate.trim());

        } catch (ParseException e) {

            e.printStackTrace();
            EbizworldUtils.appLogError("HaoLS", "insertCalendar parse date failed: " + e.toString());
        }

        return date;

    }

    public void setInsertListener(CalendarReminderListener.insertListener insertListener) {
        this.insertListener = insertListener;
    }
}
