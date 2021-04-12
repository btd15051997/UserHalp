package sg.halp.user.Interface;

public class CalendarReminderListener {

    public interface insertListener{

        void onInsert(long eventID);

    }

    public interface deleteListener{

        void onDelete(boolean isDelete);

    }
}
