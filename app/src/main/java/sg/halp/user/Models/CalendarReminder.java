package sg.halp.user.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CalendarReminder extends RealmObject {

    @PrimaryKey
    private int requestID;
    private long eventID;

    public CalendarReminder() {
    }

    public CalendarReminder(int requestID, long eventID) {
        this.requestID = requestID;
        this.eventID = eventID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }
}
