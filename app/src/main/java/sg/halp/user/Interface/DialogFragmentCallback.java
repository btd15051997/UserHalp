package sg.halp.user.Interface;

import java.util.List;

import sg.halp.user.Models.HospitalDischarge;
import sg.halp.user.Models.Schedule;
import sg.halp.user.Models.SocialMediaProfile;

public class DialogFragmentCallback {

    public interface ScheduleCallback{

        void onScheduleCallback(List<Schedule> scheduleList);

    }

    public interface HospitalDischargeCallback{

        void onHospitalDischargeCallback(HospitalDischarge hospitalDischarge);
        void onHospitalDischargeCancel(Boolean isDismiss);

    }

    public interface DialogDismissCallback{

        void onDialogDismissListener(Boolean isDismiss);
    }

    public interface BillingInfoCallback{

        void onConfirmListener(Boolean isConfirm);
        void onCancelListener(Boolean isCancel);

    }

    public interface CancelRequestListener {

        void onSuccess(Boolean isSuccess);
        void onFail(Boolean isFail);
    }

    public interface CropPictureListener{

        void onHandleCrop(String filePath);

    }

    public interface SocialNetworkListener{

        void onSuccess(SocialMediaProfile socialMediaProfile);
        void onFail(String error);

    }
}
