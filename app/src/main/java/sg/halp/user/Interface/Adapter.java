package sg.halp.user.Interface;

import sg.halp.user.Models.RequestDetail;

import java.util.Calendar;
import java.util.List;

public class Adapter {

    public interface AdaperCallbackListCalendar{

        void onCallbackListCalendar(List<Calendar> calendarList);
    }

    public interface AdaperCallbackListRequestDetail{

        void onCallbackListRequestDetail(List<RequestDetail> requestDetails);
    }
}
