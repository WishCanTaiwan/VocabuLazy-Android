package wishcantw.vocabulazy.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public class ParentActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    public void showProgressDialog(@NonNull Context context,
                                   @NonNull String title,
                                   @NonNull String message) {
        dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
