package sg.halp.user.app;

import android.content.Context;

public interface IAppController {

    void overrideDefaultFont(Context context, String staticTypefaceFieldName);
    void configSSL();
    void configRealm();
}
