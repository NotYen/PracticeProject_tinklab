package notyen.parkproject.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class AppProgressDialog extends ProgressDialog {
    public AppProgressDialog(Context context) {
        super(context);
//        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        setCancelable(false);
    }

    public void show(String msg) {
        setMessage(msg);
        super.show();
    }

    @Override
    public void show() {
        setMessage("");
        super.show();
    }
}
