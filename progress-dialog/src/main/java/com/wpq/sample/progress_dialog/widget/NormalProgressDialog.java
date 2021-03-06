package com.wpq.sample.progress_dialog.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.wpq.sample.progress_dialog.MyHttpClient;

import java.lang.ref.WeakReference;

/**
 * @author wpq
 * @version 1.0
 */
public class NormalProgressDialog extends ProgressDialog implements DialogInterface.OnCancelListener {

    private WeakReference<Context> mContext = new WeakReference<>(null);
    private volatile static NormalProgressDialog sDialog;

    private NormalProgressDialog(Context context) {
        this(context, -1);
    }

    private NormalProgressDialog(Context context, int theme) {
        super(context, theme);

        mContext = new WeakReference<>(context);
        setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // 点手机返回键等触发Dialog消失，应该取消正在进行的网络请求等
        Context context = mContext.get();
        if (context != null) {
            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
            MyHttpClient.cancelRequests(context);
        }
    }

    public static synchronized void showLoading(Context context) {
        showLoading(context, "loading...");
    }

    public static synchronized void showLoading(Context context, CharSequence message) {
        showLoading(context, message, true);
    }

    public static synchronized void showLoading(Context context, CharSequence message, boolean cancelable) {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }

        sDialog = new NormalProgressDialog(context);
        sDialog.setMessage(message);
        sDialog.setCancelable(cancelable);

        if (sDialog != null && !sDialog.isShowing() && context != null && !((Activity) context).isFinishing()) {
            sDialog.show();
        }
    }

    public static synchronized void stopLoading() {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }
        sDialog = null;
    }
}
